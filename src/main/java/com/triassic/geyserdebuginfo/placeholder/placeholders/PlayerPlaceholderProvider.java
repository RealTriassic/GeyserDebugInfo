package com.triassic.geyserdebuginfo.placeholder.placeholders;

import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;
import com.triassic.geyserdebuginfo.util.ChunkUtil;
import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.entity.EntityDefinitions;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

public class PlayerPlaceholderProvider extends PlaceholderProvider {

    @Override
    public String getIdentifier() {
        return "player";
    }

    @Override
    public String onRequest(final GeyserSession session, @NotNull final String params) {
        final SessionPlayerEntity player = session.getPlayerEntity();
        final Vector3f pos = adjustForPlayerOffset(player.getPosition());
        int[] relativeChunkCoords = ChunkUtil.getRelativeCoordinates(pos);

        return switch (params) {
            case "x" -> String.format("%.3f", pos.getX());
            case "y" -> String.format("%.3f", pos.getY());
            case "z" -> String.format("%.3f", pos.getZ());
            case "yaw" -> String.format("%.1f", player.getYaw());
            case "pitch" -> String.format("%.1f", player.getPitch());
            case "facing" -> getFacingDirection(player.getYaw());

            case "chunk_x" -> String.valueOf(session.getLastChunkPosition().getX());
            case "chunk_y" -> String.valueOf(ChunkUtil.calculateChunkY(pos.getFloorY()));
            case "chunk_z" -> String.valueOf(session.getLastChunkPosition().getY());
            case "relative_x" -> String.valueOf(relativeChunkCoords[0]);
            case "relative_y" -> String.valueOf(relativeChunkCoords[1]);
            case "relative_z" -> String.valueOf(relativeChunkCoords[2]);
            case "global_x" -> String.valueOf(ChunkUtil.getRelativeChunkCoordinates(pos.getX(), pos.getZ())[0]);
            case "global_z" -> String.valueOf(ChunkUtil.getRelativeChunkCoordinates(pos.getX(), pos.getZ())[1]);
            case "region_file" -> ChunkUtil.getRegionFileName(pos.getX(), pos.getZ());

            case "allow_flight" -> String.valueOf(session.isCanFly());
            case "gamemode" -> String.valueOf(session.getGameMode());
            case "uuid" -> String.valueOf(player.getUuid());
            case "locale" -> session.locale();
            case "version" -> session.version();
            case "ip" -> String.valueOf(session.getSocketAddress());

            case "health" -> String.valueOf(player.getHealth());
            case "max_health" -> String.valueOf(player.getMaxHealth());

            case "world" -> String.valueOf(session.getWorldName());
            case "difficulty" -> String.valueOf(session.getWorldCache().getDifficulty());
            case "time" -> String.valueOf(session.getTicks());
            case "view_distance" -> String.valueOf(session.getClientRenderDistance());

            case "name" -> session.bedrockUsername();
            case "ping" -> String.valueOf(session.ping());
            case "platform" -> session.platform().toString();

            default -> null;
        };
    }

    /**
     * Adjusts the y-coordinate of the given Vector3f by removing the player offset.
     * The x and z coordinates remain unchanged.
     *
     * @param position the original position of the player as a Vector3f
     * @return a new Vector3f with the y-coordinate adjusted by subtracting the PLAYER_OFFSET
     */
    public static Vector3f adjustForPlayerOffset(Vector3f position) {
        return Vector3f.from(position.getX(), position.getY() - EntityDefinitions.PLAYER.offset(), position.getZ()); // TODO: https://github.com/GeyserMC/Geyser/issues/5061.
    }

    /**
     * Determines the player's facing direction based on the yaw value.
     * Yaw is normalized to a range of [0, 360) and mapped to cardinal directions.
     *
     * @param yaw The yaw value of the player.
     * @return The direction the player is facing as a string.
     */
    private static String getFacingDirection(float yaw) {
        // Normalize yaw to a range of [0, 360)
        yaw = (yaw % 360 + 360) % 360;

        // Determine direction based on yaw
        if (yaw >= 45 && yaw < 135) {
            return "west (Towards negative X)";
        } else if (yaw >= 135 && yaw < 225) {
            return "north (Towards negative Z)";
        } else if (yaw >= 225 && yaw < 315) {
            return "east (Towards positive X)";
        } else {
            return "south (Towards positive Z)";
        }
    }
}
