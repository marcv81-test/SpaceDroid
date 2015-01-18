package net.marcv81.game;

import net.marcv81.gfx2d.Renderer;
import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;

public abstract class DriftingSprite extends Sprite {

    private static final float SPRITE_REMOVAL_DISTANCE = 0.75f;

    private Vector2f speed;

    // Constructor
    public DriftingSprite(Vector2f position, Vector2f speed) {
        super(position);
        this.speed = new Vector2f(speed);
    }

    public Vector2f getSpeed() {
        return new Vector2f(speed);
    }

    public void setSpeed(Vector2f v) {
        speed.set(v);
    }

    public void addToSpeed(Vector2f v) {
        speed.plus(v);
    }

    public abstract float getMass();

    public abstract float getDiameter();

    // Update the sprite position
    public void update(long timeSlice) {
        addToPosition((new Vector2f(speed)).multiply(timeSlice / 1000f));
    }

    // Find if two sprites overlap
    public boolean overlaps(DriftingSprite s) {
        return getDistance(s) < (getDiameter() + s.getDiameter()) / 2f;
    }

    public Vector2f impactPoint(DriftingSprite s) {
        return this.getPosition().multiply(s.getDiameter())
                .plus(s.getPosition().multiply(this.getDiameter()))
                .divide(this.getDiameter() + s.getDiameter());
    }

    // Deviate two colliding sprites
    public boolean collide(DriftingSprite s) {

        // Prepare the collision delta vectors
        Vector2f deltaSpeed = this.getSpeed().minus(s.getSpeed());
        Vector2f deltaPosition = this.getPosition().minus(s.getPosition());

        // Calculate the dot product between the delta vectors
        float dotProduct = deltaSpeed.dot(deltaPosition);

        // Prevent collisions from generating attracting deviations
        if (dotProduct < 0f) {

            // Calculate the collision factor
            float ratio = 2f * dotProduct / ((getMass() + s.getMass()) * deltaPosition.normSquare());

            // Apply the deviations
            this.addToSpeed((new Vector2f(deltaPosition)).multiply(-s.getMass() * ratio));
            s.addToSpeed((new Vector2f(deltaPosition)).multiply(this.getMass() * ratio));

            return true;

        // If the collision would have generated an attracting deviation
        } else {
            return false;
        }
    }

    public boolean isOutOfScope(Renderer renderer) {
        Vector2f position = getPosition();
        return position.getY() >= renderer.getTop() + SPRITE_REMOVAL_DISTANCE
                || position.getY() <= renderer.getBottom() - SPRITE_REMOVAL_DISTANCE
                || position.getX() >= renderer.getRight() + SPRITE_REMOVAL_DISTANCE
                || position.getX() <= renderer.getLeft() - SPRITE_REMOVAL_DISTANCE;
    }
}
