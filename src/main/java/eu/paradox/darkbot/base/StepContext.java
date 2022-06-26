package eu.paradox.darkbot.base;

import com.github.manolo8.darkbot.Main;
import eu.paradox.darkbot.implementation.condition.time.OnTimeReached;
import eu.paradox.darkbot.implementation.step.move.GoToPosition;
import eu.paradox.darkbot.implementation.step.move.GoToSellMap;
import eu.paradox.darkbot.implementation.step.simple.*;
import eu.paradox.darkbot.data.ConditionEntry;
import eu.paradox.darkbot.data.StepEntry;
import eu.paradox.darkbot.implementation.condition.death.OnDestroyed;
import eu.paradox.darkbot.implementation.condition.death.OnPreviousKillerSpotted;
import eu.paradox.darkbot.implementation.macro.SellResources;
import eu.paradox.darkbot.implementation.step.end.Logout;
import eu.paradox.darkbot.implementation.step.end.Stop;
import eu.paradox.darkbot.implementation.step.move.GoToMap;
import eu.paradox.darkbot.implementation.step.move.GoToSellPosition;
import eu.paradox.darkbot.implementation.step.sell.ClickSellResources;
import eu.paradox.darkbot.implementation.step.sell.OpenSell;

import java.util.*;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class StepContext {
    public enum LogLevel {
        ERROR,
        WARNING,
        INFO,
        DEBUG,
    }

    /**
     * Constructors for the steps.
     */
    public static final Map<String, Callable<? extends BaseStep>> STEPS = new HashMap<String, Callable<? extends BaseStep>>() {{
        // Simple
        put("Empty", Empty::new);
        put("RunModule", RunModule::new);
        put("RunStep", RunStep::new);
        put("StopModule", StopModule::new);
        put("Wait", Wait::new);

        // Move
        put("GoToMap", GoToMap::new);
        put("GoToSellMap", GoToSellMap::new);
        put("GoToPosition", GoToPosition::new);
        put("GoToSellPosition", GoToSellPosition::new);

        // Sell
        put("OpenSell", OpenSell::new);
        put("ClickSellResources", ClickSellResources::new);

        // End
        put("Logout", Logout::new);
        put("Stop", Stop::new);
    }};

    /**
     * Classes for the conditions.
     */
    private static final Map<String, Callable<? extends BaseCondition>> conditions = new HashMap<String, Callable<? extends BaseCondition>>() {{
        // Death
        put("OnDestroyed", OnDestroyed::new);
        put("OnPreviousKillerSpotted", OnPreviousKillerSpotted::new);

        // Time
        put("OnTimeReached", OnTimeReached::new);
    }};

    /**
     * Macros for parsing steps into more steps.
     */
    public static final Map<String, Function<StepEntry, StepEntry>> MACROS = new HashMap<String,  Function<StepEntry, StepEntry>>() {{
        put("SellResources", SellResources::parse);
    }};

    /**
     * Current stack of steps.
     */
    private final Stack<BaseStep> stepsStack = new Stack<>();

    /**
     * Steps that have been started.
     */
    private final Set<BaseStep> startedSteps = new HashSet<>();

    /**
     * Main character of the bot.
     */
    private final Main main;

    /**
     * Named root steps that can be run.
     */
    private final Map<String, BaseStep> rootSteps = new HashMap<>();

    /**
     * Creates the step context.
     *
     * @param main Main character of the bot.
     */
    public StepContext(Main main) {
        this.main = main;
    }

    /**
     * Returns the main character of the bot.
     *
     * @return The main character of the bot.
     */
    public Main getMain() {
        return this.main;
    }

    /**
     * Returns if the context is completed.
     *
     * @return If the context is completed.
     */
    public boolean isCompleted() {
        return this.stepsStack.empty();
    }

    /**
     * Parses a step entry.
     *
     * @param entry Data of the entry to parse.
     * @return The parsed step.
     */
    public BaseStep parseStep(StepEntry entry) {
        // Throw an exception if the name is invalid.
        if (entry.name == null) {
            throw new RuntimeException("Step name is not defined.");
        }

        // Parse the entry if it is a macro.
        if (MACROS.containsKey(entry.name)) {
            entry = MACROS.get(entry.name).apply(entry);
        }

        // Parse the step.
        if (!STEPS.containsKey(entry.name)) {
            throw new RuntimeException("Unknown step name defined: \"" + entry.name + "\"");
        }
        try {
            BaseStep step = STEPS.get(entry.name).call();
            step.parse(this, entry);
            return step;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Parses a condition entry.
     *
     * @param entry Data of the entry to parse.
     * @return The parsed condition.
     */
    public BaseCondition parseCondition(ConditionEntry entry) {
        // Throw an exception if the name is invalid.
        if (entry.name == null) {
            throw new RuntimeException("Condition name is not defined.");
        }
        if (!conditions.containsKey(entry.name)) {
            throw new RuntimeException("Unknown condition name defined: \"" + entry.name + "\"");
        }

        // Parse the condition.
        try {
            BaseCondition condition = conditions.get(entry.name).call();
            condition.parse(this, entry);
            return condition;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns if a named step exists.
     *
     * @param name Name of the step to check.
     * @return If the named step exists.
     */
    public boolean namedStepExists(String name) {
        return this.rootSteps.containsKey(name);
    }

    /**
     * Adds a step with a name that can be invoked by name.
     *
     * @param name The name of the entry to add.
     * @param entry The data of the step entry to parse.
     */
    public void addNamedStep(String name, StepEntry entry) {
        this.rootSteps.put(name, this.parseStep(entry));
    }

    /**
     * Adds a step to the stack that is not started. This is in order to queue
     * steps to run.
     *
     * @param step Step to add to the stack.
     */
    public void addToStack(BaseStep step) {
        if (step == null || this.stepsStack.contains(step)) {
            return;
        }
        this.stepsStack.add(step);
    }

    /**
     * Starts a step.
     *
     * @param name The name of the named step.
     */
    public void start(String name) {
        if (!this.rootSteps.containsKey(name)) {
            throw new RuntimeException("Unable to start step that is not registered: \"" + name + "\"");
        }
        this.start(this.rootSteps.get(name));
    }

    /**
     * Starts a step.
     *
     * @param step The step to run.
     */
    public void start(BaseStep step) {
        if (step == null || this.stepsStack.contains(step)) {
            return;
        }
        this.addToStack(step);
        this.startedSteps.add(step);
        step.start();
    }

    /**
     * Ticks the current step.
     */
    public void tick() {
        if (!this.stepsStack.empty()) {
            BaseStep stepToTick = this.stepsStack.peek();
            if (!this.startedSteps.contains(stepToTick)) {
                this.startedSteps.add(stepToTick);
                stepToTick.start();
            } else {
                stepToTick.tick();
            }
        }
    }

    /**
     * Completes a step and removes it from the stack.
     *
     * @param step Step to complete.
     */
    public void complete(BaseStep step) {
        this.stepsStack.remove(step);
        this.startedSteps.remove(step);
    }

    /**
     * Outputs a message to the logs.
     *
     * @param level Log level to output.
     * @param message Message to log.
     */
    public void log(LogLevel level, String message) {
        System.out.println("[" + level + "] " + message);
    }
}
