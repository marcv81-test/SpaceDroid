package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

public final class Smoke extends Particle {

    private static final long SMOKE_LIFESPAN = 650; // 0.65 second
    private static final float SMOKE_FINAL_SCALE = 3f;
    private static final float SMOKE_DISPERSION = 0.2f;
    private static final int SMOKE_ANIMATIONS = 4;

    private final Vector2f speed;
    private final float startAngle, angleRate;
    private final int animation;

    // Constructor
    public Smoke(Vector2f position, Random random) {

        // Call parent constructor
        super(position);

        // Random initial speed
        float angle = TAU * random.nextFloat();
        float norm = SMOKE_DISPERSION * random.nextFloat();
        this.speed = (new Vector2f(angle)).multiply(norm);

        // Random initial angle and rotation rate
        this.startAngle = 360f * random.nextFloat();
        this.angleRate = 360f * (random.nextFloat() - 0.5f);

        // Random smoke animation
        this.animation = random.nextInt(SMOKE_ANIMATIONS);
    }

    @Override
    public long getLifespan() {
        return SMOKE_LIFESPAN;
    }

    @Override
    public int getAnimationIndex() {
        return animation;
    }

    @Override
    public float getAngle() {
        return startAngle + angleRate * getAge() / 1000;
    }

    @Override
    public float getTransparency() {
        return 1f - getAgePercent();
    }

    @Override
    public float getScale() {
        return 1f + (SMOKE_FINAL_SCALE - 1f) * getAgePercent();
    }

    public void update(long timeSlice) {

        super.update(timeSlice);

        // Update position
        addToPosition((new Vector2f(speed)).multiply(timeSlice / 1000f));
    }
}
