package com.my.taxipool.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.my.taxipool.R;
import com.my.taxipool.util.CommuServer;
import com.my.taxipool.util.ImageHelper;
import com.my.taxipool.util.ImageResourceUtil;
import com.my.taxipool.vo.CustomerInfo;
import com.my.taxipool.vo.TmpRoom;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnCameraIdleListener, OnMapReadyCallback,View.OnTouchListener
        ,GoogleApiClient.OnConnectionFailedListener {

    public static CustomerInfo myInfo;
    TmpRoom tmpRoom= new TmpRoom();
    int info_id;

    //Big views
    private SlidingUpPanelLayout mLayout;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    //For Input Views
    private TextView tv_from;    private TextView tv_to;    private TextView tv_time;
    private Button btn;    private Spinner spinner_people;
    private RadioButton radio_bill; private RadioButton radio_point; private RadioButton radio_all;

    Button btn_place_auto;

    //For Hamburger Views
    private ImageView img_nav_info;
    private TextView tv_nav_infoname;
    private TextView tv_nav_info_nickandphone;
    private Bitmap bitmap;

    // Marker and Map
    MarkerOptions markeropt_start;
    MarkerOptions markeropt_end;
    HashMap<String,MarkerOptions> hashMapMarker = new HashMap<>();

    ImageView img_marker;
    TextView tv_home_indicator;

    private TextView tv_spotname;       //Marker's textview
    private LinearLayout view_point;    //Marker
    private LinearLayout layout_spot_from;   //한칸 (출발지)
    private LinearLayout layout_spot_to;   //한칸 (출발지)
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

    //Autocomplete
    private GoogleApiClient mGoogleApiClient;
    private static final int PLACE_AUTOCOMPLETE_TO = 0;
    private static final int PLACE_AUTOCOMPLETE_FROM = 1;

    long time;
    boolean fromto_flag = false;    //false가 from차례 true= to

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        info_id = getIntent().getIntExtra("info_id",-1);

        /* Google map start */
        FragmentManager manager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment)manager.findFragmentById(R.id.map_sliding);
        mapFragment.getMapAsync(this);//지도준비가 되면 onMapReady()가 자동호출됨
        /* Google map end */

        /* Autocomplete start */
        mGoogleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();
        /* Autocomplete end*/

        setViewIds();
        initBottomDrawer();
        setViews();

        new CommuServer(CommuServer.SELECT_BY_INFOID, new CommuServer.OnCommuListener() {
            @Override
            public void onSuccess(JSONObject object, JSONArray arr, String str) {
                if(str!= null){
                    if(str.equals("0")){
                        Log.d("ddu login","0");
                    }
                }else{
                    try{
                        myInfo = new CustomerInfo();
                        myInfo.setInfo_id(object.getString("info_id"));
                        myInfo.setProfile_pic(object.getString("profile_pic"));
                        myInfo.setPhone_no(object.getString("phone_no"));
                        myInfo.setInfo_name(object.getString("info_name"));
                        myInfo.setNickname(object.getString("nickname"));
                        myInfo.setInfo_gender(object.getString("info_gender"));
                        myInfo.setResultscore(object.getInt("score")/(double)object.getInt("cnt"));
                        Log.d("ddu HomeActivity myInfo",myInfo.toString());
                        setMyinfo();
                    }catch(JSONException e){
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailed(Error error) {
            }
        }).addParam("info_id", info_id).start();
    }

    private void setViewIds(){
        //Sliding Layout
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //toolbar & drawer & navi
        toolbar = (Toolbar) findViewById(R.id.toolbar_home);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //input리스트
        layout_spot_from = (LinearLayout) findViewById(R.id.layout_spot_from);
        layout_spot_to = (LinearLayout) findViewById(R.id.layout_spot_to);
        view_point = (LinearLayout) findViewById(R.id.view_point);
        tv_from = (TextView) findViewById(R.id.tv_from);
        tv_to = (TextView) findViewById(R.id.tv_to);
        tv_time = (TextView) findViewById(R.id.tv_time);
        spinner_people = (Spinner) findViewById(R.id.spinner);
        radio_bill= (RadioButton) findViewById(R.id.radio_bill);
        radio_point= (RadioButton) findViewById(R.id.radio_point);
        radio_all= (RadioButton) findViewById(R.id.radio_all);

        //Views in Hamburger
        View headerLayout = navigationView.getHeaderView(0);
        img_nav_info = (ImageView) headerLayout.findViewById(R.id.img_nav_info);
        tv_nav_infoname = (TextView) headerLayout.findViewById(R.id.tv_nav_infoname);
        tv_nav_info_nickandphone = (TextView) headerLayout.findViewById(R.id.tv_nav_info_nickandphone);

        //Marker
        view_point = (LinearLayout) findViewById(R.id.view_point);
        img_marker = (ImageView) findViewById(R.id.ic_place);
        tv_spotname = (TextView) findViewById(R.id.tv_spotname);
        btn = (Button) findViewById(R.id.button_exam);
        tv_home_indicator  = (TextView) findViewById(R.id.tv_home_indicator);

        btn_place_auto = (Button) findViewById(R.id.btn_place_auto);
    }

    private void setViews() {

        mLayout.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // ignore all touch events
                return true;
            }
        });

        view_point.post(new Runnable() {
            @Override
            public void run() {
                int height = view_point.getHeight();
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(img_marker.getLayoutParams());
                lp.setMargins(0, 0, 0, height);
                img_marker.setLayoutParams(lp);
            }
        }); //spot 가운데로 margin 설정

        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);

        //setOnTouchListener
        btn.setOnTouchListener(this);
        tv_spotname.setOnTouchListener(this);
        layout_spot_from.setOnTouchListener(this);
        layout_spot_to.setOnTouchListener(this);

        radio_bill.setOnClickListener(optionOnClickListener);
        radio_point.setOnClickListener(optionOnClickListener);
        radio_all.setOnClickListener(optionOnClickListener);

        spinner_people.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                tmpRoom.setPeopleWith(position+1);
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void setMyinfo() {
        //setHamburger Views start
        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL(myInfo.getProfile_pic());
                    HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream is = conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(is);
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        };
        mThread.start();
        try{
            mThread.join();
            ImageHelper ih = new ImageHelper();
            bitmap = ih.getRoundedCornerBitmap(bitmap,200);
            img_nav_info.setImageBitmap(bitmap);
            tv_nav_infoname.setText(myInfo.getNickname());
            tv_nav_info_nickandphone.setText(myInfo.getInfo_name()+" / "+myInfo.getPhone_no());
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        //setHamburger Views end

        btn_place_auto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if(fromto_flag){
                    openAutocompleteActivity("to");
                }else{
                    openAutocompleteActivity("from");
                }
            }
        });
    }

    private void initBottomDrawer() {
//        mLayout.setFadeOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            }
//        });
        mLayout.setTouchEnabled(false);

        layout_spot_from.post(new Runnable() {
            @Override
            public void run() {
                int height = layout_spot_from.getHeight() * 2; //height is ready
                mLayout.setPanelHeight(height);
            }
        }); //panel height 설정
        mLayout.setOverlayed(false);
        mLayout.setCoveredFadeColor(00000000);
        mLayout.setDragView(R.id.dragView);
        //배경 비활성화
    }

    private RadioButton.OnClickListener optionOnClickListener
            = new RadioButton.OnClickListener() {
        public void onClick(View v) {
            if(radio_all.isChecked()){
                tmpRoom.setWay(3);
            }  else if(radio_bill.isChecked()){
                tmpRoom.setWay(2);
            }  else if(radio_point.isChecked()){
                tmpRoom.setWay(1);
            }
        }
    };

    @Override
    public boolean onTouch(View v, final MotionEvent event) {
        try{
            if(event.getAction() == MotionEvent.ACTION_DOWN) {
                if (v == btn) {
                    Log.d("TAG", "In onTouch button");
                    new Thread() {
                        public void run() {
//                            GetDistance gd = new GetDistance();
//                            final JSONObject distances = gd.getDistance(tmpRoom.getStartLat(),tmpRoom.getStartLon(),tmpRoom.getEndLat(),tmpRoom.getEndLon());
                            //getdistanceJSON(distances);
                            Intent intent = new Intent(HomeActivity.this,RoomListActivity.class);
                            intent.putExtra("info_id",info_id);
                            intent.putExtra("object",tmpRoom);
                            startActivity(intent);
                        }
                    }.start();
                    return true;

                } else if (v == tv_spotname) {
                    LatLng latLng = googleMap.getCameraPosition().target;
                    if(!fromto_flag){   //출발지 설정
                        fromto_flag = true;
                        view_point.setVisibility(View.INVISIBLE);
                        tv_from.setText(tv_spotname.getText().toString());
                        tv_from.setTextColor(getResources().getColor(R.color.darkGray));

                        tmpRoom.setStartSpot(tv_spotname.getText().toString());
                        tmpRoom.setStartLat(latLng.latitude);
                        tmpRoom.setStartLon(latLng.longitude);

                        markeropt_start
                                .position(latLng)
                                .snippet(tv_spotname.getText().toString());
                        hashMapMarker.put("start",markeropt_start);

                    }else {
                        fromto_flag = false;
                        view_point.setVisibility(View.INVISIBLE);
                        tv_to.setText(tv_spotname.getText().toString());
                        tv_to.setTextColor(getResources().getColor(R.color.darkGray));

                        tmpRoom.setEndSpot(tv_spotname.getText().toString());
                        tmpRoom.setEndLat(latLng.latitude);
                        tmpRoom.setEndLon(latLng.longitude);

                        markeropt_end
                                .position(latLng)
                                .snippet(tv_spotname.getText().toString());
                        hashMapMarker.put("end",markeropt_end);
                    }
                    googleMap.clear();

                    LatLngBounds.Builder builder = new LatLngBounds.Builder();
                    boolean flag_start = false;
                    boolean flag_end = false;
                    if(hashMapMarker.containsKey("start")){
                        googleMap.addMarker(hashMapMarker.get("start"));
                        builder.include(hashMapMarker.get("start").getPosition());
                        flag_start = true;
                    }
                    if(hashMapMarker.containsKey("end")) {
                        googleMap.addMarker(hashMapMarker.get("end"));
                        builder.include(hashMapMarker.get("end").getPosition());
                        flag_end = true;
                    }
                    if( flag_start && flag_end ){
                        LatLngBounds bounds = builder.build();
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
                    }

                    return true;
                } else if (v == layout_spot_from) {
//                    openAutocompleteActivity("from");
                    view_point.setVisibility(View.VISIBLE);
                    tv_home_indicator.setText("이 위치로 출발지 설정");
                    fromto_flag = false;

                    tv_from.setText("핀을 클릭해 출발지를 지정해주세요");
                    tv_from.setTextColor(getResources().getColor(R.color.colorPink));

                    hashMapMarker.remove("start");
                    googleMap.clear();
                    if(hashMapMarker.containsKey("start"))
                        googleMap.addMarker(hashMapMarker.get("start"));
                    if(hashMapMarker.containsKey("end"))
                        googleMap.addMarker(hashMapMarker.get("end"));
                    return false;
                } else if (v == layout_spot_to) {
//                    openAutocompleteActivity("to");
                    view_point.setVisibility(View.VISIBLE);
                    tv_home_indicator.setText("이 위치로 도착지 설정");
                    tv_to.setText("핀을 클릭해 도착지를 지정해주세요");
                    tv_to.setTextColor(getResources().getColor(R.color.colorPink));
                    fromto_flag = true;

                    hashMapMarker.remove("end");
                    googleMap.clear();
                    if(hashMapMarker.containsKey("start"))
                        googleMap.addMarker(hashMapMarker.get("start"));
                    if(hashMapMarker.containsKey("end"))
                        googleMap.addMarker(hashMapMarker.get("end"));
                    return false;
                }else if (v == tv_time){
                }
            }
        }catch (Exception e){
            Log.d("TAG", e.getMessage());
        }
        return false;
    }

    @SuppressWarnings("StatementWithEmptyBody")
    //햄버거 네비게이션 선택
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        /*if (id == 0) {
        } else if (id == 1) {
        } else if (id == 2) {
        }*/
        switch (id) {
            case R.id.nav_sub_boardlist:
                break;
            case R.id.nav_sub_blocklist:
                Intent intent = new Intent(HomeActivity.this, BlockActivity.class);
                intent.putExtra("info_id",info_id);
                startActivity(intent);
                break;
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onCameraIdle() {
        LatLng latLng = googleMap.getCameraPosition().target;
        String title = getmarkerAddress(latLng);
        tv_spotname.setText(title);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng init = new LatLng(37.56, 126.97);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(init));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.setOnCameraIdleListener(this);

        ImageResourceUtil util = new ImageResourceUtil();
        Bitmap bitmap_icon = util.getBitmap(HomeActivity.this,R.drawable.ic_place_yellow_24dp);
        bitmap_icon = resizeMapIcons(bitmap_icon,150,150);

        markeropt_start = new MarkerOptions();
        markeropt_end = new MarkerOptions();
        markeropt_start.title("출발지")
                .snippet(tmpRoom.getStartSpot())
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap_icon));
        markeropt_end.title("도착지")
                .snippet(tmpRoom.getEndSpot())
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap_icon));

        /* for current location */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            tv_spotname.setText("GPS 권한설정이 필요합니다");
        }

    }

    public String getmarkerAddress(LatLng latLng){
        // GPS를 주소로 변환후 반환
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(
                    latLng.latitude,
                    latLng.longitude,
                    1);
        } catch (IOException ioException) {
            //네트워크 문제
            return "네트워크를 확인해주세요";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "GPS 좌표가 잘못되었습니다";
        }

        if (addresses == null || addresses.size() == 0) {
            return "주소를 발견하지 못했습니다";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        LatLngBounds.Builder builder = new LatLngBounds.Builder();

        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);

            boolean flag_start = false;
            boolean flag_end = false;
            if(hashMapMarker.containsKey("start")){
                googleMap.addMarker(hashMapMarker.get("start"));
                builder.include(hashMapMarker.get("start").getPosition());
                flag_start = true;
            }
            if(hashMapMarker.containsKey("end")) {
                googleMap.addMarker(hashMapMarker.get("end"));
                builder.include(hashMapMarker.get("end").getPosition());
                flag_end = true;
            }
            if( flag_start && flag_end ){
                LatLngBounds bounds = builder.build();
                googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
            }

        }else if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.COLLAPSED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.HIDDEN)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        }else{
            super.onBackPressed();
        }
    }

    private void openAutocompleteActivity(String flag_from_to) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                .build(this);
            if(flag_from_to.equals("from")){
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_FROM);
            }else if(flag_from_to.equals("to")){
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_TO);
            }
        } catch (GooglePlayServicesRepairableException e) {
            GoogleApiAvailability.getInstance().getErrorDialog(this, e.getConnectionStatusCode(),
                    0 /* requestCode */).show();
        } catch (GooglePlayServicesNotAvailableException e) {
            String message = "Google Play Services is not available: " +
                    GoogleApiAvailability.getInstance().getErrorString(e.errorCode);
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Place place = PlaceAutocomplete.getPlace(this, data);
            if(requestCode == PLACE_AUTOCOMPLETE_FROM){   //출발지 설정
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                tv_home_indicator.setText("이 위치로 출발지 설정");
                tv_home_indicator.setTextColor(getResources().getColor(R.color.colorPink));
                img_marker.setImageResource(R.drawable.ic_place_yellow_24dp);
                tv_spotname.setText(formatPlaceDetails(getResources(), place.getName(), place.getAddress()));
                fromto_flag = false;
            }else if (requestCode == PLACE_AUTOCOMPLETE_TO) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
                googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

                tv_home_indicator.setText("이 위치로 도착지 설정");
                tv_home_indicator.setTextColor(getResources().getColor(R.color.colorDart));
                tv_spotname.setText(formatPlaceDetails(getResources(), place.getName(), place.getAddress()));
                fromto_flag = true;
            }
        }else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
        } else if (resultCode == RESULT_CANCELED) {
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name,CharSequence address) {
        return Html.fromHtml(res.getString(R.string.place_details, name, address));
    }

    public void getdistanceJSON(JSONObject response){
        try{
            JSONObject jsonData = response.getJSONArray("rows").getJSONObject(0).getJSONArray("elements").getJSONObject(0);
            String distance = jsonData.getJSONObject("distance").getString("text");
            String time = jsonData.getJSONObject("duration").getString("text");
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public Bitmap resizeMapIcons(Bitmap imageBitmap,int width, int height){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}
