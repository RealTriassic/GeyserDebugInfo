package com.triassic.geyserdebuginfo.manager;

import com.triassic.geyserdebuginfo.GeyserDebugInfo;
import net.kyori.adventure.text.Component;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.cache.BossBar;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class BossBarManager {

    private final GeyserDebugInfo instance;
    private final PlaceholderManager placeholderManager;
    private final PlayerDataManager playerDataManager;
    private final HashMap<SessionPlayerEntity, BossBar> bossBars;
    private final ScheduledExecutorService executor;

    public BossBarManager(
            final GeyserDebugInfo instance
    ) {
        this.instance = instance;
        this.playerDataManager = instance.getPlayerDataManager();
        this.placeholderManager = instance.getPlaceholderManager();
        this.bossBars = new HashMap<>();
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(this::updateAllBossBars, 0, instance.getConfig().getDisplay().getBossBar().getRefreshInterval(), TimeUnit.MILLISECONDS);
    }

    public void createBossBar(final @NotNull SessionPlayerEntity player) {
        final GeyserSession session = player.getSession();

        if (bossBars.containsKey(player))
            return;

        final long entityId = session.getEntityCache().getNextEntityId().incrementAndGet();
        BossBar bossBar = new BossBar(session, entityId, Component.empty(), 1.0f, 1, 0, 0);
        player.getSession().getEntityCache().addBossBar(player.getUuid(), bossBar);
        playerDataManager.setF3Enabled(player.getUuid(), true);
        bossBars.put(player, bossBar);
    }

    public void removeBossBar(final @NotNull SessionPlayerEntity player) {
        removeBossBar(player, true);
    }

    public void removeBossBar(final @NotNull SessionPlayerEntity player, boolean updatePlayerData) {
        BossBar bossBar = bossBars.remove(player);
        if (bossBar != null)
            bossBar.removeBossBar();

        if (updatePlayerData)
            playerDataManager.setF3Enabled(player.getUuid(), false);
    }

    private void updateAllBossBars() {
        for (Map.Entry<SessionPlayerEntity, BossBar> entry : bossBars.entrySet()) {
            updateBossBar(entry.getKey());
        }
    }

    private void updateBossBar(final @NotNull SessionPlayerEntity player) {
        BossBar bossBar = bossBars.get(player);

        if (bossBar != null) {
            List<String> displayFormat = instance.getConfig().getDisplay().getBossBar().getText();

            String displayText = displayFormat.stream()
                    .map(line -> placeholderManager.setPlaceholders(player.getSession(), line))
                    .collect(Collectors.joining("\n"));

            bossBar.updateTitle(Component.text(displayText));
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}