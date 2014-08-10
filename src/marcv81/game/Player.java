package marcv81.game;

import marcv81.gfx2d.Sprite;

public class Player extends Sprite {

    private static final float DEG_PER_RAD = 57.2957795f;
    private static final float SPRITE_ANGLE = 90f;

    private static final float PLAYER_FRICTION = 0.8f;
    private static final float PLAYER_ACCELERATION = 3f;
    private static final float PLAYER_STOP_SPEED = 0.2f;
    private static final float PLAYER_START_SPEED = 0.5f;
    private static final float PLAYER_MAX_SPEED = 1.2f;

    private float speedX = 0f, speedY = 0f;
    private float accelX = 0f, accelY = 0f;
    private float lastAngle = SPRITE_ANGLE;

    public float getExhaustX() {
        return getX() + 0.15f * (float) Math.cos((lastAngle - SPRITE_ANGLE) / DEG_PER_RAD);
    }
    public float getExhaustY() {
        return getY() + 0.15f * (float) Math.sin((lastAngle - SPRITE_ANGLE)/ DEG_PER_RAD);
    }

    public void setAccelXY(float accelX, float accelY) {
        this.accelX = PLAYER_ACCELERATION * accelX;
        this.accelY = PLAYER_ACCELERATION * accelY;
        float accel = (float) Math.sqrt(accelX * accelX + accelY * accelY);
        if (accel > 0.5f) {
            lastAngle = DEG_PER_RAD * (float) Math.atan2(accelY, accelX) - SPRITE_ANGLE;
        }
    }

    public Player() {
        setY(GameRenderer.FOREGROUND_DEPTH);
    }

    @Override
    public float getAngle() {
        return lastAngle;
    }

    // Update player speed and position from acceleration
    public void update(long timeSlice) {

        float accel = (float) Math.sqrt(accelX * accelX + accelY * accelY);
        float speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);

        if (speed < PLAYER_STOP_SPEED) {
            speedX = 0f;
            speedY = 0f;
        }
        if ((accel > PLAYER_ACCELERATION / 2f)
                && (speed < PLAYER_STOP_SPEED)) {
            speedX = PLAYER_START_SPEED * accelX / PLAYER_ACCELERATION;
            speedY = PLAYER_START_SPEED * accelY / PLAYER_ACCELERATION;
        }

        speedX += (accelX - (PLAYER_FRICTION * speedX)) * timeSlice / 1000;
        speedY += (accelY - (PLAYER_FRICTION * speedY)) * timeSlice / 1000;

        if (speed > PLAYER_MAX_SPEED) {
            speedX = speedX / speed * PLAYER_MAX_SPEED;
            speedY = speedY / speed * PLAYER_MAX_SPEED;
        }

        setX(getX() + speedX * timeSlice / 1000);
        setY(getY() + speedY * timeSlice / 1000);
    }
}
