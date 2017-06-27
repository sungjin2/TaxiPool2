package com.my.taxipool.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.my.taxipool.R;
import com.my.taxipool.adapter.PointRecordAdapter;
import com.my.taxipool.util.CommuServer;
import com.my.taxipool.util.Set;
import com.my.taxipool.vo.BlockCustomerInfo;
import com.my.taxipool.vo.PointRecordInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * Created by sungjin on 2017-06-26.
 */

public class PointListActivity extends AppCompatActivity {

    private RecyclerView lecyclerView;
    private String info_id;
    private JSONArray result;
    ArrayList<PointRecordInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointrecord);
        info_id = Set.Load(getApplication(), "info_id", null);
        data = new ArrayList<>();


        initLayout();
        initData();

        if(data != null) {

            Log.d("PointListActivity", "data가 null이 아닙니다..");
        }else{
            Log.d("PointListActivity", "data가 null입니다..");
        }
    }

    private void initLayout() {
        lecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
    }

    private void initData() {

        new CommuServer("http://192.168.12.30:8888/taxi_db_test2/pointrecord.do", new CommuServer.OnCommuListener() {
            @Override

            public void onSuccess(JSONObject object, JSONArray arr, String str) {

                Log.d("PointListActivity", "LIST 조회 성공");
                result = arr;
                try {
                    for (int i = 0; i < result.length(); i++) {
                        Log.d("PointListActivity", "for문에 왔씁니다..");

                        String s_date = result.getJSONObject(i).getString("date");
                        long l_date = Long.parseLong(s_date);
                        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd");
                        Log.d("PointListActivity1",s_date +" "+result.getJSONObject(i).getString("point"));

                        //Date date = transFormat.parse(s_date);
                        Date date = new Date(l_date);
                        String a = transFormat.format(date);
                        Log.d("PointListActivity233", " "+a);
                        Log.d("PointListActivity2", " "+date);

                        String s_type = result.getJSONObject(i).getString("type");
                        Log.d("PointListActivity4", " "+s_type);
                        int type = Integer.parseInt(s_type);
                        Log.d("PointListActivity5"," "+type);


                        data.add(
                                new PointRecordInfo(
                                        info_id, result.getJSONObject(i).getString("point"), date, type)
                        );
                    }

                    lecyclerView.setAdapter(new PointRecordAdapter(data, R.layout.view_point_list_info));
                    lecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    lecyclerView.setItemAnimator(new DefaultItemAnimator());

                } catch (JSONException e) {
                    Log.d("PointListActivity", "JSONException");
                    e.printStackTrace();
                } /*catch (ParseException e) {
                    Log.d("PointListActivity", "String to Date 에러!");
                }*/
            }

            @Override
            public void onFailed(Error error) {
                Log.i("BlockActivity", "LIST 조회 실패");
            }
        }).addParam("info_id", "447433869").start();



    }
}

/*for (int i = 0; i < ) {;
            PointRecordInfo pointRecordInfo = new PointRecordInfo();
            pointRecordInfo.setPoint((i + 1) * 100);
            pointRecordInfo.setDate(date);
            if (i % 2 == 0) {
                pointRecordInfo.setType(-1);
            } else {
                pointRecordInfo.setType(1);
            }
            pointlist.add(pointRecordInfo);
            Log.d("PointListActivity test", pointlist.toString());

        }*/

