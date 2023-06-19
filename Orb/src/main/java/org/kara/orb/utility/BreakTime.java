package org.kara.orb.utility;

import com.google.common.eventbus.Subscribe;
import org.kara.orb.executor.Activity;
import org.powbot.api.event.BreakEvent;
import org.powbot.api.rt4.Bank;

public class BreakTime {
    @Subscribe
    public static void onBreakEvent(BreakEvent breakEvent) {
        if (breakEvent == null) {
            return;
        }
        if (!Utility.myTile(Location.PILLAR) && !Utility.myTile(Location.FALADOR) || Bank.opened() || Utility.getActivity() == Activity.WALK) {
            breakEvent.delay(15000);
        } else {
            breakEvent.accept();
        }
    }

    //@Subscribe
    //public void onGameTick(TickEvent tickEvent) {
    //    System.out.println("Game Tick");
    //}
}
