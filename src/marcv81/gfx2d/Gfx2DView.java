package marcv81.gfx2d;

import android.opengl.GLSurfaceView;
import android.util.Log;

public abstract class Gfx2DView extends GLSurfaceView {

	private final Gfx2DActivity activity;
	private final Gfx2DRenderer renderer;

	// Constructor
	public Gfx2DView(Gfx2DActivity activity) {
		super(activity);
		this.activity = activity;
		this.renderer = new Gfx2DRenderer(this);
		setRenderer(renderer);
		setRenderMode(RENDERMODE_WHEN_DIRTY);
	}

	public Gfx2DActivity getActivity() {
		return activity;
	}

	public Gfx2DRenderer getRenderer() {
		return renderer;
	}
}
