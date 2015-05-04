package net.marcv81.game;

import android.content.Context;
import android.os.Vibrator;
import net.marcv81.gfx2d.Engine;
import net.marcv81.gfx2d.Renderer;
import net.marcv81.gfx2d.Vector2f;

import java.util.*;

public class GameEngine extends Engine {

    private static final float BACKGROUND_SIZE = 4f;
    private static final int SPARKLES_PER_IMPACT = 5;
    private static final int ASTEROID_MAX_COUNT = 20;
    private static final int BONUS_MAX_COUNT = 3;
    private static final int IMPACT_VIBRATION_TIME = 25;

    private static final String SPRITEGROUP_BACKGROUND = "background";
    private static final String SPRITEGROUP_PLAYER = "player";
    private static final String SPRITEGROUP_ASTEROID = "asteroid";
    private static final String SPRITEGROUP_SMOKE = "smoke";
    private static final String SPRITEGROUP_SPARKLE = "sparkle";
    private static final String SPRITEGROUP_BONUS = "bonus";

    private final Vibrator vibrator;
    private final Renderer renderer;

    // Touchscreen status
    private Vector2f touchscreen = new Vector2f(0f, 0f);
    private boolean touchscreenPressed = false;

    private boolean paused = false;

    private final Random random = new Random();

    private AsteroidFactory asteroidFactory;
    private BonusFactory bonusFactory;

    private List<Background> backgrounds = new LinkedList<>();
    private List<Player> players = new LinkedList<>();
    private List<Asteroid> asteroids = new LinkedList<>();
    private List<Smoke> smokes = new LinkedList<>();
    private List<Sparkle> sparkles = new LinkedList<>();
    private List<Bonus> bonuses = new LinkedList<>();

    Player player = new Player();

    public GameEngine(Context context, Renderer renderer) {

        this.renderer = renderer;

        asteroidFactory = new AsteroidFactory(renderer, random);
        bonusFactory = new BonusFactory(renderer, random);

        renderer.setSprites(SPRITEGROUP_BACKGROUND, backgrounds);
        renderer.setSprites(SPRITEGROUP_PLAYER, players);
        renderer.setSprites(SPRITEGROUP_ASTEROID, asteroids);
        renderer.setSprites(SPRITEGROUP_SMOKE, smokes);
        renderer.setSprites(SPRITEGROUP_SPARKLE, sparkles);
        renderer.setSprites(SPRITEGROUP_BONUS, bonuses);

        players.add(player);

        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
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
            updateParticles(timeSlice, smokes);
            updateParticles(timeSlice, sparkles);
            updateBonuses(timeSlice);
        }
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

    private void updateBackground() {

        // Remove all the background tiles
        backgrounds.clear();

        // Add 4 tiles around the player
        Vector2f center = player.getPosition().divide(BACKGROUND_SIZE);
        center = new Vector2f(Math.round(center.getX()), Math.round(center.getY()));
        for (float x : new float[]{center.getX() - 0.5f, center.getX() + 0.5f}) {
            for (float y : new float[]{center.getY() - 0.5f, center.getY() + 0.5f}) {
                backgrounds.add(new Background(
                        new Vector2f(BACKGROUND_SIZE * x, BACKGROUND_SIZE * y)));
            }
        }
    }

    private void updatePlayer(long timeSlice) {

        // If touching the screen
        if (touchscreenPressed) {

            // Set the acceleration to the normalised touchscreen vector
            Vector2f v = renderer.convertScreenToWorld(touchscreen);
            v.divide(v.norm());
            player.setAcceleration(v);

            // Add smoke particles
            smokes.add(new Smoke(player.getExhaust(), random));

        } else {
            player.setAcceleration(new Vector2f(0f, 0f));
        }

        // Iterate over asteroids
        for (Asteroid asteroid : asteroids) {

            // Check player and asteroid for collision
            if (player.overlaps(asteroid) && player.collide(asteroid)) {
                createImpact(player, asteroid);
                vibrator.cancel(); // prevents the vibrator from getting stuck
                vibrator.vibrate(IMPACT_VIBRATION_TIME);
            }
        }

        // Iterate over bonuses
        for (Bonus bonus : bonuses) {

            // Check player and bonus for collision
            if (!bonus.isExploding() && player.overlaps(bonus)) {
                bonus.explode();
            }
        }

        // Update the player sprite
        player.update(timeSlice);

        // Update the camera position
        renderer.setCamera(player.getPosition());
    }

    private void updateAsteroids(long timeSlice) {

        // Iterate over all the asteroids
        for (Asteroid asteroid : asteroids) {

            // Update each asteroid
            asteroid.update(timeSlice);
        }

        // Iterate over all the asteroids
        Iterator<Asteroid> asteroidIterator = asteroids.iterator();
        while (asteroidIterator.hasNext()) {

            // Remove the asteroids which are too far
            Asteroid asteroid = asteroidIterator.next();
            if (asteroid.isOutOfScope(renderer)) {
                asteroidIterator.remove();
            }
        }

        // Add asteroids if we have space
        while (asteroids.size() < ASTEROID_MAX_COUNT) {
            asteroids.add(asteroidFactory.create());
        }

        // Iterate over asteroids pairs
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid1 = asteroids.get(i);
            for (int j = i + 1; j < asteroids.size(); j++) {
                Asteroid asteroid2 = asteroids.get(j);

                // Check asteroids pairs for collision
                if (asteroid1.overlaps(asteroid2) && asteroid1.collide(asteroid2)) {
                    createImpact(asteroid1, asteroid2);
                }
            }
        }

        // Iterate over asteroid-bonus pairs
        for (Asteroid asteroid : asteroids) {
            for (Bonus bonus : bonuses) {

                // Check asteroid and bonus for collision
                if (!bonus.isExploding() && asteroid.overlaps(bonus) && asteroid.collide(bonus)) {
                    createImpact(asteroid, bonus);
                }
            }
        }
    }

    private void updateBonuses(long timeSlice) {

        // Iterate over all the bonuses
        for (Bonus bonus : bonuses) {

            // Update each bonus
            bonus.update(timeSlice);
        }

        // Iterate over all the bonuses
        Iterator<Bonus> bonusIterator = bonuses.iterator();
        while (bonusIterator.hasNext()) {

            // Remove the bonuses which are too far
            Bonus bonus = bonusIterator.next();
            if (bonus.isOutOfScope(renderer) || bonus.isExpired()) {
                bonusIterator.remove();
            }
        }

        // Add bonuses if we have space
        while (bonuses.size() < BONUS_MAX_COUNT) {
            bonuses.add(bonusFactory.create());
        }

        // Iterate over bonuses pairs
        for (int i = 0; i < bonuses.size(); i++) {
            Bonus bonus1 = bonuses.get(i);
            for (int j = i + 1; j < bonuses.size(); j++) {
                Bonus bonus2 = bonuses.get(j);

                // Check bonuses pair for collision
                if (!bonus1.isExploding() && !bonus2.isExploding() &&
                        bonus1.overlaps(bonus2) && bonus1.collide(bonus2)) {
                    createImpact(bonus1, bonus2);
                }
            }
        }
    }

    public <T extends Particle> void updateParticles(long timeSlice, List<T> sprites) {

        // Iterate over all the particles
        Iterator<T> iterator = sprites.iterator();
        while (iterator.hasNext()) {

            // Update each particle
            T particle = iterator.next();
            particle.update(timeSlice);

            // Remove the expired particles
            if (particle.isExpired()) {
                iterator.remove();
            }
        }
    }

    private void createImpact(DriftingSprite sprite1, DriftingSprite sprite2) {
        Vector2f impactPoint = sprite1.impactPoint(sprite2);
        for (int n = 0; n < SPARKLES_PER_IMPACT; n++) {
            sparkles.add(new Sparkle(impactPoint, random));
        }
    }
}
