package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Players;
import org.powbot.api.rt4.Varpbits;


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
                if (!Location.MYTH_GUID_UPPER.contains(Players.local().tile())) {
                    if (Inventory) {
                        Log.severe("No Cape!");
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                }

                if (Location.MYTH_GUID_UPPER.contains(Players.local().tile())) {
                    localActivity = BankActivity.POTIONING;
                    return Utility.getLoopReturn();
                }
                return Utility.getLoopReturn();

            case BANKING:
                if (Inventory.isFull()) {}
                Utility.setActivity(Activity.WALK);
                return Utility.getLoopReturn();

            case POTIONING:
                if(Varpbits.varpbit(277) > 0) {
                    // Stamina potion is active
                    Log.info("Potion is Good.");
                } else {
                    Utility.setTask("Drinking Potion!");
                    // stamina potion is not active
                    Log.info("Need to Sip Potion.");
                }
                return Utility.getLoopReturn();

        }
        return 0;
    }

}
