package eu.paradox.darkbot.implementation.step.simple;

import com.github.manolo8.darkbot.extensions.features.FeatureDefinition;
import eu.paradox.darkbot.SequenceModule;
import eu.paradox.darkbot.base.BaseStep;
import eu.darkbot.api.extensions.Module;
import eu.paradox.darkbot.base.StepContext;

public class RunModule extends BaseStep {
    /**
     * Name of the module to run.
     */
    private String moduleName;

    /**
     * Module to run.
     */
    private Module module;

    /**
     * Handles the step being parsed.
     */
    @Override
    public void onParse() {
        if (!this.parameters.containsKey("module")) {
            throw new RuntimeException("Missing parameter \"module\".");
        }
        this.moduleName = (String) this.parameters.get("module");
    }

    /**
     * Handles the step starting.
     */
    @Override
    public void onStart() {
        for (FeatureDefinition featureDefinition : this.context.getMain().featureRegistry.getFeatures()) {
            if (!featureDefinition.getName().equals(this.moduleName)) continue;
            this.module = (Module) this.context.getMain().featureRegistry.getFeature(featureDefinition).get();
            this.context.getMain().setModule(this.module);
            this.log(StepContext.LogLevel.DEBUG, "Started module " + this.moduleName);
            return;
        }
        throw new RuntimeException("Unable to get module \"" + this.moduleName + "\".");
    }

    /**
     * Handles the step updating.
     */
    @Override
    public void onTick() {
        if (this.context.getMain().getModule() != null && this.context.getMain().getModule().getClass() != SequenceModule.class) return;
        this.context.getMain().setModule(this.module);
        this.log(StepContext.LogLevel.DEBUG, "Started module " + this.moduleName);
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "RunModule[module=" + this.moduleName + "]";
    }
}
