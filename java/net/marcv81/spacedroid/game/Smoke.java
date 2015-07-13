package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.common.Vector2f;
import net.marcv81.spacedroid.graphics.Sprite;
import net.marcv81.spacedroid.physics.*;

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

    private final Drifter drifter;
    private final Decliner decliner;
    private final SmokeSprite sprite;

    /**
     * Constructor.
     */
    public Smoke(Vector2f position, Random random) {

        // Randomise everything
        float angle = TAU * random.nextFloat();
        float norm = SMOKE_MAX_SPEED * random.nextFloat();
        Vector2f speed = (new Vector2f(angle)).multiply(norm);

        // Instantiate the drifter and the decliner
        drifter = new Drifter(position, speed);
        decliner = new Decliner(SMOKE_DURATION);
        sprite = new SmokeSprite(random);
    }

    //
    // Sprite implementation
    //

    public int getAnimationIndex() {
        return sprite.getAnimationIndex();
    }

    public float getAngle() {
        return sprite.getAngle();
    }

    public float getTransparency() {
        return 1f - decliner.getDeclineRatio();
    }

    public float getScale() {
        return 1f + (SMOKE_MAX_SCALE - 1f) * decliner.getDeclineRatio();
    }

    //
    // Updatable implementation
    //

    public void update(long timeSlice) {

        drifter.drift(timeSlice);
        decliner.update(timeSlice);

        sprite.update(timeSlice);
    }

    //
    // Expirable implementation delegation
    //

    public boolean isExpired() {
        return decliner.isExpired();
    }

    //
    // Driftable implementation
    //

    public Vector2f getPosition() {
        return drifter.getPosition();
    }

    public Vector2f getSpeed() {
        return drifter.getSpeed();
    }
}
