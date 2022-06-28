package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Log;

public class CraftExecutor extends ActivityExecutor {

    private CraftActivity localActivity = CraftActivity.CRAFT;

    enum CraftActivity {
        CRAFT,
        EXTRACT
    }

    @Override
    public int execute() {
        Log.info("Craft Executor");

        switch (localActivity) {
            case CRAFT:

            case EXTRACT:


        }
        return 0;
    }
}
