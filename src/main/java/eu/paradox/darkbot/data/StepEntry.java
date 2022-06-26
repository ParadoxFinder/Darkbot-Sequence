package eu.paradox.darkbot.data;

import java.util.List;
import java.util.Map;

public class StepEntry {
    /**
     * Type of the step.
     */
    public String name;

    /**
     * Parameters for the step.
     */
    public Map<String, ?> parameters;

    /**
     * Next step to run after the current step completes.
     */
    public StepEntry nextStep;

    /**
     * Next steps to run if a condition is met.
     */
    public List<ConditionEntry> conditionSteps;

    /**
     * Creates a step entry.
     */
    public StepEntry() {

    }

    /**
     * Creates a step entry.
     *
     * @param name Type of the step.
     * @param parameters Parameters for the step.
     * @param nextStep Next step to run after the current step completes.
     * @param conditionSteps Next steps to run if a condition is met.
     */
    public StepEntry(String name, Map<String, ?> parameters, StepEntry nextStep, List<ConditionEntry> conditionSteps) {
        this.name = name;
        this.parameters = parameters;
        this.nextStep = nextStep;
        this.conditionSteps = conditionSteps;
    }
}
