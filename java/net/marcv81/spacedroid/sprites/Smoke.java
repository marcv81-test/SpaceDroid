package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

/**
 * This class handles smoke Particles. They have a slow drifting speed, they revolve around
 * themselves, and they expand and disappear relatively quickly. Enough of them can simulate
 * a cloud of smoke.
 */
public final class Smoke extends Particle {

    private static final long SMOKE_LIFESPAN = 650; // 0.65 second
    private static final float SMOKE_MAX_SCALE = 3f;
    private static final float SMOKE_MAX_SPEED = 0.2f;
    private static final float SMOKE_MAX_ANGULAR_RATE = 180f;

    private static final int SMOKE_ANIMATIONS = 4;

    /**
     * Speed vector in game units.
     */
    private final Vector2f speed;

    /**
     * Rate at which this Smoke revolves around itself in degrees per second.
     */
    private final float angularRate;

    private final int animationIndex;

    /**
     * Orientation in degrees.
     */
    private float angle;

    /**
     * Constructor.
     */
    public Smoke(Vector2f position, Random random) {

        // Call parent constructor
        super(position);

        // Bounded random initial speed
        float angle = TAU * random.nextFloat();
        float norm = SMOKE_MAX_SPEED * random.nextFloat();
        this.speed = (new Vector2f(angle)).multiply(norm);

        // Random initial angle
        this.angle = 360f * random.nextFloat();

        // Bounded random angular rate
        this.angularRate = SMOKE_MAX_ANGULAR_RATE * 2f * (random.nextFloat() - 0.5f);

        // Random animation index
        this.animationIndex = random.nextInt(SMOKE_ANIMATIONS);
    }

    @Override
    public long getLifespan() {
        return SMOKE_LIFESPAN;
    }

    @Override
    public int getAnimationIndex() {
        return animationIndex;
    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public float getTransparency() {
        return 1f - getAgeRatio();
    }

    @Override
    public float getScale() {
        return 1f + (SMOKE_MAX_SCALE - 1f) * getAgeRatio();
    }

    /**
     * Updates the age, position and angle of this Smoke. Shall unconditionally be called
     * in the game loop.
     *
     * @param timeSlice Game loop time slice duration in milliseconds.
     */
    public void update(long timeSlice) {

        super.update(timeSlice);

        // Update the position according to the speed
        addToPosition((new Vector2f(speed)).multiply(timeSlice / 1000f));

        // Update the angle according to the angular rate
        angle += angularRate * timeSlice / 1000;
    }
}
