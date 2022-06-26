package eu.paradox.darkbot.implementation.condition.death;

import com.github.manolo8.darkbot.core.entities.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import eu.paradox.darkbot.base.BaseCondition;
import eu.paradox.darkbot.base.StepContext;

import java.io.File;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class OnPreviousKillerSpotted extends BaseCondition {
    /**
     * Players that are known for being hostile (have killed the player).
     */
    private List<String> knownHostilePlayers;

    /**
     * Resets the condition.
     */
    @Override
    public void reset() {
        // Read the known hostile players.
        String path = "hostilePlayers.json";
        if (new File(path).exists()) {
            try {
                Reader reader = Files.newBufferedReader(Paths.get(path));
                this.knownHostilePlayers = new Gson().fromJson(reader, new TypeToken<List<String>>() {}.getType());
                reader.close();
            } catch (Exception ex) {
                throw new RuntimeException("Failed to read sequence file \"" + path + "\"", ex);
            }
        } else {
            this.knownHostilePlayers = new ArrayList<>();
        }
    }

    /**
     * Returns if the condition is true.
     *
     * @return If the condition is true.
     */
    @Override
    public boolean isTrue() {
        // Add the previous death if the list doesn't contain it.
        String lastDeath = this.context.getMain().repairManager.getLastDestroyerName();
        if (lastDeath != null && !this.knownHostilePlayers.contains(lastDeath)) {
            this.log(StepContext.LogLevel.DEBUG, "Adding known hostile player " + lastDeath);
            this.knownHostilePlayers.add(lastDeath);
            try (OutputStreamWriter writer = new OutputStreamWriter(Files.newOutputStream(Paths.get("hostilePlayers.json")), StandardCharsets.UTF_8)) {
                writer.write(new Gson().toJson(this.knownHostilePlayers));
            } catch (IOException e) {

            }
        }

        // Return true if there is a known hostile player.
        for (Player player : this.context.getMain().mapManager.entities.players) {
            if (!player.playerInfo.isEnemy()) continue;
            if (!this.knownHostilePlayers.contains(player.playerInfo.username)) continue;
            this.log(StepContext.LogLevel.INFO, "Known hostile player spotted: " + player.playerInfo.username);
            return true;
        }

        // Return false (no known hostile players found).
        return false;
    }

    /**
     * Returns the object as a string.
     *
     * @return The object as a string.
     */
    @Override
    public String toString() {
        return "OnPreviousKillerSpotted[]";
    }
}
