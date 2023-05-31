package org.kara.wrath.utility;

import org.powbot.api.Area;
import org.powbot.api.Tile;

public class Location {
    public static final Area CAVE = new Area(new Tile(1938, 8967, 1), new Tile(1940, 8969, 1));
    public static final Area MYTH_GUILD_UPPER = new Area( new Tile(2462, 2848, 1), new Tile(2465, 2844, 1));
    public static final Area MYTH_GUILD_LOWER = new Area(new Tile(2449, 2857), new Tile(2465, 2838));
    public static final Area WRATH_ALTER = new Area(new Tile(2337, 4825), new Tile(2332, 4834));
    public static final Area MYTH_ALTER = new Area(new Tile(2442, 2820, 0), new Tile(2448, 2817, 0));
    public static final Area MYTH_STAIRS = new Area(new Tile(2456, 2841), new Tile(2458, 2838));
}
