package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;

/**
 * This class handles the player. It is a drifting sprite to which thrust can be applied.
 * The speed is limited, and a drag prevents it from drifting indefinitely to improve
 * the gameplay.
 */
public final class Player implements Sprite, Updatable, Collidable {

    protected static final float DEGREES_PER_RADIAN = 57.2957795f;

    private static final float PLAYER_RADIUS = 0.09f;
    private static final float PLAYER_MASS = 1f;

    private static final float PLAYER_DRAG = 0.8f;
    private static final float PLAYER_MAX_SPEED = 1.2f;

    private static final float PLAYER_DRAWING_ANGLE = 90f;
    private static final float PLAYER_EXHAUST_DISTANCE = 0.15f;

    /**
     * Thrust vector applied in game units.
     */
    private Vector2f thrust = new Vector2f(0f, 0f);

    /**
     * Orientation in degrees.
     */
    private float angle = 90f;

    private Collider collider;

    /**
     * Constructor.
     */
    public Player() {
        this.collider = new Collider(new Vector2f(0f, 0f), new Vector2f(0f, 0f), PLAYER_RADIUS, PLAYER_MASS);
    }

    /**
     * Gets the position of the exhaust pipe of this Player. Used to draw the smoke.
     *
     * @return Position of the exhaust pipe in game world coordinates.
     */
    public Vector2f getExhaust() {

        // Calculate the exhaust angle in radians
        float exhaustAngle = (angle + 180f) / DEGREES_PER_RADIAN;

        // Calculate and return the exhaust position
        return getPosition().plus((new Vector2f(exhaustAngle)).multiply(PLAYER_EXHAUST_DISTANCE));
    }

    /**
     * Sets the thrust of this player.
     */
    public void setThrust(Vector2f v) {

        // Set the thrust
        thrust.set(v);

        // Update the drawing angle if accelerating
        if (v.norm() > 0.5f) {
            angle = DEGREES_PER_RADIAN * v.angle();
        }
    }

    //
    // Sprite implementation
    //

    public int getAnimationIndex() {
        return 0;
    }

    public float getTransparency() {
        return 1f;
    }

    public float getAngle() {
        return angle + PLAYER_DRAWING_ANGLE;
    }

    public float getScale() {
        return 1f;
    }

    //
    // Updatable implementation
    //

    public void update(long timeSlice) {

        // There is no drag is space but it improves the gameplay
        collider.addDrag(PLAYER_DRAG, timeSlice);

        // Add acceleration up to a speed limit
        collider.addAcceleration(thrust, timeSlice);
        collider.limitSpeed(PLAYER_MAX_SPEED);

        collider.update(timeSlice);
    }

    //
    // Collidable implementation delegation
    //

    public Vector2f getPosition() {
        return collider.getPosition();
    }

    public Vector2f getSpeed() {
        return collider.getSpeed();
    }

    public float getRadius() {
        return collider.getRadius();
    }

    public float getMass() {
        return collider.getMass();
    }

    public boolean isSolid() {
        return true;
    }

    public void deviate(Vector2f speed) {
        collider.deviate(speed);
    }
}
