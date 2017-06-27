package com.my.taxipool.activity;

import android.content.SyncStatusObserver;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.my.taxipool.R;
import com.my.taxipool.adapter.BlockListAdapter;
import com.my.taxipool.util.CommuServer;
import com.my.taxipool.util.Set;
import com.my.taxipool.vo.BlockCustomerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sungjin on 2017-06-26.
 */

public class PointActivity extends AppCompatActivity {

    //포인트사용내역
    private Button bt_pointlist;

    //충전금액 설정
    private Button bt_50000;
    private Button bt_10000;
    private Button bt_5000;

    //무료충전
    private Button bt_freepoint;

    //충전하기
    private Button bt_charge;

    //현재포인트 상태 보여주기
    private TextView tv_nowpoint;

    //충전금액 입력하기
    private EditText et_inputpoint;

    //서버로 받은 결과
    private String result;
    //private BlockListAdapter adapter;
    //private ArrayList<BlockCustomerInfo> data;
    private int point;
    //ListView listView;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pointcharge);

        bt_pointlist = (Button)findViewById(R.id.bt_pointlist);
        bt_50000 = (Button)findViewById(R.id.bt_5);
        bt_10000 = (Button)findViewById(R.id.bt_1);
        bt_5000 = (Button)findViewById(R.id.bt_0_5);
        bt_freepoint = (Button)findViewById(R.id.bt_freepoint);
        bt_charge = (Button)findViewById(R.id.bt_charge);
        tv_nowpoint = (TextView)findViewById(R.id.tv_nowpoint);
        et_inputpoint = (EditText)findViewById(R.id.et_inputpoint);


        final String info_id = Set.Load(getApplication(), "info_id", null);
        //final String info_id = "447433869";

        //현재 로그인한 사람의 POINT를 조회.
        //new CommuServer("http://13.124.132.30:8888/taxi_db_test2/pointcheck.do", new CommuServer.OnCommuListener() {
        new CommuServer("http://192.168.12.30:8888/taxi_db_test2/pointcheck.do", new CommuServer.OnCommuListener() {

            @Override
            public void onSuccess(JSONObject object, JSONArray arr, String str) {
                result = str;
                Log.d("ddu string",str);

                //point = Integer.parseInt(result);
                tv_nowpoint.setText(result);
                Log.d("PointActivity","point:"+result);
            }
            @Override
            public void onFailed(Error error) {
                Log.d("PointActivity", "포인트 조회 실패");
                error.printStackTrace();
            }
        }).addParam("info_id", info_id).start();

        //무료충전 준비중 안내문구
        bt_freepoint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "준비중이에요 죄송합니다 OTL..", Toast.LENGTH_SHORT).show();
            }

        });

        //금액입력부분 클릭 시 금액초기화
        et_inputpoint.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    et_inputpoint.setText("");
                }
            }
        });

        //내역보기로 넘어가기
        bt_pointlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        bt_50000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp;
                if(et_inputpoint.getText().toString().isEmpty()){
                    tmp = 50000;
                }else{
                    tmp = Integer.parseInt(et_inputpoint.getText().toString()) + 50000;
                }
                String plus = String.valueOf(tmp);
                et_inputpoint.setText(plus);
            }
        });

        bt_10000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp;
                if(et_inputpoint.getText().toString().isEmpty()){
                    tmp = 10000;
                }else{
                    tmp = Integer.parseInt(et_inputpoint.getText().toString()) + 10000;
                }
                String plus = String.valueOf(tmp);
                et_inputpoint.setText(plus);
            }
        });

        bt_5000.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int tmp;
                if(et_inputpoint.getText().toString().isEmpty()){
                    tmp = 5000;
                }else{
                    tmp = Integer.parseInt(et_inputpoint.getText().toString()) + 5000;
                }
                String plus = String.valueOf(tmp);
                et_inputpoint.setText(plus);
            }
        });



    }
}
