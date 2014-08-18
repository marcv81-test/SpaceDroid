package marcv81.game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

public class MenuActivity extends Activity
        implements Thread.UncaughtExceptionHandler, AdapterView.OnItemClickListener {

    private static final int MENU_PLAY = 0;
    private static final int MENU_QUIT = 1;

    private static final String TAG = "AndroidTest";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(this);
        setContentView(R.layout.menu);
        ListView listView = (ListView) findViewById(R.id.menuList);
        listView.setOnItemClickListener(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.w(TAG, "Caught: " + throwable.getClass());
        String message = throwable.getMessage();
        if (message != null)
            Log.w(TAG, message);
        for (StackTraceElement s : throwable.getStackTrace())
            Log.w(TAG, s.toString());
        System.exit(1);
    }

    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        switch((int) id) {
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
