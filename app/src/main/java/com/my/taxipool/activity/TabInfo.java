package com.my.taxipool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.taxipool.R;
import com.my.taxipool.adapter.RoomSharepeopleRecyclerAdapter;
import com.my.taxipool.util.Set;
import com.my.taxipool.vo.CustomerInfo;
import com.my.taxipool.vo.Room;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Hyeon on 2017-05-30.
 */

public class TabInfo extends Fragment{
    private View rootView;
    private SupportMapFragment mapFragment;

    int state;
    Room room;
    int info_id;
    Boolean isBangjang = false;
    private ArrayList<CustomerInfo> sharePeopleList;

    //views
    private Button btn_in_room;
    private TextView tv_info_startspot;
    private TextView tv_info_endspot;
    private TextView tv_info_detail;

    //People List
    RecyclerView recyclerView_roomShare;
    private RoomSharepeopleRecyclerAdapter adapter = null;

    private ImageView img_room_gender;
    private ImageView img_room_payment;
    private ImageView img_room_alcohol;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.view_room_infobase,container,false);

        state = getActivity().getIntent().getIntExtra("state",Room.NO_MEMBER);
        setViewIds();
        setViews();
        setSharePeopleListAdapter();

        return rootView;
    }
    private void setViewIds(){
        btn_in_room = (Button) rootView.findViewById(R.id.btn_in_room);
        tv_info_startspot = (TextView) rootView.findViewById(R.id.tv_info_startspot);
        tv_info_endspot = (TextView) rootView.findViewById(R.id.tv_info_endspot);
        tv_info_detail = (TextView) rootView.findViewById(R.id.tv_info_detail);
        recyclerView_roomShare = (RecyclerView) rootView.findViewById(R.id.recyclerview_room_info);
        recyclerView_roomShare.setLayoutManager(new LinearLayoutManager(getContext()));


        img_room_gender = (ImageView) rootView.findViewById(R.id.img_room_gender);
        img_room_payment = (ImageView) rootView.findViewById(R.id.img_room_payment);
        img_room_alcohol = (ImageView) rootView.findViewById(R.id.img_room_alcohol);
    }

    private void setViews(){
        //setting distance
        tv_info_startspot.setText(room.getStart_spot());
        tv_info_endspot.setText(room.getEnd_spot());
        tv_info_detail.setText(detailToString(room.getMax_cnt(),room.getCurrent_cnt(),room.getStart_time()));

        //setting images

        img_room_gender.setImageResource(room.getImgsource_gender());
        img_room_payment.setImageResource(room.getImgsource_payment());
        img_room_alcohol.setImageResource(room.getImgsource_alcohol());


        //setting button
        if (state == Room.NO_MEMBER){
            btn_in_room.setBackgroundColor(getResources().getColor(R.color.darkGray));
            btn_in_room.setTextColor(getResources().getColor(R.color.white));
            btn_in_room.setText("합승 신청");
        }
        else if(state == Room.REQUIRING){
            btn_in_room.setBackgroundColor(getResources().getColor(R.color.darkGray));
            btn_in_room.setTextColor(getResources().getColor(R.color.white));
            btn_in_room.setText("합승신청 취소");
        }
        else if(state == Room.WAIT_TOGO){
            btn_in_room.setBackgroundColor(getResources().getColor(R.color.colorPoint));
            btn_in_room.setTextColor(getResources().getColor(R.color.white));
            btn_in_room.setText("출발 하기");
        }

        btn_in_room.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                int new_state = 0;
                switch(state){
                    case Room.NO_MEMBER :    //합승 신청
                        new_state = state + 10;
                        break;
                    case Room.REQUIRING :   //합승신청 취소
                        new_state = state -10;
                        break;
                    case Room.WAIT_TOGO :    //출발하기
                           new_state = state + 10;
//                        Intent intent = new Intent(getActivity(),);
//                        startActivity(intent);
//                        finish();
                        break;
                }
                Set.Save(getActivity(),"state",new_state);
            }
        });
    }

    public void setSharePeopleListAdapter(){
        adapter = new RoomSharepeopleRecyclerAdapter(sharePeopleList,getContext(),isBangjang);
        recyclerView_roomShare.setAdapter(adapter);
    }

    //바꿀 것
    private String detailToString(int maxCnt,int currentCnt,Date startTime){
        return "(" + currentCnt + "/" + maxCnt + ")" + startTime.toString();
    }

    @Override
    public void onResume(){
        super.onResume();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fm = getChildFragmentManager();
        mapFragment = (SupportMapFragment) fm.findFragmentById(R.id.view_info_map);
        if(mapFragment == null){
            mapFragment = SupportMapFragment.newInstance();
            fm.beginTransaction().replace(R.id.view_info_map, mapFragment).commit();
        } else {
            mapFragment.getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(final GoogleMap map) {
                    MarkerOptions startMarkerOption = new MarkerOptions();
                    MarkerOptions endMarkerOption = new MarkerOptions();
                    LatLng start_latlon = new LatLng(room.getStart_lon(), room.getStart_lat());
                    LatLng end_latlon = new LatLng(room.getEnd_lon(), room.getStart_lat());
                    LatLng cneter_latlon = new LatLng((room.getStart_lon() + room.getEnd_lon()) / 2,
                            (room.getStart_lat() + room.getStart_lat()) / 2);

                    startMarkerOption.position(start_latlon);
                    startMarkerOption.title("출발지");
                    startMarkerOption.snippet(room.getStart_spot());
                    endMarkerOption.position(end_latlon);
                    endMarkerOption.title("도착지");
                    endMarkerOption.snippet(room.getEnd_spot());

                    map.addMarker(startMarkerOption);
                    map.addMarker(endMarkerOption);
                    map.moveCamera(CameraUpdateFactory.newLatLng(cneter_latlon));
                    map.animateCamera(CameraUpdateFactory.zoomTo(10));
                }

            });
        }
    }

    public void setData(int info_id, Room room,ArrayList<CustomerInfo> sharePeopleList) {
        this.info_id = info_id;
        this.room = room;
        if(Integer.parseInt(room.getAdmin_id()) == info_id){
            isBangjang = true;
        }
        this.sharePeopleList = sharePeopleList;
    }
}