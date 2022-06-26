package eu.paradox.darkbot;

import com.github.manolo8.darkbot.Main;
import com.github.manolo8.darkbot.config.types.Option;
import com.github.manolo8.darkbot.core.itf.Behaviour;
import com.github.manolo8.darkbot.core.itf.Configurable;
import com.github.manolo8.darkbot.extensions.features.Feature;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.darkbot.api.game.other.EntityInfo;
import eu.paradox.darkbot.base.StepContext;
import eu.paradox.darkbot.data.StepEntry;

import java.io.File;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;

@Feature(name = "SequenceBehavior", description = "Behavior for running the sequences.")
public class SequenceBehavior implements Behaviour, Configurable<SequenceBehavior.Config> {
    /**
     * Configuration for the Zombie plugin.
     */
    public static class Config {
        @Option(value = "Sequence First Step", description = "Name of the step in the sequence file to start with.")
        public String SEQUENCE_FIRST_STEP = "main";
    }

    /**
     * Main character for the bot.
     */
    private Main main;

    /**
     * Configuration of the bot.
     */
    private Config config;

    /**
     * Context for running steps.
     */
    private StepContext context;

    /**
     * Whether the sequence was started.
     */
    private boolean started = false;

    /**
     * Installs the module.
     *
     * @param main Main character of the bot.
     */
    @Override
    public void install(Main main) {
        this.main = main;
    }

    /**
     * Sets the configuration of the module.
     *
     * @param config New configuration of the module.
     */
    @Override
    public void setConfig(Config config) {
        this.config = config;
    }

    /**
     * Loads the context.
     */
    public void loadContext() {
        // Return if the context already was created.
        if (this.context != null) {
            return;
        }

        // Throw an exception if the sequence file doesn't exist.
        String path = "sequence.json";
        if (!new File(path).exists()) {
            throw new RuntimeException("Sequence file \"" + path + "\" does not exist.");
        }

        // Read the sequence file.
        Map<String, StepEntry> dataSteps = null;
        try {
            Reader reader = Files.newBufferedReader(Paths.get(path));
            dataSteps = new Gson().fromJson(reader, new TypeToken<Map<String, StepEntry>>() {}.getType());
            reader.close();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to read sequence file \"" + path + "\"", ex);
        }

        // Create the context.
        this.context = new StepContext(main);
        for (String name : dataSteps.keySet()) {
            this.context.addNamedStep(name, dataSteps.get(name));
        }
    }

    /**
     * Runs a single tick of the behavior.
     */
    @Override
    public void tick() {
        // Return if the sequence module was not started.
        if (!SequenceModule.isStarted()) {
            return;
        }

        // Return if the character is not loaded.
        if (this.main.hero.playerInfo.getFaction() == EntityInfo.Faction.NONE) {
            return;
        }

        // Start and tick the sequence.
        if (!this.started) {
            this.loadContext();
            this.context.start(this.config.SEQUENCE_FIRST_STEP);
            this.started = true;
        }
        this.context.tick();
    }
}
