package marcv81.game;

import marcv81.gfx2d.Vector2f;

public class Player extends DriftingSprite {

    private static final float PLAYER_ACCELERATION_MULTIPLIER = 3f;
    private static final float PLAYER_FRICTION = 0.8f;
    private static final float PLAYER_MAX_SPEED = 1.2f;
    private static final float PLAYER_SPRITE_ANGLE = -90f;
    private static final float PLAYER_EXHAUST_DISTANCE = 0.15f;
    private static final float PLAYER_DIAMETER = 0.18f;
    private static final float PLAYER_MASS = 1f;

    private Vector2f acceleration = new Vector2f(0f, 0f);
    private float angle = PLAYER_SPRITE_ANGLE;

    // Constructor
    public Player() {
        super(new Vector2f(0f, 0f), new Vector2f(0f, 0f));
    }

    // Get the coordinates of the exhaust (to draw smoke)
    public Vector2f getExhaust() {
        float exhaustAngle = (angle - PLAYER_SPRITE_ANGLE) / DEGREE_PER_RADIAN;
        return getPosition().plus((new Vector2f(exhaustAngle)).multiply(PLAYER_EXHAUST_DISTANCE));
    }

    public void setAcceleration(Vector2f v) {

        // Set the acceleration
        acceleration.set(v).multiply(PLAYER_ACCELERATION_MULTIPLIER);

        // Update the drawing angle if accelerating
        if (v.norm() > 0.5f) {
            angle = DEGREE_PER_RADIAN * v.angle() - PLAYER_SPRITE_ANGLE;
        }
    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public float getMass() {
        return PLAYER_MASS;
    }

    @Override
    public float getDiameter() {
        return PLAYER_DIAMETER;
    }

    // Update player speed and position from acceleration
    public void update(long timeSlice) {

        // Update speed
        addToSpeed((new Vector2f(acceleration))
                .minus(getSpeed().multiply(PLAYER_FRICTION))
                .multiply(timeSlice / 1000f));

        // Limit speed
        Vector2f speed = getSpeed();
        float normSpeed = speed.norm();
        if (normSpeed > PLAYER_MAX_SPEED) {
            setSpeed(speed.multiply(PLAYER_MAX_SPEED / normSpeed));
        }

        super.update(timeSlice);
    }
}
