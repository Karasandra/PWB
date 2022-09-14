package kara.scripts.blood_rune.utility;

public class Config {
    //Config Settings
    private static boolean runeCape;
    private static int walkMethod;
    private static int retMethod;

    //Set and Return Methods
    public static void setWalkMethod(String walkMethod) {
        if (walkMethod.equals("QPC")) {
            Config.walkMethod = ObjectId.QP_CAPE;
        }
        if (walkMethod.equals("Construction")) {
            Config.walkMethod = ObjectId.CON_CAPE;
        }
        if (walkMethod.equals("Construction(T)")) {
            Config.walkMethod = ObjectId.CON_CAPE_T;
        }
    }
    public static int getWalkMethod() {
        return walkMethod;
    }

    public static void setRetMethod(String retMethod) {
        if (retMethod.equals("MYTH")) {
            Config.retMethod = ObjectId.MYTH_CAPE;
        }
        if (retMethod.equals("Craft")) {
            Config.retMethod = ObjectId.CRAFT_CAPE;
        }
        if (retMethod.equals("Craft(T)")) {
            Config.retMethod = ObjectId.CRAFT_CAPE_T;
        }
    }
    public static int getRetMethod() {
        return retMethod;
    }

    public static void setRuneCape(boolean runeCape) {
        Config.runeCape = runeCape;
    }
    public static boolean isRuneCape() {
        return runeCape;
    }
}
