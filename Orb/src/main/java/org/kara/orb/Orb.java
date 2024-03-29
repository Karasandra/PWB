package org.kara.orb;


import org.kara.orb.executor.BankExecutor;
import org.kara.orb.executor.CraftExecutor;
import org.kara.orb.executor.WalkExecutor;
import org.kara.orb.utility.BreakTime;
import org.kara.orb.utility.ObjectId;
import org.kara.orb.utility.Utility;
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
        name = "Orb",
        description = "Orb",
        version =  "0.0.1",
        category = ScriptCategory.Crafting,
        author = "Karasandra"
)

@ScriptConfiguration(name = "Example", description = "Example GUI option", optionType = OptionType.INFO)


public class  Orb extends AbstractScript {


    private final BankExecutor bankExecutor = new BankExecutor();
    private final WalkExecutor walkExecutor = new WalkExecutor();
    private final CraftExecutor craftExecutor = new CraftExecutor();




    public static void main(String[] args) {
        //new Orb().startScript();
        new ScriptUploader().uploadAndStart("Orb", "Kara", "127.0.0.1:5585", true, false);
    }

    @Override
    public void onStart() {
        Paint paint = PaintBuilder.newBuilder()
                .removeScriptNameVersion()
                .addString("Task: ", Utility::getTask)
                .trackInventoryItem(ObjectId.FIRE_ORB, "Fire Orbs", TrackInventoryOption.QuantityChange)
                .x(30)
                .y(65)
                .build();
        addPaint(paint);
        Camera.turnTo(Utility.yawReg, Utility.pitch);
        BreakTime.onBreakEvent(null);
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