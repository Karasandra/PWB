package org.kara.plank.utility;

public class Config {
    private static int craftChoice;
    private static int logChoice;

    public static void setCraftChoice(String craftChoice) {
        if (craftChoice.equals("Plank")) {
            Config.craftChoice = ObjectId.PLANK[0];
            Config.logChoice = ObjectId.LOGS[0];
        }
        if (craftChoice.equals("Oak")) {
            Config.craftChoice = ObjectId.PLANK[1];
            Config.logChoice = ObjectId.LOGS[1];
        }
        if (craftChoice.equals("Teak")) {
            Config.craftChoice = ObjectId.PLANK[2];
            Config.logChoice = ObjectId.LOGS[2];
        }
        if (craftChoice.equals("Mahogany")) {
            Config.craftChoice = ObjectId.PLANK[3];
            Config.logChoice = ObjectId.LOGS[3];
        }
    }
    public static int getCraftChoice() { return craftChoice; }
    public static int getLogChoice() { return logChoice; }
}
