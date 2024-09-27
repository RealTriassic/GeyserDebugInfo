package org.stupidcraft.geyserdebuginfo;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.command.Command;
import org.geysermc.geyser.api.command.CommandSource;
import org.geysermc.geyser.api.event.lifecycle.GeyserDefineCommandsEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserPostInitializeEvent;
import org.geysermc.geyser.api.event.lifecycle.GeyserShutdownEvent;
import org.geysermc.geyser.api.extension.Extension;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.stupidcraft.geyserdebuginfo.config.Configuration;
import org.stupidcraft.geyserdebuginfo.config.ConfigurationContainer;
import org.stupidcraft.geyserdebuginfo.listener.PlayerJoinListener;
import org.stupidcraft.geyserdebuginfo.manager.BossBarManager;
import org.stupidcraft.geyserdebuginfo.manager.PlayerDataManager;

import java.io.File;

public class GeyserDebugInfo implements Extension {

    private File dataFolder;
    private BossBarManager bossBarManager;
    private PlayerDataManager playerDataManager;
    private ConfigurationContainer<Configuration> config;

    @Subscribe
    public void onPostInitialize(GeyserPostInitializeEvent event) {
        this.dataFolder = this.dataFolder().toFile();
        if (!dataFolder.exists() && !dataFolder.mkdirs())
            logger().error("Failed to create data folder " + dataFolder.getAbsolutePath());

        loadConfig();
        this.playerDataManager = new PlayerDataManager(dataFolder, this.logger(), false);
        this.bossBarManager = new BossBarManager(playerDataManager);
        this.eventBus().register(new PlayerJoinListener(this));
    }

    /**
     * Loads the configuration file from the specified data folder.
     * This method attempts to load the configuration file and logs an error if the process fails.
     */
    private void loadConfig() {
        try {
            this.config = ConfigurationContainer.load(dataFolder.toPath(), Configuration.class);
        } catch (Throwable e) {
            logger().error("Could not load config.yml file", e);
        }
    }

    @Subscribe
    public void onShutdown(GeyserShutdownEvent event) {
        bossBarManager.shutdown();
        playerDataManager.savePlayerData();
    }

    @Subscribe
    public void onDefineCommands(GeyserDefineCommandsEvent event) {
        final Command command = Command.builder(this)
                .name("f3")
                .playerOnly(true)
                .bedrockOnly(true)
                .source(CommandSource.class)
                .description("F3 Testing Command")
                .executor((source, cmd, args) -> {
                    GeyserSession session = (GeyserSession) source.connection();
                    SessionPlayerEntity player = session.getPlayerEntity();
                    if (player != null && !playerDataManager.isF3Enabled(player.getUuid())) {
                        bossBarManager.createBossBar(player);
                    } else {
                        bossBarManager.removeBossBar(player);
                        playerDataManager.setF3Enabled(player.getUuid(), false);
                    }
                })
                .build();

        event.register(command);
    }

    public ConfigurationContainer<Configuration> config() {
        return this.config;
    }

    /**
     * Retrieves the {@link BossBarManager} instance associated with this class.
     *
     * @return the {@link BossBarManager} used for managing boss bars.
     */
    public BossBarManager bossBarManager() {
        return this.bossBarManager;
    }

    /**
     * Retrieves the {@link PlayerDataManager} instance associated with this class.
     *
     * @return the {@link PlayerDataManager} used for managing player data.
     */
    public PlayerDataManager playerDataManager() {
        return this.playerDataManager;
    }
}