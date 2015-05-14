package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;

/**
 * This class handles Sprites which drift in space according to Newton's laws of motion.
 * They provide a physics engine for the game.
 */
public abstract class DriftingSprite extends Sprite {

    /**
     * Speed of this DriftingSprite.
     */
    private Vector2f speed;

    /**
     * Constructor.
     *
     * @param position Initial position in game world coordinates.
     * @param speed    Initial speed in game units.
     */
    public DriftingSprite(Vector2f position, Vector2f speed) {
        super(position);
        this.speed = new Vector2f(speed);
    }

    /**
     * Gets the speed of this DriftingSprite in game units.
     */
    public Vector2f getSpeed() {
        return new Vector2f(speed);
    }

    /**
     * Sets the speed of this DriftingSprite in game units.
     */
    public void setSpeed(Vector2f v) {
        speed.set(v);
    }

    /**
     * Gets the mass of this DriftingSprite. To be overridden by the implementing classes.
     *
     * @return The mass of this DriftingSprite in game units.
     */
    public abstract float getMass();

    /**
     * Gets the diameter of this DriftingSprite. Used for collisions detection. To be
     * overridden by the implementing classes.
     *
     * @return The diameter of this DriftingSprite in game units.
     */
    public abstract float getDiameter();

    /**
     * Updates the position of this DriftingSprite according to its speed. Shall
     * unconditionally be called in the game loop.
     *
     * @param timeSlice Game loop time slice duration in milliseconds.
     */
    public void update(long timeSlice) {
        addToPosition((new Vector2f(speed)).multiply(timeSlice / 1000f));
    }

    /**
     * Checks if this DriftingSprite and another overlap.
     */
    public boolean overlaps(DriftingSprite s) {
        return getDistance(s) < (getDiameter() + s.getDiameter()) / 2f;
    }

    /**
     * Calculates the midpoint between this DriftingSprite and another weighted by diameter.
     * This provides a good estimate of the impact point if called when the DriftingSprites
     * collide.
     *
     * @param s Other DriftingSprite.
     * @return Position of the midpoint between both DriftingSprites weighted by diameter.
     */
    public Vector2f impactPoint(DriftingSprite s) {
        return this.getPosition().multiply(s.getDiameter())
                .plus(s.getPosition().multiply(this.getDiameter()))
                .divide(this.getDiameter() + s.getDiameter());
    }

    /**
     * Applies the speed deviation resulting from the collision between this DriftingSprite
     * and another. Implements the equations of elastic collisions in 2D. This function shall
     * only ever be called when both DriftingSprites overlap. The overlap is not a guarantee
     * of a collision, as the DriftingSprites may already be drifting away from each other.
     *
     * @param s Other DriftingSprite.
     * @return Whether the DriftingSprites actually collided or not.
     */
    public boolean collide(DriftingSprite s) {

        // Calculate the collision delta vectors
        Vector2f deltaSpeed = getSpeed().minus(s.getSpeed());
        Vector2f deltaPosition = getPosition().minus(s.getPosition());

        // Calculate the dot product between the delta vectors
        float dotProduct = deltaSpeed.dot(deltaPosition);

        // If the dot product is negative the collision does generate a repulsing deviation
        if (dotProduct < 0f) {

            // Apply the deviation according to the equations of elastic collisions
            float ratio = 2f * dotProduct / ((getMass() + s.getMass()) * deltaPosition.normSquare());
            this.speed.plus((new Vector2f(deltaPosition)).multiply(-s.getMass() * ratio));
            s.speed.plus((new Vector2f(deltaPosition)).multiply(this.getMass() * ratio));

            return true;
        }

        // If the dot product is positive the collision would generate an attracting deviation
        // This means the sprites are drifting away from each other and no deviation is required
        else {
            return false;
        }
    }

    /**
     * Apply an external force to this DriftingSprite.
     * Equation: f = ma = m dv/dt, hence dv = f/m dt.
     *
     * @param f         Force vector in game units.
     * @param timeSlice Duration of the force application in milliseconds.
     */
    public void applyForce(Vector2f f, long timeSlice) {
        speed.plus(f.multiply(timeSlice / (1000f * getMass())));
    }
}
