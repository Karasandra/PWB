package org.kara.plank.utility;

import org.kara.plank.executor.Activity;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;
import org.powbot.api.rt4.stream.item.EquipmentItemStream;

public class Utility {
    private static boolean stopping = false;
    private static Activity activity = Activity.BANK;
    private static String task = "Starting";
    public static int yawReg = Random.nextInt(70, 90);
    public static int pitch = Random.nextInt(94, 99);
    private static final int LOWER_RUN_THRESH = 50;
    private static final int UPPER_RUN_THRESH = 90;
    private static int runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);
    public static boolean needsToRun() {
        if (!Movement.running() && Movement.energyLevel() > runThreshold) {
            runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);
            return true;
        }
        return false;
    }
    public static void step(Area area) { Movement.step(area.getRandomTile()); }
    public static void move(Area area) { Movement.builder(area.getRandomTile()).move(); }
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
    public static int getLoopReturnXLong() { return Random.nextInt(600, 1000); }
    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Utility.task = task;
    }
    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }
    public static boolean isStopping() { return stopping; }
    public static GameObject getObject(int obj) { return Objects.stream().id(obj).nearest().first(); }
    public static Item getInv(int obj) { return Inventory.stream().id(obj).first(); }
    public static boolean myTile(Area map) { return map.contains(Players.local().tile()); }

    public static void tabEquip() {
        if (Game.tab() != Game.Tab.EQUIPMENT) {
            Game.tab(Game.tab().EQUIPMENT);
        }
    }
    public static boolean checkRing() {
        return Equipment.itemAt(Equipment.Slot.RING).valid();
    }
    public static boolean getIdle() { return !Players.local().inMotion(); }
}
