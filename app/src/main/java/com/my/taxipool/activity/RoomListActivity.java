package com.my.taxipool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.my.taxipool.R;
import com.my.taxipool.adapter.RoomListRecyclerAdapter;
import com.my.taxipool.adapter.RoomListRecyclerAdapterInterface;
import com.my.taxipool.util.CommuServer;
import com.my.taxipool.vo.Room;
import com.my.taxipool.vo.TmpRoom;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class RoomListActivity extends AppCompatActivity implements RoomListRecyclerAdapterInterface.OnItemClickListener {
    private RecyclerView recyclerView_roomlist;
    private RoomListRecyclerAdapter adapter;
    private ArrayList<Room> room_list = new ArrayList<Room>();
//    private static RoomListRecyclerAdapter addressAdapter;

    private int room_no;
    private String admin_id;
    private int max_cnt;
    private String payment;
    private String room_gender;
    private String alcohol;
    private String start_spot;
    private String end_spot;
    private String start_x;
    private String start_y;
    private String end_x;
    private String end_y;
    private Date start_time;
    private String room_state;
    private int current_cnt;
    TmpRoom tmpRoom ;
    int info_id;

    private TextView tv_myStartTime;
    private TextView tv_myStartSpot;
    private TextView tv_myEndSpot;
    private Button btn_goto_makeroom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomlist);

        info_id = getIntent().getIntExtra("info_id",-1);
        tmpRoom = (TmpRoom) getIntent().getSerializableExtra("object");
        Log.d("ddu tmpRoom",tmpRoom.toString());
        setViewIds();
        setViews();
        new CommuServer(CommuServer.SELECT_ROOM_LIST, new CommuServer.OnCommuListener() {
            @Override
            public void onSuccess(JSONObject object,JSONArray arr, String str) {
                Log.d("ddu result!!",arr.toString());
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                // test
                try{
                    for(int i=0; i < arr.length();i++) {
                        JSONObject jsonobject = arr.getJSONObject(i);
                        room_no = jsonobject.getInt("room_no");
                        admin_id = jsonobject.getString("admin_id");
                        payment = jsonobject.getString("payment");
                        max_cnt = jsonobject.getInt("max_cnt");
                        room_gender = jsonobject.getString("room_gender");
                        alcohol = jsonobject.getString("alcohol");
                        start_spot = jsonobject.getString("start_spot");
                        end_spot = jsonobject.getString("end_spot");
                        start_x = jsonobject.getString("start_x");
                        start_y = jsonobject.getString("start_y");
                        end_x = jsonobject.getString("end_x");
                        end_y = jsonobject.getString("end_y");
                        String start_time_string = jsonobject.getString("start_time");
                        start_time = sdf.parse(start_time_string, new ParsePosition(0));
                        room_state = jsonobject.getString("room_state");
                        current_cnt = jsonobject.getInt("current_cnt");
                        Room r = new Room(room_no, admin_id, max_cnt, payment, room_gender, alcohol, start_spot,
                                end_spot, start_x, start_y, end_x, end_y, start_time,
                                room_state, current_cnt);
                        room_list.add(r);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                setAdapter(RoomListActivity.this);
            }
            @Override
            public void onFailed(Error error) {
            }
        }).addParam("start_x", tmpRoom.getStartLon())
                .addParam("start_y", tmpRoom.getStartLat())
                .addParam("end_x", tmpRoom.getEndLon())
                .addParam("end_y", tmpRoom.getEndLat()).start();
    }
    private void setViewIds(){
        recyclerView_roomlist = (RecyclerView) findViewById(R.id.recyclerView_roomlist);
        recyclerView_roomlist.setLayoutManager(new LinearLayoutManager(this));
        tv_myEndSpot = (TextView) findViewById(R.id.my_end_spot);
        tv_myStartSpot = (TextView) findViewById(R.id.my_start_spot);
        tv_myStartTime = (TextView) findViewById(R.id.my_start_time);
        btn_goto_makeroom = (Button) findViewById(R.id.btn_goto_makeroom);
    }
    private void setViews() {
        tv_myEndSpot.setText(tmpRoom.getEndSpot());
        tv_myStartSpot.setText(tmpRoom.getStartSpot());
        tv_myStartTime.setText(tmpRoom.getTime());
        btn_goto_makeroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RoomListActivity.this,RoomRegistActivity.class);
                intent.putExtra("object",tmpRoom);
                startActivity(intent);
            }
        });
    }
    public void setAdapter(final RoomListRecyclerAdapterInterface.OnItemClickListener listener){
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        adapter = new RoomListRecyclerAdapter(room_list,getApplicationContext(),listener);
        recyclerView_roomlist.setAdapter(adapter);
    }
    @Override
    public void onItemClick(View view, int position) {
            Intent intent = new Intent(RoomListActivity.this, RoomActivity.class);
            intent.putExtra("info_id",info_id);
            intent.putExtra("room_no_from_list",room_list.get(position).getRoom_no());
//            intent.putExtra("room_no_from_list",room_list.get(position).getRoom_no());
            startActivity(intent);
    }
}