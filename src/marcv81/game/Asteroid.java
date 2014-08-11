package marcv81.game;

import marcv81.gfx2d.Sprite;

import java.util.Random;

class Asteroid extends Sprite {

    private static final int ASTEROID_ANIMATIONS = 32;
    private static final int ASTEROID_ANIMATIONS_TYPES = 2;
    private static final float ASTEROID_SPAWN_DISTANCE = 4f;
    private static final float ASTEROID_REMOVAL_DISTANCE = 6f;
    private static final float ASTEROID_DRIFT_MIN_SPEED = 0.1f;
    private static final float ASTEROID_DRIFT_MAX_SPEED = 0.5f;
    private static final int ASTEROID_ANIMATION_MIN_SPEED = 25;
    private static final int ASTEROID_ANIMATION_MAX_SPEED = 30;
    private static final int ASTEROID_EXPLOSION_TIME = 250;

    private final float speedX, speedY;
    private final float angle;
    private final int animationType;
    private final int animationSpeed;
    private final int animationDirection;
    private long age = 0, maxAge = 0;

    // Constructor
    public Asteroid(float x, float y, float speedX, float speedY, Random random) {

        // Call parent constructor
        super(x, y);

        // Set speed
        this.speedX = speedX;
        this.speedY = speedY;

        // Random initial angle
        this.angle = 360f * random.nextFloat();

        // Random animation
        this.animationType = random.nextInt(ASTEROID_ANIMATIONS_TYPES);
        this.animationSpeed = ASTEROID_ANIMATION_MIN_SPEED + 1
                + random.nextInt(ASTEROID_ANIMATION_MAX_SPEED - ASTEROID_ANIMATION_MIN_SPEED);
        this.animationDirection = random.nextInt(2); // 2 possible rotation directions
    }


    public float getSpeedX() {
        return speedX;
    }

    public float getSpeedY() {
        return speedY;
    }

    // Spawn asteroids away from the player
    public static Asteroid spawn(Player player, Random random) {

        // Random initial position at a fixed distance from the player
        float angle = TAU * random.nextFloat();
        float x = (player.getX() + ASTEROID_SPAWN_DISTANCE * (float) Math.cos(angle));
        float y = (player.getY() + ASTEROID_SPAWN_DISTANCE * (float) Math.sin(angle));

        // Random initial speed towards the player
        float r = 2f * (random.nextFloat() - 0.5f); // between -1 and 1
        float driftAngle = angle + (TAU / 2f) + (r * (TAU / 4f));
        float driftSpeed = ASTEROID_DRIFT_MIN_SPEED
                + random.nextFloat() * (ASTEROID_DRIFT_MAX_SPEED - ASTEROID_DRIFT_MIN_SPEED);
        float speedX = driftSpeed * (float) Math.cos(driftAngle);
        float speedY = driftSpeed * (float) Math.sin(driftAngle);

        // Build and return an asteroid
        return new Asteroid(x, y, speedX, speedY, random);
    }

    public void explode() {
        this.maxAge = age + ASTEROID_EXPLOSION_TIME;
    }

    @Override
    public int getAnimation() {
        int animation = (int) (animationSpeed * age / 1000 % ASTEROID_ANIMATIONS);
        animation = (animationDirection == 0) ? animation : ASTEROID_ANIMATIONS - animation - 1;
        return animation + (ASTEROID_ANIMATIONS * animationType);
    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public float getTransparency() {
        if (isExploding()) {
            return (float) (maxAge - age) / ASTEROID_EXPLOSION_TIME;
        } else {
            return 1f;
        }
    }

    public boolean isExploding() {
        return ((maxAge != 0) && (age < maxAge));
    }

    public boolean hasExploded() {
        return ((maxAge != 0) && (age >= maxAge));
    }

    public boolean isOutOfScope(Player player) {
        return getDistance(player) > ASTEROID_REMOVAL_DISTANCE;
    }

    public void update(long timeSlice) {
        age += timeSlice;
        setX(getX() + speedX * timeSlice / 1000f);
        setY(getY() + speedY * timeSlice / 1000f);
    }
}
