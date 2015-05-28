package net.marcv81.spacedroid;

import android.content.Context;
import android.os.Vibrator;
import net.marcv81.spacedroid.sprites.*;
import net.marcv81.gfx2d.GameEngine;
import net.marcv81.gfx2d.GameView;
import net.marcv81.gfx2d.Vector2f;

import java.util.*;

public final class SpacedroidEngine extends GameEngine {

    private static final float PLAYER_THRUST_MULTIPLIER = 3f;
    private static final float BACKGROUND_SIZE = 4f;
    private static final int SPARKLES_PER_IMPACT = 5;
    private static final int IMPACT_VIBRATION_TIME = 25;

    Player player = new Player();

    // Sprites lists
    protected List<Background> backgrounds = new LinkedList<>();
    protected List<Player> players = new LinkedList<>(Collections.singletonList(player));
    protected List<Asteroid> asteroids = new LinkedList<>();
    protected List<Smoke> smokes = new LinkedList<>();
    protected List<Sparkle> sparkles = new LinkedList<>();
    protected List<Bonus> bonuses = new LinkedList<>();

    private SpawnEngine spawnEngine;

    private boolean paused = false;

    private final Random random = new Random();
    private final Vibrator vibrator;

    public SpacedroidEngine(Context context) {
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    }

    @Override
    public void setGameView(GameView gameView) {

        super.setGameView(gameView);

        spawnEngine = new SpawnEngine(this, gameView, random);
    }

    @Override
    protected void update(long timeSlice) {
        if (paused && gameView.isPointerActive()) {
            paused = false;
        }
        if (!paused) {
            updateBackground();
            updatePlayer(timeSlice);
            updateAsteroids(timeSlice);
            updateExpirable(timeSlice, smokes);
            updateExpirable(timeSlice, sparkles);
            updateBonuses(timeSlice);
        }
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public boolean isPaused() {
        return paused;
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

        // If pointer is down
        if (gameView.isPointerActive()) {

            // Set the acceleration to the normalised pointer vector
            Vector2f v = gameView.getPointer();
            v.divide(v.norm());
            player.setThrust(v.multiply(PLAYER_THRUST_MULTIPLIER));

            // Add smoke particles
            smokes.add(new Smoke(player.getExhaust(), random));

        } else {
            player.setThrust(new Vector2f(0f, 0f));
        }

        // Iterate over asteroids
        for (Asteroid asteroid : asteroids) {

            // Check player and asteroid for collision
            if (CollisionUtils.collide(player, asteroid)) {
                createImpact(player, asteroid);
                vibrator.cancel(); // prevents the vibrator from getting stuck
                vibrator.vibrate(IMPACT_VIBRATION_TIME);
            }
        }

        // Iterate over bonuses
        for (Bonus bonus : bonuses) {

            // Check player and bonus for collision
            if (CollisionUtils.overlap(player, bonus)) {
                bonus.collect();
            }
        }

        // Update the player sprite
        player.update(timeSlice);

        // Update the camera position
        gameView.setCamera(player.getPosition());
    }

    private void updateAsteroids(long timeSlice) {

        // Iterate over all the asteroids
        for (Asteroid asteroid : asteroids) {

            // Update each asteroid
            asteroid.update(timeSlice);
        }

        // Remove the asteroids which are out of scope
        spawnEngine.destroyAsteroids();

        // Add asteroids if we have space
        spawnEngine.createAsteroids();

        // Iterate over asteroids pairs
        for (int i = 0; i < asteroids.size(); i++) {
            Asteroid asteroid1 = asteroids.get(i);
            for (int j = i + 1; j < asteroids.size(); j++) {
                Asteroid asteroid2 = asteroids.get(j);

                // Check asteroids pairs for collision
                if (CollisionUtils.collide(asteroid1, asteroid2)) {
                    createImpact(asteroid1, asteroid2);
                }
            }
        }

        // Iterate over asteroid-bonus pairs
        for (Asteroid asteroid : asteroids) {
            for (Bonus bonus : bonuses) {

                // Check asteroid and bonus for collision
                if (CollisionUtils.collide(asteroid, bonus)) {
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

        // Remove the bonuses which are out of scope
        spawnEngine.destroyBonuses();

        // Iterate over all the bonuses
        Iterator<Bonus> bonusIterator = bonuses.iterator();
        while (bonusIterator.hasNext()) {

            // Remove the bonuses which have expired
            Bonus bonus = bonusIterator.next();
            if (bonus.isExpired()) {
                bonusIterator.remove();
            }
        }

        // Add bonuses if we have space
        spawnEngine.createBonuses();

        // Iterate over bonuses pairs
        for (int i = 0; i < bonuses.size(); i++) {
            Bonus bonus1 = bonuses.get(i);
            for (int j = i + 1; j < bonuses.size(); j++) {
                Bonus bonus2 = bonuses.get(j);

                // Check bonuses pair for collision
                if (CollisionUtils.collide(bonus1, bonus2)) {
                    createImpact(bonus1, bonus2);
                }
            }
        }
    }

    public <T extends Updatable & Expirable> void updateExpirable(long timeSlice, List<T> expirables) {

        // Iterate over all the particles
        Iterator<T> iterator = expirables.iterator();
        while (iterator.hasNext()) {

            // Update each particle
            T expirable = iterator.next();
            expirable.update(timeSlice);

            // Remove the expired particles
            if (expirable.isExpired()) {
                iterator.remove();
            }
        }
    }

    private void createImpact(Collidable c1, Collidable c2) {
        Vector2f impactPoint = CollisionUtils.impactPoint(c1, c2);
        for (int n = 0; n < SPARKLES_PER_IMPACT; n++) {
            sparkles.add(new Sparkle(impactPoint, random));
        }
    }
}
