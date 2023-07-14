package org.kara.plank.executor;

import org.kara.plank.utility.Config;
import org.kara.plank.utility.Location;
import org.kara.plank.utility.ObjectId;
import org.kara.plank.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.stream.widget.ComponentStream;

public class WalkExecutor extends ActivityExecutor {
    private WalkActivity localActivity = WalkActivity.CRAFT;

    enum WalkActivity {
        CRAFT,
        RETURN
    }

    @Override
    public int execute() {
        if (Utility.getInv(Config.getLogChoice()).valid()) { localActivity = WalkActivity.CRAFT; } else { localActivity = WalkActivity.RETURN; }


        switch (localActivity) {
            case CRAFT -> {
                if (Utility.myTile(Location.CASTLE_WARS) || Utility.myTile(Location.WOOD_STORAGE)) {
                    GameObject balloon = Utility.getObject(ObjectId.BALLOON);
                    if (balloon.valid() && balloon.inViewport()) {
                        balloon.click("Travel");
                        Condition.wait(() -> Utility.getIdle(), 250, 6);
                        ComponentStream map = Components.stream().widget(ObjectId.MAP).action("Varrock");
                        if (map.first().visible()) {
                            map.first().click();
                            Condition.wait(() -> Utility.myTile(Location.LUMBER_YARD), 50, 10);
                            return Utility.getLoopReturnQuick();
                        }
                        if (Chat.chatting()) {
                            Utility.setActivity(Activity.RESUPPLY);
                            return Utility.getLoopReturn();
                        }
                    }
                }
                if (Utility.myTile(Location.LUMBER_YARD)) {
                    Utility.setActivity(Activity.CRAFT);
                    return Utility.getLoopReturnQuick();
                }
                Utility.step(Location.WOOD_STORAGE);
                return Utility.getLoopReturnLong();
            }
            case RETURN -> {
                Utility.tabEquip();
                if (Condition.wait(() -> Game.tab() == Game.Tab.EQUIPMENT, 50, 10)) {
                    Utility.useRing();
                    if (Condition.wait(() -> Utility.myTile(Location.CASTLE_WARS), 100, 10)) {
                        Utility.setActivity(Activity.BANK);
                    }
                }
                return Utility.getLoopReturn();
            }
        }
        return Utility.getLoopReturn();
    }
}
