package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

/**
 * This class handles sparkles. They have a variable lifespan, and a drift away from their
 * creation position while fading out. Their drift speed reduces progressively to simulate
 * weightlessness. A few are created to provide a visual clue of impacts.
 */
public final class Sparkle extends Particle {

    /**
     * Speed loss, in ratio of the original speed per second.
     */
    private static final float SPARKLE_SPEED_LOSS = 3.5f;

    private static final float SPARKLE_MIN_SPEED = 0.8f;
    private static final float SPARKLE_MAX_SPEED = 1.2f;
    private static final long SPARKLE_MIN_LIFESPAN = 200;
    private static final long SPARKLE_MAX_LIFESPAN = 800;

    /**
     * Speed vector in game units.
     */
    private final Vector2f speed;

    /**
     * Lifespan in milliseconds.
     */
    private final long lifespan;

    /**
     * Constructor
     */
    public Sparkle(Vector2f position, Random random) {

        super(position);

        // Bounded random initial speed
        float angle = TAU * random.nextFloat();
        float norm = SPARKLE_MIN_SPEED
                + random.nextFloat() * (SPARKLE_MAX_SPEED - SPARKLE_MIN_SPEED);
        this.speed = (new Vector2f(angle)).multiply(norm);

        // Bounded random lifespan
        this.lifespan = SPARKLE_MIN_LIFESPAN
                + random.nextInt((int) (SPARKLE_MAX_LIFESPAN - SPARKLE_MIN_LIFESPAN));
    }

    @Override
    public long getLifespan() {
        return lifespan;
    }

    @Override
    public float getTransparency() {

        float ratio = getAgeRatio();

        // Do not fade during the first half of the lifespan
        if (ratio < 0.5f) {
            return 1f;
        }

        // Fade off during the second half of the lifespan
        else {
            return 1f - 2f * (ratio - 0.5f);
        }
    }

    /**
     * Updates the age, speed and position of this Sparkle. Shall unconditionally be called
     * in the game loop.
     *
     * @param timeSlice Game loop time slice duration in milliseconds.
     */
    public void update(long timeSlice) {

        super.update(timeSlice);

        // Apply the speed loss
        speed.multiply(1f - SPARKLE_SPEED_LOSS * timeSlice / 1000f);

        // Update the position according to the speed
        addToPosition((new Vector2f(speed)).multiply(timeSlice / 1000f));
    }
}
