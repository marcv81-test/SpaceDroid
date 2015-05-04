package net.marcv81.gfx2d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.opengl.GLU;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import org.json.*;

import java.io.*;
import java.util.*;

public class Renderer implements GLSurfaceView.Renderer {

    private static final float SPRITE_DEPTH = 0f;
    private static final float CAMERA_DEPTH = -3f;
    private static final float EPSILON = 0.1f;

    private final Context context;

    private final HashMap<String, SpriteGroup> spriteGroups = new HashMap<>();

    private Vector2f camera = new Vector2f(0f, 0f);
    private Vector2f size = new Vector2f(0f, 0f);

    private Engine engine;

    // Constructor
    public Renderer(Context context) {
        this.context = context;
        LoadSpriteGroups(context);
    }

    public void setEngine(Engine engine) {
        this.engine = engine;
    }

    public void setSprites(String str, List<? extends Sprite> list) {
        spriteGroups.get(str).setSprites(list);
    }

    // Set the X and Y camera coordinates
    public void setCamera(Vector2f position) {
        camera.set(position);
    }

    // Get the X and Y camera coordinates
    public Vector2f getCamera() {
        return new Vector2f(camera);
    }

    // Get the X coordinate of the right of the screen
    public float getRight() {
        return camera.getX() + size.getX() / size.getY();
    }

    // Get the X coordinate of the left of the screen
    public float getLeft() {
        return camera.getX() - size.getX() / size.getY();
    }

    // Get the Y coordinate of the top of the screen
    public float getTop() {
        return camera.getY() + 1.0f;
    }

    // Get the Y coordinate of the bottom of the screen
    public float getBottom() {
        return camera.getY() - 1.0f;
    }

    public Vector2f convertScreenToWorld(Vector2f point) {
        return new Vector2f(
                (-2f * point.getX() + size.getX()) / size.getY(),
                (-2f * point.getY() + size.getY()) / size.getY());
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
        for (SpriteGroup spriteGroup : spriteGroups.values()) {
            spriteGroup.getTexture().load(gl, context);
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
                SPRITE_DEPTH - CAMERA_DEPTH - EPSILON,
                SPRITE_DEPTH - CAMERA_DEPTH + EPSILON);
        gl.glMatrixMode(GL10.GL_MODELVIEW);
    }

    @Override
    public final void onDrawFrame(GL10 gl) {

        // Call secret update (engine)
        engine.update();

        // Prepare to draw the sprites
        gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
        gl.glLoadIdentity();
        GLU.gluLookAt(gl, camera.getX(), camera.getY(), CAMERA_DEPTH, camera.getX(), camera.getY(), 0f, 0f, 1f, 0f);

        // Draw the sprites
        gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glFrontFace(GL10.GL_CW);
        for (SpriteGroup spriteGroup : spriteGroups.values()) {
            spriteGroup.draw(gl);
        }
        gl.glDisableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
        gl.glDisableClientState(GL10.GL_VERTEX_ARRAY);
    }

    private void LoadSpriteGroups(Context context) {

        try {
            // Open sprites JSON definition
            int id = context.getResources().getIdentifier("sprites", "raw", context.getPackageName());
            InputStream inputStream = context.getResources().openRawResource(id);

            // Read InputStream
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            StringBuilder stringBuilder = new StringBuilder();
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String read = bufferedReader.readLine();
            while (read != null) {
                stringBuilder.append(read);
                read = bufferedReader.readLine();
            }

            // Instantiate JSONObject from String
            JSONObject json = new JSONObject(stringBuilder.toString());

            // Instantiate SpriteGroupConfigs
            JSONArray array = json.getJSONArray("sprites");
            for (int i = 0; i < array.length(); i++) {

                String name = array.getJSONObject(i).getString("name");
                String textureFilename = array.getJSONObject(i).getString("textureFilename");
                int animationsX = array.getJSONObject(i).getInt("animationsX");
                int animationsY = array.getJSONObject(i).getInt("animationsY");
                float size = (float) array.getJSONObject(i).getDouble("size");
                boolean supportAngle = array.getJSONObject(i).getBoolean("supportAngle");
                boolean supportTransparency = array.getJSONObject(i).getBoolean("supportTransparency");
                boolean supportScaling = array.getJSONObject(i).getBoolean("supportScaling");

                SpriteTexture texture = new SpriteTexture(textureFilename, animationsX, animationsY);
                SpriteGeometry geometry = new SpriteGeometry(size);
                SpriteGroup spriteGroup = new SpriteGroup(texture, geometry,
                        supportAngle, supportTransparency, supportScaling);

                spriteGroups.put(name, spriteGroup);
            }
        }

        // Rethrow checked exceptions as runtime exceptions
        catch (JSONException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
