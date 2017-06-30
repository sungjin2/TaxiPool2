package com.my.taxipool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.my.taxipool.R;
import com.my.taxipool.adapter.MyPagerAdapter;
import com.my.taxipool.util.CommuServer;
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

public class RoomActivity extends BaseActivity {
    //viewPager
    private ViewPager viewPager;
    private TabLayout tab;

    //status
    int state;
    String info_id;
    static public int room_no;

    //items
    private Room room;
    private ArrayList<CustomerInfo> sharePeopleList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        info_id = Set.Load(RoomActivity.this,"info_id",null);
        room_no = Set.Load(RoomActivity.this,"room_no",-1);

        //변수형 정리할 것
        if(getIntent().hasExtra("room_no_from_list")){        //list 에서 넘어온 상태
            room_no = getIntent().getIntExtra("room_no_from_list",0);
        }else if (getIntent().hasExtra("room_no_from_intro")) {   //원래 room소속 상태
            room_no = getIntent().getIntExtra("room_no_from_intro",0);
        }else if(getIntent().hasExtra("room_no_from_regist")){  // 방장으로 옴
            room_no = getIntent().getIntExtra("room_no_from_regist",0);
        }
        state = Set.Load(RoomActivity.this, "state", Room.NO_MEMBER);
        getRoomInfo(room_no);
    }

    private void setViews(int state) {
        tab =  (TabLayout) findViewById(R.id.main_tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);

        /*set ViewPager*/
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        TabInfo tabInfo = new TabInfo();
        tabInfo.setData(Integer.parseInt(info_id), room, sharePeopleList);
        adapter.addFragment(tabInfo, "합승정보");
        if (state >= 20){
            TabChat tabChat = new TabChat();
            tabChat.setRoom_no(room.getRoom_no());
            adapter.addFragment(tabChat, "채팅방");
        }

        viewPager.setAdapter(adapter);
        /*set ViewPager end*/

        tab.setupWithViewPager(viewPager);
        tab.getTabAt(0).setText("합승정보");
        if (state < 20){
            tab.setVisibility(View.GONE);
        }else{
            tab.getTabAt(1).setText("채팅방");
            tab.setVisibility(View.VISIBLE);
        }
    }

    private void getRoomInfo(final int room_no){
        new CommuServer(CommuServer.SELECT_ROOM_INFO, new CommuServer.OnCommuListener() {
            @Override
            public void onSuccess(JSONObject object, JSONArray arr, String str) {
                Log.d("ddu result!!",object.toString());
                JSONObject roomInfoObject = object;
                if(roomInfoObject == null){
                    Log.d("ddu RoomActivity","no room");
                }else {
                    try {
                        room = new Room(roomInfoObject.getInt("room_no"), roomInfoObject.getString("admin_id"), roomInfoObject.getInt("max_cnt"),
                                roomInfoObject.getString("payment"), roomInfoObject.getString("room_gender"), roomInfoObject.getString("alcohol"),
                                roomInfoObject.getString("start_spot"), roomInfoObject.getString("end_spot"),
                                roomInfoObject.getDouble("start_lon"), roomInfoObject.getDouble("start_lat"),
                                roomInfoObject.getDouble("end_lon"), roomInfoObject.getDouble("end_lat"),
                                new Date(), roomInfoObject.getString("room_state"), roomInfoObject.getInt("current_cnt"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    getSharePeople(room_no);
                }
            }
            @Override
            public void onFailed(Error error) {
            }
        }).addParam("room_no", room_no).start();

    }
    private void getSharePeople(int room_no){
        new CommuServer(CommuServer.SELECT_PEOPLE_ROOMSHARE, new CommuServer.OnCommuListener() {
            @Override
            public void onSuccess(JSONObject object, JSONArray arr, String str) {
                Log.d("ddu result!!",arr.toString());
                JSONArray roomInfoObject = arr;
                if(roomInfoObject == null){
                    Log.d("ddu RoomActivity","no room");
                }else{
                    sharePeopleList = new ArrayList<>();
                    try{
                        for(int i=0; i<roomInfoObject.length();i++){
                            CustomerInfo customerInfo = new CustomerInfo();
                            customerInfo.setResultscore(roomInfoObject.getJSONObject(i).getInt("score")/(double)roomInfoObject.getJSONObject(i).getInt("cnt"));
                            customerInfo.setInfo_id(roomInfoObject.getJSONObject(i).getString("share_info_id"));
                            customerInfo.setNickname(roomInfoObject.getJSONObject(i).getString("nickname"));
                            customerInfo.setProfile_pic(roomInfoObject.getJSONObject(i).getString("profile_pic"));

                            if(info_id.equals(roomInfoObject.getJSONObject(i).getString("share_info_id"))){
                                customerInfo.setState("m");
                            }else{
                                customerInfo.setState(roomInfoObject.getJSONObject(i).getString("state"));
                            }
                            sharePeopleList.add(customerInfo);
                        }
                        Log.d("ddu RoomActivity",sharePeopleList.toString());
                        listener.onSuccess();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailed(Error error) {
            }
        }).addParam("room_no", room_no).start();
    }

    private RoomListener listener = new RoomListener() {
        @Override
        public void onSuccess() {
            runOnUiThread(new Runnable(){
                @Override
                public void run() {
                    setContentView(R.layout.activity_room);
                    setViews(state);
                }
            });
        }
    };

    public interface RoomListener {
        void onSuccess();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_at_room, menu);
        super.onCreateOptionsMenu(menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_leaveroom:
                Set.Delete(RoomActivity.this,"room_no");
                Intent intent = new Intent(getApplicationContext(),HomeActivity2.class);
                ActivityCompat.finishAffinity(this); //모든 액티비티 종료
                startActivity(intent);
                return true;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}