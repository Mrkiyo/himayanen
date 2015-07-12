package jp.osaka.himayanen.himayanen;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class MainActivity extends ActionBarActivity {
    private LocationManager mLocationManager;
    private LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            //Toast.makeText(MainActivity.this, location.toString(), Toast.LENGTH_SHORT).show();
            Log.d("Himayanen-Location", location.toString());

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Toast.makeText(MainActivity.this, "AVAILABLE", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Toast.makeText(MainActivity.this, "OUT_OF_SERVICE", Toast.LENGTH_SHORT).show();
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Toast.makeText(MainActivity.this, "TEMPORARY_UNAVAILABLE", Toast.LENGTH_SHORT).show();
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText(MainActivity.this, "onProviderEnabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(MainActivity.this, "onProviderDisabled", Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = (Button) findViewById(R.id.button);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doGet("url"); //ここにオープンデータのURLをいれる
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // ロケーションマネージャのインスタンスを取得する
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 位置情報の更新を受け取るように設定
        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, // プロバイダ
                100, // 通知のための最小時間間隔
                0, // 通知のための最小距離間隔
                mLocationListener); // 位置情報リスナー
    }

    @Override
    protected void onPause() {
        super.onPause();
        mLocationManager.removeUpdates(mLocationListener);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public String doGet(String url) {
        try {
            HttpGet method = new HttpGet(url);

            DefaultHttpClient client = new DefaultHttpClient();

            // ヘッダを設定する
            method.setHeader("Connection", "Keep-Alive");

            HttpResponse response = client.execute(method);
            int status = response.getStatusLine().getStatusCode();
            if (status != HttpStatus.SC_OK)
                throw new Exception("");

            return EntityUtils.toString(response.getEntity(), "UTF-8");
        } catch (Exception e) {
            return null;
        }
    }

}
