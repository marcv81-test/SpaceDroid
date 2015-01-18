package net.marcv81.game;

import net.marcv81.gfx2d.Renderer;

import java.util.Random;

public class AsteroidFactory extends DriftingSpriteFactory {

    private static final float ASTEROID_MIN_SPEED = 0.1f;
    private static final float ASTEROID_MAX_SPEED = 0.5f;

    public AsteroidFactory(Renderer renderer, Random random) {
        super(renderer, random);
    }

    @Override
    protected float getMinSpeed() {
        return ASTEROID_MIN_SPEED;
    }

    @Override
    protected float getMaxSpeed() {
        return ASTEROID_MAX_SPEED;
    }

    public Asteroid create() {
        return new Asteroid(randomPosition(), randomSpeed(), random);
    }
}
