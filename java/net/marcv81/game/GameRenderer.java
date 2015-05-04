package net.marcv81.game;

import android.content.Context;
import net.marcv81.gfx2d.*;

import javax.microedition.khronos.opengles.GL10;
import java.util.Iterator;
import java.util.Random;

import android.os.Vibrator;

class GameRenderer extends Renderer {

    private static final float BACKGROUND_SIZE = 4f;
    private static final int SPARKLES_PER_IMPACT = 5;
    private static final int ASTEROID_MAX_COUNT = 20;
    private static final int BONUS_MAX_COUNT = 3;
    private static final int IMPACT_VIBRATION_TIME = 25;

    private final Random random = new Random();

    private AsteroidFactory asteroidFactory = new AsteroidFactory(this, random);
    private BonusFactory bonusFactory = new BonusFactory(this, random);

    private boolean paused = false;

    private final Vibrator vibrator;

    // Touchscreen status
    private Vector2f touchscreen = new Vector2f(0f, 0f);
    private boolean touchscreenPressed = false;

    // Sprites groups
    private SpriteGroup<Background> backgrounds;
    private SpriteGroup<Player> players;
    private SpriteGroup<Asteroid> asteroids;
    private ParticleGroup<Smoke> smokes;
    private ParticleGroup<Sparkle> sparkles;
    private SpriteGroup<Bonus> bonuses;

    Player player = new Player();

    // Constructor
    GameRenderer(Context context) {

        super(context);

        SpriteGroupConfigReader spriteGroupConfigs = new SpriteGroupConfigReader(context);

        backgrounds = new SpriteGroup<>(spriteGroupConfigs.getConfig("background"));
        players = new SpriteGroup<>(spriteGroupConfigs.getConfig("player"));
        asteroids = new SpriteGroup<>(spriteGroupConfigs.getConfig("asteroid"));
        smokes = new ParticleGroup<>(spriteGroupConfigs.getConfig("smoke"));
        sparkles = new ParticleGroup<>(spriteGroupConfigs.getConfig("sparkle"));
        bonuses = new SpriteGroup<>(spriteGroupConfigs.getConfig("bonus"));

        players.getSprites().add(player);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean getPaused() {
        return this.paused;
    }

    void setTouchscreenPressed(boolean b) {
        this.touchscreenPressed = b;
    }

    void setTouchscreen(Vector2f v) {
        this.touchscreen.set(v);
    }

    @Override
    protected SpriteTexture[] getTextures() {
        return new SpriteTexture[]{
                backgrounds.getTexture(), players.getTexture(), asteroids.getTexture(),
                smokes.getTexture(), sparkles.getTexture(), bonuses.getTexture()};
    }

    @Override
    protected void update(long timeSlice) {
        if (paused && touchscreenPressed) {
            paused = false;
        }
        if (!paused) {
            updateBackground();
            updatePlayer(timeSlice);
            updateAsteroids(timeSlice);
            smokes.update(timeSlice);
            sparkles.update(timeSlice);
            updateBonuses(timeSlice);
        }
    }

    @Override
    protected void draw(GL10 gl) {
        backgrounds.draw(gl);
        players.draw(gl);
        asteroids.draw(gl);
        smokes.draw(gl);
        sparkles.draw(gl);
        bonuses.draw(gl);
    }

    private void updateBackground() {

        // Remove all the background tiles
        backgrounds.getSprites().clear();

        // Add 4 tiles around the player
        Vector2f center = player.getPosition().divide(BACKGROUND_SIZE);
        center = new Vector2f(Math.round(center.getX()), Math.round(center.getY()));
        for (float x : new float[]{center.getX() - 0.5f, center.getX() + 0.5f}) {
            for (float y : new float[]{center.getY() - 0.5f, center.getY() + 0.5f}) {
                backgrounds.getSprites().add(new Background(
                        new Vector2f(BACKGROUND_SIZE * x, BACKGROUND_SIZE * y)));
            }
        }
    }

    private void updatePlayer(long timeSlice) {

        // If touching the screen
        if (touchscreenPressed) {

            // Set the acceleration to the normalised touchscreen vector
            Vector2f v = convertScreenToWorld(touchscreen);
            v.divide(v.norm());
            player.setAcceleration(v);

            // Add smoke particles
            smokes.getSprites().add(new Smoke(player.getExhaust(), random));

        } else {
            player.setAcceleration(new Vector2f(0f, 0f));
        }

        // Iterate over asteroids
        for (Asteroid asteroid : asteroids.getSprites()) {

            // Check player and asteroid for collision
            if (player.overlaps(asteroid) && player.collide(asteroid)) {
                createImpact(player, asteroid);
                vibrator.cancel(); // prevents the vibrator from getting stuck
                vibrator.vibrate(IMPACT_VIBRATION_TIME);
            }
        }

        // Iterate over bonuses
        for (Bonus bonus : bonuses.getSprites()) {

            // Check player and bonus for collision
            if (!bonus.isExploding() && player.overlaps(bonus)) {
                bonus.explode();
            }
        }

        // Update the player sprite
        player.update(timeSlice);

        // Update the camera position
        setCamera(player.getPosition());
    }

    private void updateAsteroids(long timeSlice) {

        // Iterate over all the asteroids
        for (Asteroid asteroid : asteroids.getSprites()) {

            // Update each asteroid
            asteroid.update(timeSlice);
        }

        // Iterate over all the asteroids
        Iterator<Asteroid> asteroidIterator = asteroids.getSprites().iterator();
        while (asteroidIterator.hasNext()) {

            // Remove the asteroids which are too far
            Asteroid asteroid = asteroidIterator.next();
            if (asteroid.isOutOfScope(this)) {
                asteroidIterator.remove();
            }
        }

        // Add asteroids if we have space
        while (asteroids.getSprites().size() < ASTEROID_MAX_COUNT) {
            asteroids.getSprites().add(asteroidFactory.create());
        }

        // Iterate over asteroids pairs
        for (int i = 0; i < asteroids.getSprites().size(); i++) {
            Asteroid asteroid1 = asteroids.getSprites().get(i);
            for (int j = i + 1; j < asteroids.getSprites().size(); j++) {
                Asteroid asteroid2 = asteroids.getSprites().get(j);

                // Check asteroids pairs for collision
                if (asteroid1.overlaps(asteroid2) && asteroid1.collide(asteroid2)) {
                    createImpact(asteroid1, asteroid2);
                }
            }
        }

        // Iterate over asteroid-bonus pairs
        for (Asteroid asteroid : asteroids.getSprites()) {
            for (Bonus bonus : bonuses.getSprites()) {

                // Check asteroid and bonus for collision
                if (!bonus.isExploding() && asteroid.overlaps(bonus) && asteroid.collide(bonus)) {
                    createImpact(asteroid, bonus);
                }
            }
        }
    }

    private void updateBonuses(long timeSlice) {

        // Iterate over all the bonuses
        for (Bonus bonus : bonuses.getSprites()) {

            // Update each bonus
            bonus.update(timeSlice);
        }

        // Iterate over all the bonuses
        Iterator<Bonus> bonusIterator = bonuses.getSprites().iterator();
        while (bonusIterator.hasNext()) {

            // Remove the bonuses which are too far
            Bonus bonus = bonusIterator.next();
            if (bonus.isOutOfScope(this) || bonus.isExpired()) {
                bonusIterator.remove();
            }
        }

        // Add bonuses if we have space
        while (bonuses.getSprites().size() < BONUS_MAX_COUNT) {
            bonuses.getSprites().add(bonusFactory.create());
        }

        // Iterate over bonuses pairs
        for (int i = 0; i < bonuses.getSprites().size(); i++) {
            Bonus bonus1 = bonuses.getSprites().get(i);
            for (int j = i + 1; j < bonuses.getSprites().size(); j++) {
                Bonus bonus2 = bonuses.getSprites().get(j);

                // Check bonuses pair for collision
                if (!bonus1.isExploding() && !bonus2.isExploding() &&
                        bonus1.overlaps(bonus2) && bonus1.collide(bonus2)) {
                    createImpact(bonus1, bonus2);
                }
            }
        }
    }

    private void createImpact(DriftingSprite sprite1, DriftingSprite sprite2) {
        Vector2f impactPoint = sprite1.impactPoint(sprite2);
        for (int n = 0; n < SPARKLES_PER_IMPACT; n++) {
            sparkles.getSprites().add(new Sparkle(impactPoint, random));
        }
    }
}
