package com.my.taxipool.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.my.taxipool.R;
import com.my.taxipool.adapter.Calculator_ListViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

    /**
 * Created by KITRI on 2017-06-20.
 */

public class RoomCalculatorActivity extends Activity{
    EditText[] ed_taxicalcul;
    TextView tv_calculator_price, tv_calculator_block;
    RatingBar rb_calcul_addscore;
    boolean taxicalcul_nullcheck;
        double price;
    JSONArray result;
    String nickname;
    String info_id;
    Calculator_ListViewAdapter adapter;
    ArrayList<PeopleListItem> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        ed_taxicalcul = new EditText[3];
        ed_taxicalcul[0] = (EditText) findViewById(R.id.ed_taxicalcul1);
        ed_taxicalcul[1] = (EditText) findViewById(R.id.ed_taxicalcul2);
        ed_taxicalcul[2] = (EditText) findViewById(R.id.ed_taxicalcul3);

        tv_calculator_price = (TextView) findViewById(R.id.tv_calculator_price);
        tv_calculator_block = (TextView) findViewById(R.id.tv_calculator_block);
        rb_calcul_addscore = (RatingBar) findViewById(R.id.ratingbar_score);
        final ListView calcullistview = (ListView) findViewById(R.id.calcullistview);

        taxicalcul_nullcheck = true;

        ed_taxicalcul[0].addTextChangedListener(listener);
        ed_taxicalcul[1].addTextChangedListener(listener);
        ed_taxicalcul[2].addTextChangedListener(listener);

        rb_calcul_addscore.setRating(3);

        //EditText가 클릭되면 안에 값 제거
        for (int i = 0; i < 3; i++) {
            final int index = i;
            ed_taxicalcul[i].setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if (hasFocus) {
                        ed_taxicalcul[index].setText("");
                    }
                }
            });
        }
        tv_calculator_block.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tv_calculator_block.setText("차단 완료");
            }
        });
        data = new ArrayList<>();



        //query = "room_no="+room_no;
        //room_no 는 받아와야함

        //HttpURLConnection conn = null;

        new Thread(){
            @Override
            public void run() {
                String url = "http://192.168.12.30:8888/taxi_db_test2/roomsharepeople.do";
                String room_no = "2";
                String queryString = "room_no="+room_no;
                /*try {
                    OutputStream os = conn.getOutputStream();
                    BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                    bw.write(query);
                    bw.flush();
                    bw.close();
                }catch (Exception e) {
                }*/
                Log.d("ddu","들어옴");
//                NetworkTest nt = new NetworkTest();
                //result = null;
//                result = nt.jsonArrayNetwork(url, queryString);
                Log.d("ddu","넷 끝나고 result:"+result);
                Log.d("ddu","들어옴"+result.toString());
                try{
                    for(int i=0; i<result.length(); i++){

    data.add(new PeopleListItem(result.getJSONObject(i).getString("nickname"), result.getJSONObject(i).getString("info_id")));

                        Log.d("ddu",nickname+" "+info_id);
                    }
                    adapter = new Calculator_ListViewAdapter(RoomCalculatorActivity.this, R.layout.view_calculator_people_score,data);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            calcullistview.setAdapter(adapter);

                        }
                    });

                }catch(JSONException e){
                    e.printStackTrace();
                }
            }
        }.start();


    }

TextWatcher listener =
    new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            taxicalcul_nullcheck=true;
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            for (int j = 0; j < 3; j++){

                if(ed_taxicalcul[j].getText().toString().equals("")){
                    taxicalcul_nullcheck = false;
                    price=0;
                }else if(ed_taxicalcul[j].getText().toString().equals("0")){
                    price=0;
                    taxicalcul_nullcheck=false;
                }
            }
            if (taxicalcul_nullcheck){
                price = Double.parseDouble(ed_taxicalcul[0].getText().toString())/Double.parseDouble(ed_taxicalcul[1].getText().toString())*Double.parseDouble(ed_taxicalcul[2].getText().toString());

            }
            tv_calculator_price.setText(price+"원");
        }

        @Override
        public void afterTextChanged(Editable s) {
        }
    };
}
