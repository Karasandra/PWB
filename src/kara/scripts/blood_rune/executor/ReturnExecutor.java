package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.*;
import org.powbot.api.Condition;
import org.powbot.api.Locatable;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.walking.local.LocalPath;
import org.powbot.api.rt4.walking.local.LocalPathFinder;

public class ReturnExecutor extends ActivityExecutor {


        private ReturnActivity localacvtivity =  ReturnActivity.TELEPORT;

        enum ReturnActivity {
            TELEPORT,
            WALK
        }

    @Override
    public int execute() {
            Log.info("Return Executor");

        switch (localacvtivity) {
            case TELEPORT -> {
                Log.info("Teleporting");
                Utility.setTask("Teleporting to Bank");
                Item cape = Inventory.stream().id(Config.getRetMethod()).first();
                Tile myTile = Utility.myTile();
                if (!cape.valid()) {
                    Log.severe("No Teleport");
                    Utility.setStopping(true);
                    return Utility.getLoopReturnQuick();
                }
                if (cape.valid()) {
                    Log.fine("Cape Found");
                    cape.interact("teleport");
                    if (Condition.wait(() -> myTile != Utility.myTile(), 50, 1000   )) {
                        Log.fine("teleport successful");
                        localacvtivity = ReturnActivity.WALK;
                    } else {
                        Log.severe("teleport failed");
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                }
                return Utility.getLoopReturnLong();
            }
            case WALK -> {
                Log.info("Walking to Bank");
                Utility.setTask("Walking to Bank");
                Locatable bank = Bank.nearest();
                if (!bank.isRendered() || !bank.reachable()) {
                    Log.info("Finding Local Path");
                    LocalPath localPath = LocalPathFinder.INSTANCE.findPath((Tile) bank);
                    if (localPath.isNotEmpty()) {
                        localPath.traverseUntilReached(50, Utility::getIdle);
                    } else {
                        Log.info("Finding WW Path");
                        Movement.moveToBank();
                    }
                    if (Condition.wait(Utility::getIdle, 50, 1000)) {
                        Log.fine("Walked to bank");
                        Utility.setActivity(Activity.BANK);
                        return Utility.getLoopReturn();
                    }
                } else {
                    Log.fine("Already at bank");
                    Utility.setActivity(Activity.BANK);
                }
                return Utility.getLoopReturnLong();
            }

        }
            return Utility.getLoopReturn();
    }
}
