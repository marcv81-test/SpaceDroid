package net.marcv81.gfx2d;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

import java.util.List;

public final class Gfx2dView extends GLSurfaceView {

    // Camera
    private Vector2f camera = new Vector2f(0f, 0f);
    private Vector2f size = new Vector2f (0f, 0f);

    // Pointer
    private Vector2f pointer = new Vector2f(0f, 0f);
    private boolean pointerDown = false;

    public Gfx2dView(Context context) {
        super(context);
    }

    public void setRenderer(Gfx2dEngine engine, List<SpriteGroup> spriteGroups) {
        setRenderer(new Gfx2dRenderer(getContext(), engine, this, spriteGroups));
    }

    // Set the X and Y camera coordinates
    public void setCamera(Vector2f position) {
        camera.set(position);
    }

    // Get the X and Y camera coordinates
    public Vector2f getCamera() {
        return new Vector2f(camera);
    }

    // Get the X and Y pointer coordinates
    public Vector2f getPointer() {
        return pointer;
    }

    public boolean isPointerDown() {
        return pointerDown;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                pointerDown = true;
                pointer = new Vector2f(screenToWorldX(x), screenToWorldY(y));
                break;
            case MotionEvent.ACTION_MOVE:
                pointer = new Vector2f(screenToWorldX(x), screenToWorldY(y));
                break;
            case MotionEvent.ACTION_UP:
                pointerDown = false;
                break;
            default:
                break;
        }

        return true;
    }

    public void setSize(Vector2f size) {
        this.size = size;
    }

    public float getWorldRight() {
        return camera.getX() + size.getX() / size.getY();
    }

    public float getWorldLeft() {
        return camera.getX() - size.getX() / size.getY();
    }

    public float getWorldTop() {
        return camera.getY() + 1.0f;
    }

    public float getWorldBottom() {
        return camera.getY() - 1.0f;
    }

    // Convert X coordinate from screen to world
    public float screenToWorldX(float x) {
        return (-2f * x + size.getX()) / size.getY();
    }

    // Convert Y coordinate from screen to world
    public float screenToWorldY(float y) {
        return (-2f * y + size.getY()) / size.getY();
    }
}
