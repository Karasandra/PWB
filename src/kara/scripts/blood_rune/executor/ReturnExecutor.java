package kara.scripts.blood_rune.executor;

import kara.scripts.blood_rune.utility.Location;
import kara.scripts.blood_rune.utility.Log;
import kara.scripts.blood_rune.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.Inventory;
import org.powbot.api.rt4.Item;
import org.powbot.api.rt4.Movement;
import org.powbot.api.rt4.Players;

public class ReturnExecutor extends ActivityExecutor {


        private ReturnActivity localacvtivity =  ReturnActivity.METHOD;

        enum ReturnActivity {
            METHOD,
            MYTH,
            CRAFT
        }

    @Override
    public int execute() {
            Log.info("Return Executor");

        switch (localacvtivity) {
            case METHOD -> {
                Log.info("Deciding Method");
                if (Inventory.stream().id(Utility.MYTH_CAPE).first().valid()) {
                    Log.fine("Myth in Inv");
                    localacvtivity = ReturnActivity.MYTH;
                }
                if (Inventory.stream().id(Utility.CRAFT_CAPE).first().valid()) {
                    Log.fine("Craft in Inv");
                    localacvtivity = ReturnActivity.CRAFT;
                } else {
                    Log.severe("No Method");
                    Utility.setStopping(true);
                }
                return Utility.getLoopReturnQuick();
            }
            case MYTH -> {
                Log.info("Myth Method");
                Utility.setTask("Return Via Myth");
                if (!Location.MYTH_GUILD_UPPER.contains(Players.local().tile()) && !Location.MYTH_GUILD_LOWER.contains(Players.local().tile())) {
                    Log.info("Location Xupper-Xlower");
                    Item cape = Inventory.stream().id(Utility.MYTH_CAPE).first();
                    if (!cape.valid()) {
                        Log.severe("No Myth Cape!");
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                    if (cape.valid()) {
                        Log.fine("Cape Found");
                        cape.interact("teleport");
                        if (Condition.wait(() -> Location.MYTH_GUILD_LOWER.contains(Players.local().tile()), 50, 1500)) {
                            Log.fine("teleport successful");
                        }
                        else {
                            Log.severe("Teleport failed");
                            Utility.setStopping(true);
                            return Utility.getLoopReturnQuick();
                        }
                    }
                }
                if (Location.MYTH_GUILD_LOWER.contains(Players.local().tile())) {
                    Utility.setTask("Walking to bank");
                    Log.info("Location lower");
                    Movement.walkTo(Location.MYTH_GUILD_UPPER_STAIRS.getRandomTile());
                    if (Condition.wait(() -> Location.MYTH_GUILD_UPPER.contains(Players.local().tile()), 50, 1500)) {
                        Log.fine("walk successful");
                    } else {
                        Log.severe("walk failed");
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                }
                if (Location.MYTH_GUILD_UPPER.contains(Players.local().tile())) {
                    Log.fine("Successful return");
                    Utility.setActivity(Activity.BANK);
                    return Utility.getLoopReturnQuick();
                }
                return Utility.getLoopReturn();
            }
            case CRAFT -> {
                Log.info("Craft Method");
                Utility.setTask("Return Via Craft");
                if (!Location.CRAFT_GUILD.contains(Players.local().tile())) {
                    Log.info("Location Xcraft");
                    Item cape = Inventory.stream().id(Utility.CRAFT_CAPE).first();
                    if (!cape.valid()) {
                        Log.severe("No Craft Cape!");
                        Utility.setStopping(true);
                        return Utility.getLoopReturnQuick();
                    }
                    if (cape.valid()) {
                        Log.fine("Cape Found");
                        cape.interact("teleport");
                        if (Condition.wait(() -> Location.CRAFT_GUILD.contains(Players.local().tile()), 50, 1500)) {
                            Log.fine("teleport successful");
                        } else {
                            Log.severe("Teleport failed");
                            Utility.setStopping(true);
                            return Utility.getLoopReturnQuick();
                        }
                    }
                }
                if (Location.CRAFT_GUILD.contains(Players.local().tile())) {
                    Log.fine("Successful return");
                    Utility.setActivity(Activity.BANK);
                    return Utility.getLoopReturnQuick();
                }
                return Utility.getLoopReturn();
            }
        }

            return Utility.getLoopReturn();
    }
}
