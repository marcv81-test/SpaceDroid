package marcv81.game;

import marcv81.gfx2d.Particle;

import java.util.Random;

public class Smoke extends Particle {

    private static final long SMOKE_LIFESPAN = 650; // 0.65 second
    private static final float SMOKE_FINAL_SCALE = 3f;
    private static final float SMOKE_DISPERSION = 0.2f;
    private static final int SMOKE_ANIMATIONS = 4;

    private final float speedX, speedY;
    private final float startAngle, angleRate;
    private final int animation;

    // Constructor
    public Smoke(float x, float y, Random random) {

        // Call parent constructor
        super(x, y);

        // Random initial speed
        float angle = TAU * random.nextFloat();
        float norm = SMOKE_DISPERSION * random.nextFloat();
        this.speedX = norm * (float) Math.cos(angle);
        this.speedY = norm * (float) Math.cos(angle);

        // Random initial angle and rotation rate
        this.startAngle = 360f * random.nextFloat();
        this.angleRate = 360f * (random.nextFloat() - 0.5f);

        // Random smoke animation
        this.animation = random.nextInt(SMOKE_ANIMATIONS);
    }

    @Override
    public long getLifespan() {
        return SMOKE_LIFESPAN;
    }

    @Override
    public int getAnimation() {
        return animation;
    }

    @Override
    public float getAngle() {
        return startAngle + angleRate * getAge() / 1000;
    }

    @Override
    public float getTransparency() {
        return 1f - getAgePercent();
    }

    @Override
    public float getScale() {
        return 1f + (SMOKE_FINAL_SCALE - 1f) * getAgePercent();
    }

    public void update(long timeSlice) {

        super.update(timeSlice);

        // Update position
        setX(getX() + speedX * timeSlice / 1000);
        setY(getY() + speedY * timeSlice / 1000);
    }
}
