package com.my.taxipool.network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Hyeon on 2017-06-21.
 */

public class Network {
    URL url;
    HttpURLConnection conn;
    String result;

    public JSONArray jsonArrayNetwork(String str_url,String query){
        try{
            url = new URL(str_url);//?"+queryString);

            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            Log.i("SuccessActivity","conn1"+ conn.getRequestMethod());

            //QueryString을 추가하는 부분, UTF-8로 한글까지 Server에서 이상없이 받을 수 있음
            OutputStream os = conn.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(query);
            bw.flush();
            bw.close();

            final int responseCode = conn.getResponseCode();

            switch (responseCode){
                case HttpURLConnection.HTTP_OK:
                    //응답결과 수신
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String responseData = null;
                    while((responseData = br.readLine())!=null) {
                        Log.i("HttpNetwork","응답결과:"+responseData);
                    }
                    result = responseData;
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    break;
                default:
                    //HTTP_OK가 아닌 경우 Error를 Toast로 나타냅니다.
                    break;
            }

            if(result!=null){
                return new JSONArray(result);
            }else return new JSONArray();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public String stringNetwork(String str_url,String query){
        try{
            url = new URL(str_url);//?"+queryString);

            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            Log.i("SuccessActivity","conn1"+ conn.getRequestMethod());

            //QueryString을 추가하는 부분, UTF-8로 한글까지 Server에서 이상없이 받을 수 있음
            OutputStream os = conn.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(query);
            bw.flush();
            bw.close();

            final int responseCode = conn.getResponseCode();

            switch (responseCode){
                case HttpURLConnection.HTTP_OK:
                    //응답결과 수신
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String responseData = null;
                    while((responseData = br.readLine())!=null) {
                        Log.i("HttpNetwork","응답결과:"+responseData);
                    }
                    result = responseData;
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    break;
                default:
                    //HTTP_OK가 아닌 경우 Error를 Toast로 나타냅니다.
                    break;
            }

            if(result!=null){
                return result;
            }else return "";

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public JSONObject jsonObjectNetwork(String str_url, String query){
        try{
            url = new URL(str_url);//?"+queryString);

            conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            Log.i("SuccessActivity","conn1"+ conn.getRequestMethod());

            //QueryString을 추가하는 부분, UTF-8로 한글까지 Server에서 이상없이 받을 수 있음
            OutputStream os = conn.getOutputStream();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
            bw.write(query);
            bw.flush();
            bw.close();

            final int responseCode = conn.getResponseCode();

            switch (responseCode){
                case HttpURLConnection.HTTP_OK:
                    //응답결과 수신
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    String responseData = null;
                    while((responseData = br.readLine())!=null) {
                        Log.i("HttpNetwork","응답결과:"+responseData);
                    }
                    result = responseData;
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    break;
                default:
                    //HTTP_OK가 아닌 경우 Error를 Toast로 나타냅니다.
                    break;
            }

            if(result!=null){
                return new JSONObject(result);
            }else return new JSONObject();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
