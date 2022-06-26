package eu.paradox.darkbot.implementation.step.end;

import eu.paradox.darkbot.base.StepContext;
import eu.paradox.darkbot.base.BaseStep;

public class Logout extends BaseStep {
    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        this.log(StepContext.LogLevel.DEBUG, "Logging out account.");
        this.context.getMain().guiManager.logout.show(true);
        this.completed();
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "Logout[]";
    }
}
