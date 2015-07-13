package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.common.Vector2f;
import net.marcv81.spacedroid.physics.Collidable;

/**
 * Detect and handle elastic collisions in 2D.
 */
public class WorldPhysics {

    /**
     * Checks whether two Collidables overlap or not.
     */
    public boolean overlap(Collidable c1, Collidable c2) {
        return c1.getPosition().distance(c2.getPosition()) < (c1.getRadius() + c2.getRadius());
    }

    /**
     * Checks whether two Collidables shall collide or not. If this is the case then
     * deviates their trajectories accordingly.
     */
    public boolean collide(Collidable c1, Collidable c2, Vector2f deviation1, Vector2f deviation2) {

        // Return if the Collidables are not solid or do not overlap with each other
        if (!c1.isSolid() || !c2.isSolid() || !overlap(c1, c2)) {
            return false;
        }

        // Calculate the position/speed delta vectors and their dot product
        Vector2f deltaPosition = c1.getPosition().minus(c2.getPosition());
        Vector2f deltaSpeed = c1.getSpeed().minus(c2.getSpeed());
        float dotProduct = deltaSpeed.dot(deltaPosition);

        // If the collision generates a repulsing deviation
        if (dotProduct < 0f) {

            // Apply the deviation according to the equations of elastic collisions
            float ratio = 2f * dotProduct / ((c1.getMass() + c2.getMass()) * deltaPosition.normSquare());
            deviation1.set(new Vector2f(deltaPosition)).multiply(-c2.getMass() * ratio);
            deviation2.set(new Vector2f(deltaPosition)).multiply(c1.getMass() * ratio);

            return true;
        }

        // If the collision would generate an attracting deviation
        // the Collidables are moving away from each other already
        // and no collision shall happen
        else {
            return false;
        }
    }

    /**
     * Returns the collision point between two Collidables. Only meaningful when the
     * two Collidables just collided.
     *
     * @return Collision point in game world coordinates.
     */
    public Vector2f impactPoint(Collidable c1, Collidable c2) {
        Vector2f v1 = c1.getPosition().multiply(c2.getRadius());
        Vector2f v2 = c2.getPosition().multiply(c1.getRadius());
        float sum = c1.getRadius() + c2.getRadius();
        return v1.plus(v2).divide(sum);
    }
}
