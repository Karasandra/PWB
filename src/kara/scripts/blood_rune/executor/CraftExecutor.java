package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

public class CraftExecutor extends ActivityExecutor {

    private CraftActivity localActivity = CraftActivity.INITCRAFT;
    public static int EXTRACT_COUNT;

    enum CraftActivity {
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
            case INITCRAFT -> {
                Log.info("Craft - First Craft");
                Utility.setTask("Crafting");
                GameObject alter = Objects.stream().id(Utility.BLOOD_ALTER).nearest().first();
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
                GameObject alter2 = Objects.stream().id(Utility.BLOOD_ALTER).nearest().first();
                if (EXTRACT_COUNT == 2) {
                    Log.fine("Done Crafting");
                    Utility.setActivity(Activity.RETURN);
                    return Utility.getLoopReturn();
                }
                if (Utility.getEssenceCount() > 1) {
                    Log.info("We have Essence");
                    alter2.click();
                    Log.fine("Crafted Runes");
                }
                localActivity = CraftActivity.EXTRACT;
                return Utility.getLoopReturn();
            }
            case EXTRACT -> {
                Log.info("Craft - Extract");
                Utility.setTask("Extracting Pouch");
                Inventory.stream().id(Utility.POUCH_ITEM).action("Empty");
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
