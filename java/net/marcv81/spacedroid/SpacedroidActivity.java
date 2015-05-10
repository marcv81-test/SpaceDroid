package net.marcv81.spacedroid;

import net.marcv81.gfx2d.*;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

public final class SpacedroidActivity extends DebugActivity {

    private GameView gameView;
    private SpacedroidEngine gameEngine;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        gameEngine = new SpacedroidEngine(this);

        // Create rendering group for each sprite type
        List<SpriteRenderer> spriteGroups = new LinkedList<>();
        spriteGroups.add(new SpriteRenderer(
                new SpriteTexture("stars", 1, 1),
                new SpriteGeometry(4f),
                false, false, false,
                gameEngine.getBackgrounds()
        ));
        spriteGroups.add(new SpriteRenderer(
                new SpriteTexture("player", 1, 1),
                new SpriteGeometry(0.2f),
                true, false, false,
                gameEngine.getPlayers()
        ));
        spriteGroups.add(new SpriteRenderer(
                new SpriteTexture("asteroid", 8, 8),
                new SpriteGeometry(0.15f),
                true, false, true,
                gameEngine.getAsteroids()
        ));
        spriteGroups.add(new SpriteRenderer(
                new SpriteTexture("smoke", 2, 2),
                new SpriteGeometry(0.1f),
                true, true, true,
                gameEngine.getSmokes()
        ));
        spriteGroups.add(new SpriteRenderer(
                new SpriteTexture("sparkle", 1, 1),
                new SpriteGeometry(0.05f),
                false, true, false,
                gameEngine.getSparkles()
        ));
        spriteGroups.add(new SpriteRenderer(
                new SpriteTexture("bonus", 1, 1),
                new SpriteGeometry(0.15f),
                false, true, true,
                gameEngine.getBonuses()
        ));

        // Start gameEngine and renderer
        gameView = new GameView(this, gameEngine, spriteGroups);
        gameEngine.setGameView(gameView);

        gameView.setRenderer();
        setContentView(gameView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }

    @Override
    public void onBackPressed() {
        if (gameEngine != null) {
            if (gameEngine.isPaused()) {
                finish();
            } else {
                gameEngine.setPaused(true);
            }
        }
    }
}
