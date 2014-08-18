package marcv81.game;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import marcv81.gfx2d.DebugActivity;

public class MenuActivity extends DebugActivity implements OnItemClickListener {

    private static final int MENU_PLAY = 0;
    private static final int MENU_QUIT = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        ListView listView = (ListView) findViewById(R.id.menuList);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        switch ((int) id) {
            case MENU_PLAY:
                Intent intent = new Intent(this, GameActivity.class);
                startActivity(intent);
                break;
            case MENU_QUIT:
                finish();
                break;
        }
    }
}
