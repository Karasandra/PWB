package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import org.powbot.api.rt4.Players;

public class CraftExecutor extends ActivityExecutor {

    private CraftActivity localActivity = CraftActivity.CRAFT;

    enum CraftActivity {
        CRAFT,
        EXTRACT
    }

    @Override
    public int execute() {
        Log.info("Craft Executor");
        if (!Location.BLOOD_ALTER.contains(Players.local().tile())) {

        }

        switch (localActivity) {
            case CRAFT:

            case EXTRACT:


        }
        return 0;
    }
}
