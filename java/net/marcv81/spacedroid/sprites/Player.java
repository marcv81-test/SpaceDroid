package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

/**
 * This class handles the player. It is a drifting sprite to which thrust can be applied.
 * The speed is limited, and friction prevents the sprite from drifting indefinitely to
 * improve the gameplay.
 */
public final class Player extends DriftingSprite {

    private static final float PLAYER_DIAMETER = 0.18f;
    private static final float PLAYER_MASS = 1f;
    private static final float PLAYER_FRICTION = 0.8f;
    private static final float PLAYER_MAX_SPEED = 1.2f;

    private static final float PLAYER_DRAWING_ANGLE = 90f;
    private static final float PLAYER_EXHAUST_DISTANCE = 0.15f;

    /**
     * Thrust applied.
     */
    private Vector2f thrust = new Vector2f(0f, 0f);

    /**
     * Orientation in degrees.
     */
    private float angle = 90f;

    /**
     * Constructor.
     */
    public Player() {
        super(new Vector2f(0f, 0f), new Vector2f(0f, 0f));
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

    @Override
    public float getAngle() {
        return angle + PLAYER_DRAWING_ANGLE;
    }

    @Override
    public float getMass() {
        return PLAYER_MASS;
    }

    @Override
    public float getDiameter() {
        return PLAYER_DIAMETER;
    }

    /**
     * Updates the position and speed of this Player. Shall unconditionally be called
     * in the game loop.
     *
     * @param timeSlice Game loop time slice duration in milliseconds.
     */
    public void update(long timeSlice) {

        // Apply the external forces: thrust and friction
        // There is no friction is space but it improves the gameplay
        applyForce(thrust.minus(getSpeed().multiply(PLAYER_FRICTION)), timeSlice);

        // Limit the speed
        Vector2f speed = getSpeed();
        float normSpeed = speed.norm();
        if (normSpeed > PLAYER_MAX_SPEED) {
            setSpeed(speed.multiply(PLAYER_MAX_SPEED / normSpeed));
        }

        super.update(timeSlice);
    }
}
