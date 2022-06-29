package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import kotlin.jvm.functions.Function0;
import org.powbot.api.Condition;
import org.powbot.api.event.SkillExpGainedEvent;
import org.powbot.api.rt4.*;

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
            Log.severe("Not at Alter");
            Utility.setStopping(true);
            return Utility.getLoopReturnQuick();
        }

        switch (localActivity) {
            case CRAFT:
                Log.info("Craft - Craft");
                Utility.setTask("Crafting");
                GameObject alter = Objects.stream().id(Utility.BLOOD_ALTER).nearest().first();
                if (Utility.getEssenceCount() > 1) {
                    Log.info("We have Essence");
                    alter.click();
                }

            case EXTRACT:


        }
        return Utility.getLoopReturn();
    }
}
