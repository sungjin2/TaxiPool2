package com.my.taxipool.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.taxipool.R;
import com.my.taxipool.adapter.RoomSharePeopleListAdapter;
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
    //status
    Boolean isBangjang = false;
    static final int NO_MEMBER = 0;
    static final int REQUIRING = 10;
    //    static final int YES_MEMBER = 20;
    static final int YES_MEMBER = 20;
    static final int FINISH = 50;
    int status;
    Room room;
    private ArrayList<CustomerInfo> sharePeopleList;

    //views
    private Button btn_in_room;
    private TextView tv_info_startspot;
    private TextView tv_info_endspot;
    private TextView tv_info_detail;

    //People List
    RoomSharePeopleListAdapter adapter;
    ArrayList<CustomerInfo> data;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.view_room_infobase,container,false);
//        FragmentManager fragmentManager = getFragmentManager();
//        MapFragment mapFragment = (MapFragment) fragmentManager.findFragmentById(R.id.view_info_map);
//        mapFragment.getMapAsync(this);

        String myId = "135425414";
        status = Set.Load(getActivity(),"status",NO_MEMBER);
        Log.i("ddu",String.valueOf(status));
        if(room.getAdmin_id().equals(myId)){
            isBangjang = true;
        }
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
        listView = (ListView) rootView.findViewById(R.id.listview_room_info);
    }

    private void setViews(){
        //setting distance
        tv_info_startspot.setText(room.getStart_spot());
        tv_info_endspot.setText(room.getEnd_spot());
        tv_info_detail.setText(detailToString(room.getMax_cnt(),room.getCurrent_cnt(),room.getStart_time()));

        //setting button
        if (status == NO_MEMBER){
            btn_in_room.setBackgroundColor(getResources().getColor(R.color.darkGray));
            btn_in_room.setTextColor(getResources().getColor(R.color.white));
            btn_in_room.setText("동승 신청");
        }
        else if(status == REQUIRING){
            btn_in_room.setBackgroundColor(getResources().getColor(R.color.darkGray));
            btn_in_room.setTextColor(getResources().getColor(R.color.white));
            btn_in_room.setText("동승신청 취소");

        }
        else if(status == YES_MEMBER){
            btn_in_room.setBackgroundColor(getResources().getColor(R.color.colorPoint));
            btn_in_room.setTextColor(getResources().getColor(R.color.white));
            btn_in_room.setText("출발 하기");
        }

        btn_in_room.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(status){
                    case NO_MEMBER :
                        //합승 신청
//                        Set.Save(getActivity(),"status",status+10);
//                        status += 10;
                        break;

                    case REQUIRING :
                        //합승신청 취소
//                        Set.Save(getActivity(),"status",status-10);
//                        status -= 10;
                        break;

                    case YES_MEMBER :
                        //출발하기
//                        Set.Save(getActivity(),"status",status+10);
//                        status += 10;
//                        Intent intent = new Intent(getActivity(),);
//                        startActivity(intent);
//                        finish();
                        break;
                }
            }
        });

        if(isBangjang){
            //setOnClickListener ->  showDialog();
        }
    }

    public void setSharePeopleListAdapter(){
        adapter = new RoomSharePeopleListAdapter(getContext(), R.layout.view_room_people_info,sharePeopleList);
        listView.setAdapter(adapter);
    }

//    public void showDialog(){
//        final View innerView = getLayoutInflater().inflate(R.layout.dialog, null);
//        Dialog mDialog = new Dialog(this);
//        mDialog.setContentView(innerView);
//
//        tv_dialog_name = (TextView) findViewById(R.id.dialog_name);
//        image_dialog = (ImageView) findViewById(R.id.dialog_image);
//        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
//        params.width = WindowManager.LayoutParams.MATCH_PARENT;
//        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
//        mDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
//        mDialog.getWindow().setGravity(Gravity.BOTTOM);
//        mDialog.show();
//    }

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
                        LatLng start_latlon = new LatLng(Double.parseDouble(room.getStart_x()), Double.parseDouble(room.getStart_y()));
                        LatLng end_latlon = new LatLng(Double.parseDouble(room.getEnd_x()), Double.parseDouble(room.getEnd_y()));
                        LatLng cneter_latlon = new LatLng((Double.parseDouble(room.getStart_x()) + Double.parseDouble(room.getEnd_x())) / 2,
                                (Double.parseDouble(room.getStart_y()) + Double.parseDouble(room.getEnd_y())) / 2);

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

    public void setData(Room room,ArrayList<CustomerInfo> sharePeopleList) {
        this.room = room;
        this.sharePeopleList = sharePeopleList;
    }
}