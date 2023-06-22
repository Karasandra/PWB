package org.kara.orb.executor;

import org.kara.orb.utility.Location;
import org.kara.orb.utility.ObjectId;
import org.kara.orb.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Item;

public class WalkExecutor extends ActivityExecutor {

    @Override
    public int execute() {
        Utility.setTask("Walk Time");
        Utility.tabInv();
        Utility.poisonCure();
        Utility.eating();
        Item orb = Utility.getInv(ObjectId.UNPOWERED_ORB);
        if (orb.valid() && Utility.myTile(Location.PILLAR)) {
            Utility.setActivity(Activity.CRAFT);
            return Utility.getLoopReturnQuick();
        }
        if (orb.valid()) {
            if (!Utility.myTile(Location.TAVERLEY_DUNGEON)) {
                Utility.move(Location.TAVERLEY_DUNGEON_ENTRANCE_WALK);
            } else {
                if (Utility.myTile(Location.TAVERLEY_DUNGEON_ENTRANCE)) {
                    Utility.getObject(ObjectId.PIPE).click("Squeeze-through");
                    Condition.wait(() -> !Utility.myTile(Location.TAVERLEY_DUNGEON_ENTRANCE), 50, 20);
                    return Utility.getLoopReturnLong();
                } else {
                    if (Utility.myTile(Location.TAVERLEY_DUNGEON_RIGHT)) {
                        Utility.step(Location.TAVERLEY_DUNGEON_SPIDER);
                        Condition.wait(() -> Utility.myTile(Location.TAVERLEY_DUNGEON_SPIDER), 100, 4);
                        return Utility.getLoopReturn();
                    } else {
                        Utility.step(Location.PILLAR);
                    }
                }
            }
            return Utility.getLoopReturnXLong();
        }
        if (!orb.valid() && !Utility.myTile(Location.FALADOR)) {
            Utility.tele();
        }
        if (!orb.valid() && Utility.myTile(Location.FALADOR) && !Utility.myTile(Location.BANK)) {
            Utility.step(Location.BANK);
            return Utility.getLoopReturnXLong();
        }
        if (Utility.myTile(Location.BANK) && !orb.valid()) {
            Utility.setActivity(Activity.BANK);
            return Utility.getLoopReturnQuick();
        }
        return Utility.getLoopReturn();
    }
}
