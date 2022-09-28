package eu.paradox.darkbot.implementation.step.simple;

import eu.paradox.darkbot.base.BaseStep;

public class Wait extends BaseStep {
    /**
     * Start time of waiting.
     */
    private long startTime = 0;

    /**
     * Required delay in milliseconds.
     */
    private double requiredDelay = 0;

    /**
     * Handles the step being parsed.
     */
    @Override
    public void onParse() {
        if (!this.parameters.containsKey("delay")) {
            throw new RuntimeException("Missing parameter \"delay\".");
        }

        try {
            this.requiredDelay = this.getParameterDouble("delay") * 1000d;
        } catch (ClassCastException e) {
            this.requiredDelay = this.getParameterInt("delay") * 1000d;
        }
    }

    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        this.startTime = System.currentTimeMillis();
    }

    /**
     * Handles the step updating.
     */
    @Override
    public void onTick() {
        // Complete the step if the elapsed time is past the required wait.
        long deltaTime = System.currentTimeMillis() - this.startTime;
        if (deltaTime > this.requiredDelay) {
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
        return "WaitStep[delayMilliseconds=" + this.requiredDelay + "]";
    }
}
