package eu.paradox.darkbot.base;

import eu.paradox.darkbot.data.ConditionEntry;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseCondition {
    /**
     * Context for running steps.
     */
    public StepContext context;

    /**
     * Parameters for the condition.
     */
    public Map<String, ?> parameters;

    /**
     * Step to run after the condition becomes true.
     */
    public BaseStep nextStep;

    /**
     * Parses the condition and the children.
     *
     * @param context Context for creating other steps.
     * @param entry The entry to parse.
     */
    public void parse(StepContext context, ConditionEntry entry) {
        this.context = context;

        // Store the parameters.
        this.parameters = entry.parameters;
        if (this.parameters == null) {
            this.parameters = new HashMap<>();
        }

        // Parse the step.
        if (entry.step != null) {
            this.nextStep = context.parseStep(entry.step);
        }

        // Finish the parsing.
        this.onParse();
    }

    /**
     * Handles the step being parsed.
     */
    public void onParse() {

    }

    /**
     * Outputs a message to the logs.
     *
     * @param level Log level to output.
     * @param message Message to log.
     */
    public void log(StepContext.LogLevel level, String message) {
        this.context.log(level, message);
    }

    /**
     * Resets the condition.
     */
    public void reset() {

    }

    /**
     * Returns if the condition is true.
     *
     * @return If the condition is true.
     */
    public abstract boolean isTrue();
}
