package com.hcmut.testiq.http_requests;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * Created by thach on 5/7/17.
 */

public class HttpRequest {
    // get data from api url, return String data
    public String getDataFromAPI(String baseUrl, final String METHOD, HashMap<String, String> paramList) throws IOException {
        URL url = new URL(baseUrl);
        HttpURLConnection connect = (HttpURLConnection) url.openConnection();
        connect.setRequestMethod(METHOD);
        connect.setDoInput(true);

        if (METHOD.equals("POST")){
            connect.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            Uri.Builder builder = new Uri.Builder();

            for(HashMap.Entry<String, String> param: paramList.entrySet()){
                builder.appendQueryParameter(param.getKey(), param.getValue());
            }

            String query = builder.build().getEncodedQuery();

            connect.setDoOutput(true);
            OutputStream os = connect.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            writer.write(query);
            writer.flush();
            writer.close();
            os.close();
        }
        connect.connect();

        BufferedReader reader = new BufferedReader(new InputStreamReader(connect.getInputStream()));

        String line;
        StringBuilder response = new StringBuilder();

        while ((line = reader.readLine()) != null){
            response.append(line);
        }

        reader.close();
        connect.disconnect();

        return response.toString();
    }
}
