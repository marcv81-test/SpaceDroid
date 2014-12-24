package marcv81.gfx2d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public abstract class Renderer implements GLSurfaceView.Renderer {

    private static final long MIN_TIME_SLICE = 20; // 50 FPS
    private static final long MAX_TIME_SLICE = 50; // 20 FPS

    private static final float EPSILON = 0.1f;
    private static final float CAMERA_DEPTH = -3f;
    protected static final float FOREGROUND_DEPTH = 0f;
    protected static final float BACKGROUND_DEPTH = 10f;

    private final Context context;

    private Vector2f camera = new Vector2f(0f, 0f);
    private Vector2f size = new Vector2f(0f, 0f);

    private long previousTime = 0;

    // Return all the textures
    protected abstract SpriteTexture[] getTextures();

    // Update the engine
    protected abstract void update(long timeSlice);

    protected abstract void draw(GL10 gl);

    // Constructor
    protected Renderer(Context context) {
        this.context = context;
    }

    // Set the X and Y camera coordinates
    public void setCamera(Vector2f position) {
        camera.set(position);
    }

    public Vector2f getCamera() {
        return new Vector2f(camera);
    }

    // Get the X coordinate of the right of the screen
    public float getRight() {
        return camera.x + size.x / size.y;
    }

    // Get the X coordinate of the left of the screen
    public float getLeft() {
        return camera.x - size.x / size.y;
    }

    // Get the Y coordinate of the top of the screen
    public float getTop() {
        return camera.y + 1.0f;
    }

    // Get the Y coordinate of the bottom of the screen
    public float getBottom() {
        return camera.y - 1.0f;
    }

    public Vector2f convertScreenToWorld(Vector2f point) {
        return new Vector2f(
                (-2f * point.x + size.x) / size.y,
                (-2f * point.y + size.y) / size.y);
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
        for (SpriteTexture texture : getTextures()) {
            texture.load(gl, context);
        }
    }

    @Override
    public final void onSurfaceChanged(GL10 gl, int width, int height) {

        // Store the new size
        this.size = new Vector2f(width, height);

        // Modify the surface according to the new width and height
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float ratio = (float) width / height;
        gl.glOrthof(-ratio, ratio, -1.0f, 1.0f,
                FOREGROUND_DEPTH - CAMERA_DEPTH - EPSILON,
                BACKGROUND_DEPTH - CAMERA_DEPTH + EPSILON);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
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

        // Prepare to draw the sprites
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, camera.x, camera.y, CAMERA_DEPTH, camera.x, camera.y, 0f, 0f, 1f, 0f);

        // Draw the sprites
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glFrontFace(GL10.GL_CW);
        draw(gl);
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
