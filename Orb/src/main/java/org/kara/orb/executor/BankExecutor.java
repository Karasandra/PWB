package org.kara.orb.executor;

import org.kara.orb.utility.Location;
import org.kara.orb.utility.ObjectId;
import org.kara.orb.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.Locatable;
import org.powbot.api.rt4.*;

public class BankExecutor extends ActivityExecutor {
    private BankActivity localActivity = BankActivity.BANKING;

    enum BankActivity {
        BANKING,
        SUPPLY,
        HEAL
    }

    @Override
    public int execute() {
        //Log.info("Bank Executor");
    if (!Utility.myTile(Location.BANK) && !Utility.getObject(ObjectId.BANK).valid()) {
        //Log.severe("Not at bank");
        Utility.setActivity(Activity.WALK);
        localActivity = BankActivity.BANKING;
        return Utility.getLoopReturnQuick();
    }

        switch (localActivity) {
            case BANKING -> {
                //Log.info("Bank-Bank");
                Utility.setTask("Bank Time");
                if (!Utility.getInv(ObjectId.COSMIC_RUNE).valid()) {
                    Utility.setStopping(true);
                    return Utility.getLoopReturnQuick();
                }
                if (Utility.getInv(ObjectId.UNPOWERED_ORB).valid() && Utility.invAntiPoison().valid() && Inventory.isFull()) {
                    Bank.close();
                    Utility.setActivity(Activity.WALK);
                    return Utility.getLoopReturnQuick();
                }
                Locatable bank = Bank.nearest();
                if (!Bank.inViewport()) {
                    Camera.turnTo(bank, 20);
                }
                if (!Bank.open()) {
                    //Log.severe("Bank failed to click");
                    return Utility.getLoopReturnQuick();
                }
                if (!Condition.wait(Bank::opened, 250, 100)) {
                    //Log.severe("Bank failed to open");
                    return  Utility.getLoopReturnQuick();
                }
                if (Utility.getInv(ObjectId.FIRE_ORB).valid()) {
                    //Log.info("Depositing Runes");
                    Bank.deposit(ObjectId.FIRE_ORB, Bank.Amount.ALL);
                    Condition.wait(() -> !Utility.getInv(ObjectId.FIRE_ORB).valid(), 50,100);
                }
                if (Utility.healthLoss()) {
                    //Log.info("Healing");
                    localActivity = BankActivity.HEAL;
                    return Utility.getLoopReturn();
                }
                if (!Utility.invAntiPoison().valid() || !Utility.getInv(ObjectId.FOOD).valid()) {
                    localActivity = BankActivity.SUPPLY;
                    return Utility.getLoopReturn();
                }
                if (!Utility.getInv(ObjectId.UNPOWERED_ORB).valid()) {
                    if (Bank.stream().id(ObjectId.UNPOWERED_ORB).isEmpty()) {
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                    Bank.withdraw(ObjectId.UNPOWERED_ORB, Bank.Amount.ALL);
                    Condition.wait(() -> Utility.getInv(ObjectId.UNPOWERED_ORB).valid(), 50, 40);
                }
                return Utility.getLoopReturn();
            }
            case SUPPLY -> {
                //Log.info("Bank-Potion");
                Utility.setTask("Supply Time");
                if (Utility.invAntiPoison().valid() && Utility.getInv(ObjectId.FOOD).valid()) {
                    localActivity = BankActivity.BANKING;
                    return Utility.getLoopReturn();
                } else {
                    if (!Utility.invAntiPoison().valid()) {
                        Utility.getBankAntiPoison();
                    }
                    if (!Utility.getInv(ObjectId.FOOD).valid()) {
                        Bank.withdraw(ObjectId.FOOD, Bank.Amount.ONE);
                    }
                }
                return Utility.getLoopReturn();
            }
            case HEAL -> {
                //Log.info("Bank - Heal");
                Utility.setTask("Healing Time");
                if (Players.local().healthPercent() >= 90 && !Utility.invFood().valid()) {
                    //Log.info("Done Eating");
                    localActivity = BankActivity.BANKING;
                    return Utility.getLoopReturnQuick();
                } else {
                    Utility.setTask("Eating");
                    Item food = Utility.invFood();
                    if (!food.valid()) {
                        //Log.info("Grabbing Food");
                        Bank.withdraw(ObjectId.FOOD, Bank.Amount.ONE);
                        if (!Condition.wait(() -> Utility.invFood().valid(), 50, 40)) {
                            //Log.severe("No more food");
                            Utility.setStopping(true);
                        }
                    } else {
                        //Log.fine("Got Food");
                        int curhealth = Utility.health();
                        food.click("Eat");
                        Condition.wait(() -> Utility.health() != curhealth, 50, 20);
                        //Log.fine("Eaten");
                    }
                }
                return Utility.getLoopReturn();
            }
        }
        return Utility.getLoopReturn();
    }
}
