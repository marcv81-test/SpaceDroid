package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.common.Vector2f;
import net.marcv81.spacedroid.graphics.Sprite;
import net.marcv81.spacedroid.physics.*;

/**
 * This class handles collectible bonuses.
 */
public final class Bonus implements Sprite, Updatable, Expirable, Collidable {

    private static final float BONUS_RADIUS = 0.06f;
    private static final float BONUS_MASS = 1f;

    private static final long BONUS_EXPIRATION_DURATION = 500;
    private static final float BONUS_EXPIRATION_SCALE = 4f;

    private final Drifter drifter;
    private final Decliner decliner;

    private final float radius;
    private final float mass;

    /**
     * Constructor.
     */
    public Bonus(Vector2f position, Vector2f speed) {
        drifter = new Drifter(position, speed);
        this.radius = BONUS_RADIUS;
        this.mass = BONUS_MASS;
        this.decliner = new Decliner();
    }

    /**
     * Collect this bonus.
     */
    public void collect() {
        decliner.decline(BONUS_EXPIRATION_DURATION);
    }

    //
    // Sprite implementation
    //

    public int getAnimationIndex() {
        return 0;
    }

    public float getAngle() {
        return 0;
    }

    /**
     * Usually opaque, fading out while expiring.
     */
    public float getTransparency() {
        return 1f - decliner.getDeclineRatio();
    }

    /**
     * Usually 1x, getting bigger while expiring.
     */
    public float getScale() {
        return 1f + (BONUS_EXPIRATION_SCALE - 1f) * decliner.getDeclineRatio();
    }

    //
    // Updatable implementation
    //

    public void update(long timeSlice) {
        drifter.drift(timeSlice);
        decliner.update(timeSlice);
    }

    //
    // Expirable implementation delegation
    //

    public boolean isExpired() {
        return decliner.isExpired();
    }

    //
    // Collidable implementation
    //

    public Vector2f getPosition() {
        return drifter.getPosition();
    }

    public Vector2f getSpeed() {
        return drifter.getSpeed();
    }

    public float getRadius() {
        return radius;
    }

    public float getMass() {
        return mass;
    }

    public boolean isSolid() {
        return !decliner.isDeclining() && !decliner.isExpired();
    }

    public void collide(Class c, Vector2f v) {
        if (c == Player.class) {
            this.collect();
        } else {
            drifter.collide(v);
        }
    }
}
