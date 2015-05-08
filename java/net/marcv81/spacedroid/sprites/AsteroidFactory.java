package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.GameView;

import java.util.Random;

public final class AsteroidFactory extends DriftingSpriteFactory<Asteroid> {

    private static final float ASTEROID_MIN_SPEED = 0.1f;
    private static final float ASTEROID_MAX_SPEED = 0.5f;

    public AsteroidFactory(GameView view, Random random) {
        super(view, random);
    }

    @Override
    protected float getMinSpeed() {
        return ASTEROID_MIN_SPEED;
    }

    @Override
    protected float getMaxSpeed() {
        return ASTEROID_MAX_SPEED;
    }

    @Override
    public Asteroid create() {
        return new Asteroid(randomPosition(), randomSpeed(), random);
    }
}
