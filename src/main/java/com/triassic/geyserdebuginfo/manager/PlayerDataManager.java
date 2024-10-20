package com.triassic.geyserdebuginfo.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.triassic.geyserdebuginfo.display.DisplayType;
import lombok.Getter;
import lombok.Setter;
import org.geysermc.geyser.api.extension.ExtensionLogger;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class PlayerDataManager {

    private final File playerDataFolder;
    private final ExtensionLogger logger;
    private final ObjectMapper objectMapper;
    private final Map<UUID, Set<DisplayType>> playerDisplayStates;

    public PlayerDataManager(File dataFolder, ExtensionLogger logger) {
        this.playerDataFolder = new File(dataFolder, "playerdata");
        this.logger = logger;
        this.objectMapper = new ObjectMapper()
                .enable(SerializationFeature.INDENT_OUTPUT);
        this.playerDisplayStates = new HashMap<>();

        if (!playerDataFolder.exists() && !playerDataFolder.mkdirs()) {
            logger.error("Failed to create player data folder at " + playerDataFolder.getAbsolutePath());
        }

        loadAll();
    }

    private void loadAll() {
        File[] files = playerDataFolder.listFiles((dir, name) -> name.endsWith(".json"));
        if (files == null) return;

        for (File file : files) {
            try {
                UUID playerUuid = UUID.fromString(file.getName().replace(".json", ""));
                PlayerData data = objectMapper.readValue(file, PlayerData.class);
                Set<DisplayType> enabledDisplays = new HashSet<>();
                for (String display : data.getEnabledDisplays()) {
                    enabledDisplays.add(DisplayType.valueOf(display));
                }
                playerDisplayStates.put(playerUuid, enabledDisplays);
            } catch (IOException | IllegalArgumentException e) {
                logger.error("Failed to load player data from file: " + file.getName(), e);
            }
        }
    }

    public void save(UUID playerUuid) {
        Set<DisplayType> enabledDisplays = playerDisplayStates.get(playerUuid);
        if (enabledDisplays == null) return;

        File playerFile = new File(playerDataFolder, playerUuid + ".json");
        try {
            PlayerData data = new PlayerData();
            data.setEnabledDisplays(
                    enabledDisplays.stream()
                            .map(DisplayType::name)
                            .toList()
            );
            objectMapper.writeValue(playerFile, data);
        } catch (IOException e) {
            logger.error("Failed to save player data for " + playerUuid, e);
        }
    }

    public void setDisplayEnabled(UUID playerUuid, DisplayType displayType, boolean enabled) {
        playerDisplayStates
                .computeIfAbsent(playerUuid, k -> new HashSet<>());

        if (enabled) {
            playerDisplayStates.get(playerUuid).add(displayType);
        } else {
            playerDisplayStates.get(playerUuid).remove(displayType);
        }

        save(playerUuid);
    }

    public boolean isDisplayEnabled(UUID playerUuid, DisplayType displayType) {
        return playerDisplayStates
                .getOrDefault(playerUuid, Collections.emptySet())
                .contains(displayType);
    }

    public Set<DisplayType> getEnabledDisplays(UUID playerUuid) {
        return Collections.unmodifiableSet(
                playerDisplayStates.getOrDefault(playerUuid, Collections.emptySet())
        );
    }

    @Setter
    @Getter
    private static class PlayerData {
        private List<String> enabledDisplays = new ArrayList<>();

    }
}

