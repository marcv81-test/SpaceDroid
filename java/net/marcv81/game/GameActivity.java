package marcv81.game;

import android.os.Bundle;
import marcv81.gfx2d.DebugActivity;

public class GameActivity extends DebugActivity {

    private GameView view;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new GameView(this);
        setContentView(view);
    }

    @Override
    protected void onPause() {
        super.onPause();
        view.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        view.onResume();
    }

    @Override
    public void onBackPressed() {
        if (view.getPaused() == true) {
            finish();
        } else {
            view.setPaused(true);
        }
    }
}

