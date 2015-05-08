package net.marcv81.spacedroid.sprites;

import net.marcv81.gfx2d.GameView;

import java.util.Random;

public final class BonusFactory extends DriftingSpriteFactory<Bonus> {

    private static final float BONUS_MIN_SPEED = 0.1f;
    private static final float BONUS_MAX_SPEED = 0.25f;

    public BonusFactory(GameView view, Random random) {
        super(view, random);
    }

    @Override
    protected float getMinSpeed() {
        return BONUS_MIN_SPEED;
    }

    @Override
    protected float getMaxSpeed() {
        return BONUS_MAX_SPEED;
    }

    @Override
    public Bonus create() {
        return new Bonus(randomPosition(), randomSpeed());
    }
}
