package kara.scripts.wrath;


import kara.scripts.wrath.executor.BankExecutor;
import kara.scripts.wrath.executor.CraftExecutor;
import kara.scripts.wrath.executor.WalkExecutor;
import kara.scripts.wrath.utility.ObjectId;
import kara.scripts.wrath.utility.Utility;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Movement;
import org.powbot.api.script.*;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;

@ScriptManifest(
        name = "Wrath Rune",
        description = "Crafts True Wrath Runes",
        version =  "0.0.1",
        category = ScriptCategory.Runecrafting,
        author = "Karasandra"
)


public class Wrath_Rune extends AbstractScript {

    private final BankExecutor bankExecutor = new BankExecutor();
    private final WalkExecutor walkExecutor = new WalkExecutor();
    private final CraftExecutor craftExecutor = new CraftExecutor();

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        new kara.scripts.wrath.Wrath_Rune().startScript();
        //new ScriptUploader().uploadAndStart("Wrath Rune", "karasandra", "127.0.0.1:5555", true, false);
    }

    @Override
    public void onStart() {
        Paint paint = PaintBuilder.newBuilder()
                .removeScriptNameVersion()
                .withoutDiscordWebhook()
                .trackInventoryItem(ObjectId.WRATH_RUNE, "Blood Runes", TrackInventoryOption.QuantityChange)
                .trackInventoryItem(ObjectId.WRATH_RUNE, "Gold", TrackInventoryOption.values())
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
            return Utility.getLoopReturnQuick();
        }
        if (!Game.loggedIn()) {
            Utility.setTask("Not logged in");
            return Utility.getLoopReturnLong();
        }
        if (Game.tab() != Game.Tab.INVENTORY) {
            Utility.setTask("Opening inventory");
            Game.tab(Game.Tab.INVENTORY);
            return Utility.getLoopReturn();
        }

        if (Utility.needsToRun()) {
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