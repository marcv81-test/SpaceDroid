package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Sprite;
import net.marcv81.gfx2d.Vector2f;

/**
 * This class handles collectible bonuses.
 */
public final class Bonus implements Sprite, Updatable, Collidable, Expirable {

    private static final float BONUS_RADIUS = 0.06f;
    private static final float BONUS_MASS = 1f;

    private static final long BONUS_EXPIRATION_DURATION = 500;
    private static final float BONUS_EXPIRATION_SCALE = 4f;

    public Collider collider;
    public Expirer expirer;

    private boolean solid = true;

    public Bonus(Vector2f position, Vector2f speed) {
        this.collider = new Collider(position, speed, BONUS_RADIUS, BONUS_MASS);
        this.expirer = new Expirer();
    }

    public int getAnimationIndex() {
        return 0;
    }

    public float getAngle() {
        return 0;
    }

    /**
     * Opaque when living, fading out when expiring.
     */
    public float getTransparency() {
        return 1f - expirer.getDeclineRatio();
    }

    /**
     * 1f when living, getting bigger when expiring.
     */
    public float getScale() {
        return 1f + (BONUS_EXPIRATION_SCALE - 1f) * expirer.getDeclineRatio();
    }

    public void collect() {
        expirer.decline(BONUS_EXPIRATION_DURATION);
        solid = false;
    }

    public boolean isExpired() {
        return expirer.isExpired();
    }

    public void update(long timeSlice) {
        collider.updatePosition(timeSlice);
        expirer.update(timeSlice);
    }

    //
    // Collidable implementation delegation
    //

    public Vector2f getPosition() {
        return collider.getPosition();
    }

    public Vector2f getSpeed() {
        return collider.getSpeed();
    }

    public void setSpeed(Vector2f speed) {
        collider.setSpeed(speed);
    }

    public boolean overlaps(Collidable that) {
        return collider.overlaps(that);
    }

    public boolean collides(Collidable that) {
        return solid && collider.collides(that);
    }

    public Vector2f collisionPoint(Collidable that) {
        return collider.collisionPoint(that);
    }

    public float getRadius() {
        return collider.getRadius();
    }

    public float getMass() {
        return collider.getMass();
    }
}
