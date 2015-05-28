package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

/**
 * This class handles sparkles. They have a variable lifespan, and a drift away from their
 * creation position while fading out. Their drift speed reduces progressively to simulate
 * weightlessness. A few are created to provide a visual clue of impacts.
 */
public final class Sparkle implements Sprite, Updatable, Driftable, Expirable {

    protected static final float TAU = 6.2831853071f;

    private static final float SPARKLE_DRAG = 3.5f;
    private static final float SPARKLE_MIN_SPEED = 0.8f;
    private static final float SPARKLE_MAX_SPEED = 1.2f;
    private static final long SPARKLE_MIN_LIFESPAN = 200;
    private static final long SPARKLE_MAX_LIFESPAN = 600;

    private Drifter drifter;
    private Expirer expirer;

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

        // Instantiate the drifter and the expirer
        drifter = new Drifter(position, speed);
        expirer = new Expirer(lifespan, SPARKLE_MIN_LIFESPAN + lifespan);
    }

    public int getAnimationIndex() {
        return 0;
    }

    public float getTransparency() {
        return 1f - expirer.getDeclineRatio();
    }

    public float getScale() {
        return 1f;
    }

    public float getAngle() {
        return 0f;
    }

    public boolean isExpired() {
        return expirer.isExpired();
    }

    public void update(long timeSlice) {
        drifter.updateDrag(SPARKLE_DRAG, timeSlice);
        drifter.updatePosition(timeSlice);
        expirer.update(timeSlice);
    }

    //
    // Driftable implementation delegation
    //

    public Vector2f getPosition() {
        return drifter.getPosition();
    }

    public Vector2f getSpeed() {
        return drifter.getSpeed();
    }

    public void setSpeed(Vector2f speed) {
        drifter.setSpeed(speed);
    }
}
