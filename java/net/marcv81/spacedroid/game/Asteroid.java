package net.marcv81.spacedroid.game;

import net.marcv81.spacedroid.common.Vector2f;
import net.marcv81.spacedroid.graphics.Sprite;
import net.marcv81.spacedroid.physics.Ager;
import net.marcv81.spacedroid.physics.Collidable;
import net.marcv81.spacedroid.physics.Collider;
import net.marcv81.spacedroid.physics.Updatable;

import java.util.Random;

/**
 * This class handles asteroids. There are 2 animation cycle types, each containing 32 frames.
 * They can be played forward, backward, and at slightly different speeds. Asteroids also have
 * a constant angle and scale.
 */
public final class Asteroid implements Sprite, Updatable, Collidable {

    private static final float ASTEROID_MAX_SCALE = 4f;
    private static final float ASTEROID_MIN_SCALE = 1f;
    private static final float ASTEROID_RADIUS = 0.05f;
    private static final float ASTEROID_MASS = 1f;

    /**
     * Number of frames per animation cycle.
     */
    private static final int ASTEROID_ANIMATION_FRAMES = 32;

    /**
     * Number animation cycle types.
     */
    private static final int ASTEROID_ANIMATION_TYPES = 2;

    /**
     * Minimum animation speed in frames per second.
     */
    private static final int ASTEROID_MIN_ANIMATION_SPEED = 25;

    /**
     * Maximum animation speed in frames per second.
     */
    private static final int ASTEROID_MAX_ANIMATION_SPEED = 30;

    /**
     * Angle in degree.
     */
    private final float angle;

    private final float scale;

    /**
     * Used to select the animation cycle type. Valid values are of the form
     * n * ASTEROID_ANIMATION_FRAMES where n < ASTEROID_ANIMATION_TYPES. This
     * is either 0 or 32.
     */
    private final int animationIndexOffset;

    /**
     * Animation cycle speed in frames per second.
     */
    private final int animationSpeed;

    private final boolean animationBackward;

    private final Collider collider;
    private final Ager ager;

    /**
     * Constructor.
     */
    public Asteroid(Vector2f position, Vector2f speed, Random random) {

        // Randomise everything
        this.angle = 360f * random.nextFloat();
        this.animationIndexOffset = ASTEROID_ANIMATION_FRAMES * random.nextInt(ASTEROID_ANIMATION_TYPES);
        this.animationSpeed = ASTEROID_MIN_ANIMATION_SPEED + 1
                + random.nextInt(ASTEROID_MAX_ANIMATION_SPEED - ASTEROID_MIN_ANIMATION_SPEED);
        this.animationBackward = random.nextBoolean();
        this.scale = (ASTEROID_MAX_SCALE - ASTEROID_MIN_SCALE) * random.nextFloat() + ASTEROID_MIN_SCALE;

        // Mathematically the mass shall be proportional to the cube of the scale.
        // However using the square of the scale makes the game more interesting:
        // smaller asteroids are less likely to go too fast and bigger ones are
        // easier to push around.
        float mass = ASTEROID_MASS * scale * scale;
        float radius = ASTEROID_RADIUS * scale;

        // Instantiate the collider and the ager
        this.collider = new Collider(position, speed, radius, mass);
        this.ager = new Ager();
    }

    //
    // Sprite implementation
    //

    /**
     * Calculates the animation index from the age of this Asteroid.
     */
    public int getAnimationIndex() {
        int animationIndex = (int) (animationSpeed * ager.getAge() / 1000 % ASTEROID_ANIMATION_FRAMES);
        if (animationBackward) {
            animationIndex = ASTEROID_ANIMATION_FRAMES - animationIndex - 1;
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

    //
    // Updatable implementation
    //

    public void update(long timeSlice) {
        collider.update(timeSlice);
        ager.update(timeSlice);
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

    public float getRadius() {
        return collider.getRadius();
    }

    public float getMass() {
        return collider.getMass();
    }

    public boolean isSolid() {
        return true;
    }

    public void deviate(Vector2f speed) {
        collider.deviate(speed);
    }
}
