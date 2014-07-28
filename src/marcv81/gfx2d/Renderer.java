package marcv81.gfx2d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

public abstract class Renderer implements GLSurfaceView.Renderer {

	private static final long MIN_TIME_SLICE = 40; // 25 FPS
	private static final long MAX_TIME_SLICE = 100; // 10 FPS

	private long previousTime = 0;

	// Load the textures
	protected abstract void loadTextures(GL10 gl);

	// Draw the sprites
	protected abstract void drawSprites(GL10 gl);

	// Update the engine
	protected abstract void update(long timeSlice);

	protected float x = 0f, y = 0f;

	// Set the x and y camera coordinates
	public void setXY(float x, float y) {
		this.x = x;
		this.y = y;
	}

	// Move the x and y camera coordinates
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

		// Call the abstract method to load the textures
		loadTextures(gl);
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

		long currentTime, timeSlice;

		// Wait until the time slice is long enough
		do {
			currentTime = System.currentTimeMillis();
			timeSlice = currentTime - previousTime;
		} while (timeSlice < MIN_TIME_SLICE);

		// Slow the game down if the time slice is too long
		if (timeSlice > MAX_TIME_SLICE)
			timeSlice = MAX_TIME_SLICE;

		// Update the engine
		update(timeSlice);

		// Get ready to calculate the next time slice
		previousTime = currentTime;

		// Prepare to draw the sprites
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, x, y, -3f, x, y, 0f, 0f, 1f, 0f);

		// Call the abstract method to draw the sprites
		drawSprites(gl);
	}
}
