package marcv81.game;

import marcv81.gfx2d.Sprite;

import java.util.Random;

class Asteroid extends Sprite {

    private final static int ASTEROID_ANIMATIONS = 32;
    private final static int ASTEROID_ANIMATIONS_TYPES = 2;
    private final static float ASTEROID_SPAWN_DISTANCE = 4f;
    private final static float ASTEROID_REMOVAL_DISTANCE = 6f;
    private final static float ASTEROID_DRIFT_MIN_SPEED = 0.1f;
    private final static float ASTEROID_DRIFT_MAX_SPEED = 0.5f;
    private final static int ASTEROID_ANIMATION_MIN_SPEED = 25;
    private final static int ASTEROID_ANIMATION_MAX_SPEED = 30;

    private final static int ASTEROID_EXPLOSION_TIME = 250;

    private final float speedX, speedY;
    private final int animationType;
    private final int animationSpeed;
    private final int animationDirection;
    private long age = 0, maxAge = 0;

    // Constructor
    public Asteroid(Random random, float cameraX, float cameraY) {

        // The start position is at a fixed distance from the camera
        float angle = 6.2831853071f * random.nextFloat();
        setX(cameraX + ASTEROID_SPAWN_DISTANCE * (float) Math.cos(angle));
        setY(cameraY + ASTEROID_SPAWN_DISTANCE * (float) Math.sin(angle));
        setZ(GameRenderer.FOREGROUND_DEPTH);

        // The drift speed is vaguely towards the camera
        float r = 2f * (random.nextFloat() - 0.5f); // between -1 and 1
        float driftAngle = angle + (TAU / 2f) + (r * (TAU / 4f));
        float driftSpeed = ASTEROID_DRIFT_MIN_SPEED + random.nextFloat()
                * (ASTEROID_DRIFT_MAX_SPEED - ASTEROID_DRIFT_MIN_SPEED);
        this.speedX = driftSpeed * (float) Math.cos(driftAngle);
        this.speedY = driftSpeed * (float) Math.sin(driftAngle);

        // Random animation
        this.animationType = random.nextInt(ASTEROID_ANIMATIONS_TYPES);
        this.animationSpeed = ASTEROID_ANIMATION_MIN_SPEED
                + random.nextInt(ASTEROID_ANIMATION_MAX_SPEED
                - ASTEROID_ANIMATION_MIN_SPEED + 1);
        this.animationDirection = random.nextInt(2);
    }

    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    public void explode() {
        this.maxAge = age + ASTEROID_EXPLOSION_TIME;
    }

    @Override
    public int getAnimation() {
        int animation = (int) (animationSpeed * age / 1000 % ASTEROID_ANIMATIONS);
        animation = (animationDirection == 0) ? animation : ASTEROID_ANIMATIONS
                - animation - 1;
        return animation + (ASTEROID_ANIMATIONS * animationType);
    }

    public boolean isExploding() {
        return ((maxAge != 0) && (age < maxAge));
    }

    public boolean hasExploded() {
        return ((maxAge != 0) && (age >= maxAge));
    }

    public boolean isOutOfScope(Player player) {
        return getXYDistance(player) > ASTEROID_REMOVAL_DISTANCE;
    }

    public void update(long timeSlice) {
        setX(getX() + speedX * timeSlice / 1000f);
        setY(getY() + speedY * timeSlice / 1000f);
        age += timeSlice;
    }

    @Override
    public float getTransparency() {
        if (isExploding())
            return (maxAge - age) / (float) ASTEROID_EXPLOSION_TIME;
        else
            return 1f;
    }
}
