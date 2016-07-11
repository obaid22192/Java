/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Proxies;

import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.io.IOException;

/**
 *
 * @author obaid92
 */
public class GoogleMatrixApis {
    private static final String API_KEY = "AIzaSyDxi7M2tG70szAJbuyKSZAleKurhc1jqpE";
    
    OkHttpClient client = new OkHttpClient();

    public String run(String origin, String destination) throws IOException {
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=";
        url += origin + "&destinations=" + destination + "&mode=bicycling&language=en-US&key=" + API_KEY; 
        Request request = new Request.Builder()
            .url(url)
            .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
     }
}
