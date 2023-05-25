package kara.scripts.wrath.executor;



import kara.scripts.wrath.utility.Location;
import kara.scripts.wrath.utility.Log;
import kara.scripts.wrath.utility.ObjectId;
import kara.scripts.wrath.utility.Utility;
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
        Log.info("Bank Executor");
    if (!Location.MYTH_GUILD_UPPER.contains(Players.local().tile())) {
        Log.severe("Not at bank");
        Utility.setActivity(Activity.WALK);
        return Utility.getLoopReturnQuick();
    }

        switch (localActivity) {
            case BANKING -> {
                Log.info("Bank-Bank");
                Utility.setTask("Bank Time");
                Locatable bank = Bank.nearest();
                if (!Bank.inViewport()) {
                    Camera.turnTo(bank);
                }
                if (!Bank.open()) {
                    Log.severe("Bank failed to click");
                    Utility.setStopping(true);
                    return Utility.getLoopReturnQuick();
                }
                if (!Condition.wait(Bank::opened, 250, 100)) {
                    Log.severe("Bank failed to open");
                    Utility.setStopping(true);
                    return  Utility.getLoopReturnQuick();
                }
                if (Utility.getPouchVarpbitItem() == ObjectId.POUCH_VARPBIT_FULL && Inventory.isFull()) {
                    Log.fine("Inv Good");
                    Utility.setActivity(Activity.WALK);
                    return Utility.getLoopReturn();
                }
                if (Utility.getInvWrathRune().valid()) {
                    Log.info("Depositing Runes");
                    Bank.deposit(ObjectId.WRATH_RUNE, Bank.Amount.ALL);
                    Condition.wait(() -> !Utility.getInvWrathRune().valid(), 50,100);
                }
                if (Utility.healthLoss()) {
                    Log.info("Healing");
                    localActivity = BankActivity.HEAL;
                    return Utility.getLoopReturn();
                }
                if (Utility.getPotionVarpbit() <= 40) {
                    Log.info("Potion Low");
                    localActivity = BankActivity.POTION;
                    return Utility.getLoopReturn();
                }
                if (Utility.getEssenceCount() <= 18) {
                    Log.info("Essence Count Low");
                    Bank.withdraw(ObjectId.PURE_ESSENCE, Bank.Amount.ALL);
                    Log.fine("Essence Withdrawn");
                }
                if (Utility.getEssenceCount() >= 18 && Utility.getPouchVarpbitItem() != ObjectId.POUCH_VARPBIT_FULL) {
                    Log.info("Need to fill Pouch");
                    Inventory.stream().id(ObjectId.POUCH_ITEM).action("Fill");
                    Condition.wait(() -> Utility.getEssenceCount() <= 18, 50, 200);
                    Log.fine("Filled Pouch");
                }
                return Utility.getLoopReturn();
            }
            case POTION -> {
                Log.info("Bank-Potion");
                Utility.setTask("Potion Time");
                if (Utility.getPotionVarpbit() > 40) {
                    Bank.deposit(ObjectId.POTION_ITEM_4, Bank.Amount.ALL);
                    Bank.deposit(ObjectId.POTION_ITEM_3, Bank.Amount.ALL);
                    Bank.deposit(ObjectId.POTION_ITEM_2, Bank.Amount.ALL);
                    Bank.deposit(ObjectId.POTION_ITEM_1, Bank.Amount.ALL);
                    Log.info("Potion Deposited");
                    localActivity = BankActivity.BANKING;
                    return Utility.getLoopReturnQuick();
                } else {
                    Utility.setTask("Drinking Potion");
                    Log.info("Sip Potion");
                    Item potion = Utility.getInvPotion();
                    if (!potion.valid()) {
                        Log.info("Grabbing Potion");
                        Bank.withdraw("Stamina", Bank.Amount.ONE);
                        if (Condition.wait(() -> Utility.getInvPotion().valid(), 100, 200)) {
                            Log.fine("Got Potion & Drink");
                            potion.click("Drink");
                            Condition.wait(() -> Utility.getPotionVarpbit() > 40, 100, 200);
                            Log.fine("Drank");
                        } else {
                            Log.severe("No more stamina");
                            Utility.setStopping(true);
                            return Utility.getLoopReturnLong();
                        }
                    }
                }
                return Utility.getLoopReturn();
            }
            case HEAL -> {
                Log.info("Bank - Heal");
                Utility.setTask("Healing");
                if (Utility.healthLoss()) {
                    Log.info("Done Eating");
                    localActivity = BankActivity.BANKING;
                    return Utility.getLoopReturnQuick();
                } else {
                    Utility.setTask("Eating");
                    Item food = Utility.getInvFood();
                    if (!food.valid()) {
                        Log.info("Grabbing Food");
                        Bank.withdraw("Shark", Bank.Amount.ONE);
                        if (Condition.wait(() -> Utility.getInvFood().valid(), 100, 200)) {
                            Log.fine("Got Food");
                            food.click("Eat");
                            Condition.wait(() -> !Utility.healthLoss(), 100, 200);
                            Log.fine("Eaten");
                        } else {
                            Log.severe("No More Food");
                            Utility.setStopping(true);
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
