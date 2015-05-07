package net.marcv81.game;

import net.marcv81.gfx2d.Gfx2dView;

import java.util.Random;

public class BonusFactory extends DriftingSpriteFactory<Bonus> {

    private static final float BONUS_MIN_SPEED = 0.1f;
    private static final float BONUS_MAX_SPEED = 0.25f;

    public BonusFactory(Gfx2dView view, Random random) {
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
