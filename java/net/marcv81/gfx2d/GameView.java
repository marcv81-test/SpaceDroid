package net.marcv81.gfx2d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;
import android.view.MotionEvent;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.List;

/**
 * This class handles OpenGL ES 2D game views. It allows to move the camera
 * and track the pointer position.
 */
public final class GameView extends GLSurfaceView implements GLSurfaceView.Renderer {

    private static final float SPRITE_DEPTH = 0f;
    private static final float CAMERA_DEPTH = -3f;
    private static final float EPSILON = 0.1f;

    /**
     * GameView size in pixels.
     */
    private final Vector2f size = new Vector2f(0f, 0f);

    /**
     * Camera position in world coordinates.
     */
    private final Vector2f camera = new Vector2f(0f, 0f);

    /**
     * Pointer position in world coordinates.
     */
    private final Vector2f pointer = new Vector2f(0f, 0f);

    /**
     * Whether the pointer is currently active or not.
     */
    private boolean pointerActive = false;

    /**
     * SpriteRenderers used to render the different sprites.
     */
    private final List<SpriteRenderer> spriteRenderers;

    /**
     * GameEngine called back before rendering each frame.
     */
    private final GameEngine gameEngine;

    /**
     * Constructor.
     */
    public GameView(Context context, GameEngine gameEngine, List<SpriteRenderer> spriteRenderers) {

        super(context);

        this.gameEngine = gameEngine;
        this.spriteRenderers = spriteRenderers;
    }

    /**
     * Sets the renderer.
     */
    public void setRenderer() {
        setRenderer(this);
    }

    /**
     * Gets the camera position in world coordinates.
     */
    public Vector2f getCamera() {
        return new Vector2f(camera);
    }

    /**
     * Sets the camera position in world coordinates.
     */
    public void setCamera(Vector2f position) {
        camera.set(position);
    }

    /**
     * Gets the pointer position in world coordinates.
     */
    public Vector2f getPointer() {
        return new Vector2f(pointer);
    }

    /**
     * Tests whether the pointer is currently active or not.
     */
    public boolean isPointerActive() {
        return pointerActive;
    }

    /**
     * Gets the abscissa of the right edge of this GameView in world coordinates.
     */
    public float getRightEdge() {
        return camera.getX() + size.getX() / size.getY();
    }

    /**
     * Gets the abscissa of the left edge of this GameView in world coordinates.
     */
    public float getLeftEdge() {
        return camera.getX() - size.getX() / size.getY();
    }

    /**
     * Gets the ordinate of the top edge of this GameView in world coordinates.
     */
    public float getTopEdge() {
        return camera.getY() + 1.0f;
    }

    /**
     * Gets the ordinate of the bottom edge of this GameView in world coordinates.
     */
    public float getBottomEdge() {
        return camera.getY() - 1.0f;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointerActive = true;
                pointer.set(new Vector2f(pixelsToWorldX(x), pixelsToWorldY(y)));
                break;
            case MotionEvent.ACTION_MOVE:
                pointer.set(new Vector2f(pixelsToWorldX(x), pixelsToWorldY(y)));
                break;
            case MotionEvent.ACTION_UP:
                pointerActive = false;
                break;
            default:
                break;
        }

        return true;
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
        for (SpriteRenderer spriteGroup : spriteRenderers) {
            spriteGroup.getTexture().load(gl, getContext());
        }
    }

    @Override
    public final void onSurfaceChanged(GL10 gl, int width, int height) {

        // Update the gameView size
        size.set(new Vector2f(width, height));

        // Modify the surface according to the new width and height
        gl.glViewport(0, 0, width, height);
        gl.glMatrixMode(GL10.GL_PROJECTION);
        gl.glLoadIdentity();
        float ratio = (float) width / height;
        gl.glOrthof(-ratio, ratio, -1.0f, 1.0f,
                SPRITE_DEPTH - CAMERA_DEPTH - EPSILON,
                SPRITE_DEPTH - CAMERA_DEPTH + EPSILON);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public final void onDrawFrame(GL10 gl) {

        // Update the game engine
        gameEngine.update();

        // Prepare to draw the sprites
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, camera.getX(), camera.getY(), CAMERA_DEPTH,
                camera.getX(), camera.getY(), 0f, 0f, 1f, 0f);

        // Draw the sprites
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glFrontFace(GL10.GL_CW);
        for (SpriteRenderer spriteGroup : spriteRenderers) {
            spriteGroup.draw(gl);
        }
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    /**
     * Converts abscissa from pixels to world coordinates.
     */
    private float pixelsToWorldX(float x) {
        return (-2f * x + size.getX()) / size.getY();
    }

    /**
     * Converts ordinate from pixels to world coordinates.
     */
    private float pixelsToWorldY(float y) {
        return (-2f * y + size.getY()) / size.getY();
    }
}
