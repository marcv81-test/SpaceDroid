package net.marcv81.spacedroid.physics;

import net.marcv81.spacedroid.common.Vector2f;

/**
 * Basic implementation of Driftable. Children and containing classes may implement
 * Updatable using a combination of drift(), accelerate(), and drag().
 */
public class Drifter implements Driftable {

    private final Vector2f position;
    private final Vector2f speed;

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

    public void collide(Vector2f v) {
        speed.plus(v);
    }

    /**
     * Updates the position according to the speed.
     */
    public void drift(long timeSlice) {
        this.position.plus(getSpeed().multiply(timeSlice / 1000f));
    }

    /**
     * Updates the speed according to an acceleration.
     *
     * @param acceleration Acceleration in game units.
     * @param timeSlice    Time slice duration in milliseconds.
     */
    public void accelerate(Vector2f acceleration, long timeSlice) {
        this.speed.plus(new Vector2f(acceleration).multiply(timeSlice / 1000f));
    }

    /**
     * Reduce the speed according to a drag.
     *
     * @param drag      Drag coefficient in game units.
     * @param timeSlice Time slice duration in milliseconds.
     */
    public void drag(float drag, long timeSlice) {
        this.accelerate(getSpeed().multiply(-drag), timeSlice);
    }

    public void limitSpeed(float maxSpeedNorm) {
        float speedNorm = this.speed.norm();
        if (speedNorm > maxSpeedNorm) {
            this.speed.multiply(maxSpeedNorm / speedNorm);
        }
    }
}
