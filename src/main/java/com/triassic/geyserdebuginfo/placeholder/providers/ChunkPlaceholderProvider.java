package com.triassic.geyserdebuginfo.placeholder.providers;

import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.session.GeyserSession;
import org.jetbrains.annotations.NotNull;
import com.triassic.geyserdebuginfo.placeholder.PlaceholderProvider;
import com.triassic.geyserdebuginfo.util.ChunkUtil;
import com.triassic.geyserdebuginfo.util.PositionUtil;

import java.util.Arrays;
import java.util.List;

public class ChunkPlaceholderProvider extends PlaceholderProvider {

    @Override
    public String getIdentifier() {
        return "chunk";
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
        return Arrays.asList("x", "y", "z", "relative_x", "relative_y", "relative_z", "global_x", "global_z", "region_file");
    }

    @Override
    public String onRequest(final GeyserSession session, @NotNull final String params) {
        Vector3f pos = PositionUtil.adjustForPlayerOffset(session.getPlayerEntity().getPosition());
        int[] relativeChunkCoords = ChunkUtil.getRelativeCoordinates(pos);

        return switch (params) {
            case "x" -> String.valueOf(session.getLastChunkPosition().getX());
            case "y" -> String.valueOf(ChunkUtil.calculateChunkY(pos.getFloorY()));
            case "z" -> String.valueOf(session.getLastChunkPosition().getY());
            case "relative_x" -> String.valueOf(relativeChunkCoords[0]);
            case "relative_y" -> String.valueOf(relativeChunkCoords[1]);
            case "relative_z" -> String.valueOf(relativeChunkCoords[2]);
            case "global_x" -> String.valueOf(ChunkUtil.getRelativeChunkCoordinates(pos.getX(), pos.getZ())[0]);
            case "global_z" -> String.valueOf(ChunkUtil.getRelativeChunkCoordinates(pos.getX(), pos.getZ())[1]);
            case "region_file" -> ChunkUtil.getRegionFileName(pos.getX(), pos.getZ());
            default -> null;
        };
    }
}
