package kara.scripts.blood_rune;


import kara.scripts.blood_rune.executor.BankExecutor;
import kara.scripts.blood_rune.executor.CraftExecutor;
import kara.scripts.blood_rune.executor.ReturnExecutor;
import kara.scripts.blood_rune.executor.WalkExecutor;
import kara.scripts.blood_rune.utility.Config;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.ObjectId;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.rt4.Game;
import org.powbot.api.rt4.Movement;
import org.powbot.api.script.*;
import org.powbot.api.script.paint.Paint;
import org.powbot.api.script.paint.PaintBuilder;
import org.powbot.api.script.paint.TrackInventoryOption;
import org.powbot.mobile.service.ScriptUploader;



@ScriptManifest(
        name = "Blood Rune",
        description = "Crafts True Blood Runes",
        version =  "0.0.1",
        category = ScriptCategory.Runecrafting,
        author = "Karasandra"
)

@ScriptConfiguration.List(
        {
        @ScriptConfiguration(
            name="Bank Method",
            optionType = OptionType.STRING,
            allowedValues = {"MYTH", "Craft", "Craft(T)"},
            description = "What banking method would you like to use?",
            defaultValue = "Myth"
            ),
        @ScriptConfiguration(
            name="Fairy Method",
            optionType = OptionType.STRING,
            allowedValues = {"QPC", "Construction", "Construction(T)"},
            description = "How do you wish to get to fairy ring?",
            defaultValue = "QPC"
            ),
        @ScriptConfiguration(
            name="Runecape",
            optionType = OptionType.BOOLEAN,
            description = "Runecrafting Cape Equipped?",
            defaultValue = "false"
            )
        }
)


public class Blood_Rune extends AbstractScript {

    private final BankExecutor bankExecutor = new BankExecutor();
    private final WalkExecutor walkExecutor = new WalkExecutor();
    private final CraftExecutor craftExecutor = new CraftExecutor();
    private final ReturnExecutor returnExecutor = new ReturnExecutor();

    @SuppressWarnings("resource")
    public static void main(String[] args) {
        new ScriptUploader().uploadAndStart("Blood Rune", "karasandra", "127.0.0.1:5559", true, false);
    }

    @Override
    public void onStart() {
        Paint paint = PaintBuilder.newBuilder()
                .removeScriptNameVersion()
                .withoutDiscordWebhook()
                .trackInventoryItem(ObjectId.BLOOD_RUNE, "Blood Runes", TrackInventoryOption.QuantityChange)
                .trackInventoryItem(ObjectId.BLOOD_RUNE, "Gold", TrackInventoryOption.values())
                .addString("Task: ", Utility::getTask)
                .x(30)
                .y(65)
                .build();
        addPaint(paint);

        setGUIValues();
    }

    private void setGUIValues() {
        Object retMethod = getOption("Bank Method");
        Object walkMethod = getOption("Fairy Method");
        Object runeCape = getOption("Runecape");

        if(retMethod == null || walkMethod == null || runeCape == null) {
            Log.severe("GUI Value Null");
            Utility.setStopping(true);
            return;
        }

        Config.setRetMethod((String) retMethod);
        Config.setWalkMethod((String) walkMethod);
        Config.setRuneCape((boolean) runeCape);
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
            case RETURN -> returnExecutor.execute();
        };
            //Activity Ends

    }
}
