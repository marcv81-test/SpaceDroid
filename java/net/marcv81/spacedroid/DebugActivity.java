package net.marcv81.spacedroid;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class DebugActivity extends Activity implements Thread.UncaughtExceptionHandler {

    private static final String TAG = "DEBUG";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Log.w(TAG, "Caught: " + throwable.getClass());
        String message = throwable.getMessage();
        if (message != null) {
            Log.w(TAG, message);
        }
        for (StackTraceElement s : throwable.getStackTrace()) {
            Log.w(TAG, s.toString());
        }
        finish();
    }
}
