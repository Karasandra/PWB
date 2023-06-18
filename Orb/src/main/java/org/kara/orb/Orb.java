package org.kara.orb;


import org.kara.orb.executor.CraftExecutor;
import org.kara.orb.executor.StartExecutor;
import org.kara.orb.executor.WalkExecutor;
import org.kara.orb.utility.BreakTime;
import org.kara.orb.utility.Utility;
import org.powbot.api.rt4.Bank;
import org.powbot.api.rt4.Game;
import org.powbot.api.script.*;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;

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


    private final StartExecutor startExecutor = new StartExecutor();
    private final WalkExecutor walkExecutor = new WalkExecutor();
    private final CraftExecutor craftExecutor = new CraftExecutor();




    public static void main(String[] args) {
        new Orb().startScript();
        //new ScriptUploader().uploadAndStart("Gotr", "Kara", "127.0.0.1:5555", true, false);
    }

    @Override
    public void onStart() {
        Paint paint = PaintBuilder.newBuilder()
                .removeScriptNameVersion()
                .addString("Task: ", Utility::getTask)
                .x(30)
                .y(65)
                .build();
        addPaint(paint);
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
        if (Game.tab() != Game.Tab.INVENTORY && !Bank.opened()) {
            Utility.setTask("Opening inventory");
            Game.tab(Game.Tab.INVENTORY);
            return Utility.getLoopReturn();
        }

        //Global Conditions End

        //Activity Starts
        return switch (Utility.getActivity()) {
            case START -> startExecutor.execute();
            case WALK -> walkExecutor.execute();
            case CRAFT -> craftExecutor.execute();
        };
        //Activity Ends

    }
}