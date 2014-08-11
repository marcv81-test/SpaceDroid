package marcv81.game;

import marcv81.gfx2d.Sprite;

public class Player extends Sprite {

    private static final float PLAYER_ACCELERATION_MULTIPLIER = 3f;
    private static final float PLAYER_FRICTION = 0.8f;
    private static final float PLAYER_MAX_SPEED = 1.2f;
    private static final float PLAYER_SPRITE_ANGLE = 90f;
    private static final float PLAYER_EXHAUST_DISTANCE = 0.15f;

    private float speedX = 0f, speedY = 0f;
    private float accelerationX = 0f, accelerationY = 0f;
    private float angle = PLAYER_SPRITE_ANGLE;

    // Constructor
    public Player() {
        super(0f, 0f);
    }

    // Get the X coordinate of the exhaust (to draw smoke)
    public float getExhaustX() {
        float exhaustAngle = (angle - PLAYER_SPRITE_ANGLE) / DEGREE_PER_RADIAN;
        return getX() + PLAYER_EXHAUST_DISTANCE * (float) Math.cos(exhaustAngle);
    }

    // Get the Y coordinate of the exhaust (to draw smoke)
    public float getExhaustY() {
        float exhaustAngle = (angle - PLAYER_SPRITE_ANGLE) / DEGREE_PER_RADIAN;
        return getY() + PLAYER_EXHAUST_DISTANCE * (float) Math.sin(exhaustAngle);
    }

    public float getAcceleration() {
        return (float) Math.sqrt(accelerationX * accelerationX + accelerationY * accelerationY);
    }

    public float getSpeed() {
        return (float) Math.sqrt(speedX * speedX + speedY * speedY);
    }

    public void setAcceleration(float accelerationX, float accelerationY) {

        // Set the acceleration
        this.accelerationX = PLAYER_ACCELERATION_MULTIPLIER * accelerationX;
        this.accelerationY = PLAYER_ACCELERATION_MULTIPLIER * accelerationY;

        // Update the drawing angle if accelerating
        if (getAcceleration() > 0.5f) {
            angle = DEGREE_PER_RADIAN * (float) Math.atan2(accelerationY, accelerationX) - PLAYER_SPRITE_ANGLE;
        }
    }

    @Override
    public float getAngle() {
        return angle;
    }

    // Update player speed and position from acceleration
    public void update(long timeSlice) {

        // Update speed
        speedX += (accelerationX - (PLAYER_FRICTION * speedX)) * timeSlice / 1000;
        speedY += (accelerationY - (PLAYER_FRICTION * speedY)) * timeSlice / 1000;

        // Limit speed
        float speed = getSpeed();
        if (speed > PLAYER_MAX_SPEED) {
            speedX = speedX / speed * PLAYER_MAX_SPEED;
            speedY = speedY / speed * PLAYER_MAX_SPEED;
        }

        // Update position
        setX(getX() + speedX * timeSlice / 1000);
        setY(getY() + speedY * timeSlice / 1000);
    }
}
