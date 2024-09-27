package org.stupidcraft.geyserdebuginfo.listener;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.stupidcraft.geyserdebuginfo.GeyserDebugInfo;
import org.stupidcraft.geyserdebuginfo.manager.BossBarManager;
import org.stupidcraft.geyserdebuginfo.manager.PlayerDataManager;

/**
 * This class is responsible for listening for player connection events.
 */
public class PlayerJoinListener {

    private final BossBarManager bossBarManager;
    private final PlayerDataManager playerDataManager;

    public PlayerJoinListener(
            final GeyserDebugInfo instance
    ) {
        this.bossBarManager = instance.bossBarManager();
        this.playerDataManager = instance.playerDataManager();
    }

    /**
     * Called when a player joins.
     */
    @Subscribe
    public void onJoin(final SessionJoinEvent event) {
        final GeyserSession session = (GeyserSession) event.connection();
        final SessionPlayerEntity player = session.getPlayerEntity();

        if (playerDataManager.isF3Enabled(player.getUuid()))
            bossBarManager.createBossBar(player);
    }

    /**
     * Called when a player disconnects.
     */
    @Subscribe
    public void onDisconnect(final SessionDisconnectEvent event) {
        final GeyserSession session = (GeyserSession) event.connection();
        bossBarManager.removeBossBar(session.getPlayerEntity(), false);
    }
}
