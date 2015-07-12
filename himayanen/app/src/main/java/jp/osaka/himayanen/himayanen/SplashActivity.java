package jp.osaka.himayanen.himayanen;

//import android.app.ActionBar;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;


public class SplashActivity extends ActionBarActivity {
    private Handler mHandler = new Handler();
    private Runnable mTransition = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent();
            intent.setClassName(SplashActivity.this, "jp.osaka.himayanen.himayanen.TimetableActivity");
            startActivity(intent);
            SplashActivity.this.finish();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mHandler.postDelayed(mTransition, 2500);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mTransition);
    }
}
