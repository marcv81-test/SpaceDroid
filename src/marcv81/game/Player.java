package marcv81.game;

import marcv81.gfx2d.DriftingSprite;
import marcv81.gfx2d.Vector2f;

public class Player extends DriftingSprite {

    private static final float PLAYER_ACCELERATION_MULTIPLIER = 3f;
    private static final float PLAYER_FRICTION = 0.8f;
    private static final float PLAYER_MAX_SPEED = 1.2f;
    private static final float PLAYER_SPRITE_ANGLE = 90f;
    private static final float PLAYER_EXHAUST_DISTANCE = 0.15f;
    private static final float PLAYER_DIAMETER = 0.15f;
    private static final float PLAYER_MASS = 1f;

    private Vector2f acceleration = new Vector2f(0f, 0f);
    private float angle = PLAYER_SPRITE_ANGLE;

    // Constructor
    public Player() {
        super(new Vector2f(0f, 0f), new Vector2f(0f, 0f));
    }

    // Get the coordinates of the exhaust (to draw smoke)
    public Vector2f getExhaust() {
        Vector2f exhaustPosition = new Vector2f(getPosition());
        float exhaustAngle = (angle - PLAYER_SPRITE_ANGLE) / DEGREE_PER_RADIAN;
        Vector2f exhaustDisplacement = new Vector2f(exhaustAngle);
        exhaustDisplacement.scale(PLAYER_EXHAUST_DISTANCE);
        exhaustPosition.add(exhaustDisplacement);
        return exhaustPosition;
    }

    public void setAcceleration(Vector2f acceleration) {

        // Set the acceleration
        this.acceleration.set(acceleration);
        this.acceleration.scale(PLAYER_ACCELERATION_MULTIPLIER);

        // Update the drawing angle if accelerating
        if (acceleration.norm() > 0.5f) {
            angle = DEGREE_PER_RADIAN * acceleration.angle() - PLAYER_SPRITE_ANGLE;
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
        Vector2f deltaAcceleration = new Vector2f(acceleration);
        Vector2f friction = new Vector2f(getSpeed());
        friction.scale(PLAYER_FRICTION);
        deltaAcceleration.sub(friction);
        deltaAcceleration.scale(timeSlice / 1000f);
        getSpeed().add(deltaAcceleration);

        // Limit speed
        float normSpeed = getSpeed().norm();
        if (normSpeed > PLAYER_MAX_SPEED) {
            getSpeed().scale(PLAYER_MAX_SPEED / normSpeed);
        }

        super.update(timeSlice);
    }
}
