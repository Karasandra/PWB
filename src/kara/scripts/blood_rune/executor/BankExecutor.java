package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.Locatable;
import org.powbot.api.rt4.*;


public class BankExecutor extends ActivityExecutor {

    private BankActivity localActivity = BankActivity.BANKING;

    enum BankActivity {
        BANKING,
        POTIONING
    }

    @Override
    public int execute() {
        Log.info("Bank Executor");
        if (!Location.MYTH_GUILD_UPPER.contains(Players.local().tile()) && !Location.CRAFT_GUILD.contains(Players.local().tile())) {
            Log.severe("Not at bank");
            Utility.setActivity(Activity.RETURN);
            return Utility.getLoopReturnQuick();
        }

        switch (localActivity) {
            case BANKING -> {
                Log.info("Bank-Banking");
                Utility.setTask("Bank Management");
                Locatable bank = Bank.nearest();
                if (!Bank.inViewport()) {
                    Camera.turnTo(bank);
                }
                if (!Bank.open()) {
                    Log.severe("Bank failed to open");
                    Utility.setStopping(true);
                }
                if (!Condition.wait(Bank::opened, 250, 100)) {
                    Log.severe("Bank failed to open");
                    Utility.setStopping(true);
                    return Utility.getLoopReturnQuick();
                }
                if (Utility.getPouchVarpbitItem() == Utility.POUCH_VARPBIT_FULL && Inventory.isFull()) {
                    Log.fine("Inv Good");
                    Utility.setActivity(Activity.WALK);
                    CraftExecutor.EXTRACT_COUNT = 0;
                    return Utility.getLoopReturnQuick();
                }
                if (Utility.getPotionVarpbit() <= 40) {
                    Log.info("Potion Low");
                    localActivity = BankActivity.POTIONING;
                    return Utility.getLoopReturnQuick();
                }
                if (Utility.getInvBloodRune().valid()) {
                    Log.info("Have Blood Runes");
                    Bank.deposit(Utility.BLOOD_RUNE, Bank.Amount.ALL);
                    Log.info("Depositing Runes");
                    Condition.wait(() -> !Utility.getInvBloodRune().valid(), 50, 100);
                    Log.fine("Done Depositing");
                }
                Item bloodEssenceI = Inventory.stream().id(Utility.BLOOD_ESSENCE_INERT).first();
                Item bloodEssenceA = Inventory.stream().id(Utility.BLOOD_ESSENCE_ACTIVE).first();
                if (!bloodEssenceA.valid() && !bloodEssenceI.valid()) {
                    Log.info("No Blood Essence");
                    Bank.withdraw(Utility.BLOOD_ESSENCE_INERT, Bank.Amount.ONE);
                }
                if (bloodEssenceI.valid()) {
                    Log.info("Blood Essence Inert");
                    bloodEssenceI.interact("activate");
                }
                if (Utility.getEssenceCount() <= 18) {
                    Log.info("Essence Count Low");
                    Bank.withdraw(Utility.PURE_ESSENCE, Bank.Amount.ALL);
                    Condition.wait(() -> Utility.getEssenceCount() >= 18, 50, 200);
                    Log.fine("Essence Withdrawn");
                }
                if (Utility.getEssenceCount() >= 18 && Utility.getPouchVarpbitItem() != Utility.POUCH_VARPBIT_FULL) {
                    Log.info("Need to Fill Pouch");
                    Inventory.stream().id(Utility.POUCH_ITEM).action("Fill");
                    Condition.wait(() -> Utility.getEssenceCount() <= 18, 50, 200);
                    Log.fine("Filled Pouch");
                }
                return Utility.getLoopReturn();
            }
            case POTIONING -> {
                Log.info("Bank-Potioning");
                Utility.setTask("Potion Time");
                if (Utility.getPotionVarpbit() > 40) {
                    Bank.deposit(Utility.POTION_ITEM, Bank.Amount.ALL);
                    Condition.wait(() -> !Utility.getInvPotion().valid(), 100, 200);
                    Log.info("Potion Deposited");
                    localActivity = BankActivity.BANKING;
                    return Utility.getLoopReturnQuick();
                } else {
                    Utility.setTask("Drinking Potion!");
                    Log.info("Need to Sip Potion.");
                    if (!Utility.getInvPotion().valid()) {
                        Log.info("Grabbing Potion");
                        Bank.withdraw(Utility.POTION_ITEM, Bank.Amount.ONE);
                        Condition.wait(() -> Utility.getInvPotion().valid(), 100, 200);
                        Log.fine("Grabbed Potion");
                    }
                    if (Utility.getInvPotion().valid()) {
                        Log.info("Drinking");
                        Inventory.stream().id(Utility.POTION_ITEM).first().click();
                        Condition.wait(() -> Utility.getPotionVarpbit() > 40, 100, 200);
                        Log.fine("Drank Potion");
                    }
                }
                return Utility.getLoopReturn();
            }
        }
        return Utility.getLoopReturn();
    }

}
