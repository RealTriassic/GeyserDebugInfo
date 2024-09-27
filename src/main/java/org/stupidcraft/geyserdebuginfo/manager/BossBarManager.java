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

    private final PlayerDataManager playerDataManager;
    private final HashMap<SessionPlayerEntity, BossBar> bossBars;
    private final ScheduledExecutorService executor;

    public BossBarManager(
            final PlayerDataManager playerDataManager
    ) {
        this.playerDataManager = playerDataManager;
        this.bossBars = new HashMap<>();
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(this::updateAllBossBars, 0, 50, TimeUnit.MILLISECONDS);
    }

    /**
     * Creates a boss bar for the specified player if it doesn't already exist.
     *
     * @param player The player for whom to create the boss bar.
     * @throws IllegalArgumentException if the player is null.
     */
    public void createBossBar(final @NotNull SessionPlayerEntity player) {
        if (bossBars.containsKey(player))
            return;

        final long entityId = player.getSession().getEntityCache().getNextEntityId().incrementAndGet();
        BossBar bossBar = new BossBar(player.getSession(), entityId, Component.text("Geyser Debug Information"), 1.0f, 1, 1, 1);
        player.getSession().getEntityCache().addBossBar(player.getUuid(), bossBar);
        playerDataManager.setF3Enabled(player.getUuid(), true);
        bossBars.put(player, bossBar);
    }

    /**
     * Removes the boss bar for the specified player and updates the player data
     * to indicate that the F3 feature is disabled by default.
     *
     * @param player The player whose boss bar is to be removed.
     * @throws IllegalArgumentException if the player is null.
     */
    public void removeBossBar(final @NotNull SessionPlayerEntity player) {
        removeBossBar(player, true);
    }

    /**
     * Removes the boss bar for the specified player.
     *
     * @param player           The player whose boss bar is to be removed.
     * @param updatePlayerData Whether to update the player data to indicate that the F3 feature is disabled.
     * @throws IllegalArgumentException if the player is null.
     */
    public void removeBossBar(final @NotNull SessionPlayerEntity player, boolean updatePlayerData) {
        BossBar bossBar = bossBars.remove(player);
        if (bossBar != null)
            bossBar.removeBossBar();

        if (updatePlayerData)
            playerDataManager.setF3Enabled(player.getUuid(), false);
    }

    /**
     * Requests a bossbar update to all tracked bossbars.
     */
    private void updateAllBossBars() {
        for (Map.Entry<SessionPlayerEntity, BossBar> entry : bossBars.entrySet()) {
            updateBossBar(entry.getKey().getSession(), entry.getKey());
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
                .append(Component.text(PositionUtil.getFacingDirection(player.getYaw()) + " (" + String.format("%.1f", player.getYaw()) + " / " + String.format("%.1f", player.getPitch()) + ")"))
                .append(Component.newline())
                .append(Component.text(session.getDimensionType().bedrockId()));

        // Update the boss bar's title
        bossBar.updateTitle(text);
    }

    /**
     * Shuts down the executor service, stopping all updates.
     */
    public void shutdown() {
        executor.shutdown();
    }
}