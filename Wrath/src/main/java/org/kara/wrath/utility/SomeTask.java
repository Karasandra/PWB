package org.kara.wrath.utility;

import com.google.common.eventbus.Subscribe;
import org.powbot.api.event.BreakEvent;

public class SomeTask {
    @Subscribe
    void breakEvent(BreakEvent breakEvent) {
        boolean walking = Utility.myTile(Location.MYTH_ALTER);
        if (!walking && Utility.getTask().equals("Going Underground")) {
            breakEvent.delay(5000);
        } else {
            breakEvent.accept();
        }
    }
}
