package eu.paradox.darkbot.data;

import java.util.Map;

public class ConditionEntry {
    /**
     * Type of the condition.
     */
    public String name;

    /**
     * Parameters for the condition.
     */
    public Map<String, ?> parameters;

    /**
     * Step to run if the condition is met.
     */
    public StepEntry step;
}
