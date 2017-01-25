package com.testapplication;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
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
    ListView listView;
    List<RowEntry> rowEntries;
    SwipeRefreshLayout swipeContainer;

    //  URL for network call
    String urlStr = "https://dl.dropboxusercontent.com/u/746330/facts.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        actionBar = getActionBar();
        listView = (ListView) findViewById(R.id.listview);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.swipeContainer);

        //  Network Call for the first time
        try {

            Log.i("MainActivity", "onCreate: Network Call for the first time.");
            Log.d("MainActivity", "onCreate: URL*" + urlStr);

            URL url = new URL(urlStr);
            new NetworkCall().execute(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        //  For pulldown refresh functionality
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                try {

                    Log.i("swipeContainer","setOnRefreshListener: Making network call.");
                    Log.d("swipeContainer", "setOnRefreshListener: URL*" + urlStr);

                    URL url = new URL(urlStr);
                    new NetworkCall().execute(url);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    /*
    *   AsyncTask for handling pre- & post- network call execution
    * */

    public class NetworkCall extends AsyncTask<URL, Integer, Long>{

        JSONObject jsonObject = null;
        String actionBarTitle = "";

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            swipeContainer.setRefreshing(true);
        }

        @Override
        protected Long doInBackground(URL... urls) {

            URL url = urls[0];
            Log.d("NetworkCall","doInBackground: URL*"+url.toString());
            jsonObject = new NetworkJSONRetriever().fetchFromNetwork(url);
            return 0l;
        }

        @Override
        protected void onPostExecute(Long aLong) {
            super.onPostExecute(aLong);
            Log.i("NetworkCall", "Entering onPostExecute");

            //  JSON Parsing Logic
            Log.i("NetworkCall", "Parsing JSON");
            try {
                if(null != jsonObject) {

                    actionBarTitle = jsonObject.getString("title");
                    actionBar.setTitle(actionBarTitle);
                    Log.d("NetworkCall", "jsonObject: title*" + actionBarTitle);

                    JSONArray rows = jsonObject.getJSONArray("rows");
                    Log.d("NetworkCall", "jsonObject: ArraySize*" + rows.length());

                    rowEntries = new ArrayList<RowEntry>();

                    for (int i=0; i<rows.length(); i++) {
                        JSONObject row = rows.getJSONObject(i);

                        String rowTitle = row.getString("title").equals("null")?"":row.getString("title");
                        String rowDesc = row.getString("description").equals("null")?"":row.getString("description");
                        //  To handle Picasso empty path exception, it not set to empty String
                        String rowImageHref = row.getString("imageHref");

                        RowEntry rowData = new RowEntry();
                        rowData.setTitle(rowTitle);
                        rowData.setDescription(rowDesc);
                        rowData.setImageHref(rowImageHref);

                        //  Hiding the row if all the data of the row is null
                        boolean hideRowFlag = rowTitle.isEmpty() && rowDesc.isEmpty() && rowImageHref.equals("null");

                        if(!hideRowFlag) {
                            rowEntries.add(rowData);
                        }
                    }

                    CustomList adapter = new CustomList(MainActivity.this, rowEntries.toArray(new RowEntry[0]));
                    listView.setAdapter(adapter);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            swipeContainer.setRefreshing(false);

            Log.i("NetworkCall", "Exiting onPostExecute");

        }
    }

}
