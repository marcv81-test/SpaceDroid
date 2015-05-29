package net.marcv81.spacedroid.physics;

import net.marcv81.spacedroid.common.Vector2f;

/**
 * Implementations of this interface may collide with each other according to
 * the equations of elastic collisions in 2D implemented in CollisionUtils.
 */
public interface Collidable {

    /**
     * @return Position in game world coordinates.
     */
    Vector2f getPosition();

    /**
     * @return Speed in game units.
     */
    Vector2f getSpeed();

    /**
     * @return Radius in game units.
     */
    float getRadius();

    /**
     * @return Mass in game units.
     */
    float getMass();

    /**
     * @return Whether this Collidable is able to collide or not.
     */
    boolean isSolid();

    /**
     * Add a vector to the speed to deviate the trajectory.
     */
    void deviate(Vector2f speed);
}
