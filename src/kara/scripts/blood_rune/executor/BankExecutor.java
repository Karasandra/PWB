package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;



public class BankExecutor extends ActivityExecutor {

    private BankActivity localActivity = BankActivity.RETURNING;

    enum BankActivity {
        BANKING,
        RETURNING,
        POTIONING
    }

    @Override
    public int execute() {

        switch (localActivity) {

            case RETURNING:
                Log.info("Bank-Returning");
                if (!Location.MYTH_GUID_UPPER.contains(Players.local().tile()) && !Location.MYTH_GUILD_LOWER.contains(Players.local().tile())) {
                    Log.info("Location Xupper-Xlower");
                    Utility.setTask("Returning to Bank");
                    Item cape = Inventory.stream().id(Utility.MYTH_CAPE).first();
                    if (!cape.valid()) {
                        Log.severe("No Cape!");
                        Utility.setStopping(true);
                    }
                    if (cape.valid()) {
                        Log.fine("Cape Found");
                        cape.interact("teleport");
                        if (Condition.wait(() -> Location.MYTH_GUILD_LOWER.contains(Players.local().tile()), 50, 1500)) {
                            Log.fine("teleport successful");
                        }
                        else {
                            Log.severe("Teleport failed");
                            Utility.setStopping(true);
                        }
                    }
                }
                if (Location.MYTH_GUILD_LOWER.contains(Players.local().tile())) {
                    Utility.setTask("Walking to bank");
                    Log.info("Location lower");
                    Movement.walkTo(Location.MYTH_GUID_UPPER.getRandomTile());
                    if (Condition.wait(() -> Location.MYTH_GUID_UPPER.contains(Players.local().tile()), 50, 1500)) {
                        Log.fine("walk successful");
                    }
                    else {
                        Log.severe("walk failed");
                        Utility.setStopping(true);
                    }
                }
                if (Location.MYTH_GUID_UPPER.contains(Players.local().tile())) {
                    Log.fine("Successful return");
                    localActivity = BankActivity.POTIONING;
                }
                return Utility.getLoopReturn();

            case BANKING:
                Log.info("Bank-Banking");
                if (Utility.getPouchVarpbit() == 30 && Inventory.isFull()) {
                    Utility.setActivity(Activity.WALK);
                    return Utility.getLoopReturn();
                }

            case POTIONING:
                Log.info("Bank-Potioning");
                Utility.setTask("Potion Check");
                if(Utility.getPotionVarpbit() > 40) {
                    // Stamina potion is active
                    Log.info("Potion is Good.");
                    localActivity = BankActivity.BANKING;
                } else {
                    Utility.setTask("Drinking Potion!");
                    // stamina potion is not active
                    Log.info("Need to Sip Potion.");
                }
                return Utility.getLoopReturn();
        }

        return Utility.getLoopReturnQuick();
    }

}
