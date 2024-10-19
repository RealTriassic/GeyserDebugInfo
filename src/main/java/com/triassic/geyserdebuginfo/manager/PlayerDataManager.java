package com.triassic.geyserdebuginfo.manager;

import com.triassic.geyserdebuginfo.display.DisplayType;
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
    private final Map<UUID, Map<DisplayType, Boolean>> playerDisplayStates;
    private final boolean defaultEnabled;

    public PlayerDataManager(
            final File dataFolder,
            final ExtensionLogger logger,
            final boolean defaultEnabled
    ) {
        this.playerDataFile = new File(dataFolder, "playerdata.yml");
        this.logger = logger;
        this.yaml = new Yaml(createDumperOptions());
        this.playerDisplayStates = new HashMap<>();
        this.defaultEnabled = defaultEnabled;

        load();
    }

    private DumperOptions createDumperOptions() {
        DumperOptions options = new DumperOptions();
        options.setIndent(2);
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        return options;
    }

    private void load() {
        if (!playerDataFile.exists()) return;

        try (FileReader reader = new FileReader(playerDataFile)) {
            Map<String, Map<String, Boolean>> data = yaml.load(reader);
            if (data != null) {
                for (Map.Entry<String, Map<String, Boolean>> entry : data.entrySet()) {
                    UUID playerUuid = UUID.fromString(entry.getKey());
                    Map<DisplayType, Boolean> displayStates = new HashMap<>();
                    for (Map.Entry<String, Boolean> stateEntry : entry.getValue().entrySet()) {
                        displayStates.put(DisplayType.valueOf(stateEntry.getKey()), stateEntry.getValue());
                    }
                    playerDisplayStates.put(playerUuid, displayStates);
                }
            }
        } catch (Throwable error) {
            logger.error("Failed to load player data file", error);
        }
    }

    public void save() {
        try (FileWriter writer = new FileWriter(playerDataFile)) {
            Map<String, Map<String, Boolean>> data = new HashMap<>();
            for (Map.Entry<UUID, Map<DisplayType, Boolean>> entry : playerDisplayStates.entrySet()) {
                UUID playerUuid = entry.getKey();
                Map<String, Boolean> stateMap = new HashMap<>();
                for (Map.Entry<DisplayType, Boolean> stateEntry : entry.getValue().entrySet()) {
                    Boolean enabled = stateEntry.getValue();
                    if (enabled != defaultEnabled) {
                        stateMap.put(stateEntry.getKey().name(), enabled);
                    }
                }
                if (!stateMap.isEmpty()) {
                    data.put(playerUuid.toString(), stateMap);
                }
            }
            yaml.dump(data, writer);
        } catch (Throwable error) {
            logger.error("Failed to save player data to file", error);
        }
    }

    public void setDisplayEnabled(UUID playerUuid, DisplayType displayType, boolean enabled) {
        playerDisplayStates
                .computeIfAbsent(playerUuid, k -> new HashMap<>())
                .put(displayType, enabled);

        save();
    }

    public boolean isDisplayEnabled(UUID playerUuid, DisplayType displayType) {
        return playerDisplayStates
                .getOrDefault(playerUuid, new HashMap<>())
                .getOrDefault(displayType, defaultEnabled);
    }
}

