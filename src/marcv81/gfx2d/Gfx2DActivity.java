package marcv81.gfx2d;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public abstract class Gfx2DActivity extends Activity implements
		UncaughtExceptionHandler {

	public static final String TAG = "AndroidTest";

	protected Gfx2DView view;
	protected Gfx2DThread thread;

	// Create the view
	protected abstract Gfx2DView createView();

	// Create the game thread
	protected abstract Gfx2DThread createThread();

	public Gfx2DView getView() {
		return view;
	}

	public Gfx2DThread getThread() {
		return thread;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(this);

		view = createView();
		setContentView(view);

		thread = createThread();
		thread.start();
		thread.setRunning(true);
	}

	@Override
	protected void onPause() {
		super.onPause();
		view.onPause();
		thread.setRunning(false);
	}

	@Override
	protected void onResume() {
		super.onResume();
		view.onResume();
		thread.setRunning(true);
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
