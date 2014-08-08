package marcv81.game;

public class Player {

    private static final float DEG_PER_RAD = 57.2957795f;
    private static final float SPRITE_ANGLE = 90f;

    private static final float PLAYER_FRICTION = 0.8f;
    private static final float PLAYER_ACCELERATION = 3f;
    private static final float PLAYER_STOP_SPEED = 0.2f;
    private static final float PLAYER_START_SPEED = 0.5f;

    private float x = 0f, y = 0f;
    private float speedX = 0f, speedY = 0f;
    private float accelX = 0f, accelY = 0f;
    private float lastAngle = 90f;

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setAccelXY(float accelX, float accelY) {
        this.accelX = PLAYER_ACCELERATION * accelX;
        this.accelY = PLAYER_ACCELERATION * accelY;
    }

    // Get the drawing angle from player speed
    public float getAngle() {
        float speed = (float) Math.sqrt(speedX * speedX + speedY * speedY);
        if (speed >= PLAYER_STOP_SPEED) {
            lastAngle = DEG_PER_RAD * (float) Math.atan2(speedY, speedX)
                    - SPRITE_ANGLE;
        }
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

        x += speedX * timeSlice / 1000;
        y += speedY * timeSlice / 1000;
    }
}
