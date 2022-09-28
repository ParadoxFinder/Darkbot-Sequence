package eu.paradox.darkbot.implementation.step.move;

import eu.darkbot.api.game.other.EntityInfo;
import eu.darkbot.api.managers.OreAPI;
import eu.paradox.darkbot.base.StepContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GoToSellMap extends GoToMap {
    /**
     * Maps that are valid for selling resources.
     */
    private static final Map<EntityInfo.Faction, List<String>> SELL_MAPS = new HashMap<EntityInfo.Faction, List<String>>()
        {{
            put(EntityInfo.Faction.MMO, new ArrayList<String>() {{
                add("1-1");
                add("1-8");
                add("5-2");
            }});
            put(EntityInfo.Faction.EIC, new ArrayList<String>() {{
                add("2-1");
                add("2-8");
                add("5-2");
            }});
            put(EntityInfo.Faction.VRU, new ArrayList<String>() {{
                add("3-1");
                add("3-8");
                add("5-2");
            }});
        }};

    /**
     * Options for the maps for selling resources.
     */
    private List<String> sellMapOptions = new ArrayList<>();

    /**
     * Minimum amount of Palladium to override to selling at 5-2.
     */
    private int minimumPalladiumToOverride = 15;

    /**
     * Handles the step being parsed.
     */
    @Override
    public void onParse() {
        // Create a list of the filtered sell maps.
        List<String> sellMapFilter = new ArrayList<>();
        if (this.parameters.containsKey("maps")) {
            sellMapFilter = (List<String>) this.parameters.get("maps");
        }

        // Get the options for the sell maps.
        EntityInfo.Faction faction = this.context.getMain().hero.playerInfo.getFaction();
        if (!SELL_MAPS.containsKey(faction)) {
            throw new RuntimeException("Unknown faction " + faction);
        }
        List<String> sellMapOptions = new ArrayList<>();
        for (String mapName : SELL_MAPS.get(faction)) {
            if (sellMapFilter.isEmpty() || sellMapFilter.contains(mapName)) {
                sellMapOptions.add(mapName);
            }
        }
        if (sellMapOptions.isEmpty()) {
            throw new RuntimeException("No valid sell location was found with the given filter.");
        }

        // Store the options.
        this.sellMapOptions = sellMapOptions;
        if (this.parameters.containsKey("minimumPalladiumToOverride")) {
            this.minimumPalladiumToOverride = this.getParameterInt("minimumPalladiumToOverride");
        }
    }

    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        // Determine the map to go to.
        // TODO: Make less manual.
        String currentMap = this.context.getMain().hero.map.name;
        boolean isInUpperMap = (currentMap.endsWith("-5") || currentMap.endsWith("-6") || currentMap.endsWith("-7") || currentMap.endsWith("-8") || currentMap.endsWith("-BL") || currentMap.startsWith("5-") || currentMap.equals("4-4") || currentMap.equals("4-5"));
        if (this.sellMapOptions.size() == 1) {
            this.mapName = this.sellMapOptions.get(0);
        } else if (this.sellMapOptions.contains("5-2") && (currentMap.equals("5-1") || currentMap.equals("5-2"))) {
            this.mapName = "5-2";
        } else if (this.sellMapOptions.contains("1-8") && (isInUpperMap || !this.sellMapOptions.contains("1-1"))) {
            this.mapName = "1-8";
        } else if (this.sellMapOptions.contains("2-8") && (isInUpperMap || !this.sellMapOptions.contains("2-1"))) {
            this.mapName = "2-8";
        } else if (this.sellMapOptions.contains("3-8") && (isInUpperMap || !this.sellMapOptions.contains("3-1"))) {
            this.mapName = "3-8";
        } else if (this.sellMapOptions.contains("1-1")) {
            this.mapName = "1-1";
        } else if (this.sellMapOptions.contains("2-1")) {
            this.mapName = "2-1";
        } else if (this.sellMapOptions.contains("3-1")) {
            this.mapName = "3-1";
        }
        if (!this.mapName.equals("5-2") && this.sellMapOptions.contains("5-2") && this.context.getMain().guiManager.oreTrade.getAmount(OreAPI.Ore.PALLADIUM) >= this.minimumPalladiumToOverride) {
            this.log(StepContext.LogLevel.INFO, "Overriding sell map to 5-2 to sell " + this.context.getMain().guiManager.oreTrade.getAmount(OreAPI.Ore.PALLADIUM) + " Palladium.");
            this.mapName = "5-2";
        }

        // Prepare going to the map.
        super.onStart();
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "GoToSellMap[options=(" + String.join(",", this.sellMapOptions) + "),map=" + this.mapName + "]";
    }
}
