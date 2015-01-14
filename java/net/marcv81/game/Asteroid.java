package net.marcv81.game;

import net.marcv81.gfx2d.Renderer;
import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

class Asteroid extends DriftingSprite {

    private static final int ASTEROID_ANIMATIONS = 32;
    private static final int ASTEROID_ANIMATIONS_TYPES = 2;
    private static final float ASTEROID_SPAWN_DISTANCE = 0.5f;
    private static final float ASTEROID_REMOVAL_DISTANCE = 0.75f;
    private static final float ASTEROID_DRIFT_MIN_SPEED = 0.1f;
    private static final float ASTEROID_DRIFT_MAX_SPEED = 0.5f;
    private static final int ASTEROID_ANIMATION_MIN_SPEED = 25;
    private static final int ASTEROID_ANIMATION_MAX_SPEED = 30;
    private static final float ASTEROID_DIAMETER = 0.10f;

    private final float angle;
    private final float scale;
    private final int animationType;
    private final int animationSpeed;
    private final int animationDirection;
    private long age = 0;

    // Constructor
    public Asteroid(Vector2f position, Vector2f speed, Random random) {

        // Call parent constructor
        super(position, speed);

        // Random initial angle
        this.angle = 360f * random.nextFloat();

        // Random animation
        this.animationType = random.nextInt(ASTEROID_ANIMATIONS_TYPES);
        this.animationSpeed = ASTEROID_ANIMATION_MIN_SPEED + 1
                + random.nextInt(ASTEROID_ANIMATION_MAX_SPEED - ASTEROID_ANIMATION_MIN_SPEED);
        this.animationDirection = random.nextInt(2); // 2 possible rotation directions

        // Random scale between 1 and 4
        this.scale = 3f * random.nextFloat() + 1f;
    }

    // Spawn asteroids at the edge of the play area
    public static Asteroid spawn(Renderer renderer, Random random) {

        // Random initial position
        Vector2f position;
        switch (random.nextInt(4)) {

            // Right edge
            case 0:
                position = new Vector2f(
                        renderer.getRight() + ASTEROID_SPAWN_DISTANCE,
                        renderer.getBottom() + random.nextFloat()
                                * (renderer.getTop() - renderer.getBottom()));
                break;

            // Left edge
            case 1:
                position = new Vector2f(
                        renderer.getLeft() - ASTEROID_SPAWN_DISTANCE,
                        renderer.getBottom() + random.nextFloat()
                                * (renderer.getTop() - renderer.getBottom()));
                break;

            // Top edge
            case 2:
                position = new Vector2f(
                        renderer.getRight() + random.nextFloat()
                                * (renderer.getLeft() - renderer.getRight()),
                        renderer.getTop() + ASTEROID_SPAWN_DISTANCE);
                break;

            // Bottom edge
            default:
                position = new Vector2f(
                        renderer.getRight() + random.nextFloat()
                                * (renderer.getLeft() - renderer.getRight()),
                        renderer.getBottom() - ASTEROID_SPAWN_DISTANCE);
                break;
        }

        // Random initial speed
        float driftAngle = TAU * random.nextFloat();
        float driftSpeed = ASTEROID_DRIFT_MIN_SPEED + random.nextFloat()
                * (ASTEROID_DRIFT_MAX_SPEED - ASTEROID_DRIFT_MIN_SPEED);
        Vector2f speed = (new Vector2f(driftAngle)).multiply(driftSpeed);

        // Return a new asteroid
        return new Asteroid(position, speed, random);
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
    public float getScale() {
        return scale;
    }

    @Override
    public float getMass() {
        return scale * scale;
    }

    @Override
    public float getDiameter() {
        return scale * ASTEROID_DIAMETER;
    }

    public boolean isOutOfScope(Renderer renderer) {
        Vector2f position = getPosition();
        return position.y >= renderer.getTop() + ASTEROID_REMOVAL_DISTANCE
                || position.y <= renderer.getBottom() - ASTEROID_REMOVAL_DISTANCE
                || position.x >= renderer.getRight() + ASTEROID_REMOVAL_DISTANCE
                || position.x <= renderer.getLeft() - ASTEROID_REMOVAL_DISTANCE;
    }

    public void update(long timeSlice) {
        age += timeSlice;
        super.update(timeSlice);
    }
}
