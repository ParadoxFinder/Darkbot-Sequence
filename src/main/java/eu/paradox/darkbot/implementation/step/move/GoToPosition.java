package eu.paradox.darkbot.implementation.step.move;

import com.github.manolo8.darkbot.core.utils.Location;
import eu.paradox.darkbot.base.BaseStep;
import eu.paradox.darkbot.base.StepContext;

public class GoToPosition extends BaseStep {
    /**
     * X position to travel to.
     */
    protected double x;

    /**
     * Y position to travel to.
     */
    protected double y;

    /**
     * Handles the step being parsed.
     */
    @Override
    public void onParse() {
        if (!this.parameters.containsKey("x")) {
            throw new RuntimeException("Missing parameter \"x\".");
        }
        this.x = this.getParameterDouble("x");
        if (!this.parameters.containsKey("y")) {
            throw new RuntimeException("Missing parameter \"y\".");
        }
        this.y = this.getParameterDouble("y");
    }

    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        this.log(StepContext.LogLevel.DEBUG, "Going to position " + this.x + "," + this.y);
    }

    /**
     * Handles the step updating.
     */
    @Override
    public void onTick() {
        Location targetLocation = new Location(this.x * 100d, this.y * 100d);
        if (this.context.getMain().hero.drive.getCurrentLocation().distance(targetLocation) < 100) {
            this.log(StepContext.LogLevel.DEBUG, "Reached position " + this.x + "," + this.y);
            this.completed();
            return;
        }
        this.context.getMain().hero.drive.moveTo(targetLocation);
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "GoToPosition[x=" + this.x + "," + this.y + "]";
    }
}
