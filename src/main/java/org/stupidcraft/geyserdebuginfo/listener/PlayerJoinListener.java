package org.stupidcraft.geyserdebuginfo.listener;

import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.stupidcraft.geyserdebuginfo.GeyserDebugInfo;
import org.stupidcraft.geyserdebuginfo.manager.BossBarManager;
import org.stupidcraft.geyserdebuginfo.manager.PlayerDataManager;

public class PlayerJoinListener {

    private final BossBarManager bossBarManager;
    private final PlayerDataManager playerDataManager;

    public PlayerJoinListener(
            final GeyserDebugInfo instance
    ) {
        this.bossBarManager = instance.bossBarManager();
        this.playerDataManager = instance.playerDataManager();
    }

    @Subscribe
    public void onJoin(SessionJoinEvent event) {
        GeyserSession session = (GeyserSession) event.connection();
        SessionPlayerEntity player = session.getPlayerEntity();

        if (playerDataManager.isF3Enabled(player.getUuid())) {
            bossBarManager.createBossBar(session, player);
        }
    }

    @Subscribe
    public void onDisconnect(SessionDisconnectEvent event) {
        GeyserSession session = (GeyserSession) event.connection();
        SessionPlayerEntity player = session.getPlayerEntity();

        bossBarManager.removeBossBar(player);
    }
}
