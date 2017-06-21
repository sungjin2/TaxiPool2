package com.my.taxipool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.my.taxipool.R;
import com.my.taxipool.adapter.BlockListAdapter;
//import com.my.taxipool.adapter.RecyclerViewAdapterInterface;
import com.my.taxipool.network.NetworkTest;
import com.my.taxipool.util.Set;
import com.my.taxipool.vo.BlockCustomerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by sungjin on 2017-06-20.
 */

public class BlockActivity extends AppCompatActivity{
    JSONArray result;
    String nickname;
    String info_id;
    BlockListAdapter adapter;
    ArrayList<BlockCustomerInfo> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blocklist);
        //ListView
        final ListView listView=(ListView)findViewById(R.id.listview);
        data = new ArrayList<>();
        new Thread(){
            @Override
            public void run() {
                String url = "http://192.168.12.30:8888/taxi_db_test2/blocklist.do?";
                String query = "info_id="+"A";
                url += query;

                NetworkTest nt = new NetworkTest();
                result = nt.jsonArrayNetwork(url);
                if(result==null){
                    Log.d("ddu","resul null");
                }else{
                    try{
                        for(int i=0; i<result.length(); i++){
                        /* 변수명 주의!
                           JSON의 결과로 오는 것의 INFO_ID는 BLACKLIST_ID(NICKNAME, PROFILE도 마찬가지다) */
                            Log.d("BlockActivity ArrayList",new BlockCustomerInfo("A", result.getJSONObject(i).getString("INFO_ID"),
                                    result.getJSONObject(i).getString("NICKNAME"), R.drawable.defaultprofile).toString());
                            data.add(new BlockCustomerInfo("A", result.getJSONObject(i).getString("INFO_ID"),
                                    result.getJSONObject(i).getString("NICKNAME"), R.drawable.defaultprofile));
                        /*nickname = result.getJSONObject(i).getString("NICKNAME");
                        info_id = result.getJSONObject(i).getString("INFO_ID");*/

//                        result.getJSONObject(i).getString("PROFILE");
                            Log.d("ddu",nickname+" "+info_id);
                        }
                        adapter = new  BlockListAdapter(BlockActivity.this, R.layout.view_list_people_info,data);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                listView.setAdapter(adapter);
                            }
                        });

                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    public void refresh(){
        Log.d("BlcokActivity>refresh","옴");
        Intent intent = new Intent(BlockActivity.this, BlockActivity.class);
        startActivity(intent);
        finish();


        //adapter.notifyDataSetChanged();
    }
}
