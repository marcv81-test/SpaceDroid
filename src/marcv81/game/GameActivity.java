package marcv81.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import java.lang.Thread.UncaughtExceptionHandler;

public class GameActivity extends Activity implements UncaughtExceptionHandler {

	private static final String TAG = "AndroidTest";

	private GameView view;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(this);
		view = new GameView(this);
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
	public void uncaughtException(Thread thread, Throwable throwable) {
		Log.w(TAG, "Caught: " + throwable.getClass());
		String message = throwable.getMessage();
		if (message != null)
			Log.w(TAG, message);
		for (StackTraceElement s : throwable.getStackTrace())
			Log.w(TAG, s.toString());
		System.exit(1);
	}
}

