package eu.paradox.darkbot.implementation.step.simple;

import eu.paradox.darkbot.base.BaseStep;

public class StopModule extends BaseStep {
    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        this.context.getMain().setModule(null);
        this.completed();
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "StopModule[]";
    }
}
