package kara.scripts.blood_rune.utility;

import kara.scripts.blood_rune.executor.Activity;
import kara.scripts.bone_collector.utility.Log;
import org.powbot.api.Random;

public class Utility {
    //General
    private static boolean stopping = false;

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

    private static final int LOWER_RUN_THRESH = 10;
    private static final int UPPER_RUN_THRESH = 60;
    public static int runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);


    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Utility.task = task;
    }

    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }

    public static boolean isStopping() { return stopping; }
}
