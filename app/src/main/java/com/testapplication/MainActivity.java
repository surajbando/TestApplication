package com.testapplication;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends Activity {


    Button btnNwCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnNwCall = (Button) findViewById(R.id.btn_nwcall);

        btnNwCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {

//                    String urlStr = "https://www.google.co.in/url?q=https%3A%2F%2Fdl.dropboxusercontent.com%2Fu%2F746330%2Ffacts.json&sa=D&sntz=1&usg=AFQjCNHW9vc0qfXTV_GAhYMgnhLqbkQvoQ";
                    String urlStr = "https://dl.dropboxusercontent.com/u/746330/facts.json";
                    URL url = new URL(urlStr);

                    new NetworkCall().execute(url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public class NetworkCall extends AsyncTask<URL, Integer, Long>{

        JSONObject jsonObject = null;

        @Override
        protected Long doInBackground(URL... urls) {

            URL url = urls[0];

            jsonObject = new NetworkJSONRetriever().fetchFromNetwork(url);

            return 0l;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Log.v("NetworkCall", " onPostExecute entering.");

            try {
                if(null != jsonObject) {
                    Log.v("NetworkCall", "jsonObject_title*" + jsonObject.getString("title"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("NetworkCall", " onPostExecute exiting.");

        }
    }

}
