package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.model.Skill;


public class WalkExecutor extends ActivityExecutor {

    private WalkActivity localActivity = WalkActivity.METHOD;
    private static int LA_DOOR_COUNT;
    private static int HA_DOOR_COUNT;

    enum WalkActivity {
        METHOD,
        FAIRY,
        LOW,
        HIGH,
        OHGOD
    }

    @Override
    public int execute() {
        Log.info("Walk Executor");

        switch (localActivity) {
            case FAIRY:
                if (Location.LEGEND_GUILD.contains(Players.local().tile())) {
                    GameObject fairy = Objects.stream().id(Utility.FAIRY_RING).nearest().first();
                    if (!fairy.inViewport()) {
                        Camera.turnTo(fairy);
                    }
                    fairy.click("Last-destination (DLS)");
                    if (Condition.wait(() -> Location.FAIRY_RING_DLS.contains(Players.local().tile()), 50, 250)) {
                        Log.info("TP good");
                        localActivity = WalkActivity.METHOD;
                        return Utility.getLoopReturn();
                    } else {
                        Log.severe("Fairy TP Failed");
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                }
                else {
                    Log.info("Tp to Fairy");
                    Utility.setTask("Tp to Fairy Ring");
                    Item cape = Inventory.stream().id(Utility.QP_CAPE).first();
                    if (!cape.valid()) {
                        Log.severe("No QP Cape!");
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                    if (cape.valid()) {
                        Log.fine("Cape found");
                        cape.interact("teleport");
                        if (Condition.wait(() -> Location.LEGEND_GUILD.contains(Players.local().tile()), 50, 500)) {
                            Log.fine("teleport successful");
                        } else {
                            Log.severe("Teleport failed");
                            Utility.setStopping(true);
                            return Utility.getLoopReturnQuick();
                        }
                    }
                }
                return Utility.getLoopReturn();

            case METHOD:
                LA_DOOR_COUNT = 0;
                HA_DOOR_COUNT = 0;
                Log.info("Choosing Method");
                int agility = Skill.Agility.realLevel();
                int mining = Skill.Mining.realLevel();
                if (agility >= 93 && mining >= 78) {
                    Log.info("Using High Method");
                    localActivity = WalkActivity.HIGH;
                }
                if (agility < 93 && agility >= 74){
                    Log.info("Using Low Method");
                    localActivity = WalkActivity.LOW;
                }
                else {
                    Log.info("Person is snail");
                    localActivity = WalkActivity.OHGOD;
                }
                return Utility.getLoopReturnQuick();

            case LOW:
                Log.info("Low Agility");
                Utility.setTask("Walking - Low Agility");
                if (!Location.BLOOD_ALTER_RUIN_LA.contains(Players.local().tile()))  {

                            switch (doorCount) {
                        case 0 -> {
                            int sdoor = Utility.CAVE_DOOR_1;
                            return sdoor;
                        }
                            }
                    GameObject door = Utility.getObject(doorCount);
                    if (LA_DOOR_COUNT == 0) {
                        GameObject door = Utility.getObject(Utility.CAVE_DOOR_1);
                    }
                    if (LA_DOOR_COUNT == 1) {
                        GameObject door = Utility.getObject(Utility.CAVE_DOOR_2);
                    }
                    if (LA_DOOR_COUNT == 2) {
                        GameObject door = Utility.getObject(Utility.CAVE_DOOR_LA_1);
                    }
                    if (LA_DOOR_COUNT == 3) {
                        GameObject door = Utility.getObject(Utility.CAVE_DOOR_LA_2);
                    }
                    if (LA_DOOR_COUNT == 4) {
                        GameObject door = Utility.getObject(Utility.CAVE_DOOR_LA_3);
                    }
                    if (LA_DOOR_COUNT == 5) {
                        GameObject door = Utility.getObject(Utility.CAVE_DOOR_LA_4);
                    } else if (!door) {
                        
                    }
                    Condition.wait(() -> Utility.getIdle(), 100, 500);
                    LA_DOOR_COUNT++;
                }
                if (Location.BLOOD_ALTER_RUIN_LA.contains(Players.local().tile())) {
                    Log.info("Finished Path");
                    Utility.getObject(Utility.MYST_RUIN).click();
                }
                return Utility.getLoopReturnQuick();

            case HIGH:
                Log.info("High Agility");
                Utility.setTask("Walking - High Agility");

            case OHGOD:
                Log.info("Why God");
                Utility.setTask("Walking - Snail Style");


        }



        return Utility.getLoopReturn();
    }
}
