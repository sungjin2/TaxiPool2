package com.my.taxipool.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.taxipool.R;
import com.my.taxipool.adapter.MyPagerAdapter;
import com.my.taxipool.network.NetworkTest;
import com.my.taxipool.util.Set;
import com.my.taxipool.vo.CustomerInfo;
import com.my.taxipool.vo.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hyeon on 2017-05-28.
 */

public class RoomActivity extends AppCompatActivity{
    //Dialog dialog;
    TextView tv_dialog_name;    ImageView image_dialog;
    TextView btn_dialog_yes;    TextView btn_dialog_no;

    //viewPager
    private ViewPager viewPager;
    private TabLayout tab;

    //status
    Boolean isBangjang;
    static final int NO_MEMBER = 0;
    static final int REQUIRING = 10;
    static final int WAIT_TOGO = 20;
    static final int FINISH = 50;
    int status;
    String room_num;

    //items
    private Room room;
    private ArrayList<CustomerInfo> sharePeopleList;

    //network
    private String responseData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isBangjang = getIntent().getBooleanExtra("isBangjang", false);
        status = Set.Load(RoomActivity.this, "status", NO_MEMBER);
        room_num = Set.Load(RoomActivity.this, "room_num", "0");
//        Log.i("ddu room_status",String.valueOf(status));
//        Log.i("ddu room_num",String.valueOf(room_num));
        new Thread() {
            @Override
            public void run() {
                getRoomInfo(room_num);
            }
        }.start();
    }

    private void set_TabViews() {
        tab =  (TabLayout) findViewById(R.id.main_tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tab.setupWithViewPager(viewPager);
        tab.getTabAt(0).setText("합승정보");
        tab.getTabAt(1).setText("채팅방");
    }
    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        TabInfo tabInfo = new TabInfo();
        TabChat tabChat = new TabChat();
        tabInfo.setData(room,sharePeopleList);
        tabChat.setRoom_no(room.getRoom_no());
        adapter.addFragment(tabInfo, "합승정보");
        adapter.addFragment(tabChat, "채팅방");

        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private void getRoomInfo(String room_no){
        String url = "http://192.168.12.30:8888/taxi_db_test2/roominfo.do?"+
                "room_no="+room_no;
        NetworkTest nt = new NetworkTest();
        JSONObject roomInfoObject = nt.jsonObjectNetwork(url);
        if(roomInfoObject == null){
            Log.d("ddu RoomActivity","no room");
        }
        try{
            room = new Room(roomInfoObject.getInt("room_no"),roomInfoObject.getString("admin_id"),roomInfoObject.getInt("max_cnt"),
                    roomInfoObject.getString("payment"),roomInfoObject.getString("room_gender"),roomInfoObject.getString("alcohol"),
                    roomInfoObject.getString("start_spot"), roomInfoObject.getString("end_spot"),
                    roomInfoObject.getString("start_x"),roomInfoObject.getString("start_y"),
                    roomInfoObject.getString("end_x"),roomInfoObject.getString("end_y"),
                    new Date(),roomInfoObject.getString("room_state"),roomInfoObject.getInt("current_cnt"));
        }catch(JSONException e){
            e.printStackTrace();
        }
        getSharePeople(room_no);
    }
    private void getSharePeople(String room_no){
        String url = "http://192.168.12.30:8888/taxi_db_test2/roomsharepeople.do?"+
                "room_no="+room_no;
        NetworkTest nt = new NetworkTest();
        JSONArray roomInfoObject = nt.arrayJSONNetwork(url);
        if(roomInfoObject == null){
            Log.d("ddu RoomActivity","no room");
        }
        sharePeopleList = new ArrayList<>();
        try{
            for(int i=0; i<roomInfoObject.length();i++){
                CustomerInfo customerInfo = new CustomerInfo();
                customerInfo.setInfo_id(roomInfoObject.getJSONObject(i).getString("share_info_id"));
                customerInfo.setNickname(roomInfoObject.getJSONObject(i).getString("nickname"));
                sharePeopleList.add(customerInfo);
            }
            listener.onSuccess();
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    private RoomListener listener = new RoomListener() {
        @Override
        public void onSuccess() {
//            Log.d("ddu results: ",room.toString());
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    if (status >= 20) {
                        setContentView(R.layout.activity_room_yes_member);
                        set_TabViews();
                    } else {
                        setContentView(R.layout.activity_room_no_member);
                    }
                }
            });
        }
    };

    public interface RoomListener {
        void onSuccess();
    }

}