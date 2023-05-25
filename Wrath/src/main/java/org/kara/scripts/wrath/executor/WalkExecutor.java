package org.kara.scripts.wrath.executor;



import org.kara.scripts.wrath.utility.Location;
import org.kara.scripts.wrath.utility.Log;
import org.kara.scripts.wrath.utility.ObjectId;
import org.kara.scripts.wrath.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Players;

public class WalkExecutor extends ActivityExecutor {


    @Override
    public int execute() {
        Log.info("Walk Executor");
        if (!Location.MYTH_GUILD_LOWER.contains(Players.local().tile()) && Utility.tpCape == 0) {
            Utility.tpCape = 1;
            Utility.tele();
            return Utility.getLoopReturnQuick();
        }
        if (Utility.getPouchVarpbitItem() == ObjectId.POUCH_VARPBIT_FULL && Inventory.isFull()) {
            if (Location.MYTH_ALTER.contains(Players.local().tile())) {
                Log.fine("Got to Alter");
                Utility.getObject(ObjectId.MYTH_ALTER).click();
                Condition.wait(() -> Location.WRATH_ALTER.contains(Players.local().tile()), 100, 250);
                Utility.setActivity(Activity.CRAFT);
                Utility.tpCape = 0;
                return Utility.getLoopReturn();
            } else {
                Log.fine("Going to Alter");
                Utility.go(Location.WRATH_ALTER, ObjectId.WRATH_ALTER);
                return Utility.getLoopReturnLong();
            }
        } else {
            if (Location.MYTH_GUILD_UPPER.contains(Players.local().tile())) {
                Log.fine("Got to Bank");
                Utility.setActivity(Activity.BANK);
                Utility.tpCape = 0;
                return Utility.getLoopReturn();
            } else {
                Log.fine("Going to Bank");
                Utility.go(Location.MYTH_GUILD_UPPER, ObjectId.BANK);
                return Utility.getLoopReturnLong();
            }
        }
    }
}
