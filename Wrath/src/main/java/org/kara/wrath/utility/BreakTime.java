package org.kara.wrath.utility;

import com.google.common.eventbus.Subscribe;
import org.powbot.api.event.BreakEvent;
import org.powbot.api.rt4.Bank;

public class BreakTime {
    @Subscribe
    public static void onBreakEvent(BreakEvent breakEvent) {
        if (breakEvent == null) {
            return;
        }
        boolean walking = Utility.myTile(Location.MYTH_ALTER);
        if (!walking && Utility.getTask().equals("Going Underground") || Bank.opened()) {
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