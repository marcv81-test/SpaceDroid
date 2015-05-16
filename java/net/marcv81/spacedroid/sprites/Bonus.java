package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

/**
 * This class handles bonuses. When collected they fade out and grow for 500 milliseconds.
 */
public final class Bonus extends DriftingSprite {

    private static final float BONUS_MASS = 1f;
    private static final float BONUS_DIAMETER = 0.12f;

    private static final long BONUS_ANIMATION_DURATION = 500;
    private static final float BONUS_ANIMATION_MAX_SCALE = 4f;

    /**
     * Age in milliseconds. Used to render the Bonus collection animation.
     */
    private long age = 0;

    /**
     * Lifespan in milliseconds. Used to render the Bonus collection animation.
     * Remains set to 0 until this Bonus is collected.
     */
    private long lifespan = 0;

    /**
     * Constructor.
     */
    public Bonus(Vector2f position, Vector2f speed) {
        super(position, speed);
    }

    @Override
    public float getMass() {
        return BONUS_MASS;
    }

    @Override
    public float getDiameter() {
        return BONUS_DIAMETER;
    }

    /**
     * Gets the transparency of this Bonus. Usually opaque but fading out when collected.
     */
    @Override
    public float getTransparency() {
        if (!hasBeenCollected()) {
            return 1f;
        } else {
            return (lifespan - age) / (float) BONUS_ANIMATION_DURATION;
        }
    }

    /**
     * Gets the scale of this Bonus. Usually 1f but getting bigger when collected.
     */
    @Override
    public float getScale() {
        if (!hasBeenCollected()) {
            return 1f;
        } else {
            return BONUS_ANIMATION_MAX_SCALE - ((BONUS_ANIMATION_MAX_SCALE - 1f)
                    * (lifespan - age) / (float) BONUS_ANIMATION_DURATION);
        }
    }

    /**
     * Collects this Bonus. Sets the lifespan, which triggers the Bonus collection animation.
     */
    public void collect() {
        if (!hasBeenCollected()) {
            lifespan = age + BONUS_ANIMATION_DURATION;
        }
    }

    /**
     * Checks whether this Bonus has already been collected or not yet.
     */
    public boolean hasBeenCollected() {
        return lifespan > 0;
    }

    /**
     * Checks whether this Bonus has exceeded its lifespan or not. The game engine shall dispose
     * of expired Bonuses.
     */
    public boolean isExpired() {
        return (lifespan > 0) && (age >= lifespan);
    }

    /**
     * Updates the position and age of this Bonus. Shall unconditionally be called
     * in the game loop.
     *
     * @param timeSlice Game loop time slice duration in milliseconds.
     */
    public void update(long timeSlice) {
        super.update(timeSlice);
        age += timeSlice;
    }
}
