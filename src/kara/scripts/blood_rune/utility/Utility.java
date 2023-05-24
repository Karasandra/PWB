package kara.scripts.blood_rune.utility;

import kara.scripts.blood_rune.executor.Activity;
import kara.scripts.bone_collector.utility.Log;
import org.powbot.api.Random;
import org.powbot.api.Tile;
import org.powbot.api.rt4.*;


public class Utility {
    //General
    private static boolean stopping = false;
    private static Activity activity = Activity.BANK;
    private static String task = "Starting";
    private static final int LOWER_RUN_THRESH = 50;
    private static final int UPPER_RUN_THRESH = 90;
    private static int runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);


    //Utility
    public static boolean needsToRun() {
        if (!Movement.running() && Movement.energyLevel() > runThreshold) {
            runThreshold = Random.nextInt(LOWER_RUN_THRESH, UPPER_RUN_THRESH);
            return true;
        }
        return false;
    }
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
    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Utility.task = task;
    }
    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }
    public static boolean isStopping() { return stopping; }
    public static int getPouchVarpbitItem() { return Varpbits.varpbit(ObjectId.POUCH_VARPBIT_ITEM); }
    public static int getPotionVarpbit() { return Varpbits.varpbit(ObjectId.POTION_VARPBIT); }
    public static int getEssenceCount() { return (int) Inventory.stream().id(ObjectId.PURE_ESSENCE).count(); }
    public static Item getInvPotion() { return Inventory.stream().id(ObjectId.POTION_ITEM_4, ObjectId.POTION_ITEM_3, ObjectId.POTION_ITEM_2, ObjectId.POTION_ITEM_1).first(); }
    public static Item getInvBloodRune() { return Inventory.stream().id(ObjectId.BLOOD_RUNE).first(); }
    public static GameObject getObject(int obj) { return Objects.stream().id(obj).nearest().first(); }
    public static boolean getIdle() { return !Players.local().inMotion(); }
    public static Tile myTile() { return Players.local().tile(); }
}
