package com.triassic.geyserdebuginfo;

import com.triassic.geyserdebuginfo.command.commands.ReloadCommand;
import com.triassic.geyserdebuginfo.command.commands.ToggleCommand;
import com.triassic.geyserdebuginfo.configuration.Configuration;
import com.triassic.geyserdebuginfo.configuration.ConfigurationContainer;
import com.triassic.geyserdebuginfo.listener.PlayerJoinListener;
import com.triassic.geyserdebuginfo.manager.PlayerDataManager;
import com.triassic.geyserdebuginfo.placeholder.PlaceholderManager;
import com.triassic.geyserdebuginfo.placeholder.modifiers.MathModifierProvider;
import com.triassic.geyserdebuginfo.placeholder.modifiers.TextModifierProvider;
import com.triassic.geyserdebuginfo.placeholder.placeholders.PlayerPlaceholderProvider;
import com.triassic.geyserdebuginfo.placeholder.placeholders.ServerPlaceholderProvider;
import lombok.Getter;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCommandsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPreInitializeEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserShutdownEvent;
import org.geysermc.geyser.api.extension.Extension;
import org.geysermc.geyser.api.extension.ExtensionLogger;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.stream.Stream;

public class GeyserDebugInfo implements Extension {

    @Getter
    private Path dataFolder;
    @Getter
    private ExtensionLogger logger;
    @Getter
    private Configuration config;
    @Getter
    private PlayerDataManager playerDataManager;
    @Getter
    private PlaceholderManager placeholderManager;

    private ConfigurationContainer configContainer;

    @Subscribe
    public void onPreInitialize(GeyserPreInitializeEvent event) {
        long startTime = System.currentTimeMillis();

        this.logger = logger();
        this.dataFolder = dataFolder();

        try {
            if (Files.notExists(dataFolder))
                Files.createDirectories(dataFolder);
        } catch (IOException e) {
            logger.error("Failed to create data folder " + dataFolder.toAbsolutePath(), e);
            extensionManager().disable(this);
            return;
        }

        this.configContainer = new ConfigurationContainer(dataFolder, logger);

        if (!configContainer.load(Configuration.class)) {
            logger.error("Failed to load the configuration. Please check config.yml for issues.");
            extensionManager().disable(this);
            return;
        }

        this.config = configContainer.get();

        if (config == null) {
            logger.error("Failed to load the configuration. Please check config.yml for issues.");
            extensionManager().disable(this);
            return;
        }

        this.playerDataManager = new PlayerDataManager(this.dataFolder().toFile(), logger(), false);
        this.placeholderManager = new PlaceholderManager();
        this.eventBus().register(new PlayerJoinListener(this));

        Stream.of(
                new PlayerPlaceholderProvider(),
                new ServerPlaceholderProvider()
        ).forEach(placeholderManager::registerProvider);

        Stream.of(
                new MathModifierProvider(),
                new TextModifierProvider()
        ).forEach(placeholderManager::registerProvider);

        logger.info("Enabled in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    @Subscribe
    public void onDefineCommands(GeyserDefineCommandsEvent event) {
        Stream.of(
                new ToggleCommand(this),
                new ReloadCommand(this)
        ).forEach(command -> event.register(command.getCommand()));
    }

    @Subscribe
    public void onShutdown(GeyserShutdownEvent event) {
        playerDataManager.save();
    }

    public boolean reloadConfig() {
        boolean reloaded = configContainer.load(configContainer.get().getClass());
        if (reloaded)
            this.config = configContainer.get();

        return reloaded;
    }
}