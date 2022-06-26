package eu.paradox.darkbot.implementation.step.simple;

import eu.paradox.darkbot.base.BaseStep;

public class RunStep extends BaseStep {
    /**
     * Name of the step to run.
     */
    private String stepName;

    /**
     * Handles the step being parsed.
     */
    @Override
    public void onParse() {
        if (!this.parameters.containsKey("step")) {
            throw new RuntimeException("Missing parameter \"step\".");
        }
        this.stepName = (String) this.parameters.get("step");
    }

    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        this.context.addToStack(this.nextStep);
        this.context.start(this.stepName);
        this.completed();
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "RunStep[step=" + this.stepName + "]";
    }
}
