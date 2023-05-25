package org.kara.scripts.bone_collector.executor;

import org.kara.scripts.bone_collector.utility.Log;
import org.kara.scripts.bone_collector.utility.Location;
import org.kara.scripts.bone_collector.utility.Utility;
import org.powbot.api.Condition;
import org.powbot.api.rt4.*;


public class CollectExecutor extends ActivityExecutor {

    private CollectActivity localActivity = CollectActivity.COLLECTING_RIGHT;

    enum CollectActivity {
        WALKING,
        COLLECTING_LEFT,
        COLLECTING_RIGHT,
        LEAVING
    }


    @Override
    public int execute() {


        switch (localActivity) {
            case WALKING:

                if (Inventory.isFull()) {
                    Log.fine("No more room. Time to bank.");
                    Utility.setTask("Leaving");
                    localActivity = CollectActivity.LEAVING;
                    return Utility.getLoopReturn();
                }

                if (Location.DUNGEON_AREA_LEFT.contains(Players.local().tile())) {
                    Utility.setTask("Moving to Right Side");
                    Movement.walkTo(Location.DUNGEON_AREA_RIGHT.getRandomTile());
                    Condition.wait(() -> Location.DUNGEON_AREA_RIGHT.contains(Players.local()), 100, 500);
                    Log.fine("Walk Complete");
                    localActivity = CollectActivity.COLLECTING_RIGHT;
                    return Utility.getLoopReturn();
                }

                if (Location.DUNGEON_AREA_RIGHT.contains(Players.local().tile())) {
                    Utility.setTask("Moving to Left Side");
                    Movement.walkTo(Location.DUNGEON_AREA_LEFT.getRandomTile());
                    Condition.wait(() -> Location.DUNGEON_AREA_LEFT.contains(Players.local()), 100, 500);
                    Log.fine("Walk Complete");
                    localActivity = CollectActivity.COLLECTING_LEFT;
                    return Utility.getLoopReturn();
                }

                if (!Location.DUNGEON_AREA.contains(Players.local().tile())) {
                    Utility.setTask("???? Where are we????");
                    Movement.walkTo(Location.DUNGEON_AREA_RIGHT.getRandomTile());
                    Condition.wait(() -> Location.DUNGEON_AREA_RIGHT.contains(Players.local()), 100, 500);
                    Log.fine("Unknown area");
                    localActivity = CollectActivity.COLLECTING_RIGHT;
                    return Utility.getLoopReturn();
                }

                Utility.setTask("????WE FUCKED????");
                Utility.setStopping(true);
                return Utility.getLoopReturn();

            case COLLECTING_RIGHT:
                if (Inventory.isFull()) {
                    Log.fine("No more room. Time to bank.");
                    Utility.setTask("Leaving");
                    localActivity = CollectActivity.LEAVING;
                    return Utility.getLoopReturn();
                }

                if (Location.DUNGEON_AREA_RIGHT.contains(Players.local())) {
                    Utility.setTask("Waiting on spawn");
                    if (Condition.wait(() -> Utility.getBone(Location.DUNGEON_AREA_RIGHT).valid(), 100, 100)) {
                        Log.info("No bone foundR");
                        localActivity = CollectActivity.WALKING;
                        return Utility.getLoopReturn();
                    }
                    Log.fine("Bone Found");
                    GroundItem bone = Utility.getNearestBone(Location.DUNGEON_AREA_RIGHT);
                    if (bone.valid()) {
                        Utility.setTask("Grabbing Bone");
                        if (!bone.inViewport()) {
                            Camera.turnTo(bone);
                        }

                        if (!bone.interact(a -> true)) {
                            Log.severe("Failed to pickup bone");
                            return Utility.getLoopReturn();
                        }
                        Condition.wait(() -> !bone.valid(), 50, 1500);
                        return Utility.getLoopReturn();
                    }
                    return Utility.getLoopReturn();
                }

                Utility.setTask("Not at Right Side.");
                localActivity = CollectActivity.WALKING;
                return Utility.getLoopReturn();

            case COLLECTING_LEFT:
                if (Inventory.isFull()) {
                    Log.fine("No more room. Time to bank.");
                    Utility.setTask("Leaving");
                    localActivity = CollectActivity.LEAVING;
                    return Utility.getLoopReturn();
                }

                if (Location.DUNGEON_AREA_LEFT.contains(Players.local())) {
                    Utility.setTask("Waiting on spawn");
                    if (Condition.wait(() -> Utility.getBone(Location.DUNGEON_AREA_LEFT).valid(), 100, 100)) {
                        Log.info("No bone found Left");
                        localActivity = CollectActivity.WALKING;
                        return Utility.getLoopReturn();
                    }
                    GroundItem bone = Utility.getNearestBone(Location.DUNGEON_AREA_LEFT);
                    if (bone.valid()) {
                        Utility.setTask("Grabbing Bone");
                        if (!bone.inViewport()) {
                            Camera.turnTo(bone);
                        }

                        if (!bone.interact(a -> true)) {
                            Log.severe("Failed to pickup bone");
                            return Utility.getLoopReturn();
                        }
                        Condition.wait(() -> !bone.valid(), 50, 1500);
                        return Utility.getLoopReturn();
                    }
                    return Utility.getLoopReturn();
                }

                Utility.setTask("Not at Left Side.");
                localActivity = CollectActivity.WALKING;
                return Utility.getLoopReturn();

            case LEAVING:
                localActivity = CollectActivity.WALKING;

                if (Inventory.isFull()) {
                    Movement.walkTo(Location.EDGEVILLE_BANK.getRandomTile());
                    Condition.wait(() -> Location.EDGEVILLE_BANK.contains(Players.local()), 50, 1500);
                    Utility.setActivity(Activity.BANK);
                    return Utility.getLoopReturn();
                }

                Log.info("Item in inventory disappeared.");
                Utility.setTask("Idling");
                Utility.setStopping(true);
                return Utility.getLoopReturn();
        }
        return Utility.getLoopReturn();
    }
}
