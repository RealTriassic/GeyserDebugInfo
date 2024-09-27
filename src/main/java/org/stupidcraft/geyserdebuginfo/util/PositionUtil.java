package org.stupidcraft.geyserdebuginfo.util;

import org.cloudburstmc.math.vector.Vector3f;
import org.geysermc.geyser.entity.EntityDefinitions;

/**
 * Utility class for handling position-related calculations, such as facing direction.
 */
public class PositionUtil {

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
    public static String getFacingDirection(float yaw) {
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
