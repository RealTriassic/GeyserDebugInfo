package com.triassic.geyserdebuginfo;

import com.triassic.geyserdebuginfo.command.commands.ReloadCommand;
import com.triassic.geyserdebuginfo.command.commands.ToggleCommand;
import com.triassic.geyserdebuginfo.configuration.ConfigurationLoader;
import com.triassic.geyserdebuginfo.configuration.Configuration;
import com.triassic.geyserdebuginfo.listener.PlayerJoinListener;
import com.triassic.geyserdebuginfo.manager.BossBarManager;
import com.triassic.geyserdebuginfo.manager.PlaceholderManager;
import com.triassic.geyserdebuginfo.manager.PlayerDataManager;
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

import java.io.File;
import java.util.stream.Stream;

public class GeyserDebugInfo implements Extension {

    private File dataFolder;

    @Getter
    private Configuration config;
    @Getter
    private BossBarManager bossBarManager;
    @Getter
    private PlayerDataManager playerDataManager;
    @Getter
    private PlaceholderManager placeholderManager;

    /**
     * Initializes the extension.
     * Sets up the data folder, loads configuration,
     * and registers event listeners.
     */
    @Subscribe
    public void onPostInitialize(GeyserPreInitializeEvent event) {
        long startTime = System.currentTimeMillis();

        this.dataFolder = this.dataFolder().toFile();
        if (!dataFolder.exists() && !dataFolder.mkdirs())
            logger().error("Failed to create data folder " + dataFolder.getAbsolutePath());

        this.config = new ConfigurationLoader(this)
                .load(Configuration.class);

        this.playerDataManager = new PlayerDataManager(dataFolder, this.logger(), false);
        this.placeholderManager = new PlaceholderManager();
        this.bossBarManager = new BossBarManager(this);
        this.eventBus().register(new PlayerJoinListener(this));

        Stream.of(
                new PlayerPlaceholderProvider(),
                new ServerPlaceholderProvider()
        ).forEach(placeholderManager::registerProvider);

        Stream.of(
                new MathModifierProvider(),
                new TextModifierProvider()
        ).forEach(placeholderManager::registerProvider);

        logger().info("Enabled in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * Loads the configuration file from the specified data folder.
     * This method attempts to load the configuration file and logs an error if the process fails.
     */
    private void loadConfig() {

    }

    /**
     * Defines and registers commands for the extension.
     */
    @Subscribe
    public void onDefineCommands(GeyserDefineCommandsEvent event) {
        Stream.of(
                new ToggleCommand(this),
                new ReloadCommand(this)
        ).forEach(command -> event.register(command.createCommand()));
    }

    /**
     * Called during the shutdown process.
     * Cleans up resources and saves player data.
     */
    @Subscribe
    public void onShutdown(GeyserShutdownEvent event) {
        bossBarManager.shutdown();
        playerDataManager.savePlayerData();
    }

    /**
     * Reloads the configuration settings.
     */
    public synchronized void reload() {
        // TODO: add reloading for configuration.
    }
}