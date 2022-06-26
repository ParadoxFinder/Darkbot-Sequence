package eu.paradox.darkbot.implementation.step.sell;

import com.github.manolo8.darkbot.core.objects.OreTradeGui;
import eu.darkbot.api.managers.OreAPI;
import eu.paradox.darkbot.base.StepContext;
import eu.paradox.darkbot.base.BaseStep;

import java.util.ArrayList;
import java.util.List;

public class ClickSellResources extends BaseStep {
    /**
     * Delay between selling ores.
     */
    private static final long SELL_DELAY_MILLISECONDS = 500L;

    /**
     * Ores to sell.
     */
    private List<OreTradeGui.Ore> oresToSell;

    /**
     * Current ore to sell.
     */
    private int currentOre = 0;

    /**
     * Last sell time of the ore.
     */
    private long lastSellTime = 0;

    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        // Get the resources to sell.
        this.oresToSell = new ArrayList<>();
        this.currentOre = 0;
        this.lastSellTime = 0;
        for (OreTradeGui.Ore ore : OreTradeGui.Ore.values()) {
            if (this.context.getMain().guiManager.oreTrade.getAmount(OreAPI.Ore.valueOf(ore.name())) > 0) {
                this.oresToSell.add(ore);
            }
        }
    }

    /**
     * Handles the step updating.
     */
    @Override
    public void onTick() {
        // Complete the step if all resources are sold.
        if (this.currentOre >= this.oresToSell.size()) {
            this.log(StepContext.LogLevel.DEBUG, "All resources have been sold.");
            this.completed();
            return;
        }

        // Wait for the next tick if not enough time has passed for selling.
        if (System.currentTimeMillis() - this.lastSellTime < SELL_DELAY_MILLISECONDS) {
            return;
        }

        // Sell the current ore.
        this.log(StepContext.LogLevel.DEBUG, "Selling " + this.oresToSell.get(this.currentOre).name());
        this.context.getMain().guiManager.oreTrade.sellOre(this.oresToSell.get(this.currentOre));
        this.currentOre += 1;
        this.lastSellTime = System.currentTimeMillis();
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "ClickSellResources[]";
    }
}
