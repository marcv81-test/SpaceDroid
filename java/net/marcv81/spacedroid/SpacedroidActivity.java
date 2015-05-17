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
                4f,
                R.drawable.stars, 1, 1,
                false, false, false,
                gameEngine.backgrounds
        ));
        spriteGroups.add(new SpriteRenderer(
                0.2f,
                R.drawable.player, 1, 1,
                true, false, false,
                gameEngine.players
        ));
        spriteGroups.add(new SpriteRenderer(
                0.15f,
                R.drawable.asteroid, 8, 8,
                true, false, true,
                gameEngine.asteroids
        ));
        spriteGroups.add(new SpriteRenderer(
                0.1f,
                R.drawable.smoke, 2, 2,
                true, true, true,
                gameEngine.smokes
        ));
        spriteGroups.add(new SpriteRenderer(
                0.05f,
                R.drawable.sparkle, 1, 1,
                false, true, false,
                gameEngine.sparkles
        ));
        spriteGroups.add(new SpriteRenderer(
                0.15f,
                R.drawable.bonus, 1, 1,
                false, true, true,
                gameEngine.bonuses
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
