package com.triassic.geyserdebuginfo.configuration;

import org.geysermc.geyser.api.extension.ExtensionLogger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigurationContainer {

    private static final String HEADER = """
            GeyserDebugInfo Configuration File
            A Geyser extension that strives to provide F3-like debug information for Bedrock Edition players.
            
            Report any issues on our GitHub repository:
            https://github.com/RealTriassic/GeyserDebugInfo""";

    private final Path configFile;
    private final ExtensionLogger logger;
    private final YamlConfigurationLoader loader;
    private final Class<? extends Configuration> configClass;
    private final AtomicReference<Configuration> config = new AtomicReference<>();

    public ConfigurationContainer(
            final Path dataFolder,
            final ExtensionLogger logger,
            final Class<? extends Configuration> configClass) {
        this.logger = logger;
        this.configClass = configClass;
        this.configFile = dataFolder.resolve("config.yml");

        this.loader = YamlConfigurationLoader.builder()
                .indent(2)
                .path(configFile)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(options -> options
                        .shouldCopyDefaults(true)
                        .header(HEADER))
                .build();

        this.load();
    }

    /**
     * Loads the configuration from the file.
     * If loading fails, the previous configuration is retained.
     *
     * @return true if the configuration was loaded successfully, false otherwise.
     */
    private boolean load() {
        try {
            final Configuration loadedConfig = loadConfig();
            config.set(loadedConfig);
            return true;
        } catch (Throwable e) {
            logger.error("Failed to load configuration", e);
            return false;
        }
    }

    /**
     * Loads the configuration from the file and creates a new Configuration object.
     *
     * @return the loaded Configuration object.
     * @throws IOException if an error occurs while reading or parsing the file.
     */
    private Configuration loadConfig() throws IOException {
        CommentedConfigurationNode node = loader.load();
        Configuration loadedConfig = node.get(configClass);

        if (Files.notExists(configFile)) {
            node.set(configClass, loadedConfig);
            loader.save(node);
        }

        return loadedConfig;
    }

    /**
     * Reloads the configuration from the file.
     * If reloading fails, the previous configuration is retained.
     *
     * @return true if the configuration was reloaded successfully, false otherwise.
     */
    public boolean reload() {
        return load();
    }

    @Nullable
    public Configuration get() {
        return config.get();
    }
}