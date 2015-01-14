package net.marcv81.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;
import net.marcv81.gfx2d.Vector2f;

class GameView extends GLSurfaceView {

    private final GameRenderer renderer;

    GameView(Context context) {
        super(context);
        renderer = new GameRenderer(context);
        setRenderer(renderer);
    }

    void setPaused(boolean paused) {
        renderer.setPaused(paused);
    }

    boolean getPaused() {
        return renderer.getPaused();
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                renderer.setTouchscreenPressed(true);
                renderer.setTouchscreen(new Vector2f(x, y));
                break;
            case MotionEvent.ACTION_MOVE:
                renderer.setTouchscreen(new Vector2f(x, y));
                break;
            case MotionEvent.ACTION_UP:
                renderer.setTouchscreenPressed(false);
                break;
            default:
                break;
        }
        return true;
    }
}
