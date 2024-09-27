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
import org.stupidcraft.geyserdebuginfo.listener.PlayerJoinListener;
import org.stupidcraft.geyserdebuginfo.manager.BossBarManager;
import org.stupidcraft.geyserdebuginfo.manager.PlayerDataManager;

import java.io.File;

public class GeyserDebugInfo implements Extension {

    private BossBarManager bossBarManager;
    private PlayerDataManager playerDataManager;

    @Subscribe
    public void onPostInitialize(GeyserPostInitializeEvent event) {
        File dataFolder = this.dataFolder().toFile();
        if (!dataFolder.exists() && !dataFolder.mkdirs())
            logger().error("Failed to create data folder " + dataFolder.getAbsolutePath());

        this.playerDataManager = new PlayerDataManager(dataFolder, this.logger(), false);
        this.bossBarManager = new BossBarManager(playerDataManager);
        this.eventBus().register(new PlayerJoinListener(this));
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