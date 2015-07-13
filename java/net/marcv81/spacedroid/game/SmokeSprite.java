package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.physics.Updatable;

import java.util.Random;

/**
 * Delegate class implementing part of the Sprite interface for Smoke objects.
 */
public class SmokeSprite implements Updatable {

    protected static final float TAU = 6.2831853071f;

    /**
     * Orientation in degrees.
     */
    private float angle;

    /**
     * Rate at which this Smoke revolves around itself in degrees per second.
     */
    private final float angularRate;
    private static final float SMOKE_MAX_ANGULAR_RATE = 180f;

    /**
     * Animation index.
     */
    private final int animationIndex;
    private static final int SMOKE_ANIMATIONS = 4;

    public SmokeSprite(Random random) {

        // Random angle
        this.angle = 360f * random.nextFloat();

        // Bounded random angular rate
        this.angularRate = SMOKE_MAX_ANGULAR_RATE * 2f * (random.nextFloat() - 0.5f);

        // Bounded random animation index
        this.animationIndex = random.nextInt(SMOKE_ANIMATIONS);
    }

    public void update(long timeSlice) {

        // Update the angle according to the angular rate
        angle += angularRate * timeSlice / 1000;
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public float getAngle() {
        return angle;
    }
}
