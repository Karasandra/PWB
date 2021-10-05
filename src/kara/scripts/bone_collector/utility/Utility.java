package kara.scripts.bone_collector.utility;

import kara.scripts.api.Log;
import org.powbot.api.Area;
import org.powbot.api.Random;
import org.powbot.api.Tile;
import org.powbot.api.rt4.GroundItem;
import org.powbot.api.rt4.GroundItems;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;


public class Utility {
    //General
    private static boolean stopping = false;
    private static Activity activity = Activity.COLLECT;
    private static String task = "Starting";
    public static Tile myTile = Players.local().tile();

    //Utility
    public static String getTask() { return task; }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Utility.activity = activity;
    }

    public static int getQuickLoopReturn() { return Random.nextInt(0, 10); }
    public static int getLoopReturn() { return Random.nextInt(0, 50); }
    public static int getLongLoopReturn() { return Random.nextInt(200, 400); }

    private static final int LOWER_RUN_THRESH = 10;
    private static final int UPPER_RUN_THRESH = 30;
    private static int runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);


    public static boolean needsToRun() {
        if (!Movement.running() && Movement.energyLevel() > runThreshold) {
            runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);
            return true;
        }
        return false;
    }

    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Utility.task = task;
    }

    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }

    public static boolean isStopping() { return stopping; }

    public static GroundItem getNearestBone(Area area) {
        return GroundItems.stream().id(526).within(area).nearest().first();
    }

    public static boolean getBoneCheck(Area area) {
        return GroundItems.stream().id(526).within(area) != null;
    }

    public static Tile northTile = myTile.derive(3, 0);
    public static Tile southTile = myTile.derive(-3, 0);
    public static Tile westTile = myTile.derive(0, -3);
    public static Tile eastTile = myTile.derive(0, 3);
}
