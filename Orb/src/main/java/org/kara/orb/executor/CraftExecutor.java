package org.kara.orb.executor;

import org.kara.orb.utility.ObjectId;
import org.kara.orb.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.Components;
import org.powbot.api.rt4.Magic;
import org.powbot.api.rt4.Skills;
import org.powbot.api.rt4.walking.model.Skill;

public class CraftExecutor extends ActivityExecutor {
    private CraftActivity localActivity = CraftActivity.INITIAL;

    enum CraftActivity {
        INITIAL,
        SECOND
    }

    @Override
    public int execute() {
        Utility.setTask("Craft Time");
        switch (localActivity) {
            case INITIAL -> {
                if (!Utility.getInv(ObjectId.UNPOWERED_ORB).valid()) {
                    Utility.setActivity(Activity.WALK);
                    return Utility.getLoopReturn();
                }
                Camera.turnTo(Utility.yawReg, Utility.pitch);
                Utility.poisonCure();
                Utility.tabMagic();
                Magic.Spell.CHARGE_FIRE_ORB.cast();
                Utility.getObject(ObjectId.PILLAR).click();
                Condition.wait(Utility::getIdle, 50, 60);
                localActivity = CraftActivity.SECOND;
                return Utility.getLoopReturnLong();
            }
            case SECOND -> {
                Utility.tabInv();
                if (!Utility.getInv(ObjectId.UNPOWERED_ORB).valid()) {
                    Utility.setActivity(Activity.WALK);
                    localActivity = CraftActivity.INITIAL;
                    return Utility.getLoopReturn();
                }
                Condition.wait(() -> !Utility.getInv(ObjectId.UNPOWERED_ORB).valid(), 100,10);
                Components.stream().widget(270).action("Charge").first().click();
                Condition.wait(() -> !Utility.getInv(ObjectId.UNPOWERED_ORB).valid(), 100,10);
                if (!Utility.getInv(ObjectId.COSMIC_RUNE).valid()) {
                    Utility.setStopping(true);
                    return Utility.getLoopReturnQuick();
                }
                if (Skills.timeSinceExpGained(Skill.Magic) >= 5000) {
                    localActivity = CraftActivity.INITIAL;
                    return Utility.getLoopReturn();
                }
            }
        }
        return Utility.getLoopReturn();
    }
}
