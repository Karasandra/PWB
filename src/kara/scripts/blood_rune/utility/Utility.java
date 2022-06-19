package kara.scripts.blood_rune.utility;

import kara.scripts.blood_rune.executor.Activity;
import kara.scripts.bone_collector.utility.Log;
import org.powbot.api.Random;
import org.powbot.api.rt4.Movement;
import org.w3c.dom.ranges.Range;

public class Utility {
    //General
    private static boolean stopping = false;

    private static boolean threshold = false;
    private static Activity activity = Activity.BANK;
    private static String task = "Starting";
    public static int _BLOODRUNE = 565;



    //Utility
    public static String getTask() { return task; }

    public static Activity getActivity() {
        return activity;
    }

    public static void setActivity(Activity activity) {
        Utility.activity = activity;
    }

    public static int getLoopReturnQuick() { return Random.nextInt(0, 10); }
    public static int getLoopReturn() { return Random.nextInt(0, 50); }
    public static int getLoopReturnLong() { return Random.nextInt(200, 400); }

    private static final int LOWER_RUN_THRESH = 20;
    private static final int UPPER_RUN_THRESH = 50;
    public static boolean runThreshold() {
        return Movement.energyLevel() < 60;
    }



    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Utility.task = task;
    }

    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }

    public static boolean isStopping() { return stopping; }
}
