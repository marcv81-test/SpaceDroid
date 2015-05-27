package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

/**
 * This class provides a basic implementation of Collidable.
 */
public final class Collider extends Drifter implements Collidable {

    /**
     * Radius in game units.
     */
    private float radius;

    /**
     * Mass in game units.
     */
    private float mass;

    /**
     * Constructor.
     */
    public Collider(Vector2f position, Vector2f speed, float radius, float mass) {
        super(position, speed);
        this.radius = radius;
        this.mass = mass;
    }

    public float getRadius() {
        return radius;
    }

    public float getMass() {
        return mass;
    }

    public boolean overlaps(Collidable that) {
        return this.getPosition().distance(that.getPosition()) < (this.radius + that.getRadius());
    }

    public boolean collides(Collidable that) {

        // Return if the Collidables do not overlap with each other
        if (!this.overlaps(that)) {
            return false;
        }

        // Calculate the position/speed delta vectors and their dot product
        Vector2f deltaPosition = this.getPosition().minus(that.getPosition());
        Vector2f deltaSpeed = this.getSpeed().minus(that.getSpeed());
        float dotProduct = deltaSpeed.dot(deltaPosition);

        // If the collision generates a repulsing deviation
        if (dotProduct < 0f) {

            // Apply the deviation according to the equations of elastic collisions
            float ratio = 2f * dotProduct / ((this.getMass() + that.getMass()) * deltaPosition.normSquare());
            Vector2f thisDeviation = (new Vector2f(deltaPosition)).multiply(-that.getMass() * ratio);
            this.setSpeed(this.getSpeed().plus(thisDeviation));
            Vector2f thatDeviation = (new Vector2f(deltaPosition)).multiply(this.getMass() * ratio);
            that.setSpeed(that.getSpeed().plus(thatDeviation));

            return true;
        }

        // If the collision would generate an attracting deviation
        // the Collidables are moving away from each other already
        // and no collision shall happen
        else {
            return false;
        }
    }

    public Vector2f collisionPoint(Collidable that) {
        Vector2f v1 = this.getPosition().multiply(that.getRadius());
        Vector2f v2 = that.getPosition().multiply(this.getRadius());
        float sum = this.getRadius() + that.getRadius();
        return v1.plus(v2).divide(sum);
    }
}
