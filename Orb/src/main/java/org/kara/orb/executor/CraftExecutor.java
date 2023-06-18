package org.kara.orb.executor;

import org.kara.orb.utility.ObjectId;
import org.kara.orb.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Magic;

public class CraftExecutor extends ActivityExecutor {

    @Override
    public int execute() {
        Utility.poisonCure();
        Utility.tabMagic();
        Magic.Spell.CHARGE_FIRE_ORB.cast();
        Utility.getObject(ObjectId.PILLAR).click();
        Condition.wait(() -> !Utility.getInv(ObjectId.UNPOWERED_ORB).valid(), 1000, 80);
        if (!Utility.getInv(ObjectId.UNPOWERED_ORB).valid()) {
            Utility.setActivity(Activity.WALK);
            return Utility.getLoopReturn();
        }
        if (!Utility.getInv(ObjectId.COSMIC_RUNE).valid()) {
            Utility.setStopping(true);
            return Utility.getLoopReturnQuick();
        }
        return Utility.getLoopReturnLong();
    }
}
