package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.common.Vector2f;
import net.marcv81.spacedroid.graphics.Sprite;
import net.marcv81.spacedroid.physics.Collidable;
import net.marcv81.spacedroid.physics.Drifter;
import net.marcv81.spacedroid.physics.Updatable;

import java.util.Random;

public final class Asteroid implements Sprite, Updatable, Collidable {

    private static final float ASTEROID_RADIUS = 0.05f;
    private static final float ASTEROID_MASS = 1f;

    private final float radius;
    private final float mass;

    private final Drifter drifter;
    private final AsteroidSprite sprite;

    /**
     * Constructor.
     */
    public Asteroid(Vector2f position, Vector2f speed, Random random) {

        drifter = new Drifter(position, speed);
        sprite = new AsteroidSprite(random);

        // Mathematically the mass shall be proportional to the cube of the scale.
        // However using the square of the scale makes the game more interesting:
        // smaller asteroids are less likely to go too fast and bigger ones are
        // easier to push around.
        float scale = sprite.getScale();
        this.mass = ASTEROID_MASS * scale * scale;
        this.radius = ASTEROID_RADIUS * scale;
    }

    //
    // Sprite implementation delegation
    //

    public int getAnimationIndex() {
        return sprite.getAnimationIndex();
    }

    public float getTransparency() {
        return sprite.getTransparency();
    }

    public float getAngle() {
        return sprite.getAngle();
    }

    public float getScale() {
        return sprite.getScale();
    }

    //
    // Updatable implementation
    //

    public void update(long timeSlice) {
        drifter.drift(timeSlice);
        sprite.update(timeSlice);
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
        return true;
    }

    public void collide(Class c, Vector2f v) {
        drifter.collide(v);
    }
}
