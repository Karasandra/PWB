package org.kara.wrath.utility;

import org.powbot.api.Area;
import org.powbot.api.Tile;

public class Location {
    public static final Area MYTH_GUILD_UPPER = new Area(
            new Tile(2461, 2849, 1),
            new Tile(2461, 2843, 1),
            new Tile(2454, 2843, 1),
            new Tile(2454, 2838, 1),
            new Tile(2461, 2838, 1),
            new Tile(2467, 2846, 1),
            new Tile(2467, 2849, 1)
    );
    public static final Area MYTH_GUILD_LOWER = new Area(new Tile(2449, 2857), new Tile(2465, 2838));
    public static final Area WRATH_ALTER = new Area(new Tile(2337, 4825), new Tile(2332, 4834));
    public static final Area MYTH_ALTER = new Area(new Tile(2448, 2817, 0), new Tile(2444, 2829, 0));
}
