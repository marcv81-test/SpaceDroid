package marcv81.gfx2d;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.content.Context;

public abstract class Renderer implements GLSurfaceView.Renderer {

	private static final long MIN_TIME_SLICE = 40; // 25 FPS
	private static final long MAX_TIME_SLICE = 100; // 10 FPS

	private final Context context;

	private float cameraX = 0f, cameraY = 0f;

	private int width = 0, height = 0;

	private long previousTime = 0;

	// Return all the sprites
	protected abstract Sprite[] getSprites();

	// Update the engine
	protected abstract void update(long timeSlice);

	// Constructor
	protected Renderer(Context context) {
		this.context = context;
	}

	// Get the camera X coordinate
	public float getCameraX() {
		return cameraX;
	}

	// Get the camera Y coordinate
	public float getCameraY() {
		return cameraY;
	}

	// Set the X and Y camera coordinates
	public void setXY(float x, float y) {
		this.cameraX = x;
		this.cameraY = y;
	}

	// Move the X and Y camera coordinates
	public void moveXY(float dx, float dy) {
		this.cameraX += dx;
		this.cameraY += dy;
	}

	// Get the surface width
	public float getWidth() {
		return width;
	}

	// Get the surface height
	public float getHeight() {
		return height;
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

		// Load the textures
		for (Sprite sprite : getSprites()) {
			sprite.loadTexture(gl, context);
		}
	}

	@Override
	public final void onSurfaceChanged(GL10 gl, int width, int height) {

		this.width = width;
		this.height = height;

		// Modify the surface according to the new width and height
		gl.glViewport(0, 0, width, height);
		float ratio = (float) width / height;
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glFrustumf(-ratio, ratio, -1f, 1f, 3f, 20f);
	}

	@Override
	public final void onDrawFrame(GL10 gl) {

		long currentTime = System.currentTimeMillis();
		long timeSlice = currentTime - previousTime;

		// Wait until the time slice is long enough
		while (timeSlice < MIN_TIME_SLICE) {
			try {
				Thread.sleep(MIN_TIME_SLICE - timeSlice);
			} catch (InterruptedException e) {
				// Don't care
			}
			currentTime = System.currentTimeMillis();
			timeSlice = currentTime - previousTime;
		}

		// Slow the game down if the time slice is too long
		if (timeSlice > MAX_TIME_SLICE)
			timeSlice = MAX_TIME_SLICE;

		// Update the engine
		update(timeSlice);

		// Get ready to calculate the next time slice
		previousTime = currentTime;

		// Buffer the camera coordinates
		float x = this.cameraX, y = this.cameraY;

		// Prepare to draw the sprites
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glMatrixMode(GL10.GL_MODELVIEW);
		gl.glLoadIdentity();
		GLU.gluLookAt(gl, x, y, -3f, x, y, 0f, 0f, 1f, 0f);

		// Draw the sprites
		for (Sprite sprite : getSprites()) {
			sprite.drawAll(gl, x, y);
		}
	}
}
