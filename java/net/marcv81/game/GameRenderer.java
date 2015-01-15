package net.marcv81.game;

import android.content.Context;
import net.marcv81.gfx2d.*;

import javax.microedition.khronos.opengles.GL10;
import java.util.Iterator;
import java.util.Random;
import android.os.Vibrator;

class GameRenderer extends Renderer {

    // Background texture
    private static final int BACKGROUND_RESOURCE = R.drawable.stars;
    public static final float BACKGROUND_SIZE = 4f;
    private static final int BACKGROUND_ANIMATIONS_X = 1;
    private static final int BACKGROUND_ANIMATIONS_Y = 1;
    private static final boolean BACKGROUND_SUPPORT_ANGLE = false;
    private static final boolean BACKGROUND_SUPPORT_TRANSPARENCY = false;
    private static final boolean BACKGROUND_SUPPORT_SCALING = false;

    // Player texture
    private static final int PLAYER_RESOURCE = R.drawable.player;
    private static final float PLAYER_SIZE = 0.2f;
    private static final int PLAYER_ANIMATIONS_X = 1;
    private static final int PLAYER_ANIMATIONS_Y = 1;
    private static final boolean PLAYER_SUPPORT_ANGLE = true;
    private static final boolean PLAYER_SUPPORT_TRANSPARENCY = false;
    private static final boolean PLAYER_SUPPORT_SCALING = false;

    // Asteroid texture
    private static final int ASTEROID_RESOURCE = R.drawable.asteroid;
    private static final float ASTEROID_SIZE = 0.15f;
    private static final int ASTEROID_ANIMATIONS_X = 8;
    private static final int ASTEROID_ANIMATIONS_Y = 8;
    private static final boolean ASTEROID_SUPPORT_ANGLE = true;
    private static final boolean ASTEROID_SUPPORT_TRANSPARENCY = false;
    private static final boolean ASTEROID_SUPPORT_SCALING = true;

    // Smoke texture
    private static final int SMOKE_RESOURCE = R.drawable.smoke;
    private static final float SMOKE_SIZE = 0.1f;
    private static final int SMOKE_ANIMATIONS_X = 2;
    private static final int SMOKE_ANIMATIONS_Y = 2;
    private static final boolean SMOKE_SUPPORT_ANGLE = true;
    private static final boolean SMOKE_SUPPORT_TRANSPARENCY = true;
    private static final boolean SMOKE_SUPPORT_SCALING = true;

    // Sparkle texture
    private static final int SPARKLE_RESOURCE = R.drawable.sparkle;
    private static final float SPARKLE_SIZE = 0.05f;
    private static final int SPARKLE_ANIMATIONS_X = 1;
    private static final int SPARKLE_ANIMATIONS_Y = 1;
    private static final boolean SPARKLE_SUPPORT_ANGLE = false;
    private static final boolean SPARKLE_SUPPORT_TRANSPARENCY = true;
    private static final boolean SPARKLE_SUPPORT_SCALING = false;

    // Bonus texture
    private static final int BONUS_RESOURCE = R.drawable.bonus;
    private static final float BONUS_SIZE = 0.15f;
    private static final int BONUS_ANIMATIONS_X = 1;
    private static final int BONUS_ANIMATIONS_Y = 1;
    private static final boolean BONUS_SUPPORT_ANGLE = false;
    private static final boolean BONUS_SUPPORT_TRANSPARENCY = false;
    private static final boolean BONUS_SUPPORT_SCALING = false;

    private static final int SPARKLES_PER_IMPACT = 5;
    private static final int ASTEROID_MAX_COUNT = 20;
    private static final int IMPACT_VIBRATION_TIME = 25;

    private boolean paused = false;

    private final Vibrator vibrator;

    // Touchscreen status
    private Vector2f touchscreen = new Vector2f(0f, 0f);
    private boolean touchscreenPressed = false;

    private final Random random = new Random();

    // Sprites groups
    private final SpriteGroup<Background> backgrounds = new SpriteGroup<>(
            new SpriteTexture(BACKGROUND_RESOURCE, BACKGROUND_ANIMATIONS_X, BACKGROUND_ANIMATIONS_Y),
            new SpriteGeometry(BACKGROUND_SIZE),
            BACKGROUND_SUPPORT_ANGLE, BACKGROUND_SUPPORT_TRANSPARENCY, BACKGROUND_SUPPORT_SCALING
    );
    private final SpriteGroup<Player> players = new SpriteGroup<>(
            new SpriteTexture(PLAYER_RESOURCE, PLAYER_ANIMATIONS_X, PLAYER_ANIMATIONS_Y),
            new SpriteGeometry(PLAYER_SIZE),
            PLAYER_SUPPORT_ANGLE, PLAYER_SUPPORT_TRANSPARENCY, PLAYER_SUPPORT_SCALING
    );
    private final SpriteGroup<Asteroid> asteroids = new SpriteGroup<>(
            new SpriteTexture(ASTEROID_RESOURCE, ASTEROID_ANIMATIONS_X, ASTEROID_ANIMATIONS_Y),
            new SpriteGeometry(ASTEROID_SIZE),
            ASTEROID_SUPPORT_ANGLE, ASTEROID_SUPPORT_TRANSPARENCY, ASTEROID_SUPPORT_SCALING
    );
    private final ParticleGroup<Smoke> smokes = new ParticleGroup<>(
            new SpriteTexture(SMOKE_RESOURCE, SMOKE_ANIMATIONS_X, SMOKE_ANIMATIONS_Y),
            new SpriteGeometry(SMOKE_SIZE),
            SMOKE_SUPPORT_ANGLE, SMOKE_SUPPORT_TRANSPARENCY, SMOKE_SUPPORT_SCALING
    );
    private final ParticleGroup<Sparkle> sparkles = new ParticleGroup<>(
            new SpriteTexture(SPARKLE_RESOURCE, SPARKLE_ANIMATIONS_X, SPARKLE_ANIMATIONS_Y),
            new SpriteGeometry(SPARKLE_SIZE),
            SPARKLE_SUPPORT_ANGLE, SPARKLE_SUPPORT_TRANSPARENCY, SPARKLE_SUPPORT_SCALING
    );
    private final SpriteGroup<Bonus> bonuses = new SpriteGroup<>(
            new SpriteTexture(BONUS_RESOURCE, BONUS_ANIMATIONS_X, BONUS_ANIMATIONS_Y),
            new SpriteGeometry(BONUS_SIZE),
            BONUS_SUPPORT_ANGLE, BONUS_SUPPORT_TRANSPARENCY, BONUS_SUPPORT_SCALING
    );

    Player player = new Player();

    // Constructor
    GameRenderer(Context context) {
        super(context);
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
            updateBonuses();
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

        // If not touching the screen
        } else {
            player.setAcceleration(new Vector2f(0f, 0f));
        }

        // Update the player sprite
        player.update(timeSlice);

        // Update the camera position
        setCamera(player.getPosition());
    }

    private void updateAsteroids(long timeSlice) {

        // Iterate over all the asteroids
        Iterator<Asteroid> asteroidIterator = asteroids.getSprites().iterator();
        while (asteroidIterator.hasNext()) {

            // Update each asteroid
            Asteroid asteroid = asteroidIterator.next();
            asteroid.update(timeSlice);

            // Remove the asteroids which are too far
            if (asteroid.isOutOfScope(this)) {
                asteroidIterator.remove();
            }
        }

        // Add asteroids if we have space
        while (asteroids.getSprites().size() < ASTEROID_MAX_COUNT) {
            asteroids.getSprites().add(Asteroid.spawn(this, random));
        }

        // Iterate over asteroids
        for (int i = 0; i < asteroids.getSprites().size(); i++) {
            Asteroid asteroid1 = asteroids.getSprites().get(i);

            // Iterate over asteroids pairs
            for (int j = i + 1; j < asteroids.getSprites().size(); j++) {
                Asteroid asteroid2 = asteroids.getSprites().get(j);

                // Check asteroids pair for collision
                if (asteroid1.overlaps(asteroid2) && asteroid1.collide(asteroid2)) {
                    Vector2f impactPoint = asteroid1.impactPoint(asteroid2);
                    for (int n = 0; n < SPARKLES_PER_IMPACT; n++) {
                        sparkles.getSprites().add(new Sparkle(impactPoint, random));
                    }
                }
            }

            // Check asteroid and player for collision
            if (asteroid1.overlaps(player) && asteroid1.collide(player)) {
                Vector2f impactPoint = asteroid1.impactPoint(player);
                for (int n = 0; n < SPARKLES_PER_IMPACT; n++) {
                    sparkles.getSprites().add(new Sparkle(impactPoint, random));
                    vibrator.cancel(); // prevents the vibrator from getting stuck
                    vibrator.vibrate(IMPACT_VIBRATION_TIME);
                }
            }
        }
    }

    private void updateBonuses() {
        bonuses.getSprites().clear();
        if (touchscreenPressed) {
            bonuses.getSprites().add(new Bonus(getCamera().plus(convertScreenToWorld(touchscreen))));
        }
    }
}
