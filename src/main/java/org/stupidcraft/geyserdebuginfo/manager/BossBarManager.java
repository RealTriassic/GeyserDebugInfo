package org.stupidcraft.geyserdebuginfo.manager;

import net.kyori.adventure.text.Component;
import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.geysermc.geyser.session.cache.BossBar;
import org.jetbrains.annotations.NotNull;
import org.stupidcraft.geyserdebuginfo.GeyserDebugInfo;
import org.stupidcraft.geyserdebuginfo.config.Configuration;
import org.stupidcraft.geyserdebuginfo.config.ConfigurationContainer;
import org.stupidcraft.geyserdebuginfo.util.ChunkUtil;
import org.stupidcraft.geyserdebuginfo.util.PositionUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * This class is responsible for creating, updating, and removing boss bars for each player.
 */
public class BossBarManager {

    private final PlayerDataManager playerDataManager;
    private final HashMap<SessionPlayerEntity, BossBar> bossBars;
    private final ScheduledExecutorService executor;
    private final ConfigurationContainer<Configuration> config;

    public BossBarManager(
            final GeyserDebugInfo instance
    ) {
        this.config = instance.config();
        this.playerDataManager = instance.playerDataManager();
        this.bossBars = new HashMap<>();
        this.executor = Executors.newSingleThreadScheduledExecutor();

        executor.scheduleAtFixedRate(this::updateAllBossBars, 0, config.get().getBossBarSettings().getRefreshInterval(), TimeUnit.MILLISECONDS);
    }

    /**
     * Creates a boss bar for the specified player if it doesn't already exist.
     *
     * @param player The player for whom to create the boss bar.
     * @throws IllegalArgumentException if the player is null.
     */
    public void createBossBar(final @NotNull SessionPlayerEntity player) {
        final GeyserSession session = player.getSession();

        if (bossBars.containsKey(player))
            return;

        final long entityId = session.getEntityCache().getNextEntityId().incrementAndGet();
        BossBar bossBar = new BossBar(session, entityId, Component.text("Geyser Debug Information"), 1.0f, 1, 1, 1);
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
     * Requests a bossbar update for all tracked bossbars.
     */
    private void updateAllBossBars() {
        for (Map.Entry<SessionPlayerEntity, BossBar> entry : bossBars.entrySet()) {
            updateBossBar(entry.getKey());
        }
    }

    /**
     * Updates the boss bar with current up-to-date data.
     *
     * @param player The player whose boss bar is to be updated.
     * @throws IllegalArgumentException if the player is null.
     */
    private void updateBossBar(final @NotNull SessionPlayerEntity player) {
        BossBar bossBar = bossBars.get(player);

        if (bossBar != null) {
            List<String> displayFormat = config.get().getBossBarSettings().getDisplayFormat();
            Vector3f pos = PositionUtil.adjustForPlayerOffset(player.getPosition());

            String displayText = displayFormat.stream()
                    .map(line -> new PlaceholderReplacer(pos, player.getSession()).replace(line))
                    .collect(Collectors.joining("\n"));

            bossBar.updateTitle(Component.text(displayText));
        }
    }

    /**
     * Shuts down the executor service, stopping all updates.
     */
    public void shutdown() {
        executor.shutdown();
    }

    /**
     * Encapsulates the logic for replacing placeholders in the display text.
     */
    private record PlaceholderReplacer(Vector3f pos, GeyserSession session) {

        public String replace(final String line) {
            int[] relativeChunkCoords = ChunkUtil.getRelativeCoordinates(pos);

            return line
                    .replace("%x%", String.format("%.3f", pos.getX()))
                    .replace("%y%", String.format("%.3f", pos.getY()))
                    .replace("%z%", String.format("%.3f", pos.getZ()))
                    .replace("%floor_x%", String.valueOf(pos.getFloorX()))
                    .replace("%floor_y%", String.valueOf(pos.getFloorY()))
                    .replace("%floor_z%", String.valueOf(pos.getFloorZ()))
                    .replace("%relative_chunk_x%", String.valueOf(relativeChunkCoords[0]))
                    .replace("%relative_chunk_y%", String.valueOf(relativeChunkCoords[1]))
                    .replace("%relative_chunk_z%", String.valueOf(relativeChunkCoords[2]))
                    .replace("%chunk_x%", String.valueOf(session.getLastChunkPosition().getX()))
                    .replace("%chunk_y%", String.valueOf((pos.getFloorY() < 0) ? (pos.getFloorY() - 15) / 16 : pos.getFloorY() / 16))
                    .replace("%chunk_z%", String.valueOf(session.getLastChunkPosition().getY()))
                    .replace("%facing%", PositionUtil.getFacingDirection(session.getPlayerEntity().getYaw()))
                    .replace("%yaw%", String.format("%.1f", session.getPlayerEntity().getYaw()))
                    .replace("%pitch%", String.format("%.1f", session.getPlayerEntity().getPitch()))
                    .replace("%global_chunk_x%", String.valueOf(ChunkUtil.getRelativeChunkCoordinates(pos.getX(), pos.getZ())[0]))
                    .replace("%global_chunk_z%", String.valueOf(ChunkUtil.getRelativeChunkCoordinates(pos.getX(), pos.getZ())[1]))
                    .replace("%region_file%", ChunkUtil.getRegionFileName(pos.getX(), pos.getZ()));
        }
    }
}