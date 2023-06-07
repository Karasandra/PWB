package org.kara.wrath.executor;


import org.kara.wrath.utility.Location;
import org.kara.wrath.utility.ObjectId;
import org.kara.wrath.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.ModelInteractionType;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.GameObject;
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
                Utility.setTask("Walking Time");
                //Log.info("Walk Executor");
                if (!Utility.myTile(Location.MYTH_GUILD_LOWER) && Utility.tpCape == 0) {
                    //Log.fine("Teleport");
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
                //Log.info("Walk - Bank");
                if (Utility.myTile(Location.MYTH_GUILD_UPPER) || Utility.getObject(ObjectId.BANK).valid()) {
                    Utility.setActivity(Activity.BANK);
                    localActivity = WalkActivity.START;
                    return Utility.getLoopReturnLong();
                } else {
                    GameObject stairs = Utility.getObject(ObjectId.MYTH_STAIRS);
                    if (stairs.valid()) {
                        stairs.click("Climb-up");
                    } else {
                        if (Utility.myTile(Location.MYTH_GUILD_LOWER)) {
                            Utility.step(Location.MYTH_STAIRS);
                        } else {
                            Utility.step(Location.MYTH_GUILD_UPPER);
                        }
                    }
                    Condition.wait(() -> Utility.myTile(Location.MYTH_GUILD_UPPER), 50, 20);
                    return Utility.getLoopReturnQuick();
                }
            }
            case UNDER -> {
                Utility.setTask("Going Underground");
                //Log.info("Walk - Under");
                GameObject statue = Utility.getObject(ObjectId.STATUE);
                if (Utility.myTile(Location.MYTH_GUILD_LOWER) || statue.valid()) {
                    if (!statue.inViewport()) {
                        Camera.turnTo(statue);
                        return Utility.getLoopReturn();
                    }
                    statue.click("Enter");
                    Condition.wait(() -> !Utility.myTile(Location.MYTH_GUILD_LOWER), 50, 30);
                    return Utility.getLoopReturnLong();
                }
                Camera.turnTo(Utility.yawUnd, Utility.pitch);
                if (!Utility.myTile(Location.MYTH_ALTER)) {
                    Utility.step(Location.CAVE);
                    GameObject cave = Utility.getObject(ObjectId.CAVE);
                    if (cave.valid() && cave.inViewport()) {
                        cave.interactionType(ModelInteractionType.HullAccurate).click("Enter");
                        Condition.wait(() -> Utility.myTile(Location.MYTH_ALTER), 50, 30);
                    }
                    return Utility.getLoopReturnQuick();
                }
                localActivity = WalkActivity.ALTER;
                return Utility.getLoopReturn();
            }
            case ALTER -> {
                Utility.setTask("Going to Wrath Alter");
                //Log.info("Walk - Alter");
                if (Utility.myTile(Location.WRATH_ALTER)) {
                    Utility.setActivity(Activity.CRAFT);
                    localActivity = WalkActivity.START;
                } else {
                    Utility.getObject(ObjectId.MYTH_ALTER).click("Enter");
                    Condition.wait(() -> Utility.myTile(Location.WRATH_ALTER), 50, 20);
                }
                return Utility.getLoopReturnLong();
            }
        }
        return Utility.getLoopReturn();
    }
}
