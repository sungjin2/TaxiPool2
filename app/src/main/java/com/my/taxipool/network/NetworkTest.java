package com.my.taxipool.network;

/* Created by Hyeon on 2017-05-24.
*/

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NetworkTest {


    public JSONObject jsonNetwork(String url){
        HttpURLConnection c = null;
        Log.d("dduni url: ",url.toString());
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(3000);
            c.setReadTimeout(3000);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    Log.d("dduni LOG: ", sb.toString());
                    if(sb.toString().length()>0){
                        return new JSONObject(sb.toString());
                    }else{
                        return new JSONObject();
                    }
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String stringNetwork(String url){
        HttpURLConnection c = null;
        Log.d("dduni url: ",url.toString());
        try {
            URL u = new URL(url);
            c = (HttpURLConnection) u.openConnection();
            c.setUseCaches(false);
            c.setAllowUserInteraction(false);
            c.setConnectTimeout(3000);
            c.setReadTimeout(3000);
            c.connect();
            int status = c.getResponseCode();

            switch (status) {
                case 200:
                case 201:
                    BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    StringBuilder sb = new StringBuilder();
                    String line;
                    while ((line = br.readLine()) != null) {
                        sb.append(line+"\n");
                    }
                    br.close();
                    Log.d("dduni LOG: ", sb.toString());
                    if(sb.toString().length()>0){
                        return sb.toString();
                    }else{
                        return "";
                    }
            }
        }catch (MalformedURLException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private String readStream(InputStream in) throws IOException {
        StringBuilder jsonHtml = new StringBuilder();
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "UTF-8"));
        String line = null;

        while((line = reader.readLine()) != null)
            jsonHtml.append(line);
        reader.close();
        return jsonHtml.toString();
    }
}