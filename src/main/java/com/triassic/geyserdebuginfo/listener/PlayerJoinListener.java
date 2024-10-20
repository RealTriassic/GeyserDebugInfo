package com.triassic.geyserdebuginfo.listener;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import com.triassic.geyserdebuginfo.display.DisplayManager;
import com.triassic.geyserdebuginfo.display.DisplayType;
import com.triassic.geyserdebuginfo.manager.PlayerDataManager;
import org.geysermc.event.subscribe.Subscribe;
import org.geysermc.geyser.api.event.bedrock.SessionDisconnectEvent;
import org.geysermc.geyser.api.event.bedrock.SessionJoinEvent;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;

import java.util.Set;

public class PlayerJoinListener {

    private final DisplayManager displayManager;
    private final PlayerDataManager playerDataManager;

    public PlayerJoinListener(
            final GeyserDebugInfo instance
    ) {
        this.displayManager = instance.getDisplayManager();
        this.playerDataManager = instance.getPlayerDataManager();
    }

    @Subscribe
    public void onJoin(final SessionJoinEvent event) {
        final GeyserSession session = (GeyserSession) event.connection();
        final Set<DisplayType> enabledDisplays = playerDataManager.getEnabledDisplays(session.playerUuid());

        for (DisplayType displayType : enabledDisplays) {
            displayManager.subscribePlayer(session, displayType);
        }
    }

    @Subscribe
    public void onDisconnect(final SessionDisconnectEvent event) {
        final GeyserSession session = (GeyserSession) event.connection();
        // TODO: Clean-up.
    }
}
