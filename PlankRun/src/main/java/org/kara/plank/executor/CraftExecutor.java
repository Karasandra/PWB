package org.kara.plank.executor;

import org.kara.plank.utility.Config;
import org.kara.plank.utility.ObjectId;
import org.kara.plank.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Components;
import org.powbot.api.rt4.stream.widget.ComponentStream;



public class CraftExecutor extends ActivityExecutor {

    @Override
    public int execute() {
        if (Utility.getInv(ObjectId.GOLD).getStack() <= Config.getGoldCut()) {
            Utility.setStopping(true);
            return Utility.getLoopReturnQuick();
        }
        Utility.getObject(ObjectId.WORKER).click("Craft");
        ComponentStream craft = Components.stream().widget(Config.getChat()).action("Craft");
        if (craft.first().valid()) {
            craft.first().click();
            if (Condition.wait(() -> Utility.getInv(Config.getCraftChoice()).valid(), 100, 10)) {
                Utility.setActivity(Activity.WALK);
                return Utility.getLoopReturnQuick();
            }
        }
        return Utility.getLoopReturn();
    }
}
