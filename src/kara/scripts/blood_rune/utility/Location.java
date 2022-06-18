package kara.scripts.blood_rune.utility;

import org.powbot.api.Area;
import org.powbot.api.Tile;

public class Location {
    public static final Area DUNGEON_AREA = new Area(new Tile(3092, 9895), new Tile(3124, 9866));
    public static final Area DUNGEON_AREA_LEFT = new Area(
            new Tile(3116, 9892),
            new Tile(3119, 9892),
            new Tile(3119, 9894),
            new Tile(3122, 9894),
            new Tile(3122, 9891),
            new Tile(3116, 9891));
}
