package eu.paradox.darkbot.implementation.step.sell;

import com.github.manolo8.darkbot.core.entities.BasePoint;
import com.github.manolo8.darkbot.core.utils.Location;
import eu.paradox.darkbot.base.StepContext;
import eu.paradox.darkbot.implementation.step.move.GoToSellPosition;
import eu.paradox.darkbot.base.BaseStep;

public class OpenSell extends BaseStep {
    /**
     * Sell location to open.
     */
    private BasePoint sellBasePoint;

    /**
     * Handles the step starting.
     */
    public void onStart() {
        // Determine the sell location.
        String currentMap = this.context.getMain().hero.map.name;
        if (!GoToSellPosition.SELL_LOCATIONS_PER_MAP.containsKey(currentMap)) {
            throw new RuntimeException("No sell location stored for " + currentMap);
        }
        Location sellLocation = GoToSellPosition.SELL_LOCATIONS_PER_MAP.get(currentMap);
        sellLocation = new Location(sellLocation.x * 100d, sellLocation.y * 100d);

        // Determine the sell base point.
        for (Object point : this.context.getMain().mapManager.entities.basePoints.stream().filter(b -> b.locationInfo.isLoaded()).toArray()) {
            BasePoint basePoint = (BasePoint) point;
            if (this.sellBasePoint == null || sellLocation.distance(basePoint) < sellLocation.distance(this.sellBasePoint)) {
                this.sellBasePoint = basePoint;
            }
        }
    }

    /**
     * Handles the step updating.
     */
    @Override
    public void onTick() {
        if (this.context.getMain().guiManager.oreTrade.showTrade(true, this.sellBasePoint)) {
            this.log(StepContext.LogLevel.DEBUG, "Opened sell window.");
            this.completed();
        }
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "OpenSell[]";
    }
}
