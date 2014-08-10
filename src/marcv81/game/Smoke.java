package marcv81.game;

import marcv81.gfx2d.Sprite;

import java.util.Random;

public class Smoke extends Sprite {

    private static final long SMOKE_LIFESPAN = 650; // 0.7 second

    private final float speedX, speedY;
    private final float startAngle, angleRate;
    private long age = 0;

    // Constructor
    public Smoke(Random random, float x, float y, float speedX, float speedY) {

        setX(x);
        setY(y);
        this.speedX = speedX;
        this.speedY = speedY;

        this.startAngle = 360f * random.nextFloat();
        this.angleRate = 360f * (random.nextFloat() - 0.5f);
    }

    public float getAngle() {
        return startAngle + angleRate * age / 1000;
    }

    @Override
    public float getTransparency() {
        return 1f - ((float) age / SMOKE_LIFESPAN);
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
