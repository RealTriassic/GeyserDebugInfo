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
            case "facing" -> PositionUtil.getFacingDirection(player.getYaw());
            case "yaw" -> String.format("%.1f", player.getYaw());
            case "pitch" -> String.format("%.1f", player.getPitch());
            default -> null;
        };
    }
}
