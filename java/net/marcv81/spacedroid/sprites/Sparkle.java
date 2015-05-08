package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

public final class Sparkle extends Particle {

    private static final float SPARKLE_FRICTION = 3.5f;
    private static final float SPARKLE_MIN_DISPERSION = 0.8f;
    private static final float SPARKLE_MAX_DISPERSION = 1.2f;
    private static final long SPARKLE_MIN_LIFESPAN = 200;
    private static final long SPARKLE_MAX_LIFESPAN = 800;

    private final Vector2f speed;
    private final long lifespan;

    public Sparkle(Vector2f position, Random random) {

        super(position);

        // Random initial speed
        float angle = TAU * random.nextFloat();
        float norm = SPARKLE_MIN_DISPERSION
                + random.nextFloat() * (SPARKLE_MAX_DISPERSION - SPARKLE_MIN_DISPERSION);
        this.speed = (new Vector2f(angle)).multiply(norm);

        // Random lifespan
        this.lifespan = SPARKLE_MIN_LIFESPAN
                + random.nextInt((int) (SPARKLE_MAX_LIFESPAN - SPARKLE_MIN_LIFESPAN));
    }

    @Override
    public long getLifespan() {
        return lifespan;
    }

    @Override
    public float getTransparency() {
        float percent = getAgePercent();
        if (percent < 0.5f) {
            return 1f;
        } else {
            return 1f - 2f * (percent - 0.5f);
        }
    }

    public void update(long timeSlice) {

        super.update(timeSlice);

        // Apply friction
        speed.multiply(1f - SPARKLE_FRICTION * timeSlice / 1000f);

        // Update position
        addToPosition((new Vector2f(speed)).multiply(timeSlice / 1000f));
    }
}
