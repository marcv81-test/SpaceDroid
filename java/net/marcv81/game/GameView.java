package net.marcv81.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import net.marcv81.gfx2d.Vector2f;

class GameView extends GLSurfaceView {

    private final net.marcv81.gfx2d.Renderer renderer;
    private final GameEngine engine;

    GameView(Context context) {
        super(context);
        renderer = new net.marcv81.gfx2d.Renderer(context);
        engine = new GameEngine(context, renderer);
        renderer.setEngine(engine);
        setRenderer(renderer);
    }

    void setPaused(boolean paused) {
        engine.setPaused(paused);
    }

    boolean getPaused() {
        return engine.getPaused();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                engine.setTouchscreenPressed(true);
                engine.setTouchscreen(new Vector2f(x, y));
                break;
            case MotionEvent.ACTION_MOVE:
                engine.setTouchscreen(new Vector2f(x, y));
                break;
            case MotionEvent.ACTION_UP:
                engine.setTouchscreenPressed(false);
                break;
            default:
                break;
        }
        return true;
    }
}
