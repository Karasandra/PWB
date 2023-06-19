package org.kara.orb.utility;

import org.kara.orb.executor.Activity;
import org.powbot.api.Area;
import org.powbot.api.Condition;
import org.powbot.api.Random;
import org.powbot.api.rt4.*;

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
    public static int getLoopReturnXLong() { return Random.nextInt(800, 1200); }
    public static void setTask(String task) {
        Log.info("TASK: " + task);
        Utility.task = task;
    }
    public static void setStopping(boolean stopping) { Utility.stopping = stopping; }
    public static boolean isStopping() { return stopping; }
    public static GameObject getObject(int obj) { return Objects.stream().id(obj).nearest().first(); }
    public static Item getInv(Integer integer) { return Inventory.stream().id(integer).first(); }
    public static boolean myTile(Area map) { return map.contains(Players.local().tile()); }
    public static void tabMagic() {
        if (Game.tab() != Game.Tab.MAGIC) {
            Game.tab(Game.tab().MAGIC);
        }
    }
    public static void tabInv() {
        if (Game.tab() != Game.Tab.INVENTORY) {
            Game.tab(Game.tab().INVENTORY);
        }
    }
    public static void tele() {
        tabMagic();
        Magic.Spell.FALADOR_TELEPORT.cast();
        Condition.wait(() -> Utility.myTile(Location.FALADOR), 50, 60);
    }
    public static void getBankAntiPoison() {
        if(Bank.withdraw(ObjectId.ANTI_POISON_4, Bank.Amount.ONE)) return;
        if(Bank.withdraw(ObjectId.ANTI_POISON_3, Bank.Amount.ONE)) return;
        if(Bank.withdraw(ObjectId.ANTI_POISON_2, Bank.Amount.ONE)) return;
        Bank.withdraw(ObjectId.ANTI_POISON_1, Bank.Amount.ONE);
    }
    public static void poisonCure() {
        if (Combat.isPoisoned()) {
            Inventory.stream().id(ObjectId.ANTI_POISON_4, ObjectId.ANTI_POISON_3, ObjectId.ANTI_POISON_2, ObjectId.ANTI_POISON_1).first().click("Drink");
        }
    }
    public static Item invAntiPoison() {
        return Inventory.stream().id(ObjectId.ANTI_POISON_4, ObjectId.ANTI_POISON_3, ObjectId.ANTI_POISON_2, ObjectId.ANTI_POISON_1).first();
    }
    public static Item invFood() {
        return Inventory.stream().id(ObjectId.FOOD).first();
    }
    public static boolean healthLoss() {
        return Players.local().healthPercent() <= healthRandom();
    }
    private static int healthRandom() { return Random.nextInt(70, 80); }
    public static int health() { return Players.local().healthPercent(); }
    public static boolean getIdle() { return !Players.local().inMotion(); }
}
