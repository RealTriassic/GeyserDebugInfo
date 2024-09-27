package org.stupidcraft.geyserdebuginfo.util;

import org.cloudburstmc.math.vector.Vector3f;

/**
 * Utility class for handling chunk-related calculations, such as chunk coordinates and region file names.
 */
public class ChunkUtil {

    private static final int CHUNK_SIZE = 16;
    private static final int REGION_SIZE = 32;
    private static final int CHUNK_MASK = REGION_SIZE - 1;

    /**
     * Calculates the global chunk coordinates from player position.
     *
     * @param playerX The player's X coordinate.
     * @param playerZ The player's Z coordinate.
     * @return An array containing the global chunk coordinates: [chunkX, chunkZ].
     */
    public static int[] getGlobalChunkCoordinates(float playerX, float playerZ) {
        int chunkX = (int) Math.floor(playerX / CHUNK_SIZE);
        int chunkZ = (int) Math.floor(playerZ / CHUNK_SIZE);
        return new int[]{chunkX, chunkZ};
    }

    /**
     * Calculates the relative chunk coordinates within its region from player position.
     *
     * @param playerX The player's X coordinate.
     * @param playerZ The player's Z coordinate.
     * @return An array containing the relative chunk coordinates: [relativeChunkX, relativeChunkZ].
     */
    public static int[] getRelativeChunkCoordinates(float playerX, float playerZ) {
        int[] globalCoords = getGlobalChunkCoordinates(playerX, playerZ);
        return getRelativeChunkCoordinates(globalCoords[0], globalCoords[1]);
    }

    /**
     * Calculates the relative chunk coordinates within its region from global chunk coordinates.
     *
     * @param chunkX The global chunk X coordinate.
     * @param chunkZ The global chunk Z coordinate.
     * @return An array containing the relative chunk coordinates: [relativeChunkX, relativeChunkZ].
     */
    public static int[] getRelativeChunkCoordinates(int chunkX, int chunkZ) {
        int relativeChunkX = chunkX & CHUNK_MASK;
        int relativeChunkZ = chunkZ & CHUNK_MASK;
        return new int[]{relativeChunkX, relativeChunkZ};
    }

    /**
     * Calculates the region file name based on the player's position.
     *
     * @param playerX The player's X coordinate.
     * @param playerZ The player's Z coordinate.
     * @return The name of the region file corresponding to the player's position.
     */
    public static String getRegionFileName(float playerX, float playerZ) {
        int[] globalCoords = getGlobalChunkCoordinates(playerX, playerZ);
        int regionX = globalCoords[0] >> 5;
        int regionZ = globalCoords[1] >> 5;

        return String.format("r.%d.%d.mca", regionX, regionZ);
    }

    /**
     * Calculates the relative chunk coordinates (within a chunk) for a given absolute Vector3f coordinate.
     * Each component of the Vector3f is normalized to a range of [0, 15].
     *
     * @param absolutePosition The absolute coordinates as a Vector3f.
     * @return An array of integers containing the relative chunk coordinates [relativeX, relativeY, relativeZ].
     */
    public static int[] getRelativeCoordinates(Vector3f absolutePosition) {
        int relativeX = normalizeToChunkCoordinate(absolutePosition.getFloorX());
        int relativeY = normalizeToChunkCoordinate(absolutePosition.getFloorY());
        int relativeZ = normalizeToChunkCoordinate(absolutePosition.getFloorZ());

        return new int[]{relativeX, relativeY, relativeZ};
    }

    /**
     * Normalizes the absolute coordinate to a range of [0, 15].
     *
     * @param absoluteCoordinate The absolute coordinate.
     * @return The normalized coordinate (0-15).
     */
    public static int normalizeToChunkCoordinate(int absoluteCoordinate) {
        return (absoluteCoordinate % 16 + 16) % 16;
    }
}