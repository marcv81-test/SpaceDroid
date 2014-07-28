package marcv81.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import android.util.Log;

import marcv81.gfx2d.Gfx2DSprite;
import marcv81.gfx2d.Gfx2DThread;
import marcv81.gfx2d.Gfx2DActivity;

public class GameThread extends Gfx2DThread {

	private static final long MIN_TIME_SLICE = 40; // 25 FPS
	private static final long MAX_TIME_SLICE = 100; // 10 FPS

	private static final int MAX_TIME_BETWEEN_FIREBALLS = 250;

	private final Random random = new Random();
	private long currentTime = 0, nextFireball = 0;

	// Scene sprites
	private final Background background;
	private final Fireballs fireballs;

	private ArrayList<Gfx2DSprite> sprites = new ArrayList<Gfx2DSprite>();

	public List<Gfx2DSprite> getSprites() {
		return sprites;
	}

	protected long getMinTimeSlice() {
		return MIN_TIME_SLICE;
	}

	protected long getMaxTimeSlice() {
		return MAX_TIME_SLICE;
	}

	public GameThread(Gfx2DActivity activity) {
		super(activity);
		background = new Background();
		fireballs = new Fireballs();
		sprites.add(background);
		sprites.add(fireballs);
	}

	protected void update(long timeSlice) {

		// Add random fireballs
		currentTime += timeSlice;
		if (currentTime > nextFireball) {
			fireballs.add(getActivity().getView().getRenderer().getX() + 2f
					* (random.nextFloat() - 0.5f), getActivity().getView()
					.getRenderer().getY()
					+ 2f * (random.nextFloat() - 0.5f));
			nextFireball = currentTime
					+ random.nextInt(MAX_TIME_BETWEEN_FIREBALLS);
		}

		fireballs.update(timeSlice);
	}
}
