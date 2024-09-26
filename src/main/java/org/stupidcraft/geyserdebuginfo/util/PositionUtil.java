package org.stupidcraft.geyserdebuginfo.util;

import org.cloudburstmc.math.vector.Vector3f;

/**
 * Utility class for handling position-related calculations, such as chunk coordinates and facing direction.
 */
public class PositionUtil {

    private static final float PLAYER_OFFSET = 1.62f; // TODO: Remove when https://github.com/GeyserMC/Geyser/issues/5061 solved.

    /**
     * Adjusts the y-coordinate of the given Vector3f by removing the player offset.
     * The x and z coordinates remain unchanged.
     *
     * @param position the original position of the player as a Vector3f
     * @return a new Vector3f with the y-coordinate adjusted by subtracting the PLAYER_OFFSET
     */
    public static Vector3f adjustForPlayerOffset(Vector3f position) {
        return Vector3f.from(position.getX(), position.getY() - PLAYER_OFFSET, position.getZ());
    }

    /**
     * Calculates the relative chunk coordinates (within a chunk) for a given absolute Vector3f coordinate.
     * Each component of the Vector3f is normalized to a range of [0, 15].
     *
     * @param absolutePosition The absolute coordinates as a Vector3f.
     * @return An array of integers containing the relative chunk coordinates [relativeX, relativeY, relativeZ].
     */
    public static int[] getRelativeChunkCoordinates(Vector3f absolutePosition) {
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
    private static int normalizeToChunkCoordinate(int absoluteCoordinate) {
        return (absoluteCoordinate % 16 + 16) % 16;
    }

    /**
     * Determines the player's facing direction based on the yaw value.
     * Yaw is normalized to a range of [0, 360) and mapped to cardinal directions.
     *
     * @param yaw The yaw value of the player.
     * @return The direction the player is facing as a string.
     */
    public static String getFacingDirection(float yaw) {
        // Normalize yaw to a range of [0, 360)
        yaw = (yaw % 360 + 360) % 360;

        // Determine direction based on yaw
        if (yaw >= 45 && yaw < 135) {
            return "west (towards -X)";
        } else if (yaw >= 135 && yaw < 225) {
            return "north (towards -Z)";
        } else if (yaw >= 225 && yaw < 315) {
            return "east (towards +X)";
        } else {
            return "south (towards +Z)";
        }
    }
}
