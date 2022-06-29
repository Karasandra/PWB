package kara.scripts.blood_rune.utility;

import kara.scripts.blood_rune.executor.Activity;
import kara.scripts.bone_collector.utility.Log;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;


public class Utility {
    //General
    private static boolean stopping = false;
    private static Activity activity = Activity.BANK;
    private static String task = "Starting";


    //Item and Object ID's
    public static int BLOOD_RUNE = 565;
    public static int MYTH_CAPE = 123456;
    public static int CON_CAPE = 123456;
    public static int QP_CAPE = 123456;
    public static int CRAFT_CAPE = 123456;
    public static int POUCH_ITEM = 123456;
    public static int POTION_ITEM = 123456;
    public static int POTION_VARPBIT = 277;
    public static int POUCH_VARPBIT_ITEM = 123456;
    public static int POUCH_VARPBIT_FULL = 123456;
    public static int POUCH_VARPBIT_EMPTY = 123456;
    public static int PURE_ESSENCE = 123456;
    public static int BLOOD_ESSENCE_INERT = 123456;
    public static int BLOOD_ESSENCE_ACTIVE = 123456;
    public static int BLOOD_ALTER = 123456;


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
    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Utility.task = task;
    }

    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }

    public static boolean isStopping() { return stopping; }
    public static int getPouchVarpbitItem() { return Varpbits.varpbit(POUCH_VARPBIT_ITEM); }
    public static int getPotionVarpbit() { return Varpbits.varpbit(POTION_VARPBIT); }
    public static int getEssenceCount() { return (int) Inventory.stream().id(PURE_ESSENCE).count(); }
    public static Item getInvPotion() { return Inventory.stream().id(POTION_ITEM).first(); }
    public static Item getInvBloodRune() { return Inventory.stream().id(BLOOD_RUNE).first(); }
    public static int getIdle() { return Players.local().idleAnimation(); }
}
