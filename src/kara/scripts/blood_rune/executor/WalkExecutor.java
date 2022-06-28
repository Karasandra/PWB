package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;

public class WalkExecutor extends ActivityExecutor {

    private WalkActivity localActivity = WalkActivity.METHOD;

    enum WalkActivity {
        METHOD,
        LOW,
        HIGH
    }

    @Override
    public int execute() {
        Log.info("Walk Executor");

        switch (localActivity) {
            case METHOD:
                Log.info("Choosing Method");

            case LOW:
                Log.info("Low Agility");
                Utility.setTask("Walking - Low Agility");

            case HIGH:
                Log.info("High Agility");
                Utility.setTask("Walking - High Agility");


        }



        return Utility.getLoopReturnQuick();
    }
}
