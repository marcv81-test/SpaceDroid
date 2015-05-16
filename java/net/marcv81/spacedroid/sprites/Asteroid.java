package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

/**
 * This class handles asteroids. There are 2 animation cycle types, each containing 32 frames.
 * They can be played forward, backward, and at slightly different speeds. Asteroids also have
 * a constant angle and scale.
 */
public final class Asteroid extends DriftingSprite {

    private static final float ASTEROID_MAX_SCALE = 4f;
    private static final float ASTEROID_MIN_SCALE = 1f;
    private static final float ASTEROID_MASS = 1f;
    private static final float ASTEROID_DIAMETER = 0.10f;

    /**
     * Number of frames per animation cycle.
     */
    private static final int ASTEROID_ANIMATION_FRAMES = 32;

    /**
     * Number animation cycle types.
     */
    private static final int ASTEROID_ANIMATION_TYPES = 2;

    /**
     * Minimum animation speed in frames per second.
     */
    private static final int ASTEROID_MIN_ANIMATION_SPEED = 25;

    /**
     * Maximum animation speed in frames per second.
     */
    private static final int ASTEROID_MAX_ANIMATION_SPEED = 30;

    /**
     * Angle in degree.
     */
    private final float angle;

    private final float scale;

    /**
     * Used to select the animation cycle type. Valid values are of the form
     * n * ASTEROID_ANIMATION_FRAMES where n < ASTEROID_ANIMATION_TYPES. This
     * is either 0 or 32.
     */
    private final int animationIndexOffset;

    /**
     * Animation cycle speed in frames per second.
     */
    private final int animationSpeed;

    private final boolean animationBackward;

    /**
     * Age in milliseconds. Used to determine the appropriate animation index.
     */
    private long age = 0;

    /**
     * Constructor.
     */
    public Asteroid(Vector2f position, Vector2f speed, Random random) {

        // Call parent constructor
        super(position, speed);

        // Randomise everything else
        this.angle = 360f * random.nextFloat();
        this.animationIndexOffset = ASTEROID_ANIMATION_FRAMES * random.nextInt(ASTEROID_ANIMATION_TYPES);
        this.animationSpeed = ASTEROID_MIN_ANIMATION_SPEED + 1
                + random.nextInt(ASTEROID_MAX_ANIMATION_SPEED - ASTEROID_MIN_ANIMATION_SPEED);
        this.animationBackward = random.nextBoolean();
        this.scale = (ASTEROID_MAX_SCALE - ASTEROID_MIN_SCALE) * random.nextFloat() + ASTEROID_MIN_SCALE;
    }

    /**
     * Calculates the animation index from the age of this Asteroid.
     */
    @Override
    public int getAnimationIndex() {
        int animationIndex = (int) (animationSpeed * age / 1000 % ASTEROID_ANIMATION_FRAMES);
        if (animationBackward) {
            animationIndex = ASTEROID_ANIMATION_FRAMES - animationIndex - 1;
        }
        return animationIndex + animationIndexOffset;
    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public float getScale() {
        return scale;
    }

    /**
     * Gets the mass of this Asteroid. Mathematically the mass shall be proportional
     * to the cube of the scale. However using the square of the scale makes the game
     * more interesting: smaller asteroids are less likely to go too fast and bigger
     * ones are easier to push around.
     */
    @Override
    public float getMass() {
        return ASTEROID_MASS * scale * scale;
    }

    @Override
    public float getDiameter() {
        return scale * ASTEROID_DIAMETER;
    }

    /**
     * Updates the position and age of this Asteroid. Shall unconditionally be called
     * in the game loop.
     *
     * @param timeSlice Game loop time slice duration in milliseconds.
     */
    public void update(long timeSlice) {
        age += timeSlice;
        super.update(timeSlice);
    }
}
