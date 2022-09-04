package kara.scripts.blood_rune.executor;

import jdk.jshell.execution.Util;
import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.model.Skill;


public class WalkExecutor extends ActivityExecutor {

    private WalkActivity localActivity = WalkActivity.METHOD;

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
