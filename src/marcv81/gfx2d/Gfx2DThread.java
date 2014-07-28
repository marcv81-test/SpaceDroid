package marcv81.gfx2d;

import java.util.List;

public abstract class Gfx2DThread extends Thread {

	private final Gfx2DActivity activity;

	private boolean running = true;
	private long previousTime = 0;

	// Constructor
	protected Gfx2DThread(Gfx2DActivity activity) {
		this.activity = activity;
		Thread.setDefaultUncaughtExceptionHandler(activity);
	}

	// Minimum duration of a time slice
	protected abstract long getMinTimeSlice();

	// Maximum duration of a time slice
	protected abstract long getMaxTimeSlice();

	// Update the game
	protected abstract void update(long timeSlice);

	// Return the list of sprites
	public abstract List<Gfx2DSprite> getSprites();

	public Gfx2DActivity getActivity() {
		return activity;
	}

	// Set the running state
	public void setRunning(boolean running) {
		this.running = running;
	}

	@Override
	public void run() {

		while (running) {

			// Calculate the current time slice
			long currentTime = System.currentTimeMillis();
			long timeSlice = currentTime - previousTime;

			// Skip a frame if the time slice is too short
			if (timeSlice >= getMinTimeSlice()) {

				// Slow the game down if the time slice is too long
				if (timeSlice > getMaxTimeSlice())
					timeSlice = getMaxTimeSlice();

				// Update the game
				update(timeSlice);

				// Request a render update
				activity.getView().requestRender();

				// Get ready to calculate the next time slice
				previousTime = currentTime;
			}

		}
	}
}
