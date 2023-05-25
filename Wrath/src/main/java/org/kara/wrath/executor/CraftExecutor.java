package org.kara.wrath.executor;


import org.kara.wrath.utility.Location;
import org.kara.wrath.utility.Log;
import org.kara.wrath.utility.ObjectId;
import org.kara.wrath.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.GameObject;

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
        if (!Utility.myTile(Location.WRATH_ALTER)) {
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
                GameObject alter = Utility.getObject(ObjectId.WRATH_ALTER);
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
                GameObject alter = Utility.getObject(ObjectId.WRATH_ALTER);
            if (Utility.getEssenceCount() > 1) {
                    Log.info("We have Essence");
                    alter.click();
                    Condition.wait(() -> Utility.getEssenceCount() == 0, 50, 200);
                    Log.fine("Crafted Runes");
                }
            if (EXTRACT_COUNT == 2 && Utility.getEssenceCount() == 0) {
                    Log.fine("Done Crafting");
                    EXTRACT_COUNT = 0;
                    localActivity = CraftActivity.SETUP;
                    Utility.setActivity(Activity.WALK);
                    return Utility.getLoopReturnLong();
            } else {
                localActivity = CraftActivity.EXTRACT;
                return Utility.getLoopReturn();
                }
            }
            case EXTRACT -> {
                Log.info("Craft - Extract");
                Utility.setTask("Extracting Pouch");
                Utility.getInvPouch().click("Empty");
                if (Condition.wait(() -> Utility.getEssenceCount() != 0, 50, 250)) {
                    Log.fine("Runes Extracted");
                    EXTRACT_COUNT = EXTRACT_COUNT + 1;
                    localActivity = CraftActivity.SECCRAFT;
                } else {
                    Log.info("waiting");
                }
                return Utility.getLoopReturn();
            }
        }
        return Utility.getLoopReturn();
    }
}