package org.stupidcraft.geyserdebuginfo.manager;

import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.cache.BossBar;
import org.jetbrains.annotations.NotNull;
import org.stupidcraft.geyserdebuginfo.util.PositionUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * This class is responsible for creating, updating, and removing boss bars for each player.
 */
public class BossBarManager {

    private static final long UPDATE_DELAY_MS = 20;

    private final ScheduledExecutorService executor;
    private final Map<SessionPlayerEntity, BossBar> bossBars;
    private final PlayerDataManager playerDataManager;

    /**
     * Constructs a new BossBarManager.
     */
    public BossBarManager(
            final PlayerDataManager playerDataManager
    ) {
        this.executor = Executors.newSingleThreadScheduledExecutor();
        this.bossBars = new HashMap<>();
        this.playerDataManager = playerDataManager;
    }

    /**
     * Creates a boss bar for the specified player if it doesn't already exist.
     *
     * @param player The player for whom to create the boss bar.
     */
    public void createBossBar(final @NotNull GeyserSession session, final @NotNull SessionPlayerEntity player) {
        if (bossBars.containsKey(player)) return;

        final long entityId = player.getSession().getEntityCache().getNextEntityId().incrementAndGet();
        BossBar bossBar = new BossBar(player.getSession(), entityId, Component.text("Geyser Debug Information"), 1.0f, 1, 1, 1);
        player.getSession().getEntityCache().addBossBar(player.getUuid(), bossBar);
        playerDataManager.setF3Enabled(player.getUuid(), true);
        bossBars.put(player, bossBar);

        // Schedule task to update the boss bar every UPDATE_DELAY_MS milliseconds
        executor.scheduleAtFixedRate(() -> updateBossBar(session, player), 0, UPDATE_DELAY_MS, TimeUnit.MILLISECONDS);
    }

    /**
     * Removes the boss bar for the specified player.
     *
     * @param player The player whose boss bar is to be removed.
     */
    public void removeBossBar(final @NotNull SessionPlayerEntity player) {
        BossBar bossBar = bossBars.remove(player);
        playerDataManager.setF3Enabled(player.getUuid(), false);
        if (bossBar != null) {
            bossBar.removeBossBar();
        }
    }

    /**
     * Updates the boss bar with the current player's coordinates and orientation.
     *
     * @param player The player whose boss bar is being updated.
     */
    private void updateBossBar(final @NotNull GeyserSession session, final @NotNull SessionPlayerEntity player) {
        BossBar bossBar = bossBars.get(player);
        if (bossBar == null) return;

        Vector3f pos = PositionUtil.adjustForPlayerOffset(player.getPosition());

        Component text = Component.text("Geyser Debug Information")
                .append(Component.newline())
                .append(Component.newline())
                .append(Component.text(String.format("%.3f", pos.getX()) + " "))
                .append(Component.text(String.format("%.3f", pos.getY()) + " "))
                .append(Component.text(String.format("%.3f", pos.getZ())))
                .append(Component.newline())
                .append(Component.text(pos.getFloorX() + " "))
                .append(Component.text(pos.getFloorY() + " "))
                .append(Component.text(pos.getFloorZ()))
                .append(Component.newline())
                .append(Component.text(Arrays.toString(PositionUtil.getRelativeChunkCoordinates(pos))))
                .append(Component.newline())
                .append(Component.text(session.getLastChunkPosition().getX() + " "))
                .append(Component.text(pos.getFloorY() / 16 + " "))
                .append(Component.text(session.getLastChunkPosition().getY()))
                .append(Component.newline())
                .append(Component.text(PositionUtil.getFacingDirection(player.getYaw()) + " (" +  String.format("%.1f", player.getYaw()) + " / " + String.format("%.1f", player.getPitch()) + ")"))
                .append(Component.newline())
                .append(Component.text(session.getDimensionType().bedrockId()));

        // Update the boss bar's title
        bossBar.updateTitle(text);
    }

    /**
     * Shuts down the scheduled executor service, stopping all updates.
     */
    public void shutdown() {
        executor.shutdown();
    }
}