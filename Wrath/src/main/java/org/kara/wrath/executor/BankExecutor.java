package org.kara.wrath.executor;


import org.kara.wrath.utility.Location;
import org.kara.wrath.utility.ObjectId;
import org.kara.wrath.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.Locatable;
import org.powbot.api.rt4.*;



public class BankExecutor extends ActivityExecutor {
    private BankActivity localActivity = BankActivity.BANKING;

    enum BankActivity {
        BANKING,
        POTION,
        HEAL
    }

    @Override
    public int execute() {
        //Log.info("Bank Executor");
    if (!Utility.myTile(Location.MYTH_GUILD_UPPER) || !Utility.getObject(ObjectId.BANK).inViewport()) {
        //Log.severe("Not at bank");
        Utility.setActivity(Activity.WALK);
        return Utility.getLoopReturnQuick();
    }

        switch (localActivity) {
            case BANKING -> {
                //Log.info("Bank-Bank");
                Utility.setTask("Bank Time");
                if (Utility.getPouchVarpbitItem() == ObjectId.POUCH_VARPBIT_FULL && Inventory.isFull()) {
                    //Log.fine("Inv Good");
                    Bank.close();
                    Utility.setActivity(Activity.WALK);
                    return Utility.getLoopReturnLong();
                }
                Locatable bank = Bank.nearest();
                if (!Bank.inViewport()) {
                    Camera.turnTo(bank, 45);
                }
                if (!Bank.open()) {
                    //Log.severe("Bank failed to click");
                    return Utility.getLoopReturnQuick();
                }
                if (!Condition.wait(Bank::opened, 250, 100)) {
                    //Log.severe("Bank failed to open");
                    return  Utility.getLoopReturnQuick();
                }
                if (Utility.getInvWrathRune().valid()) {
                    //Log.info("Depositing Runes");
                    Bank.deposit(ObjectId.WRATH_RUNE, Bank.Amount.ALL);
                    Condition.wait(() -> !Utility.getInvWrathRune().valid(), 50,100);
                }
                if (Utility.healthLoss()) {
                    //Log.info("Healing");
                    localActivity = BankActivity.HEAL;
                    return Utility.getLoopReturn();
                }
                if (Utility.getPotionVarpbit() <= 40 && Movement.energyLevel() <= 60) {
                    //Log.info("Potion Low");
                    localActivity = BankActivity.POTION;
                    return Utility.getLoopReturn();
                }
                if (Utility.getEssenceCount() <= 18) {
                    //Log.info("Essence Count Low");
                    Bank.withdraw(ObjectId.PURE_ESSENCE, Bank.Amount.ALL);
                    //Log.fine("Essence Withdrawn");
                }
                if (Utility.getEssenceCount() >= 18 && Utility.getPouchVarpbitItem() != ObjectId.POUCH_VARPBIT_FULL) {
                    //Log.info("Need to fill Pouch");
                    Utility.getInvPouch().click("Fill");
                    Condition.wait(() -> Utility.getEssenceCount() <= 18, 50, 200);
                    //Log.fine("Filled Pouch");
                }
                return Utility.getLoopReturn();
            }
            case POTION -> {
                //Log.info("Bank-Potion");
                Utility.setTask("Potion Time");
                if (Utility.getPotionVarpbit() > 40) {
                    Utility.depBankPotion();
                    if (Condition.wait(() -> !Utility.getInvPotion().valid(), 100, 200)) {
                        //Log.info("Potion Deposited");
                        localActivity = BankActivity.BANKING;
                    }
                    return Utility.getLoopReturnQuick();
                } else {
                    Utility.setTask("Drinking Potion");
                    //Log.info("Sip Potion");
                    if (!Utility.getInvPotion().valid()) {
                        //Log.info("Grabbing Potion");
                        Utility.getBankPotion();
                        if (Condition.wait(() -> Utility.getInvPotion().valid(), 100, 200)) {
                            //Log.fine("Got Potion & Drink");
                            Utility.getInvPotion().click("Drink");
                            if(Condition.wait(() -> Utility.getPotionVarpbit() > 40, 100, 200)) {
                                //Log.fine("Drank");
                            } else {
                                //Log.severe("No more stamina");
                                Utility.setStopping(true);
                            }
                        } else {
                            //Log.severe("No more stamina");
                            Utility.setStopping(true);
                        }
                    } else {
                        //Log.fine("Got Potion & Drink");
                        Utility.getInvPotion().click("Drink");
                        if(Condition.wait(() -> Utility.getPotionVarpbit() > 40, 100, 200)) {
                            //Log.fine("Drank");
                        } else {
                            //Log.severe("No more stamina");
                            Utility.setStopping(true);
                        }
                    }
                    return Utility.getLoopReturnLong();
                }
            }
            case HEAL -> {
                //Log.info("Bank - Heal");
                Utility.setTask("Healing Time");
                if (!Utility.healthLoss() && !Utility.getInvFood().valid()) {
                    //Log.info("Done Eating");
                    localActivity = BankActivity.BANKING;
                    return Utility.getLoopReturnQuick();
                } else {
                    Utility.setTask("Eating");
                    Item food = Utility.getInvFood();
                    if (!food.valid()) {
                        //Log.info("Grabbing Food");
                        Bank.withdraw(ObjectId.FOOD, Bank.Amount.ONE);
                    } else {
                        if (Condition.wait(() -> Utility.getInvFood().valid(), 100, 200)) {
                            //Log.fine("Got Food");
                            int curhealth = Utility.health();
                            food.click("Eat");
                            Condition.wait(() -> Utility.health() != curhealth, 50, 20);
                            //Log.fine("Eaten");
                        } else{
                            //Log.severe("No More Food?");
                            return Utility.getLoopReturnQuick();
                        }
                    }
                }
                return Utility.getLoopReturn();
            }
        }
        return Utility.getLoopReturn();
    }
}
