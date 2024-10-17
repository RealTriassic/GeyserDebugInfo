package com.triassic.geyserdebuginfo.manager;

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

    private DumperOptions createDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
    }

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

    public void setF3Enabled(UUID playerUuid, boolean enabled) {
        if ((defaultEnabled && enabled) || (!defaultEnabled && !enabled)) {
            playerF3States.remove(playerUuid);
        } else {
            playerF3States.put(playerUuid, enabled);
        }
        savePlayerData();
    }

    public boolean isF3Enabled(UUID playerUuid) {
        return playerF3States.getOrDefault(playerUuid, defaultEnabled);
    }
}
