package kara.scripts.bone_collector.executor;

import kara.scripts.api.Log;
import kara.scripts.bone_collector.utility.Activity;
import kara.scripts.bone_collector.utility.Location;
import kara.scripts.bone_collector.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.Tile;
import org.powbot.api.rt4.Players;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Objects;


public class BankExecutor extends ActivityExecutor {

    private BankActivity localActivity = BankActivity.WALKING;

    enum BankActivity {
        WALKING,
        BANKING,
        RETURNING
    }

    @Override
    public int execute() {


        switch (localActivity) {
            case WALKING:
                if (Inventory.isEmpty()) {
                    Log.fine("Inv Empty time to go Collect.");
                    Utility.setTask("Returning");
                    localActivity = BankActivity.RETURNING;
                    return Utility.getLoopReturn();
                }

                if (Location.EDGEVILLE_BANK.contains(Players.local().tile()) && Inventory.isFull()) {
                    Log.fine("We are at the bank.");
                    Utility.setTask("Banking");
                    localActivity = BankActivity.BANKING;
                    return Utility.getLoopReturn();
                }
                Movement.walkTo(Location.EDGEVILLE_BANK.getRandomTile());
                if (!Condition.wait(() -> Location.EDGEVILLE_BANK.contains(Players.local()), 50, 1500)) {
                    Utility.setTask("????WE FUCKED????");
                    Utility.setStopping(true);
                    return Utility.getLoopReturn();
                }
                Log.info("That is odd.");
                return Utility.getLoopReturn();

            case BANKING:
                if (Inventory.isNotEmpty()) {
                    Log.fine("We arrived at bank with full inv.");
                    Utility.setTask("Opening Bank");

                    GameObject booth = Objects.stream().id(Utility.BOOTH).nearest().first();
                    if (!booth.inViewport()) {
                        Camera.turnTo(booth);
                    }
                    if (!Bank.opened() && !Bank.open()) {
                        Log.severe("Failed to Bank");
                        return Utility.getLoopReturn();
                    }
                    if (!Condition.wait(Bank::opened, 50, 1500)) {
                        Log.severe("Bank failed to open");
                        return Utility.getLoopReturn();
                    }

                    Log.fine("Opened Bank");
                    Utility.setTask("Depositing");

                    if (!Bank.depositInventory()) {
                        Log.severe("Failed to deposit");
                        return Utility.getLoopReturn();
                    }
                    if (!Condition.wait(Inventory::isEmpty, 50, 60)) {
                        Log.severe("Deposit failed");
                        return Utility.getLoopReturn();
                    }
                    Log.fine("Deposited");
                    localActivity = BankActivity.RETURNING;
                    return Utility.getLoopReturn();
                }
                    // WidgetActionEvent(widgetId=786473, interaction=Deposit inventory, name=,id=1)
                Log.fine("We already deposited.");
                localActivity = BankActivity.WALKING;
                return Utility.getLoopReturn();


            case RETURNING:
                localActivity = BankActivity.WALKING;

                if (Inventory.isEmpty()) {
                    Movement.walkTo(Location.DUNGEON_AREA.getRandomTile());
                    Condition.wait(() -> Location.DUNGEON_AREA.contains(Players.local()), 50, 1500);
                    Utility.setActivity(Activity.COLLECT);
                    return Utility.getLoopReturn();
                }

                Log.info("Inventory is not empty");
                Utility.setTask("????");
                Utility.setStopping(true);
                return Utility.getLoopReturn();
        }

        return Utility.getLoopReturn();
    }
}
