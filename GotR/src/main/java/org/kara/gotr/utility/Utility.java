package org.kara.gotr.utility;

import org.kara.gotr.executor.Activity;
import org.powbot.api.Area;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;

public class Utility {
    private static boolean stopping = false;
    private static Activity activity = Activity.START;
    private static String task = "Starting";
    public static int yawReg = Random.nextInt(50, 90);
    public static int yawUnd = Random.nextInt(170, 190);
    public static int pitch = Random.nextInt(94, 99);
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
    public static int getPouchVarpbitItem() { return Varpbits.varpbit(ObjectId.POUCH_VARPBIT_ITEM); }
    public static int getEssenceCount() { return (int) Inventory.stream().id(999999999).count(); }
    public static Item getInvPouch() { return Inventory.stream().id(ObjectId.POUCH_ITEM).first(); }
    public static GameObject getObject(int obj) { return Objects.stream().id(obj).nearest().first(); }
    public static boolean myTile(Area map) { return map.contains(Players.local().tile()); }
}
