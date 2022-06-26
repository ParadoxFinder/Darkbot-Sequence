package eu.paradox.darkbot.implementation.step.move;

import com.github.manolo8.darkbot.core.utils.Location;

import java.util.HashMap;
import java.util.Map;

public class GoToSellPosition extends GoToPosition {
    /**
     * Sell locations of the maps.
     */
    public static final Map<String, Location> SELL_LOCATIONS_PER_MAP = new HashMap<String, Location>() {{
        put("1-1", new Location(10, 18));
        put("1-8", new Location(13, 74));
        put("2-1", new Location(187, 7));
        put("2-8", new Location(94, 11));
        put("3-1", new Location(198, 113));
        put("3-8", new Location(195, 58));
        put("5-2", new Location(101, 64));
    }};

    /**
     * Handles the step being parsed.
     */
    @Override
    public void onParse() {

    }

    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        String currentMap = this.context.getMain().hero.map.name;
        if (!SELL_LOCATIONS_PER_MAP.containsKey(currentMap)) {
            throw new RuntimeException("No sell location stored for " + currentMap);
        }
        this.x = SELL_LOCATIONS_PER_MAP.get(currentMap).x;
        this.y = SELL_LOCATIONS_PER_MAP.get(currentMap).y;
        super.onStart();
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "GoToSellPosition[x=" + this.x + "," + this.y + "]";
    }
}
