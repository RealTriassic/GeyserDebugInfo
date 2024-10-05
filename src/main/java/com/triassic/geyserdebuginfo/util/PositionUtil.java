package com.triassic.geyserdebuginfo.util;

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
}
