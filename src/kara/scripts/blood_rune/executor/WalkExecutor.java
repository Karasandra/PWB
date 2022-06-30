package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.rt4.walking.model.Skill;


public class WalkExecutor extends ActivityExecutor {

    private WalkActivity localActivity = WalkActivity.METHOD;

    enum WalkActivity {
        METHOD,
        LOW,
        HIGH,
        OHGOD
    }

    @Override
    public int execute() {
        Log.info("Walk Executor");

        switch (localActivity) {
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
