package kara.scripts.blood_rune;


import kara.scripts.blood_rune.executor.BankExecutor;
import kara.scripts.blood_rune.executor.CraftExecutor;
import kara.scripts.blood_rune.executor.WalkExecutor;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Movement;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.mobile.service.ScriptUploader;

@ScriptManifest(
        name = "Blood Rune",
        description = "Blood Runes",
        version =  "0.0.1"
)


public class Blood_Rune extends AbstractScript {

    private final BankExecutor bankExecutor = new BankExecutor();
    private final WalkExecutor walkExecutor = new WalkExecutor();
    private final CraftExecutor craftExecutor = new CraftExecutor();

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("Blood Rune", "karasandra", "127.0.0.1:5559", true, true);
    }

    @Override
    public void onStart() {
        Paint paint = PaintBuilder.newBuilder()
                .withoutDiscordWebhook()
                .trackInventoryItem(Utility._BLOODRUNE, "Blood Runes", TrackInventoryOption.QuantityChange)
                .trackInventoryItem(Utility._BLOODRUNE, "Gold", TrackInventoryOption.values())
                .addString("Task: ", Utility::getTask)
                .x(30)
                .y(65)
                .build();
        addPaint(paint);
    }

    @Override
    public void poll() {
        try {
            Thread.sleep(loop());
        } catch (InterruptedException ignore) {
        }
    }

    public int loop() {
        //GLOBAL CONDITIONS (Start)
        if (Utility.isStopping()) {
            Utility.setTask("Stopping");
            this.controller.stop();
            return Utility.getLoopReturn();
        }
        if (!Game.loggedIn()) {
            Utility.setTask("Not logged in");
            return Utility.getLoopReturn();
        }
        if (Game.tab() != Game.Tab.INVENTORY) {
            Utility.setTask("Opening inventory");
            Game.tab(Game.Tab.INVENTORY);
            return Utility.getLoopReturn();
        }

        if (Movement.running() != Movement.running(true) ) {
            Utility.setTask("Enabling run");
            Movement.running(true);
            return Utility.getLoopReturn();
        }
            //Global Conditions End

            //Activity Starts
        return switch (Utility.getActivity()) {
            case WALK -> walkExecutor.execute();
            case BANK -> bankExecutor.execute();
            case CRAFT -> craftExecutor.execute();
        };
            //Activity Ends

    }
}
