package org.kara.orb.executor;

import org.kara.orb.utility.Location;
import org.kara.orb.utility.ObjectId;
import org.kara.orb.utility.Utility;
import org.powbot.api.rt4.Item;

public class WalkExecutor extends ActivityExecutor {

    @Override
    public int execute() {
        Utility.setTask("Walk Time");
        Utility.tabInv();
        Item orb = Utility.getInv(ObjectId.UNPOWERED_ORB);
        if (orb.valid()) {
            Utility.move(Location.Pillar);
        }
        if (!orb.valid() && !Utility.myTile(Location.FALADOR)) {
            Utility.tele();
        }
        if (!orb.valid() && Utility.myTile(Location.FALADOR) && !Utility.myTile(Location.BANK)) {
            Utility.step(Location.BANK);
        }
        if (Utility.myTile(Location.BANK) && !orb.valid()) {
            Utility.setActivity(Activity.BANK);
            return Utility.getLoopReturnQuick();
        }
        Utility.poisonCure();
        if (Utility.myTile(Location.Pillar)) {
            Utility.setActivity(Activity.CRAFT);
            return Utility.getLoopReturnQuick();
        }
        return Utility.getLoopReturn();
    }
}
