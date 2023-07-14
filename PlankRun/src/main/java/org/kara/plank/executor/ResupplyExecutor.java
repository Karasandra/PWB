package org.kara.plank.executor;

import org.kara.plank.utility.Config;
import org.kara.plank.utility.Location;
import org.kara.plank.utility.ObjectId;
import org.kara.plank.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.Locatable;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.Chat;
import java.util.List;



public class ResupplyExecutor extends ActivityExecutor {
    private ResupplyActivity localActivity = ResupplyActivity.BANK;

    enum ResupplyActivity {
        BANK,
        DEPOSIT
    }


    @Override
    public int execute() {

        switch (localActivity) {
            case BANK -> {
                if (Utility.getInv(ObjectId.FIRE_WOOD).valid()) {
                    localActivity = ResupplyActivity.DEPOSIT;
                    return Utility.getLoopReturn();
                }
                if (!Utility.myTile(Location.CASTLE_WARS)) {
                    Utility.step(Location.CASTLE_WARS);
                    return Utility.getLoopReturnLong();
                }
                if (!Bank.open()) {
                    //Log.severe("Bank failed to click");
                    return Utility.getLoopReturnQuick();
                }
                if (!Condition.wait(Bank::opened, 250, 100)) {
                    //Log.severe("Bank failed to open");
                    return Utility.getLoopReturnQuick();
                }
                if (Utility.getInv(Config.getLogChoice()).valid()) {
                    Bank.deposit(Config.getLogChoice(), Bank.Amount.ALL);
                    Condition.wait(() -> !Utility.getInv(Config.getLogChoice()).valid(), 50, 100);
                }
                if (!Utility.getInv(ObjectId.FIRE_WOOD).valid()) {
                    if (Bank.stream().id(ObjectId.FIRE_WOOD).isEmpty()) {
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                    Bank.withdrawModeNoted();
                    Bank.withdraw(ObjectId.FIRE_WOOD, Bank.Amount.valueOf("100"));
                    Condition.wait(() -> Utility.getInv(ObjectId.FIRE_WOOD).valid(), 50, 40);
                    Bank.withdrawModeQuantity();
                }
                return Utility.getLoopReturnQuick();
            }
            case DEPOSIT -> {
                if (!Utility.myTile(Location.WOOD_STORAGE)) {
                    Utility.step(Location.WOOD_STORAGE);
                    return Utility.getLoopReturnLong();
                }
                Condition.wait(() -> Utility.getObject(ObjectId.WOOD_STORAGE).click("Deposit"), 50, 40);
                if (Chat.chatting()) {
                    if (Chat.completeChat(String.valueOf(List.of("Hello", "Hi")))) {
                        Condition.wait(() -> !Chat.chatting(), 250, 8);
                    }
                }
                if (!Utility.getInv(ObjectId.FIRE_WOOD).valid()) {
                    localActivity = ResupplyActivity.BANK;
                    Utility.setActivity(Activity.BANK);
                    return Utility.getLoopReturn();
                }
                return Utility.getLoopReturnQuick();
            }
        }
        return Utility.getLoopReturnQuick();
    }
}
