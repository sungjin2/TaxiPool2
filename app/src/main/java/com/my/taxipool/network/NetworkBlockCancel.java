package com.my.taxipool.network;

import android.util.Log;
import android.widget.Toast;

import com.my.taxipool.activity.BlockActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by sungjin on 2017-06-21.
 */

public class NetworkBlockCancel {
    public void sendData(final String info_id, final String block_info_id) {
        Log.d("NetworkBlockCancel1",info_id);


                new Thread() {
                    @Override
                    public void run() {
                        URL url;
                HttpURLConnection conn;

                try {
                    String queryString = "info_id="+info_id+"&block_info_id="+block_info_id;

                    Log.i("SuccessActivity",info_id);

                    url = new URL("http://192.168.12.30:8888/taxi_db_test2/blocklistcancel.do");//?"+queryString);

                    conn = (HttpURLConnection)url.openConnection();
                    conn.setRequestMethod("POST");
                    conn.setDoInput(true);
                    conn.setDoOutput(true);
                    conn.setUseCaches(false);
                    Log.i("SuccessActivity","conn1"+ conn.getRequestMethod());

                    //QueryString을 추가하는 부분, UTF-8로 한글까지 Server에서 이상없이 받을 수 있음
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    bw.write(queryString);
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
                            break;
                        case HttpURLConnection.HTTP_NOT_FOUND:
                            break;
                        default:
                            //HTTP_OK가 아닌 경우 Error를 Toast로 나타냅니다.
                            break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
        /*
        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();*/
    }
}
