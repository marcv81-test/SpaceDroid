package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;

/**
 * This class handles Sprites with a set lifespan and used for particles effects.
 */
public abstract class Particle extends Sprite {

    /**
     * Age in milliseconds.
     */
    private long age = 0;

    /**
     * Constructor.
     */
    public Particle(Vector2f position) {
        super(position);
    }

    /**
     * Gets the lifespan of this Particle. To be overridden by the implementing classes.
     *
     * @return Lifespan of this Particle in milliseconds.
     */
    public abstract long getLifespan();

    /**
     * @return Age of this Particle  in milliseconds.
     */
    public long getAge() {
        return age;
    }

    /**
     * @return Age of this Particle as a ratio of its lifespan, between 0f and 1f.
     */
    public float getAgeRatio() {
        return (float) age / getLifespan();
    }

    /**
     * Checks whether this Particle has exceeded its lifespan or not. The game engine shall
     * dispose of expired Particles.
     */
    public boolean isExpired() {
        return age >= getLifespan();
    }

    /**
     * Updates the age of this Particle. Shall unconditionally be called in the game loop.
     *
     * @param timeSlice Game loop time slice duration in milliseconds.
     */
    public void update(long timeSlice) {
        age += timeSlice;
    }
}
