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
    private final AtomicReference<Configuration> config = new AtomicReference<>();

    public ConfigurationContainer(
            final Path dataFolder,
            final ExtensionLogger logger
    ) {
        this.logger = logger;
        this.configFile = dataFolder.resolve("config.yml");

        this.loader = YamlConfigurationLoader.builder()
                .indent(2)
                .path(configFile)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(options -> options
                        .shouldCopyDefaults(true)
                        .header(HEADER))
                .build();
    }

    public boolean load(Class<? extends Configuration> clazz) {
        try {
            final Configuration loadedConfig = load0(clazz);
            config.set(loadedConfig);
            return true;
        } catch (Throwable e) {
            logger.error("Failed to load configuration", e);
            return false;
        }
    }

    private Configuration load0(Class<? extends Configuration> clazz) throws IOException {
        final CommentedConfigurationNode node = loader.load();
        final Configuration loadedConfig = node.get(clazz);

        if (Files.notExists(configFile)) {
            node.set(clazz, loadedConfig);
            loader.save(node);
        }

        return loadedConfig;
    }

    @Nullable
    public Configuration get() {
        return config.get();
    }
}