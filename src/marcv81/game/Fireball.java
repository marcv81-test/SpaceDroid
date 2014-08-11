package marcv81.game;

import marcv81.gfx2d.Particle;

import java.util.Random;

public class Fireball extends Particle {

    private static final long FIREBALL_LIFESPAN = 650; // 0.65 second
    private static final long FIREBALL_ANIMATIONS = 16;

    private final float speedX, speedY;
    private final float startAngle, angleRate;

    // Constructor
    public Fireball(float x, float y, float speedX, float speedY, Random random) {

        // Call parent constructor
        super(x, y);

        // Set speed
        this.speedX = speedX;
        this.speedY = speedY;

        // Random initial angle and rotation rate
        this.startAngle = 360f * random.nextFloat();
        this.angleRate = 360f * (random.nextFloat() - 0.5f);
    }

    @Override
    public long getLifespan() {
        return FIREBALL_LIFESPAN;
    }

    @Override
    public int getAnimation() {
        return (int) (FIREBALL_ANIMATIONS * getAge() / FIREBALL_LIFESPAN);
    }

    // Return the fireball drawing angle
    public float getAngle() {
        return startAngle + angleRate * getAge() / 1000;
    }

    public void update(long timeSlice) {

        super.update(timeSlice);

        // Update position
        setX(getX() + speedX * timeSlice / 1000);
        setY(getY() + speedY * timeSlice / 1000);
    }
}
