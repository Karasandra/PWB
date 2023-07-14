package org.kara.plank.executor;

import org.kara.plank.utility.Config;
import org.kara.plank.utility.Location;
import org.kara.plank.utility.ObjectId;
import org.kara.plank.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;

public class BankExecutor extends ActivityExecutor {
    private BankActivity localActivity = BankActivity.BANKING;

    enum BankActivity {
        BANKING,
        RING
    }

    @Override
    public int execute() {
        //Log.info("Bank Executor");
        if (!Utility.myTile(Location.CASTLE_WARS)) {
            //Log.severe("Not at bank");
            Utility.setActivity(Activity.WALK);
            localActivity = BankActivity.BANKING;
            return Utility.getLoopReturnQuick();
        }

        switch (localActivity) {
            case BANKING -> {
                //Log.info("Bank-Bank");
                Utility.setTask("Bank Time");
                if (Utility.getInv(ObjectId.GOLD).getStack() <= Config.getGoldCut()) {
                    Utility.setStopping(true);
                    return Utility.getLoopReturnQuick();
                }
                if (Utility.getInv(Config.getLogChoice()).valid() && Inventory.isFull()) {
                    Bank.close();
                    Utility.setActivity(Activity.WALK);
                    return Utility.getLoopReturnQuick();
                }
                boolean ringValid;
                if (Utility.checkRing()) {
                    ringValid = true;
                } else { ringValid = false; }
                if (!Bank.open()) {
                    //Log.severe("Bank failed to click");
                    return Utility.getLoopReturnQuick();
                }
                if (!Condition.wait(Bank::opened, 250, 100)) {
                    //Log.severe("Bank failed to open");
                    return  Utility.getLoopReturnQuick();
                }
                if (Utility.getInv(Config.getCraftChoice()).valid()) {
                    //Log.info("Depositing Runes");
                    Bank.deposit(Config.getCraftChoice(), Bank.Amount.ALL);
                    Condition.wait(() -> !Utility.getInv(Config.getCraftChoice()).valid(), 50,100);
                }
                if (ringValid) {
                    localActivity = BankActivity.RING;
                    return Utility.getLoopReturn();
                }
                if (!Utility.getInv(Config.getLogChoice()).valid()) {
                    if (Bank.stream().id(Config.getLogChoice()).isEmpty()) {
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                    Bank.withdraw(Config.getLogChoice(), Bank.Amount.ALL);
                    Condition.wait(() -> Utility.getInv(Config.getLogChoice()).valid(), 50, 40);
                }
                return Utility.getLoopReturn();
            }
            case RING -> {
                //Log.info("Bank - Heal");
                Utility.setTask("Ring");
                if (Utility.checkRing()) {
                    //Log.info("Done Eating");
                    localActivity = BankActivity.BANKING;
                    return Utility.getLoopReturnQuick();
                } else {
                    Utility.setTask("Ring Withdraw");
                    Item ring = Utility.getInv(ObjectId.RING);
                    if (!ring.valid()) {
                        //Log.info("Grabbing Food");
                        Bank.withdraw(ObjectId.RING, Bank.Amount.ONE);
                        if (!Condition.wait(() -> Utility.getInv(ObjectId.RING).valid(), 50, 40)) {
                            //Log.severe("No more food");
                            Utility.setStopping(true);
                        }
                    } else {
                        //Log.fine("Got Food");
                        ring.click("Wear");
                        Condition.wait(() -> !Utility.getInv(ObjectId.RING).valid(), 50, 20);
                        //Log.fine("Eaten");
                    }
                }
                return Utility.getLoopReturn();
            }
        }
        return Utility.getLoopReturn();
    }
}
