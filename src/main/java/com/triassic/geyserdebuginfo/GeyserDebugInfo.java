package com.triassic.geyserdebuginfo;

import com.triassic.geyserdebuginfo.command.commands.ReloadCommand;
import com.triassic.geyserdebuginfo.command.commands.ToggleCommand;
import com.triassic.geyserdebuginfo.configuration.Configuration;
import com.triassic.geyserdebuginfo.configuration.ConfigurationContainer;
import com.triassic.geyserdebuginfo.display.DisplayManager;
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
import java.nio.file.Path;
import java.util.stream.Stream;

public class GeyserDebugInfo implements Extension {

    @Getter
    private Path dataFolder;
    @Getter
    private ExtensionLogger logger;
    @Getter
    private ConfigurationContainer<Configuration> config;
    @Getter
    private PlayerDataManager playerDataManager;
    @Getter
    private PlaceholderManager placeholderManager;
    @Getter
    private DisplayManager displayManager;

    @Subscribe
    public void onPreInitialize(GeyserPreInitializeEvent event) {
        long startTime = System.currentTimeMillis();

        this.logger = logger();
        this.dataFolder = dataFolder();

        try {
            this.config = ConfigurationContainer.load(dataFolder, Configuration.class);
        } catch (IOException e) {
            logger.error("Failed to load configuration", e);
            return;
        }

        this.playerDataManager = new PlayerDataManager(this.dataFolder().toFile(), logger());
        this.placeholderManager = new PlaceholderManager();
        this.displayManager = new DisplayManager(this);
        this.eventBus().register(new PlayerJoinListener(this));

        Stream.of(
                new PlayerPlaceholderProvider(),
                new ServerPlaceholderProvider()
        ).forEach(placeholderManager::register);

        Stream.of(
                new MathModifierProvider(),
                new TextModifierProvider()
        ).forEach(placeholderManager::register);

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
        // playerDataManager.save();
    }
}