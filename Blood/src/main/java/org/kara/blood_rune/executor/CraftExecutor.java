package org.kara.blood_rune.executor;

import org.kara.blood_rune.utility.Location;
import org.kara.blood_rune.utility.Log;
import org.kara.blood_rune.utility.ObjectId;
import org.kara.blood_rune.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

public class CraftExecutor extends ActivityExecutor {

    private CraftActivity localActivity = CraftActivity.SETUP;
    private static int EXTRACT_COUNT;

    enum CraftActivity {
        SETUP,
        INITCRAFT,
        SECCRAFT,
        EXTRACT
    }

    @Override
    public int execute() {
        Log.info("Craft Executor");
        if (!Location.BLOOD_ALTER.contains(Players.local().tile())) {
            Log.severe("Not at Alter");
            Utility.setStopping(true);
            return Utility.getLoopReturnQuick();
        }

        switch (localActivity) {
            case SETUP -> {
                EXTRACT_COUNT = 0;
                localActivity = CraftActivity.INITCRAFT;
                return Utility.getLoopReturnQuick();
            }
            case INITCRAFT -> {
                Log.info("Craft - First Craft");
                Utility.setTask("Crafting");
                GameObject alter = Objects.stream().id(ObjectId.BLOOD_ALTER).nearest().first();
                if (Utility.getEssenceCount() > 1) {
                    Log.info("We have Essence");
                    alter.click();
                    Condition.wait(() -> Utility.getEssenceCount() == 0, 50, 200);
                    Log.fine("Crafted Runes");
                }
                localActivity = CraftActivity.EXTRACT;
                return Utility.getLoopReturn();
            }
            case SECCRAFT -> {
                Log.info("Craft - Second Craft");
                Utility.setTask("Crafting");
                GameObject alter2 = Objects.stream().id(ObjectId.BLOOD_ALTER).nearest().first();
                if (Utility.getEssenceCount() > 1) {
                    Log.info("We have Essence");
                    alter2.click();
                    Log.fine("Crafted Runes");
                }
                if (EXTRACT_COUNT == 2) {
                    Log.fine("Done Crafting");
                    EXTRACT_COUNT = 0;
                    Utility.setActivity(Activity.RETURN);
                    return Utility.getLoopReturn();
                }
                localActivity = CraftActivity.EXTRACT;
                return Utility.getLoopReturn();
            }
            case EXTRACT -> {
                Log.info("Craft - Extract");
                Utility.setTask("Extracting Pouch");
                Inventory.stream().id(ObjectId.POUCH_ITEM).action("Empty");
                if (Condition.wait(() -> Utility.getEssenceCount() > 0, 50, 40)) {
                    Log.fine("Runes Extracted");
                    EXTRACT_COUNT++;
                    localActivity = CraftActivity.SECCRAFT;
                } else {
                    Log.info("No More Runes");
                    Utility.setActivity(Activity.RETURN);
                }
                return Utility.getLoopReturn();
            }
        }
        return Utility.getLoopReturn();
    }
}
