package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

/**
 * Implementations of this interface can collide with each other according to the
 * equations of elastic collisions in 2D.
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
     * Checks whether two Collidables overlap or not.
     *
     * @param that Other Collidable.
     * @return Whether the Collidables overlap or not.
     */
    boolean overlaps(Collidable that);

    /**
     * Checks whether two Collidables shall collide or not. If this is the case then
     * deviates their trajectories accordingly.
     *
     * @param that Other Collidable.
     * @return Whether a collision occurred or not.
     */
    boolean collides(Collidable that);

    /**
     * Returns the collision point between two Collidables. Only meaningful when the
     * two Collidables just collided.
     *
     * @param that Other Collidable.
     * @return Collision point in game world coordinates.
     */
    Vector2f collisionPoint(Collidable that);
}
