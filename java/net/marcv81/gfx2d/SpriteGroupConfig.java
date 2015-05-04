package net.marcv81.gfx2d;

public class SpriteGroupConfig {

    private String textureFilename;
    private int animationsX;
    private int animationsY;
    private float size;
    private boolean supportAngle;
    private boolean supportTransparency;
    private boolean supportScaling;

    public SpriteGroupConfig(String textureFilename, int animationsX, int animationsY, float size,
                             boolean supportAngle, boolean supportTransparency, boolean supportScaling) {

        this.textureFilename = textureFilename;
        this.animationsX = animationsX;
        this.animationsY = animationsY;
        this.size = size;
        this.supportAngle = supportAngle;
        this.supportTransparency = supportTransparency;
        this.supportScaling = supportScaling;
    }

    public String getTextureFilename() {
        return textureFilename;
    }

    public int getAnimationsX() {
        return animationsX;
    }

    public int getAnimationsY() {
        return animationsY;
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
