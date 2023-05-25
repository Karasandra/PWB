package org.kara.wrath.executor;


import org.kara.wrath.utility.Location;
import org.kara.wrath.utility.Log;
import org.kara.wrath.utility.ObjectId;
import org.kara.wrath.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;

public class WalkExecutor extends ActivityExecutor {

    private WalkActivity localActivity = WalkActivity.START;

    enum WalkActivity {
        START,
        BANK,
        UNDER,
        ALTER
    }


    @Override
    public int execute() {
        switch (localActivity) {
            case START -> {
                Log.info("Walk Executor");
                if (!Utility.myTile(Location.MYTH_GUILD_LOWER) && Utility.tpCape == 0) {
                    Log.fine("Teleport");
                    Utility.tpCape = 1;
                    Utility.tele();
                    return Utility.getLoopReturnQuick();
                }
                if (Utility.getPouchVarpbitItem() == ObjectId.POUCH_VARPBIT_FULL && Inventory.isFull()) {
                    localActivity = WalkActivity.UNDER;
                } else {
                    localActivity = WalkActivity.BANK;
                }
                Utility.tpCape = 0;
                return Utility.getLoopReturn();
            }
            case BANK -> {
                Utility.setTask("Going to Bank");
                Log.info("Walk - Bank");
                if (Utility.myTile(Location.MYTH_GUILD_UPPER)) {
                    Utility.setActivity(Activity.BANK);
                    localActivity = WalkActivity.START;
                    return Utility.getLoopReturnLong();
                } else {
                    Utility.go(Location.MYTH_GUILD_UPPER, ObjectId.BANK);
                    Condition.wait(() -> Utility.myTile(Location.MYTH_GUILD_UPPER), 100, 250);
                    return Utility.getLoopReturn();
                }
            }
            case UNDER -> {
                Utility.setTask("Going to Cave");
                Log.info("Walk - Under");
                if (Utility.myTile(Location.MYTH_GUILD_LOWER)) {
                    Utility.getObject(ObjectId.STATUE).click();
                }
                Condition.wait(() -> !Utility.myTile(Location.MYTH_GUILD_LOWER), 100, 250);
                if (Utility.myTile(Location.MYTH_ALTER)) {
                    localActivity = WalkActivity.ALTER;
                    return Utility.getLoopReturn();
                }
                if (!Utility.myTile(Location.MYTH_GUILD_LOWER)) {
                    Utility.go(Location.CAVE, ObjectId.CAVE);
                    Utility.getObject(ObjectId.CAVE).click();
                    return Utility.getLoopReturn();
                }
            }
            case ALTER -> {
                Utility.setTask("Going to Alter");
                Log.info("Walk - Alter");
                if (Utility.myTile(Location.WRATH_ALTER)) {
                    Utility.setActivity(Activity.CRAFT);
                    localActivity = WalkActivity.START;
                    return Utility.getLoopReturnLong();
                } else {
                    Utility.getObject(ObjectId.MYTH_ALTER).click();
                    Condition.wait(() -> Utility.myTile(Location.WRATH_ALTER), 100, 250);
                    return Utility.getLoopReturn();
                }
            }
        }
        return Utility.getLoopReturn();
    }
}
