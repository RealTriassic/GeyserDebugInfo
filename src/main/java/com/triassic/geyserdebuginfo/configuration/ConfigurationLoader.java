package com.triassic.geyserdebuginfo.configuration;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import org.geysermc.geyser.api.extension.ExtensionLogger;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.yaml.NodeStyle;
import org.spongepowered.configurate.yaml.YamlConfigurationLoader;

import java.io.File;
import java.io.IOException;

public class ConfigurationLoader {

    private static final String HEADER = """
            GeyserDebugInfo Configuration File
            A Geyser extension that strives to provide F3-like debug information for Bedrock Edition players.
            
            Report any issues on our GitHub repository:
            https://github.com/RealTriassic/GeyserDebugInfo""";

    private final File configFile;
    private final ExtensionLogger logger;

    public ConfigurationLoader(
            GeyserDebugInfo instance
    ) {
        this.configFile = new File(instance.dataFolder().toFile(), "config.yml");
        this.logger = instance.logger();
    }

    @Nullable
    public <T extends Configuration> T load(Class<T> configClass) {
        try {
            return load0(configClass);
        } catch (IOException e) {
            logger.error("Failed to load configuration", e);
            return null;
        }
    }

    private <T extends Configuration> T load0(Class<T> configClass) throws IOException {
        final YamlConfigurationLoader loader = YamlConfigurationLoader.builder()
                .file(configFile)
                .indent(2)
                .nodeStyle(NodeStyle.BLOCK)
                .defaultOptions(options -> options.shouldCopyDefaults(true)
                        .header(HEADER))
                .build();

        final CommentedConfigurationNode node = loader.load();
        T config = node.get(configClass);

        if (!configFile.exists()) {
            node.set(configClass, config);
            loader.save(node);
        }

        return config;
    }
}