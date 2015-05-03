package net.marcv81.gfx2d;

public class SpriteGroupConfig {

    private String textureFilename;
    private int animationX;
    private int animationY;
    private float size;
    private boolean supportAngle;
    private boolean supportTransparency;
    private boolean supportScaling;

    public SpriteGroupConfig(String textureFilename, int animationX, int animationY, float size,
                             boolean supportAngle, boolean supportTransparency, boolean supportScaling) {

        this.textureFilename = textureFilename;
        this.animationX = animationX;
        this.animationY = animationY;
        this.size = size;
        this.supportAngle = supportAngle;
        this.supportTransparency = supportTransparency;
        this.supportScaling = supportScaling;
    }

    public String getTextureFilename() {
        return textureFilename;
    }

    public int getAnimationX() {
        return animationX;
    }

    public int getAnimationY() {
        return animationY;
    }

    public float getSize() {
        return size;
    }

    public boolean isAngleSupported() {
        return supportAngle;
    }

    public boolean isTransparencySupported() {
        return supportTransparency;
    }

    public boolean isScalingSupported() {
        return supportScaling;
    }
}
