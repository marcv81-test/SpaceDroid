package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.physics.Ager;
import net.marcv81.spacedroid.physics.Updatable;

import java.util.Random;

/**
 * Delegate class implementing most of the Sprite interface for Asteroid objects.
 */
public class AsteroidSprite implements Updatable {

    /**
     * Angle in degrees.
     */
    private final float angle;

    /**
     * Scale.
     */
    private final float scale;
    private static final float MIN_SCALE = 1f;
    private static final float MAX_SCALE = 4f;

    /**
     * Animation speed in frames per second.
     */
    private final int animationSpeed;
    private static final int MIN_ANIMATION_SPEED = 25;
    private static final int MAX_ANIMATION_SPEED = 30;

    /**
     * Whether the animation plays forward or backward.
     */
    private final boolean animationBackward;

    /**
     * Used to select the animation type. Valid values are of the form
     * n * ANIMATION_FRAMES_COUNT where n < ANIMATION_TYPES_COUNT. This
     * is either 0 or 32.
     */
    private final int animationIndexOffset;
    private static final int ANIMATION_FRAMES_COUNT = 32;
    private static final int ANIMATION_TYPES_COUNT = 2;

    /**
     * Age.
     */
    private final Ager ager = new Ager();

    public AsteroidSprite(Random random) {

        // Random angle
        this.angle = 360f * random.nextFloat();

        // Bounded random scale
        this.scale = (MAX_SCALE - MIN_SCALE) * random.nextFloat() + MIN_SCALE;

        // Bounded random animation speed
        this.animationSpeed = MIN_ANIMATION_SPEED
                + random.nextInt(MAX_ANIMATION_SPEED - MIN_ANIMATION_SPEED + 1);

        // Random animation direction
        this.animationBackward = random.nextBoolean();

        // Random animation index offset
        this.animationIndexOffset = ANIMATION_FRAMES_COUNT * random.nextInt(ANIMATION_TYPES_COUNT);
    }

    public void update(long timeSlice) {
        ager.update(timeSlice);
    }

    /**
     * Calculates animation index from age.
     */
    public int getAnimationIndex() {
        int animationIndex = (int) (animationSpeed * ager.getAge() / 1000 % ANIMATION_FRAMES_COUNT);
        if (animationBackward) {
            animationIndex = ANIMATION_FRAMES_COUNT - animationIndex - 1;
        }
        return animationIndex + animationIndexOffset;
    }

    public float getTransparency() {
        return 1f;
    }

    public float getAngle() {
        return angle;
    }

    public float getScale() {
        return scale;
    }
}
