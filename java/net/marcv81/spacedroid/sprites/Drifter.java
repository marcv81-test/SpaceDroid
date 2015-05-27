package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

/**
 * This class provides a basic implementation of Driftable with additional functions
 * to facilitate the implementation of Updatable.
 */
public class Drifter implements Driftable {

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

    public void setSpeed(Vector2f speed) {
        this.speed.set(speed);
    }

    /**
     * Updates the position according to the speed.
     *
     * @param timeSlice Time slice duration in milliseconds.
     */
    public void updatePosition(long timeSlice) {
        position.plus(getSpeed().multiply(timeSlice / 1000f));
    }

    /**
     * Updates the speed according to an acceleration.
     *
     * @param acceleration Acceleration vector in game units.
     * @param timeSlice    Time slice duration in milliseconds.
     */
    public void updateSpeed(Vector2f acceleration, long timeSlice) {
        speed.plus(new Vector2f(acceleration).multiply(timeSlice / 1000f));
    }

    /**
     * Updates the speed according to a drag proportional to the speed.
     *
     * @param drag      Drag coefficient in game units.
     * @param timeSlice Time slice duration in milliseconds.
     */
    public void updateDrag(float drag, long timeSlice) {
        updateSpeed(getSpeed().multiply(-drag), timeSlice);
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
