package org.kara.plank.utility;

public class Config {
    private static int craftChoice;
    private static int logChoice;
    private static int chatChoice;
    private static int goldCut;
    public static void setGoldCut(String goldCut) {
        Config.goldCut = Integer.valueOf(goldCut);
    }

    public static void setCraftChoice(String craftChoice) {
        if (craftChoice.equals("Plank")) {
            Config.craftChoice = ObjectId.PLANK[0];
            Config.logChoice = ObjectId.LOGS[0];
            Config.chatChoice = ObjectId.LUMBER_CHAT[0];
        }
        if (craftChoice.equals("Oak")) {
            Config.craftChoice = ObjectId.PLANK[1];
            Config.logChoice = ObjectId.LOGS[1];
            Config.chatChoice = ObjectId.LUMBER_CHAT[1];
        }
        if (craftChoice.equals("Teak")) {
            Config.craftChoice = ObjectId.PLANK[2];
            Config.logChoice = ObjectId.LOGS[2];
            Config.chatChoice = ObjectId.LUMBER_CHAT[2];
        }
        if (craftChoice.equals("Mahogany")) {
            Config.craftChoice = ObjectId.PLANK[3];
            Config.logChoice = ObjectId.LOGS[3];
            Config.chatChoice = ObjectId.LUMBER_CHAT[3];
        }
    }
    public static int getCraftChoice() { return craftChoice; }
    public static int getLogChoice() { return logChoice; }
    public static int getChat() { return  chatChoice; }
    public static int getGoldCut() { return goldCut; }
}
