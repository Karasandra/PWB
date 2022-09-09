package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.ObjectId;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.model.Skill;


public class WalkExecutor extends ActivityExecutor {

    private WalkActivity localActivity = WalkActivity.METHOD;
    private static int LA_DOOR_COUNT;
    private static int HA_DOOR_COUNT;
    private static int SN_DOOR_COUNT;

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
            case FAIRY -> {
                if (Location.LEGEND_GUILD.contains(Players.local().tile())) {
                    GameObject fairy = Objects.stream().id(ObjectId.FAIRY_RING).nearest().first();
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
                } else {
                    Log.info("Tp to Fairy");
                    Utility.setTask("Tp to Fairy Ring");
                    Item cape = Inventory.stream().id(ObjectId.QP_CAPE).first();
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
                return Utility.getLoopReturnLong();
            }
            case METHOD -> {
                LA_DOOR_COUNT = 0;
                HA_DOOR_COUNT = 0;
                SN_DOOR_COUNT = 0;
                Log.info("Choosing Method");
                int agility = Skill.Agility.realLevel();
                int mining = Skill.Mining.realLevel();
                if (agility >= 93 && mining >= 78) {
                    Log.info("Using High Method");
                    localActivity = WalkActivity.HIGH;
                }
                if (agility < 93 && agility >= 74) {
                    Log.info("Using Low Method");
                    localActivity = WalkActivity.LOW;
                } else {
                    Log.info("Person is snail");
                    localActivity = WalkActivity.OHGOD;
                }
                return Utility.getLoopReturnQuick();
            }
            case LOW -> {
                Log.info("Low Agility");
                Utility.setTask("Walking - Low Agility");
                GameObject doorCountLA = null;
                if (!Location.BLOOD_ALTER_RUIN_LA.contains(Players.local().tile())) {
                    if (LA_DOOR_COUNT == 0) {
                        doorCountLA = Utility.getObject(ObjectId.CAVE_DOOR_1);
                    }
                    if (LA_DOOR_COUNT == 1) {
                        doorCountLA = Utility.getObject(ObjectId.CAVE_DOOR_2);
                    }
                    if (LA_DOOR_COUNT == 2) {
                        doorCountLA = Utility.getObject(ObjectId.CAVE_DOOR_LA_1);
                    }
                    if (LA_DOOR_COUNT == 3) {
                        doorCountLA = Utility.getObject(ObjectId.CAVE_DOOR_LA_2);
                    }
                    if (LA_DOOR_COUNT == 4) {
                        Movement.walkTo(Location.LA_DOOR_ENTRY.getRandomTile());
                        doorCountLA = Utility.getObject(ObjectId.CAVE_DOOR_LA_3);
                    }
                    if (LA_DOOR_COUNT == 5) {
                        doorCountLA = Utility.getObject(ObjectId.CAVE_DOOR_LA_4);
                    }
                    if (doorCountLA != null && !doorCountLA.inViewport()) {
                        Camera.turnTo(doorCountLA);
                    }
                    if (doorCountLA != null) {
                        doorCountLA.click();
                    }
                    Condition.wait(Utility::getIdle, 100, 500);
                    LA_DOOR_COUNT++;
                }
                if (Location.BLOOD_ALTER_RUIN_LA.contains(Players.local().tile())) {
                    Log.info("Finished Path");
                    LA_DOOR_COUNT = 0;
                    GameObject ruin = Utility.getObject(ObjectId.MYST_RUIN);
                    if (!ruin.inViewport()) {
                        Camera.turnTo(ruin);
                    }
                    ruin.click();
                    if (Condition.wait(() -> Location.BLOOD_ALTER.contains(Players.local().tile()), 50, 600)) {
                        Log.fine("Entered Alter");
                        Utility.setActivity(Activity.CRAFT);
                        return Utility.getLoopReturnQuick();
                    } else {
                        Log.severe("Did not go into Ruins");
                        Utility.setStopping(true);
                    }
                }
                return Utility.getLoopReturnLong();
            }
            case HIGH -> {
                Log.info("High Agility");
                Utility.setTask("Walking - High Agility");
                GameObject doorCountHA = null;
                if (!Location.BLOOD_ALTER_RUIN_LA.contains(Players.local().tile())) {
                    if (HA_DOOR_COUNT == 0) {
                        doorCountHA = Utility.getObject(ObjectId.CAVE_DOOR_1);
                    }
                    if (HA_DOOR_COUNT == 1) {
                        doorCountHA = Utility.getObject(ObjectId.CAVE_DOOR_2);
                    }
                    if (HA_DOOR_COUNT == 2) {
                        Movement.walkTo(Location.HA_DOOR_ENTRY.getRandomTile());
                        doorCountHA = Utility.getObject(ObjectId.CAVE_DOOR_HA_1);
                    }
                    if (HA_DOOR_COUNT == 3) {
                        doorCountHA = Utility.getObject(ObjectId.CAVE_DOOR_HA_2);
                    }
                    if (doorCountHA != null && !doorCountHA.inViewport()) {
                        Camera.turnTo(doorCountHA);
                    }
                    if (doorCountHA != null) {
                        doorCountHA.click();
                    }
                    Condition.wait(Utility::getIdle, 100, 500);
                    HA_DOOR_COUNT++;
                }
                if (Location.BLOOD_ALTER_RUIN_HA.contains(Players.local().tile())) {
                    Log.info("Finished Path");
                    HA_DOOR_COUNT = 0;
                    GameObject ruin = Utility.getObject(ObjectId.MYST_RUIN);
                    if (!ruin.inViewport()) {
                        Camera.turnTo(ruin);
                    }
                    ruin.click();
                    if (Condition.wait(() -> Location.BLOOD_ALTER.contains(Players.local().tile()), 50, 600)) {
                        Log.fine("Entered Alter");
                        Utility.setActivity(Activity.CRAFT);
                        return Utility.getLoopReturnQuick();
                    } else {
                        Log.severe("Did not go into Ruins");
                        Utility.setStopping(true);
                    }
                }
                return Utility.getLoopReturnLong();
            }
            case OHGOD -> {
                Log.info("Why God");
                Utility.setTask("Walking - Snail Style");
                GameObject doorCountSN = null;
                if (!Location.BLOOD_ALTER_RUIN_SNAIL.contains(Players.local().tile())) {
                    if (SN_DOOR_COUNT == 0) {
                        doorCountSN = Utility.getObject(ObjectId.CAVE_DOOR_1);
                    }
                    if (SN_DOOR_COUNT == 1) {
                        doorCountSN = Utility.getObject(ObjectId.CAVE_DOOR_2);
                    }
                    if (SN_DOOR_COUNT == 2) {
                        doorCountSN = Utility.getObject(ObjectId.CAVE_DOOR_LA_1);
                    }
                    if (SN_DOOR_COUNT == 3) {
                        doorCountSN = Utility.getObject(ObjectId.CAVE_DOOR_LA_2);
                    }
                    if (SN_DOOR_COUNT == 4) {
                        Movement.walkTo(Location.BLOOD_ALTER_RUIN_SNAIL.getRandomTile());
                        return Utility.getLoopReturn();
                    }
                    if (doorCountSN != null && !doorCountSN.inViewport()) {
                        Camera.turnTo(doorCountSN);
                    }
                    if (doorCountSN != null) {
                        doorCountSN.click();
                    }
                    Condition.wait(Utility::getIdle, 100, 500);
                    SN_DOOR_COUNT++;
                }
                if (Location.BLOOD_ALTER_RUIN_SNAIL.contains(Players.local().tile())) {
                    Log.info("Finished Path");
                    SN_DOOR_COUNT = 0;
                    GameObject ruin = Utility.getObject(ObjectId.MYST_RUIN);
                    if (!ruin.inViewport()) {
                       Camera.turnTo(ruin);
                    }
                    ruin.click();
                    if (Condition.wait(() -> Location.BLOOD_ALTER.contains(Players.local().tile()), 50, 600)) {
                       Log.fine("Entered Alter");
                       Utility.setActivity(Activity.CRAFT);
                       return Utility.getLoopReturnQuick();
                    } else {
                       Log.severe("Did not go into Ruins");
                       Utility.setStopping(true);
                    }
                }
                return Utility.getLoopReturnLong();
            }
        }
        return Utility.getLoopReturn();
    }
}
