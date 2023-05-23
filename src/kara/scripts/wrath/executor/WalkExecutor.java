package kara.scripts.wrath.executor;

import kara.scripts.wrath.utility.Log;
import kara.scripts.wrath.utility.Utility;

public class WalkExecutor extends ActivityExecutor {


    @Override
    public int execute() {
        Log.info("Walk Executor");
        Log.info("Bank-Potion");
        Utility.setTask("Potion Time");
        return Utility.getLoopReturnQuick();
    }
}
