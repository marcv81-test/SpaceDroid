package net.marcv81.gfx2d;

import android.content.Context;
import android.opengl.GLSurfaceView.Renderer;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import java.util.List;

public final class Gfx2dRenderer implements Renderer {

    private static final float SPRITE_DEPTH = 0f;
    private static final float CAMERA_DEPTH = -3f;
    private static final float EPSILON = 0.1f;

    private final Context context;
    private final Gfx2dEngine engine;
    private final Gfx2dView view;
    private final List<SpriteGroup> spriteGroups;

    // Constructor
    public Gfx2dRenderer(Context context, Gfx2dEngine engine, Gfx2dView view, List<SpriteGroup> spriteGroups) {
        this.context = context;
        this.engine = engine;
        this.view = view;
        this.spriteGroups = spriteGroups;
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
        for (SpriteGroup spriteGroup : spriteGroups) {
            spriteGroup.getTexture().load(gl, context);
        }
    }

    @Override
    public final void onSurfaceChanged(GL10 gl, int width, int height) {

        view.setSize(new Vector2f(width, height));

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

        // Update the engine
        engine.update();

        // Prepare to draw the sprites
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, view.getCamera().getX(), view.getCamera().getY(), CAMERA_DEPTH,
                view.getCamera().getX(), view.getCamera().getY(), 0f, 0f, 1f, 0f);

        // Draw the sprites
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glFrontFace(GL10.GL_CW);
        for (SpriteGroup spriteGroup : spriteGroups) {
            spriteGroup.draw(gl);
        }
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }
}
