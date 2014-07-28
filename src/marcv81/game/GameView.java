package marcv81.game;

import marcv81.gfx2d.Gfx2DView;
import marcv81.gfx2d.Gfx2DActivity;
import android.view.MotionEvent;

class GameView extends Gfx2DView {

	private final float SCROLL_DIVIDER = 150f;

	private float previousX, previousY;

	GameView(Gfx2DActivity activity) {
		super(activity);
	}

	@Override
	public boolean onTouchEvent(MotionEvent e) {

		float x = e.getX();
		float y = e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_MOVE:
			float dx = (x - previousX);
			float dy = (y - previousY);
			getRenderer().moveXY(dx / SCROLL_DIVIDER, dy / SCROLL_DIVIDER);
		}
		previousX = x;
		previousY = y;
		return true;
	}
}
