package marcv81.game;

import marcv81.gfx2d.Sprite;

import java.util.Random;

public class Smoke extends Sprite {

    private static final long SMOKE_LIFESPAN = 650; // 0.65 second
    private static final float SMOKE_FINAL_SCALE = 3f;
    private static final float SMOKE_DISPERSION = 0.2f;
    private static final int SMOKE_ANIMATIONS = 4;

    private final float speedX, speedY;
    private final float startAngle, angleRate;
    private final int animation;
    private long age = 0;

    // Constructor
    public Smoke(Random random, float x, float y) {

        setX(x);
        setY(y);

        // Random initial speed
        float angle = TAU * random.nextFloat();
        float norm = SMOKE_DISPERSION * random.nextFloat();
        this.speedX = norm * (float) Math.cos(angle);
        this.speedY = norm * (float) Math.cos(angle);

        // Random initial angle and rotation
        this.startAngle = 360f * random.nextFloat();
        this.angleRate = 360f * (random.nextFloat() - 0.5f);

        // Random smoke animation
        this.animation = random.nextInt(SMOKE_ANIMATIONS);
    }

    @Override
    public int getAnimation() {
        return animation;
    }

    @Override
    public float getAngle() {
        return startAngle + angleRate * age / 1000;
    }

    @Override
    public float getTransparency() {
        return 1f - ((float) age / SMOKE_LIFESPAN);
    }

    @Override
    public float getScale() {
        return 1f + (SMOKE_FINAL_SCALE - 1f) * ((float) age / SMOKE_LIFESPAN);
    }

    public boolean isExpired() {
        return age >= SMOKE_LIFESPAN;
    }

    public void update(long timeSlice) {
        setX(getX() + speedX * timeSlice / 1000);
        setY(getY() + speedY * timeSlice / 1000);
        age += timeSlice;
    }
}
