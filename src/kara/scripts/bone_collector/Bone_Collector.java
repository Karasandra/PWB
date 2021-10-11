package kara.scripts.bone_collector;


import kara.scripts.bone_collector.executor.BankExecutor;
import kara.scripts.bone_collector.executor.CollectExecutor;
import kara.scripts.bone_collector.utility.Utility;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Movement;
import org.powbot.api.script.AbstractScript;
import org.powbot.api.script.ScriptManifest;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.mobile.service.ScriptUploader;
import java.util.concurrent.Callable;


@ScriptManifest(
        name = "Bone Collector",
        description = "Collects bones at Edgeville dungeon",
        version =  "0.0.1"
)


public class Bone_Collector extends AbstractScript {

    private final BankExecutor bankExecutor = new BankExecutor();
    private final CollectExecutor collectExecutor = new CollectExecutor();

    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("Bone Collector", "karasandra", "127.0.0.1:5559", true, false);
    }

    @Override
    public void onStart() {
        Paint p = new PaintBuilder()
                .trackInventoryItem(Utility.BONE, "Bones", TrackInventoryOption.QuantityChange)
                .addString("Task: ", (Callable<String>) Utility::getTask)
                .x(30)
                .y(65)
                .build();

        addPaint(p);
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

        if (Utility.needsToRun()) {
            Utility.setTask("Enabling run");
            Movement.running(true);
            return Utility.getLoopReturn();
        }

        //GLOBAL CONDITIONS (End)

        //ACTIVITIES (Start)
        switch (Utility.getActivity()) {
            case BANK:
                return bankExecutor.execute();
            case COLLECT:
                return collectExecutor.execute();
        }
        //ACTIVITIES (End)

        return Utility.getLoopReturn();
    }

}



