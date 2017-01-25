package com.testapplication;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/*
* This class is used to make the network call and return the response in JSONObject
* */

public class NetworkJSONRetriever {

    public JSONObject fetchFromNetwork(URL url) {
        JSONObject retJsonObject = null;
        InputStream is = null;

        try {

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(3000);
            conn.setInstanceFollowRedirects(false);
            conn.setRequestMethod("GET");

            conn.connect();

            int responseCode = conn.getResponseCode();

            Log.d("fetchFromNetwork", "responseCode*"+responseCode);

            if (responseCode == conn.HTTP_OK) {

                is = conn.getInputStream();

                BufferedReader r = new BufferedReader(new InputStreamReader(is));
                StringBuilder total = new StringBuilder();
                String line;

                while((line = r.readLine()) != null){
                    total.append(line);
                    total.append('\n');
                }

                retJsonObject = new JSONObject(total.toString());
                Log.v("NetworkJSONRetriever : ", "Connection Response Recieved");

            }

        }catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return retJsonObject;
    }


}
