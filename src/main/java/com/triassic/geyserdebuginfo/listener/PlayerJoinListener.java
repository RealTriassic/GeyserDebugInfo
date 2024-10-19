package com.triassic.geyserdebuginfo.listener;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.manager.PlayerDataManager;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;

public class PlayerJoinListener {

    private final PlayerDataManager playerDataManager;

    public PlayerJoinListener(
            final GeyserDebugInfo instance
    ) {
        this.playerDataManager = instance.getPlayerDataManager();
    }

    @Subscribe
    public void onJoin(final SessionJoinEvent event) {
        final GeyserSession session = (GeyserSession) event.connection();
        final SessionPlayerEntity player = session.getPlayerEntity();

        if (playerDataManager.isF3Enabled(player.getUuid())) {
            // TODO: Re-implement this.
        }
    }

    @Subscribe
    public void onDisconnect(final SessionDisconnectEvent event) {
        final GeyserSession session = (GeyserSession) event.connection();
        // TODO: Remove Bossbar
    }
}
