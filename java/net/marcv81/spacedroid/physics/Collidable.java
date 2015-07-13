package net.marcv81.spacedroid.physics;

import net.marcv81.spacedroid.common.Vector2f;

/**
 * Implementations of this interface may collide with each other according to
 * the equations of elastic collisions in 2D.
 */
public interface Collidable extends Driftable {

    /**
     * @return Radius in game units.
     */
    float getRadius();

    /**
     * @return Mass in game units.
     */
    float getMass();

    /**
     * @return Whether this Collidable is solid or not. Only solid Collidables
     * may collide.
     */
    boolean isSolid();

    void collide(Class c, Vector2f v);
}
