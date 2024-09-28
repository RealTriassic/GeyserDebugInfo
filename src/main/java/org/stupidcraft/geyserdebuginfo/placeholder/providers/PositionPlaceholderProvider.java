package org.stupidcraft.geyserdebuginfo.placeholder.providers;

import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.entity.type.player.SessionPlayerEntity;
import org.geysermc.geyser.session.GeyserSession;
import org.stupidcraft.geyserdebuginfo.placeholder.PlaceholderProvider;
import org.stupidcraft.geyserdebuginfo.util.PositionUtil;

import java.util.HashMap;
import java.util.Map;

public class PositionPlaceholderProvider implements PlaceholderProvider {

    @Override
    public Map<String, String> getPlaceholders(String line, GeyserSession session) {
        SessionPlayerEntity player = session.getPlayerEntity();
        Vector3f pos = PositionUtil.adjustForPlayerOffset(player.getPosition());

        return new HashMap<>(Map.ofEntries(
                Map.entry("x", String.format("%.3f", pos.getX())),
                Map.entry("y", String.format("%.3f", pos.getY())),
                Map.entry("z", String.format("%.3f", pos.getZ())),
                Map.entry("facing", PositionUtil.getFacingDirection(player.getYaw())),
                Map.entry("yaw", String.format("%.1f", player.getYaw())),
                Map.entry("pitch", String.format("%.1f", player.getPitch()))
        ));
    }
}
