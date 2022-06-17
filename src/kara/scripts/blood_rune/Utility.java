package kara.scripts.blood_rune;

import kara.scripts.api.Log;
import org.powbot.api.Random;
import org.powbot.api.rt4.Movement;

public class Utility {
    //General
    private static boolean stopping = false;
    private static String task = "Starting";
    public static int _BLOODRUNE = 526;



    //Utility
    public static String getTask() { return task; }


    public static int getLoopReturnQuick() { return Random.nextInt(0, 10); }
    public static int getLoopReturn() { return Random.nextInt(0, 50); }
    public static int getLoopReturnLong() { return Random.nextInt(200, 400); }

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
        kara.scripts.blood_rune.Utility.task = task;
    }

    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }

    public static boolean isStopping() { return stopping; }
}
