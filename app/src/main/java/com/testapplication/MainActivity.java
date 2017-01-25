package com.testapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {

    ActionBar actionBar;
//    Button btnNwCall;
    ListView listView;
    List<RowEntry> rowEntries;
    SwipeRefreshLayout swipeContainer;

//    URL for network call
//    String urlStr = "https://www.google.co.in/url?q=https%3A%2F%2Fdl.dropboxusercontent.com%2Fu%2F746330%2Ffacts.json&sa=D&sntz=1&usg=AFQjCNHW9vc0qfXTV_GAhYMgnhLqbkQvoQ";
    String urlStr = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getActionBar();
        listView = (ListView) findViewById(R.id.listview);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //        Network Call for the first time
        try {
            URL url = new URL(urlStr);
            new NetworkCall().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

//        Uncomment below for button network call
        /*btnNwCall = (Button) findViewById(R.id.btn_nwcall);
        btnNwCall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    URL url = new URL(urlStr);

                    new NetworkCall().execute(url);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        });*/

//        For pulldown refresh functionality
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);

                try {
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
        String actionBarTitle = "";

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

//            JSON Parsing Logic
            try {
                if(null != jsonObject) {

                    actionBarTitle = jsonObject.getString("title");
                    actionBar.setTitle(actionBarTitle);
                    Log.v("NetworkCall", "jsonObject_title*" + actionBarTitle);

                    JSONArray rows = jsonObject.getJSONArray("rows");
                    rowEntries = new ArrayList<RowEntry>();

                    for (int i=0; i<rows.length(); i++) {
                        JSONObject row = rows.getJSONObject(i);

                        RowEntry rowData = new RowEntry();
                        rowData.setTitle(row.getString("title"));
                        rowData.setDescription(row.getString("description"));
                        rowData.setImageHref(row.getString("imageHref"));

                        rowEntries.add(rowData);
                    }

                    CustomList adapter = new CustomList(MainActivity.this, rowEntries.toArray(new RowEntry[0]));
                    listView.setAdapter(adapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.v("NetworkCall", " onPostExecute exiting.");

        }
    }

}
