package marcv81.game;

import android.util.Log;
import marcv81.gfx2d.Gfx2DActivity;
import marcv81.gfx2d.Gfx2DView;
import marcv81.gfx2d.Gfx2DThread;

public class GameActivity extends Gfx2DActivity {

	public GameActivity() {
		Log.i(Gfx2DActivity.TAG, "GameActivity()");
	}

	public Gfx2DView createView() {
		return new GameView(this);
	}

	public Gfx2DThread createThread() {
		return new GameThread(this);
	}

}
