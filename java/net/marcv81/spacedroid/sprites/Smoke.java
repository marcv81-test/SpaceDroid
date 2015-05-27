package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

/**
 * This class handles smoke Particles. They have a slow drifting speed, they revolve around
 * themselves, and they expand and disappear relatively quickly. Enough of them can simulate
 * a cloud of smoke.
 */
public final class Smoke implements Sprite, Updatable, Driftable, Expirable {

    protected static final float TAU = 6.2831853071f;

    private static final long SMOKE_DURATION = 650; // 0.65 second
    private static final float SMOKE_MAX_SCALE = 3f;
    private static final float SMOKE_MAX_SPEED = 0.2f;
    private static final float SMOKE_MAX_ANGULAR_RATE = 180f;

    private static final int SMOKE_ANIMATIONS = 4;

    /**
     * Rate at which this Smoke revolves around itself in degrees per second.
     */
    private final float angularRate;

    private final int animationIndex;

    /**
     * Orientation in degrees.
     */
    private float angle;

    private Drifter drifter;
    private Expirer expirer;

    /**
     * Constructor.
     */
    public Smoke(Vector2f position, Random random) {

        // Randomise everything
        float angle = TAU * random.nextFloat();
        float norm = SMOKE_MAX_SPEED * random.nextFloat();
        Vector2f speed = (new Vector2f(angle)).multiply(norm);
        this.angle = 360f * random.nextFloat();
        this.angularRate = SMOKE_MAX_ANGULAR_RATE * 2f * (random.nextFloat() - 0.5f);
        this.animationIndex = random.nextInt(SMOKE_ANIMATIONS);

        // Instantiate the drifter and the expirer
        drifter = new Drifter(position, speed);
        expirer = new Expirer(SMOKE_DURATION);
    }

    public Vector2f getPosition() {
        return drifter.getPosition();
    }

    public int getAnimationIndex() {
        return animationIndex;
    }

    public float getAngle() {
        return angle;
    }

    public float getTransparency() {
        return 1f - expirer.getDeclineRatio();
    }

    public float getScale() {
        return 1f + (SMOKE_MAX_SCALE - 1f) * expirer.getDeclineRatio();
    }

    public boolean isExpired() {
        return expirer.isExpired();
    }

    public void update(long timeSlice) {

        drifter.updatePosition(timeSlice);
        expirer.update(timeSlice);

        // Update the angle according to the angular rate
        angle += angularRate * timeSlice / 1000;
    }

    public Vector2f getSpeed() {
        return drifter.getSpeed();
    }

    public void setSpeed(Vector2f speed) {
        drifter.setSpeed(speed);
    }
}
