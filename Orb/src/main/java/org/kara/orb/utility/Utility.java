package org.kara.orb.utility;

import org.kara.orb.executor.Activity;
import org.powbot.api.Area;
import org.powbot.api.Random;
import org.powbot.api.rt4.GameObject;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Objects;
import org.powbot.api.rt4.Players;

public class Utility {
    private static boolean stopping = false;
    private static Activity activity = Activity.START;
    private static String task = "Starting";
    public static void step(Area area) { Movement.step(area.getRandomTile()); }
    public static String getTask() { return task; }
    public static Activity getActivity() {
        return activity;
    }
    public static void setActivity(Activity activity) {
        Utility.activity = activity;
    }
    public static int getLoopReturnQuick() { return Random.nextInt(0, 10); }
    public static int getLoopReturn() { return Random.nextInt(10, 200); }
    public static int getLoopReturnLong() { return Random.nextInt(200, 400); }
    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Utility.task = task;
    }
    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }
    public static boolean isStopping() { return stopping; }
    public static GameObject getObject(int obj) { return Objects.stream().id(obj).nearest().first(); }
    public static boolean myTile(Area map) { return map.contains(Players.local().tile()); }
}
