package eu.paradox.darkbot.implementation.condition.death;

import eu.paradox.darkbot.base.BaseCondition;
import eu.paradox.darkbot.base.StepContext;

public class OnDestroyed extends BaseCondition {
    /**
     * The total deaths at the time of loading.
     */
    private int initialDeaths;

    /**
     * Resets the condition.
     */
    @Override
    public void reset() {
        this.initialDeaths = this.context.getMain().repairManager.getDeathAmount();
    }

    /**
     * Returns if the condition is true.
     *
     * @return If the condition is true.
     */
    @Override
    public boolean isTrue() {
        boolean conditionValid = this.context.getMain().repairManager.getDeathAmount() > this.initialDeaths;
        if (conditionValid) {
            this.log(StepContext.LogLevel.INFO, "Player was destroyed by " + this.context.getMain().repairManager.getLastDestroyerName());
        }
        return conditionValid;
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "OnDestroyed[]";
    }
}
