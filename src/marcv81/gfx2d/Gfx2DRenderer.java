package marcv81.gfx2d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public class Gfx2DRenderer implements GLSurfaceView.Renderer {

	private final Gfx2DView view;
	private float x = 0f, y = 0f;

	// Constructor
	public Gfx2DRenderer(Gfx2DView view) {
		this.view = view;
	}

	public Gfx2DView getView() {
		return view;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	// Set the camera coordinates
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	// Move the camera coordinates
	public void moveXY(float dx, float dy) {
		this.x += dx;
		this.y += dy;
	}

	@Override
	public final void onSurfaceCreated(GL10 gl, EGLConfig config) {

		// Prepare the renderer
		gl.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glShadeModel(GL10.GL_SMOOTH);
		gl.glClearDepthf(1.0f);
		gl.glEnable(GL10.GL_DEPTH_TEST);
		gl.glDepthFunc(GL10.GL_LEQUAL);

		// Load the sprites textures
		for (Gfx2DSprite sprite : getView().getActivity().getThread()
				.getSprites()) {
			sprite.loadTexture(gl, getView().getContext());
		}
	}

	@Override
	public final void onSurfaceChanged(GL10 gl, int width, int height) {

		// Modify the surface according to the new width and height
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1f, 1f, 3f, 20f);
	}

	@Override
	public final void onDrawFrame(GL10 gl) {

		// Buffer the camera coordinates
		float x = this.x, y = this.y;

		// Prepare to draw the sprites
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, x, y, -3f, x, y, 0f, 0f, 1f, 0f);

		// Draw the sprites
		for (Gfx2DSprite sprite : getView().getActivity().getThread()
				.getSprites()) {
			sprite.drawAll(gl, x, y);
		}
	}
}
