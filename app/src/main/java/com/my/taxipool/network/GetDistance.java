package com.my.taxipool.network;

import android.util.Log;

import org.json.JSONObject;

/**
 * Created by Hyeon on 2017-06-13.
 */

public class GetDistance {
    String api_key = "AIzaSyB4brXqWeQOMC6QUK4B4OtiG3WB50cn4AU";
    public JSONObject getDistance(double startLat,double startLon,double endLat,double endLon){

        String startSpot = startLat + "," + startLon;
        String endSpot = endLat + "," + endLon;
        String url = "https://maps.googleapis.com/maps/api/distancematrix/json?mode=transit&units=metric" +
                "&origins=" + startSpot +
                "&destinations=" + endSpot +
                "&key=" + api_key;

        NetworkTest nt = new NetworkTest();
        JSONObject response = nt.jsonNetwork(url);
        if(response != null){
            Log.i("★log", response.toString());
        }
        return response;
    }

    //aws 서버에서
    public JSONObject selectNear(double startLat,double startLon,double endLat,double endLon){
        String url = "http://192.168.12.30:8888/taxi_db_test2/roomlist.do?" +
                "&start_x=" + startLon +
                "&start_y=" + startLat +
                "&end_x=" + endLon +
                "&end_y=" + endLat ;
        NetworkTest nt = new NetworkTest();
        JSONObject response = nt.jsonNetwork(url);
        if(response != null){
            Log.i("★log", response.toString());
        }
        return response;
    }
}
