package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

/**
 * This class handles sparkles. They have a variable lifespan, and a drift away from their
 * creation position while fading out. Their drift speed reduces progressively to simulate
 * weightlessness. A few are created to provide a visual clue of impacts.
 */
public final class Sparkle implements Sprite, Updatable, Expirable {

    protected static final float TAU = 6.2831853071f;

    private static final float SPARKLE_DRAG = 3.5f;
    private static final float SPARKLE_MIN_SPEED = 0.8f;
    private static final float SPARKLE_MAX_SPEED = 1.2f;
    private static final long SPARKLE_MIN_LIFESPAN = 200;
    private static final long SPARKLE_MAX_LIFESPAN = 600;

    private Drifter drifter;
    private Decliner decliner;

    /**
     * Constructor
     */
    public Sparkle(Vector2f position, Random random) {

        // Randomise everything
        float angle = TAU * random.nextFloat();
        float norm = SPARKLE_MIN_SPEED
                + random.nextFloat() * (SPARKLE_MAX_SPEED - SPARKLE_MIN_SPEED);
        Vector2f speed = (new Vector2f(angle)).multiply(norm);
        long lifespan = random.nextInt((int) SPARKLE_MAX_LIFESPAN);

        // Instantiate the drifter and the decliner
        drifter = new Drifter(position, speed);
        decliner = new Decliner(lifespan, SPARKLE_MIN_LIFESPAN + lifespan);
    }

    //
    // Sprite implementation
    //

    public Vector2f getPosition() {
        return drifter.getPosition();
    }

    public int getAnimationIndex() {
        return 0;
    }

    public float getTransparency() {
        return 1f - decliner.getDeclineRatio();
    }

    public float getScale() {
        return 1f;
    }

    public float getAngle() {
        return 0f;
    }

    //
    // Updatable implementation
    //

    public void update(long timeSlice) {

        drifter.addDrag(SPARKLE_DRAG, timeSlice);

        drifter.update(timeSlice);
        decliner.update(timeSlice);
    }

    //
    // Expirable implementation delegation
    //

    public boolean isExpired() {
        return decliner.isExpired();
    }
}
