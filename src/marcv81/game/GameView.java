package marcv81.game;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

class GameView extends GLSurfaceView {

	protected final float SCROLL_DIVIDER = 150f;

	protected float previousX, previousY;

	protected final GameRenderer renderer;

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
		case MotionEvent.ACTION_MOVE:
			float dx = (x - previousX);
			float dy = (y - previousY);
			renderer.moveXY(dx / SCROLL_DIVIDER, dy / SCROLL_DIVIDER);
		}
		previousX = x;
		previousY = y;
		return true;
	}
}
