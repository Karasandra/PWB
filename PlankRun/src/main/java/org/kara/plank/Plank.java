package org.kara.plank;

import org.kara.plank.executor.BankExecutor;
import org.kara.plank.executor.CraftExecutor;
import org.kara.plank.executor.WalkExecutor;
import org.kara.plank.utility.Config;
import org.kara.plank.utility.Log;
import org.kara.plank.utility.Utility;
import org.powbot.api.rt4.Camera;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Movement;
import org.powbot.api.script.*;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.mobile.service.ScriptUploader;

import static java.lang.System.exit;


@ScriptManifest(
        name = "Plank Crafting",
        description = "Crafts Planks Manually",
        version =  "0.0.1",
        category = ScriptCategory.Crafting,
        author = "Karasandra"
)


@ScriptConfiguration.List(
        {
                @ScriptConfiguration(
                        name = "Plank Options",
                        optionType = OptionType.STRING,
                        allowedValues = {"Plank", "Oak", "Teak", "Mahogany"},
                        description = "What banking method would you like to use?",
                        defaultValue = "Plank"
                ),
                @ScriptConfiguration(
                        name = "Example",
                        description = "Example GUI option",
                        optionType = OptionType.INFO)
        }
)


public class  Plank extends AbstractScript {


    private final BankExecutor bankExecutor = new BankExecutor();
    private final WalkExecutor walkExecutor = new WalkExecutor();
    private final CraftExecutor craftExecutor = new CraftExecutor();




    public static void main(String[] args) {
        //new Plank().startScript();
        new ScriptUploader().uploadAndStart("Plank", "Kara", "127.0.0.1:5585", true, false);
    }

    @Override
    public void onStart() {
        Paint paint = PaintBuilder.newBuilder()
                .removeScriptNameVersion()
                .addString("Task: ", Utility::getTask)
                .trackInventoryItem(Config.getCraftChoice(), "Planks", TrackInventoryOption.QuantityChange)
                .x(30)
                .y(65)
                .build();
        addPaint(paint);
        Camera.turnTo(Utility.yawReg, Utility.pitch);
        setConfig();
    }

    private void setConfig() {
        Object craftChoice = getOption("Plank Options");

        if(craftChoice == null) {
            Log.severe("GUI Value Null");
            Utility.setStopping(true);
            return;
        }

        Config.setCraftChoice((String) craftChoice);
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
        if (Utility.needsToRun()) {
            Utility.setTask("Enabling run");
            Movement.running(true);
            return Utility.getLoopReturn();
        }
        Utility.tabEquip();

        //Global Conditions End

        //Activity Starts
        return switch (Utility.getActivity()) {
            case BANK -> bankExecutor.execute();
            case WALK -> walkExecutor.execute();
            case CRAFT -> craftExecutor.execute();
        };
        //Activity Ends

    }
}