package jp.osaka.himayanen.himayanen;

import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;


public class TimetableActivity extends ActionBarActivity {
    public static String baseURL = "http://data.lodosaka.jp/osaka-events/events20150626.ttl";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timetable);

        Button himayanen = (Button) findViewById(R.id.himayanen);
        himayanen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AsyncTask t = new AsyncTask() {
                    @Override
                    protected Object doInBackground(Object[] params) {
                        String result = doGet();
                        Log.d("himayanen", result + "");
                        return null;
                    }
                };
                t.execute();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_timetable, menu);
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

    public String doGet() {
        HttpClient objHttp = new DefaultHttpClient();
        HttpParams params = objHttp.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 1000); //接続のタイムアウト
        HttpConnectionParams.setSoTimeout(params, 1000); //データ取得のタイムアウト
        String sReturn = "";
        try {

            String querySQL =
                    "PREFIX geo: <http://www.w3.org/2003/01/geo/wgs84_pos#>\n" +
                            "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                            "\n" +
                            "SELECT DISTINCT * WHERE{\n" +
                            "?uri rdfs:label ?label;\n" +
                            "geo:lat ?lat;\n" +
                            "geo:long ?long.\n" +
                            "FILTER ( ?lat > 34.701 && ?lat < 34.709\n" +
                            "&& ?long > 135.49 && ?long < 135.50\n" +
                            ")\n" +
                            "}\n" +
                            "LIMIT 100";

            String sUrl ="http://db.lodc.jp/sparql";
            String url= URLEncoder.encode(baseURL, "UTF-8");
            String query1 = "?default-graph-uri=" + url;
            String enc = URLEncoder.encode(querySQL, "UTF-8");
            String query2 = "&query=" + enc;
            String format = "&format=application%2Fsparql-results%2Bjson";

            HttpGet objGet   = new HttpGet(sUrl+query1+query2+format);

            HttpResponse objResponse = objHttp.execute(objGet);

            InputStream objStream = objResponse.getEntity().getContent();
            InputStreamReader objReader = new InputStreamReader(objStream);
            BufferedReader objBuf = new BufferedReader(objReader);
            StringBuilder objJson = new StringBuilder();
            String sLine;
            while((sLine = objBuf.readLine()) != null){
                objJson.append(sLine);
            }
            sReturn = objJson.toString();
            objStream.close();

        } catch (Exception e) {
            return null;
        }
        return sReturn;
    }
}
