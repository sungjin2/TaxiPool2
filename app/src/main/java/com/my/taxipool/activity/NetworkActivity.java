package com.my.taxipool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.my.taxipool.R;
import com.my.taxipool.vo.CustomerInfo;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class NetworkActivity extends AppCompatActivity {
    private String cookieValues="";

    Handler handler = new Handler();
    CustomerInfo info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);

        Intent intent = getIntent();
        final long id = intent.getExtras().getLong("id");
        final String profile = intent.getExtras().getString("profile");
        //URI url = new URI(profile);
        //TextView tv = (TextView)findViewById(R.id.k_id);
        //tv.setText(id);

        TextView kid = (TextView)findViewById(R.id.tv_network);
        // 성별 아직
        Button bt_commit = (Button)findViewById(R.id.btn_network);

        bt_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                info = new CustomerInfo(Long.toString(id), phone.getText().toString(), name.getText().toString(), nickname.getText().toString(), "M");
//                Log.i("SuccessActivity",info.toString());


                new Thread() {
                    @Override
                    public void run() {
                        URL url;
                        HttpURLConnection conn;
                        //super.run();
                        try {
                            String queryString = "info_id=pk&phone_no=테스트&info_name=테스트&nickname=네스트&info_gender=남";
//                            String queryString = "info_id="+info.getInfo_id()+
//                                    "&phone_no="+info.getPhone_no()+
//                                    "&info_name="+info.getInfo_name()+
//                                    "&nickname="+info.getNickname()+
//                                    "&info_gender="+info.getInfo_gender();
                            url = new URL("http://192.168.12.30:8888/taxi_db_test/customertest.do?"+ queryString);

                            //url = new URL("http://192.168.12.30:8888/taxi_db_test/customertest.do?info_id=%ED%85%8C%EC%8A%A4%ED%8A%B8&phone_no=%ED%85%8C%EC%8A%A4%ED%8A%B8&info_name=%ED%85%8C%EC%8A%A4%ED%8A%B8&nickname=%EB%84%A4%EC%8A%A4%ED%8A%B8&info_gender=%EB%82%A8");
                            conn = (HttpURLConnection)url.openConnection();
                            conn.setRequestMethod("GET");//405, 방
                            conn.setDoInput(true);
                            conn.setDoOutput(true);
                            conn.setUseCaches(false);
                            Log.i("SuccessActivity","conn1"+ conn.getRequestMethod());


                            /*String queryString  =  "info_id="+customer.getInfo_id()+
                                    "&phone_no="+customer.getPhone_no()+
                                    "&info_name="+customer.getInfo_name()+
                                    "&nickname="+customer.getNickname()+
                                    "&info_gender="+customer.getInfo_gender();*/


                            //OutputStream os = conn.getOutputStream();
                            //BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                            //bw.write(queryString);
                            Log.i("SuccessActivity","conn2");

                            //bw.flush();
                            //bw.close();

                            final int responseCode = conn.getResponseCode();
                            Log.i("SuccessActivity","conn3");

                            switch (responseCode){
                                case HttpURLConnection.HTTP_OK:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "정상응답", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                    //응답 header 확인
                                    Map<String, List<String>> responseHeaders = conn.getHeaderFields();
                                    Set<String> keys = responseHeaders.keySet();
                                    Log.i("HttpNetwork", "응답헤더목록*******************************************");
                                    for(String key : keys){
                                        List<String>values = responseHeaders.get(key);
                                        Log.i("HttpNetwork", key+"="+values.toString());
                                        if("Set-Cookie".equals(key)){
                                            for(String value: values) {
                                                cookieValues += value;
                                                cookieValues += ";";
                                            }
                                        }
                                    }
                                    //응답결과 수신
                                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                                    String responseData = null;
                                    while((responseData = br.readLine())!=null) {
                                        Log.i("HttpNetwork","응답결과:"+responseData);
                                    }
                                    break;
                                case HttpURLConnection.HTTP_NOT_FOUND:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "NOT FOUND", Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    break;
                                default:
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(getApplicationContext(), "response code:" + responseCode, Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                    break;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }.start();

            }
        });


    }
}

