package marcv81.game;

import marcv81.gfx2d.Sprite;

public class PlayerSprite extends Sprite {

    private static final float PLAYER_SCALE = 0.2f;

    private static final int PLAYER_RESOURCE = R.drawable.player;
    private static final int PLAYER_GFX_X = 1;
    private static final int PLAYERL_GFX_Y = 1;

    // Constructor
    PlayerSprite() {
        super(PLAYER_RESOURCE, PLAYER_GFX_X, PLAYERL_GFX_Y, PLAYER_SCALE);
    }
}
