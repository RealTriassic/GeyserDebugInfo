package com.triassic.geyserdebuginfo.config;

import org.spongepowered.configurate.CommentedConfigurationNode;
import org.spongepowered.configurate.ConfigurateException;
import org.spongepowered.configurate.hocon.HoconConfigurationLoader;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.atomic.AtomicReference;

public class ConfigurationContainer<C> {

    private final AtomicReference<C> config;
    private final HoconConfigurationLoader loader;
    private final Class<C> clazz;

    private ConfigurationContainer(
            final C config,
            final Class<C> clazz,
            final HoconConfigurationLoader loader
    ) {
        this.config = new AtomicReference<>(config);
        this.loader = loader;
        this.clazz = clazz;
    }

    public static <C> ConfigurationContainer<C> load(Path path, Class<C> clazz) throws IOException {
        path = path.resolve("config.conf");
        final boolean firstCreation = Files.notExists(path);
        final HoconConfigurationLoader loader = HoconConfigurationLoader.builder()
                .defaultOptions(opts -> opts
                        .shouldCopyDefaults(true)
                        .header("""
                                GeyserDebugInfo Configuration File
                                
                                Report any issues on our GitHub repository:
                                https://github.com/StupidCraft/GeyserDebugInfo""")
                )
                .path(path)
                .build();

        final CommentedConfigurationNode node = loader.load();
        final C config = node.get(clazz);
        if (firstCreation) {
            node.set(clazz, config);
            loader.save(node);
        }

        return new ConfigurationContainer<>(config, clazz, loader);
    }

    public C get() {
        return this.config.get();
    }

    public void reload() {
        CompletableFuture.runAsync(() -> {
            try {
                final CommentedConfigurationNode node = loader.load();
                config.set(node.get(clazz));
            } catch (ConfigurateException exception) {
                throw new CompletionException("Could not load config.conf file", exception);
            }
        });
    }
}
