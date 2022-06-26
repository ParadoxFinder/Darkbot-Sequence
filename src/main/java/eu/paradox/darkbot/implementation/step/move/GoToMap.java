package eu.paradox.darkbot.implementation.step.move;

import com.github.manolo8.darkbot.core.objects.Map;
import com.github.manolo8.darkbot.modules.MapModule;
import eu.paradox.darkbot.base.StepContext;
import eu.paradox.darkbot.base.BaseStep;

public class GoToMap extends BaseStep {
    /**
     * Name of the map to travel to.
     */
    protected String mapName;

    /**
     * Map to travel to.
     */
    private Map map;

    /**
     * Handles the step being parsed.
     */
    @Override
    public void onParse() {
        if (!this.parameters.containsKey("map")) {
            throw new RuntimeException("Missing parameter \"map\".");
        }
        this.mapName = (String) this.parameters.get("map");
    }

    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        this.log(StepContext.LogLevel.DEBUG, "Going to map " + this.mapName);
        this.map = this.context.getMain().starManager.byName(this.mapName);
    }

    /**
     * Handles the step updating.
     */
    @Override
    public void onTick() {
        if (this.context.getMain().hero.map.id == this.map.id) {
            this.log(StepContext.LogLevel.DEBUG, "Reached map " + this.mapName);
            this.completed();
            return;
        }
        this.context.getMain().setModule(new MapModule()).setTarget(this.map);
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "GoToMap[map=" + this.mapName + "]";
    }
}
