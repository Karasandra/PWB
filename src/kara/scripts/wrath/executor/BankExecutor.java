package kara.scripts.wrath.executor;

import kara.scripts.wrath.utility.Log;
import kara.scripts.wrath.utility.Utility;

public class BankExecutor extends ActivityExecutor {
    private BankActivity localActivity = BankActivity.BANKING;

    enum BankActivity {
        BANKING,
        POTION
    }

    @Override
    public int execute() {
        Log.info("Bank Executor");


        switch (localActivity) {
            case BANKING -> {
                Log.info("Bank-Bank");
                Utility.setTask("Bank Time");
                return Utility.getLoopReturnQuick();
            }
            case POTION -> {
                Log.info("Bank-Potion");
                Utility.setTask("Potion Time");
                return Utility.getLoopReturnQuick();
            }
        }
        return Utility.getLoopReturn();
    }
}
