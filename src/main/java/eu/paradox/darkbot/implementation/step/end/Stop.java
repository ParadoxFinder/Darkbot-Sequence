package eu.paradox.darkbot.implementation.step.end;

import eu.paradox.darkbot.base.StepContext;
import eu.paradox.darkbot.base.BaseStep;

public class Stop extends BaseStep {
    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        this.log(StepContext.LogLevel.DEBUG, "Stopping bot.");
        this.context.getMain().setRunning(false);
        this.completed();
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "Stop[]";
    }
}
