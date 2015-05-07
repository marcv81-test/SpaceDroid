package net.marcv81.game;

import net.marcv81.gfx2d.*;
import android.os.Bundle;

import java.util.LinkedList;
import java.util.List;

public class GameActivity extends DebugActivity {

    private Gfx2dView view;
    private GameEngine engine;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        engine = new GameEngine(this);
        view = new Gfx2dView(this);

        // Create rendering group for each sprite type
        List<SpriteGroup> spriteGroups = new LinkedList<>();
        spriteGroups.add(new SpriteGroup(
                new SpriteTexture("stars", 1, 1),
                new SpriteGeometry(4f),
                false, false, false,
                engine.getBackgrounds()
        ));
        spriteGroups.add(new SpriteGroup(
                new SpriteTexture("player", 1, 1),
                new SpriteGeometry(0.2f),
                true, false, false,
                engine.getPlayers()
        ));
        spriteGroups.add(new SpriteGroup(
                new SpriteTexture("asteroid", 8, 8),
                new SpriteGeometry(0.15f),
                true, false, true,
                engine.getAsteroids()
        ));
        spriteGroups.add(new SpriteGroup(
                new SpriteTexture("smoke", 2, 2),
                new SpriteGeometry(0.1f),
                true, true, true,
                engine.getSmokes()
        ));
        spriteGroups.add(new SpriteGroup(
                new SpriteTexture("sparkle", 1, 1),
                new SpriteGeometry(0.05f),
                false, true, false,
                engine.getSparkles()
        ));
        spriteGroups.add(new SpriteGroup(
                new SpriteTexture("bonus", 1, 1),
                new SpriteGeometry(0.15f),
                false, true, true,
                engine.getBonuses()
        ));

        // Start engine and renderer
        engine.setView(view, spriteGroups);
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    @Override
    public void onBackPressed() {
        if(engine != null) {
            if (engine.isPaused()) {
                finish();
            } else {
                engine.setPaused(true);
            }
        }
    }
}
