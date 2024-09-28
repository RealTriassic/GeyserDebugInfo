package org.stupidcraft.geyserdebuginfo.placeholder.providers;

import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.session.GeyserSession;
import org.stupidcraft.geyserdebuginfo.placeholder.PlaceholderProvider;
import org.stupidcraft.geyserdebuginfo.util.ChunkUtil;
import org.stupidcraft.geyserdebuginfo.util.PositionUtil;

import java.util.HashMap;
import java.util.Map;

public class ChunkPlaceholderProvider implements PlaceholderProvider {

    @Override
    public Map<String, String> getPlaceholders(String line, GeyserSession session) {
        Vector3f pos = PositionUtil.adjustForPlayerOffset(session.getPlayerEntity().getPosition());
        int[] relativeChunkCoords = ChunkUtil.getRelativeCoordinates(pos);

        return new HashMap<>(Map.ofEntries(
                Map.entry("relative_chunk_x", String.valueOf(relativeChunkCoords[0])),
                Map.entry("relative_chunk_y", String.valueOf(relativeChunkCoords[1])),
                Map.entry("relative_chunk_z", String.valueOf(relativeChunkCoords[2])),
                Map.entry("chunk_x", String.valueOf(session.getLastChunkPosition().getX())),
                Map.entry("chunk_y", String.valueOf(ChunkUtil.calculateChunkY(pos.getFloorY()))),
                Map.entry("chunk_z", String.valueOf(session.getLastChunkPosition().getY())),
                Map.entry("global_chunk_x", String.valueOf(ChunkUtil.getRelativeChunkCoordinates(pos.getX(), pos.getZ())[0])),
                Map.entry("global_chunk_z", String.valueOf(ChunkUtil.getRelativeChunkCoordinates(pos.getX(), pos.getZ())[1])),
                Map.entry("region_file", ChunkUtil.getRegionFileName(pos.getX(), pos.getZ()))
        ));
    }
}
