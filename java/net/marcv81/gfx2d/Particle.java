package net.marcv81.gfx2d;

// Particles are sprites with a fixed lifespan
public abstract class Particle extends Sprite {

    private long age = 0;

    // Constructor
    public Particle(Vector2f position) {
        super(position);
    }

    // To be overridden in the particle implementation
    public abstract long getLifespan();

    public long getAge() {
        return age;
    }

    public float getAgePercent() {
        return (float) age / getLifespan();
    }

    public boolean isExpired() {
        return age >= getLifespan();
    }

    public void update(long timeSlice) {
        age += timeSlice;
    }
}
