package net.marcv81.spacedroid.physics;

import net.marcv81.spacedroid.common.Vector2f;

/**
 * Instances of this class drift according to Newton's laws of motion in 2D,
 * providing the bases of a physics engine for the game. Additional functions
 * facilitate the implementation of Updatable.
 */
public class Drifter {

    /**
     * Position in game world coordinates.
     */
    private final Vector2f position;

    /**
     * Speed vector in game units.
     */
    private final Vector2f speed;

    /**
     * Constructor.
     */
    public Drifter(Vector2f position, Vector2f speed) {
        this.position = new Vector2f(position);
        this.speed = new Vector2f(speed);
    }

    public Vector2f getPosition() {
        return new Vector2f(position);
    }

    public Vector2f getSpeed() {
        return new Vector2f(speed);
    }

    public void deviate(Vector2f speed) {
        this.speed.plus(speed);
    }

    /**
     * Updates the position according to the speed.
     *
     * @param timeSlice Time slice duration in milliseconds.
     */
    public void update(long timeSlice) {
        position.plus(getSpeed().multiply(timeSlice / 1000f));
    }

    /**
     * Updates the speed according to an acceleration.
     *
     * @param acceleration Acceleration vector in game units.
     * @param timeSlice    Time slice duration in milliseconds.
     */
    public void addAcceleration(Vector2f acceleration, long timeSlice) {
        speed.plus(new Vector2f(acceleration).multiply(timeSlice / 1000f));
    }

    /**
     * Updates the speed according to a drag proportional to the speed.
     *
     * @param drag      Drag coefficient in game units.
     * @param timeSlice Time slice duration in milliseconds.
     */
    public void addDrag(float drag, long timeSlice) {
        addAcceleration(getSpeed().multiply(-drag), timeSlice);
    }

    /**
     * Limits the norm of the speed.
     *
     * @param maxSpeedNorm Maximum norm of the speed.
     */
    public void limitSpeed(float maxSpeedNorm) {
        float speedNorm = speed.norm();
        if (speedNorm > maxSpeedNorm) {
            speed.multiply(maxSpeedNorm / speedNorm);
        }
    }
}
