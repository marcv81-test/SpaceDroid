package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.Vector2f;

public final class Bonus extends DriftingSprite {

    private static final float BONUS_MASS = 1f;
    private static final float BONUS_DIAMETER = 0.12f;

    private long age = 0;
    private long lifespan = 0;

    // Constructor
    public Bonus(Vector2f position, Vector2f speed) {
        super(position, speed);
    }

    @Override
    public float getMass() {
        return BONUS_MASS;
    }

    @Override
    public float getDiameter() {
        return BONUS_DIAMETER;
    }

    @Override
    public float getTransparency() {
        if(!isExploding()) {
            return 1f;
        } else {
            return (lifespan - age) / 500f;
        }
    }

    @Override
    public float getScale() {
        if(!isExploding()) {
            return 1f;
        } else {
            return 4f - 3f * (lifespan - age) / 500f;
        }
    }

    public long getAge() {
        return age;
    }

    public void explode() {
        this.lifespan = age + 500;
    }

    public boolean isExploding() {
        return lifespan > 0;
    }

    public boolean isExpired() {
        return isExploding() && (age >= lifespan);
    }

    public void update(long timeSlice) {
        super.update(timeSlice);
        age += timeSlice;
    }
}
