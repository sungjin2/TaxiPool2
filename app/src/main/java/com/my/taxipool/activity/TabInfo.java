package com.my.taxipool.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.taxipool.R;

/**
 * Created by Hyeon on 2017-05-30.
 */

public class TabInfo extends Fragment
        implements OnMapReadyCallback {
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        rootView = inflater.inflate(R.layout.view_room_infobase,container,false);
        FragmentManager fragmentManager = getFragmentManager();
//        MapFragment mapFragment = (MapFragment)fragmentManager.findFragmentById(R.id.main_map);
//        mapFragment.getMapAsync(this);
        /* Google map end */
        return rootView;
    }
    @Override
    public void onResume(){
        super.onResume();
    }
    @Override
    public void onMapReady(final GoogleMap map) {

        LatLng SEOUL = new LatLng(37.56, 126.97);

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(SEOUL);
        markerOptions.title("서울");
        markerOptions.snippet("한국의 수도");
        map.addMarker(markerOptions);

        map.moveCamera(CameraUpdateFactory.newLatLng(SEOUL));
        map.animateCamera(CameraUpdateFactory.zoomTo(10));
    }
}