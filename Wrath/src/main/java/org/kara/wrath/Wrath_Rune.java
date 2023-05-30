package org.kara.wrath;


import org.kara.wrath.executor.BankExecutor;
import org.kara.wrath.executor.CraftExecutor;
import org.kara.wrath.executor.WalkExecutor;
import org.kara.wrath.utility.BreakTime;
import org.kara.wrath.utility.ObjectId;
import org.kara.wrath.utility.Utility;
import org.powbot.api.Events;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.Game;
import org.powbot.api.script.*;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;

import static java.lang.System.exit;


@ScriptManifest(
        name = "Wrath Rune",
        description = "Crafts True Wrath Runes",
        version =  "0.0.1",
        category = ScriptCategory.Runecrafting,
        author = "Karasandra"
)

@ScriptConfiguration(name = "Example", description = "Example GUI option", optionType = OptionType.INFO)


public class Wrath_Rune extends AbstractScript {


    private final BankExecutor bankExecutor = new BankExecutor();
    private final WalkExecutor walkExecutor = new WalkExecutor();
    private final CraftExecutor craftExecutor = new CraftExecutor();



    public static void main(String[] args) {
        new Wrath_Rune().startScript();
        //new ScriptUploader().uploadAndStart("Wrath", "Kara", "127.0.0.1:5555", true, false);
    }

    @Override
    public void onStart() {
        Paint paint = PaintBuilder.newBuilder()
                .removeScriptNameVersion()
                .trackInventoryItem(ObjectId.WRATH_RUNE, "Wrath Runes", TrackInventoryOption.QuantityChange)
                .trackInventoryItem(ObjectId.WRATH_RUNE, "Gold", TrackInventoryOption.Price)
                .addString("Task: ", Utility::getTask)
                .x(30)
                .y(65)
                .build();
        addPaint(paint);
        Camera.turnTo(Utility.yawReg, Utility.pitch);
        Events.register(new BreakTime());
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
            exit(0);
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