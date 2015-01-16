package net.marcv81.game;

import net.marcv81.gfx2d.Renderer;
import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

public class Bonus extends DriftingSprite {

    private static final float BONUS_MASS = 1f;
    private static final float BONUS_DIAMETER = 0.12f;
    private static final float BONUS_REMOVAL_DISTANCE = 0.75f;
    private static final float BONUS_SPAWN_DISTANCE = 0.5f;
    private static final float BONUS_DRIFT_MIN_SPEED = 0.1f;
    private static final float BONUS_DRIFT_MAX_SPEED = 0.25f;

    private long age = 0;
    private long lifespan = 0;

    // Constructor
    public Bonus(Vector2f position, Vector2f speed) {

        // Call parent constructor
        super(position, speed);
    }

    // Spawn bonuses at the edge of the play area
    public static Bonus spawn(Renderer renderer, Random random) {

        // Random initial position
        Vector2f position;
        switch (random.nextInt(4)) {

            // Right edge
            case 0:
                position = new Vector2f(
                        renderer.getRight() + BONUS_SPAWN_DISTANCE,
                        renderer.getBottom() + random.nextFloat()
                                * (renderer.getTop() - renderer.getBottom()));
                break;

            // Left edge
            case 1:
                position = new Vector2f(
                        renderer.getLeft() - BONUS_SPAWN_DISTANCE,
                        renderer.getBottom() + random.nextFloat()
                                * (renderer.getTop() - renderer.getBottom()));
                break;

            // Top edge
            case 2:
                position = new Vector2f(
                        renderer.getRight() + random.nextFloat()
                                * (renderer.getLeft() - renderer.getRight()),
                        renderer.getTop() + BONUS_SPAWN_DISTANCE);
                break;

            // Bottom edge
            default:
                position = new Vector2f(
                        renderer.getRight() + random.nextFloat()
                                * (renderer.getLeft() - renderer.getRight()),
                        renderer.getBottom() - BONUS_SPAWN_DISTANCE);
                break;
        }

        // Random initial speed
        float driftAngle = TAU * random.nextFloat();
        float driftSpeed = BONUS_DRIFT_MIN_SPEED + random.nextFloat()
                * (BONUS_DRIFT_MAX_SPEED - BONUS_DRIFT_MIN_SPEED);
        Vector2f speed = (new Vector2f(driftAngle)).multiply(driftSpeed);

        // Return a new bonus
        return new Bonus(position, speed);
    }

    @Override
    public float getMass() {
        return BONUS_MASS;
    }

    @Override
    public float getDiameter() {
        return BONUS_DIAMETER;
    }

    @Override
    public float getTransparency() {
        if(!isExploding()) {
            return 1f;
        } else {
            return (lifespan - age) / 500f;
        }
    }

    @Override
    public float getScale() {
        if(!isExploding()) {
            return 1f;
        } else {
            return 4f - 3f * (lifespan - age) / 500f;
        }
    }

    public boolean isOutOfScope(Renderer renderer) {
        Vector2f position = getPosition();
        return position.getY() >= renderer.getTop() + BONUS_REMOVAL_DISTANCE
                || position.getY() <= renderer.getBottom() - BONUS_REMOVAL_DISTANCE
                || position.getX() >= renderer.getRight() + BONUS_REMOVAL_DISTANCE
                || position.getX() <= renderer.getLeft() - BONUS_REMOVAL_DISTANCE;
    }

    public long getAge() {
        return age;
    }

    public void explode() {
        this.lifespan = age + 500;
    }

    public boolean isExploding() {
        return lifespan > 0;
    }

    public boolean isExpired() {
        return isExploding() && (age >= lifespan);
    }

    public void update(long timeSlice) {
        super.update(timeSlice);
        age += timeSlice;
    }
}
