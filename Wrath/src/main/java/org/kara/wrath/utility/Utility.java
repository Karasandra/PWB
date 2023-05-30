package org.kara.wrath.utility;


import org.kara.wrath.executor.Activity;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;


public class Utility {
    private static boolean stopping = false;
    private static Activity activity = Activity.BANK;
    private static String task = "Starting";
    public static int yawReg = Random.nextInt(50, 90);
    public static int yawUnd = Random.nextInt(170, 190);
    public static int pitch = Random.nextInt(94, 99);
    public static int tpCape = 0;
    //public static void go(Area area,int obj) { Movement.builder(area.getRandomTile()).setAutoRun(true).setRunMin(40).setRunMax(95).setWalkUntil(() -> getObject(obj).distance() <= 10).move(); }
    public static void step(Area area) { Movement.step(area.getRandomTile()); }
    public static void tele() {
        Item cape = Inventory.stream().id(ObjectId.MYTH_CAPE).first();
        if (!cape.valid()) {
            //Log.severe("No Teleport");
            Utility.setStopping(true);
        } else {
            //Log.fine("Cape Found");
            cape.click("teleport");
            Condition.wait(() -> Utility.myTile(Location.MYTH_GUILD_LOWER), 50, 1000);
        }
    }
    public static String getTask() { return task; }
    public static Activity getActivity() {
        return activity;
    }
    public static void setActivity(Activity activity) {
        Utility.activity = activity;
    }
    public static int getLoopReturnQuick() { return Random.nextInt(0, 10); }
    public static int getLoopReturn() { return Random.nextInt(10, 200); }
    public static int getLoopReturnLong() { return Random.nextInt(250, 600); }
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
    public static void getBankPotion() {
        if(Bank.withdraw(ObjectId.POTION_ITEM_4, Bank.Amount.ONE)) return;
        if(Bank.withdraw(ObjectId.POTION_ITEM_3, Bank.Amount.ONE)) return;
        if(Bank.withdraw(ObjectId.POTION_ITEM_2, Bank.Amount.ONE)) return;
        Bank.withdraw(ObjectId.POTION_ITEM_1, Bank.Amount.ONE);
    }
    public static void depBankPotion() {
        if(Bank.deposit(ObjectId.POTION_ITEM_4, Bank.Amount.ONE)) return;
        if(Bank.deposit(ObjectId.POTION_ITEM_3, Bank.Amount.ONE)) return;
        if(Bank.deposit(ObjectId.POTION_ITEM_2, Bank.Amount.ONE)) return;
        Bank.deposit(ObjectId.POTION_ITEM_1, Bank.Amount.ONE);
    }
    public static Item getInvFood() { return Inventory.stream().id(ObjectId.FOOD).first(); }
    public static Item getInvPouch() { return Inventory.stream().id(ObjectId.POUCH_ITEM).first(); }
    public static GameObject getObject(int obj) { return Objects.stream().id(obj).nearest().first(); }
    public static Item getInvWrathRune() { return Inventory.stream().id(ObjectId.WRATH_RUNE).first(); }
    public static boolean myTile(Area map) { return map.contains(Players.local().tile()); }
    public static boolean healthLoss() {
        return Players.local().healthPercent() <= healthRandom();
    }
    private static int healthRandom() { return Random.nextInt(60, 75); }
    public static int health() { return Players.local().healthPercent(); }
}
