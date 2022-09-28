package eu.paradox.darkbot.base;

import eu.paradox.darkbot.data.ConditionEntry;
import eu.paradox.darkbot.data.StepEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public abstract class BaseStep {
    /**
     * Context for running steps.
     */
    public StepContext context;

    /**
     * Parameters for the step.
     */
    public Map<String, ?> parameters;

    /**
     * Next step to run after the current step completes.
     */
    public BaseStep nextStep;

    /**
     * Next steps to run if a condition is met.
     */
    public List<BaseCondition> conditionSteps = new ArrayList<>();

    /**
     * Gets an double parameter.
     *
     * @param name Name of the parameter to get.
     * @return The double value of the parameter.
     */
    public double getParameterDouble(String name) {
        return (Double) this.parameters.get(name);
    }

    /**
     * Gets an integer parameter.
     *
     * @param name Name of the parameter to get.
     * @return The integer value of the parameter.
     */
    public int getParameterInt(String name) {
        try {
            return (int) Math.floor(this.getParameterDouble(name));
        } catch (RuntimeException e) {
            return (Integer) this.parameters.get(name);
        }
    }

    /**
     * Parses the entry and the children.
     *
     * @param context Context for creating other steps.
     * @param entry The entry to parse.
     */
    public void parse(StepContext context, StepEntry entry) {
        this.context = context;

        // Store the parameters.
        this.parameters = entry.parameters;
        if (this.parameters == null) {
            this.parameters = new HashMap<>();
        }

        // Parse the next step.
        if (entry.nextStep != null) {
            this.nextStep = context.parseStep(entry.nextStep);
        }

        // Parse the conditions.
        if (entry.conditionSteps != null) {
            for (ConditionEntry conditionEntry : entry.conditionSteps) {
                this.conditionSteps.add(context.parseCondition(conditionEntry));
            }
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
     * Invoked when the step is started.
     */
    public final void start() {
        // Display a message if one is configured.
        if (this.parameters != null && this.parameters.containsKey("output")) {
            this.log(StepContext.LogLevel.INFO, (String) this.parameters.get("output"));
        }

        // Reset the conditions.
        for (BaseCondition condition : this.conditionSteps) {
            condition.reset();
        }

        // Start the step.
        this.onStart();
        this.tick();
    }

    /**
     * Handles the step starting.
     */
    public void onStart() {

    }

    /**
     * Ticks the step.
     */
    public final void tick() {
        // Start the next condition if one of them is valid.
        for (BaseCondition condition : this.conditionSteps) {
            if (condition.isTrue()) {
                this.log(StepContext.LogLevel.DEBUG, "Condition met: " + condition);
                this.context.start(condition.nextStep);
                return;
            }
        }

        // Tick the step.
        this.onTick();
    }

    /**
     * Handles the step updating.
     */
    public void onTick() {

    }

    /**
     * Completes the step and starts the next step.
     */
    public void completed() {
        this.context.complete(this);
        this.context.start(this.nextStep);
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
}
