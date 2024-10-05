package com.triassic.geyserdebuginfo.placeholder.placeholders;

import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;
import com.triassic.geyserdebuginfo.util.PositionUtil;
import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;

public class PositionPlaceholderProvider extends PlaceholderProvider {

    @Override
    public String getIdentifier() {
        return "position";
    }

    @Override
    public String onRequest(final GeyserSession session, @NotNull final String params) {
        SessionPlayerEntity player = session.getPlayerEntity();
        Vector3f pos = PositionUtil.adjustForPlayerOffset(player.getPosition());

        return switch (params) {
            case "x" -> String.format("%.3f", pos.getX());
            case "y" -> String.format("%.3f", pos.getY());
            case "z" -> String.format("%.3f", pos.getZ());
            case "facing" -> getFacingDirection(player.getYaw());
            case "yaw" -> String.format("%.1f", player.getYaw());
            case "pitch" -> String.format("%.1f", player.getPitch());
            default -> null;
        };
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
