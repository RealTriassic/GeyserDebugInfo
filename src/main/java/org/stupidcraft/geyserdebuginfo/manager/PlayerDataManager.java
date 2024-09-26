package org.stupidcraft.geyserdebuginfo.manager;

import org.geysermc.geyser.api.extension.ExtensionLogger;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerDataManager {

    private final File playerDataFile;
    private final ExtensionLogger logger;
    private final Yaml yaml;
    private final Map<UUID, Boolean> playerF3States;
    private final boolean defaultEnabled;

    /**
     * Constructs a new PlayerDataManager and loads player data from the YAML file.
     *
     * @param dataFolder     The data folder to use for storing the player data file.
     * @param logger         The logger used for logging errors and information during operations.
     * @param defaultEnabled If true, track players with F3 disabled; otherwise, track players with F3 enabled.
     */
    public PlayerDataManager(
            final File dataFolder,
            final ExtensionLogger logger,
            final boolean defaultEnabled
    ) {
        this.playerDataFile = new File(dataFolder, "playerdata.yml");
        this.logger = logger;
        this.yaml = new Yaml(createDumperOptions());
        this.playerF3States = new HashMap<>();
        this.defaultEnabled = defaultEnabled;

        loadPlayerData();
    }

    /**
     * Creates DumperOptions for the YAML configuration.
     *
     * @return DumperOptions configured for the YAML.
     */
    private DumperOptions createDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
    }

    /**
     * Loads player data from the YAML file.
     */
    private void loadPlayerData() {
        if (!playerDataFile.exists()) return;

        try (FileReader reader = new FileReader(playerDataFile)) {
            Map<String, Boolean> data = yaml.load(reader);
            if (data != null) {
                for (Map.Entry<String, Boolean> entry : data.entrySet()) {
                    playerF3States.put(UUID.fromString(entry.getKey()), entry.getValue());
                }
            }
        } catch (Throwable error) {
            logger.error("Failed to load playerdata file", error);
        }
    }

    /**
     * Saves the current player F3 states to the YAML file.
     */
    public void savePlayerData() {
        try (FileWriter writer = new FileWriter(playerDataFile)) {
            Map<String, Boolean> data = new HashMap<>();
            for (Map.Entry<UUID, Boolean> entry : playerF3States.entrySet()) {
                Boolean state = entry.getValue();
                if ((defaultEnabled && !state) || (!defaultEnabled && state)) {
                    data.put(entry.getKey().toString(), state);
                }
            }
            yaml.dump(data, writer);
        } catch (Throwable error) {
            logger.error("Failed to save playerdata to file", error);
        }
    }

    /**
     * Sets the F3 enabled state for a player.
     *
     * @param playerUuid The UUID of the player.
     * @param enabled    The F3 enabled state to set.
     */
    public void setF3Enabled(UUID playerUuid, boolean enabled) {
        if ((defaultEnabled && enabled) || (!defaultEnabled && !enabled)) {
            playerF3States.remove(playerUuid);
        } else {
            playerF3States.put(playerUuid, enabled);
        }
        savePlayerData();
    }

    /**
     * Checks if F3 is enabled for the specified player.
     *
     * @param playerUuid The UUID of the player.
     * @return true if F3 is enabled, false otherwise.
     */
    public boolean isF3Enabled(UUID playerUuid) {
        return playerF3States.getOrDefault(playerUuid, defaultEnabled);
    }
}
