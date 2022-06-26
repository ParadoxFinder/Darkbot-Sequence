package eu.paradox.darkbot;

import com.github.manolo8.darkbot.Main;
import com.github.manolo8.darkbot.core.itf.Module;
import com.github.manolo8.darkbot.extensions.features.Feature;

@Feature(name = "Sequence", description = "Follows a sequence of instructions.")
public class SequenceModule implements Module {
    /**
     * Whether the module was started.
     */
    private static boolean started = false;

    /**
     * Returns whether the bot was started.
     *
     * @return If the module was started.
     */
    public static boolean isStarted() {
        return started;
    }

    /**
     * Installs the module.
     *
     * @param main Main character of the bot.
     */
    @Override
    public void install(Main main) {

    }

    /**
     * Returns whether the bot can refresh (reinstall).
     *
     * @return Whether the bot can refresh (reinstall).
     */
    @Override
    public boolean canRefresh() {
        return false;
    }

    /**
     * Runs a single tick of the module.
     */
    @Override
    public void tick() {
        started = true;
    }
}
