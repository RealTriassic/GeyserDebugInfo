package com.triassic.geyserdebuginfo.placeholder.placeholders;

import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;
import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;
import com.triassic.geyserdebuginfo.util.PositionUtil;

import java.util.Arrays;
import java.util.List;

public class PositionPlaceholderProvider extends PlaceholderProvider {

    @Override
    public String getIdentifier() {
        return "position";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String getAuthor() {
        return "Triassic";
    }

    @Override
    public List<String> getPlaceholders() {
        return Arrays.asList("x", "y", "z", "facing", "yaw", "pitch");
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
