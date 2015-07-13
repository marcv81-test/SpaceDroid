package net.marcv81.spacedroid.physics;

import net.marcv81.spacedroid.common.Vector2f;

/**
 * Implementations of this interface drift according to Newton's laws of
 * motion in 2D.
 */
public interface Driftable {

    /**
     * @return Position in game world coordinates.
     */
    Vector2f getPosition();

    /**
     * @return Speed in game units.
     */
    Vector2f getSpeed();
}
