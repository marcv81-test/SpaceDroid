package marcv81.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class GameView extends GLSurfaceView {

    private final GameRenderer renderer;

    GameView(Context context) {
        super(context);
        renderer = new GameRenderer(context);
        setRenderer(renderer);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                renderer.setPointerDown(true);
                renderer.setPointerXY(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                renderer.setPointerXY(x, y);
                break;
            case MotionEvent.ACTION_UP:
                renderer.setPointerDown(false);
                break;
        }
        return true;
    }
}
