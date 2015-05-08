package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

import java.util.Random;

public final class Asteroid extends DriftingSprite {

    private static final int ASTEROID_ANIMATIONS = 32;
    private static final int ASTEROID_ANIMATIONS_TYPES = 2;
    private static final int ASTEROID_ANIMATION_MIN_SPEED = 25;
    private static final int ASTEROID_ANIMATION_MAX_SPEED = 30;
    private static final float ASTEROID_DIAMETER = 0.10f;

    private final float angle;
    private final float scale;
    private final int animationType;
    private final int animationSpeed;
    private final int animationDirection;
    private long age = 0;

    // Constructor
    public Asteroid(Vector2f position, Vector2f speed, Random random) {

        // Call parent constructor
        super(position, speed);

        // Random initial angle
        this.angle = 360f * random.nextFloat();

        // Random animation
        this.animationType = random.nextInt(ASTEROID_ANIMATIONS_TYPES);
        this.animationSpeed = ASTEROID_ANIMATION_MIN_SPEED + 1
                + random.nextInt(ASTEROID_ANIMATION_MAX_SPEED - ASTEROID_ANIMATION_MIN_SPEED);
        this.animationDirection = random.nextInt(2); // 2 possible rotation directions

        // Random scale between 1 and 4
        this.scale = 3f * random.nextFloat() + 1f;
    }

    @Override
    public int getAnimation() {
        int animation = (int) (animationSpeed * age / 1000 % ASTEROID_ANIMATIONS);
        animation = (animationDirection == 0) ? animation : ASTEROID_ANIMATIONS - animation - 1;
        return animation + (ASTEROID_ANIMATIONS * animationType);
    }

    @Override
    public float getAngle() {
        return angle;
    }

    @Override
    public float getScale() {
        return scale;
    }

    @Override
    public float getMass() {
        return scale * scale;
    }

    @Override
    public float getDiameter() {
        return scale * ASTEROID_DIAMETER;
    }

    public void update(long timeSlice) {
        age += timeSlice;
        super.update(timeSlice);
    }
}
