package net.marcv81.spacedroid.physics;

import net.marcv81.spacedroid.common.Vector2f;

/**
 * This class provides a partial implementation of Collidable based on Drifter.
 */
public final class Collider extends Drifter {

    /**
     * Radius in game units.
     */
    private final float radius;

    /**
     * Mass in game units.
     */
    private final float mass;

    /**
     * Constructor.
     */
    public Collider(Vector2f position, Vector2f speed, float radius, float mass) {
        super(position, speed);
        this.radius = radius;
        this.mass = mass;
    }

    public float getRadius() {
        return radius;
    }

    public float getMass() {
        return mass;
    }
}
