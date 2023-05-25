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
    public static int tpCape = 0;
    //public static void go(Area area,int obj) { Movement.builder(area.getRandomTile()).setAutoRun(true).setRunMin(40).setRunMax(95).setWalkUntil(() -> getObject(obj).distance() <= 10).move(); }
    public static void step(Area area) { Movement.step(area.getRandomTile()); }
    public static void tele() {
        Item cape = Inventory.stream().id(ObjectId.MYTH_CAPE).first();
        if (!cape.valid()) {
            Log.severe("No Teleport");
            Utility.setStopping(true);
        } else {
            Log.fine("Cape Found");
            cape.click("teleport");
            if (Condition.wait(() -> Utility.myTile(Location.MYTH_GUILD_LOWER), 50, 1000)) {
                Log.fine("Teleported!");
            } else {
                Log.severe("Did not move");
                Utility.setStopping(true);
            }
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
    public static int getLoopReturnLong() { return Random.nextInt(500, 1000); }
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
    public static void getBankPotion() { if(Bank.withdraw(ObjectId.POTION_ITEM_4, Bank.Amount.ONE)) {} else { if(Bank.withdraw(ObjectId.POTION_ITEM_3, Bank.Amount.ONE)) {} else { if(Bank.withdraw(ObjectId.POTION_ITEM_2, Bank.Amount.ONE)) {} else { if(Bank.withdraw(ObjectId.POTION_ITEM_1, Bank.Amount.ONE)) {}}}}}
    public static void depBankPotion() { if(Bank.deposit(ObjectId.POTION_ITEM_4, Bank.Amount.ONE)) {} else { if(Bank.deposit(ObjectId.POTION_ITEM_3, Bank.Amount.ONE)) {} else { if(Bank.deposit(ObjectId.POTION_ITEM_2, Bank.Amount.ONE)) {} else { if(Bank.deposit(ObjectId.POTION_ITEM_1, Bank.Amount.ONE)) {}}}}}
    public static Item getInvFood() { return Inventory.stream().id(ObjectId.FOOD).first(); }
    public static Item getInvPouch() { return Inventory.stream().id(ObjectId.POUCH_ITEM).first(); }
    public static GameObject getObject(int obj) { return Objects.stream().id(obj).nearest().first(); }
    public static Item getInvWrathRune() { return Inventory.stream().id(ObjectId.WRATH_RUNE).first(); }
    public static boolean myTile(Area map) { return map.contains(Players.local().tile()); }
    public static boolean healthLoss() {
        return Players.local().healthPercent() <= 70;
    }
}
