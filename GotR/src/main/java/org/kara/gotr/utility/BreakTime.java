package org.kara.gotr.utility;

import com.google.common.eventbus.Subscribe;
import org.kara.gotr.executor.Activity;
import org.powbot.api.event.BreakEvent;
import org.powbot.api.rt4.Bank;

public class BreakTime {
    @Subscribe
    public static void onBreakEvent(BreakEvent breakEvent) {
        if (breakEvent == null) {
            return;
        }
        if (Utility.getActivity().equals(Activity.WALK) || Bank.opened()) {
            breakEvent.delay(5000);
        } else {
            breakEvent.accept();
        }
    }

    //@Subscribe
    //public void onGameTick(TickEvent tickEvent) {
    //    System.out.println("Game Tick");
    //}
}
