package com.my.taxipool.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.my.taxipool.util.Set;
import com.my.taxipool.view.InputCustomView;
import com.my.taxipool.vo.CustomerInfo;
import com.my.taxipool.vo.TmpRoom;

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


public class HomeActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnCameraIdleListener, OnMapReadyCallback
        ,GoogleApiClient.OnConnectionFailedListener {

    //for data
    public static CustomerInfo myInfo;
    private TmpRoom tmpRoom= new TmpRoom();
    private String info_id;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBar actionbar;
    private Toolbar toolbar;

    //For Input Views
    private LinearLayout layout_inputs;
    private InputCustomView view_spot_from;
    private InputCustomView view_spot_to;
    private InputCustomView view_time;

//    private Button btn_place_auto;
    private Button btn_home;

    private  Boolean flag_fromto = false;

    //For Hamburger Views
    private ImageView img_nav_info;
    private TextView tv_nav_infoname;
    private TextView tv_nav_info_nickandphone;
    private Bitmap bitmap;

    // Marker and Map
    MarkerOptions markeropt_start;
    MarkerOptions markeropt_end;
    HashMap<String,MarkerOptions> hashMapMarker = new HashMap<>();

    LinearLayout layout_home_indicator;
    ImageView img_marker;
    TextView tv_home_indicator;

    private TextView tv_spotname;       //Marker's textview
    private LinearLayout view_point;    //Marker
    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

    //Autocomplete
    private GoogleApiClient mGoogleApiClient;
    private static final int PLACE_AUTOCOMPLETE_TO = 0;
    private static final int PLACE_AUTOCOMPLETE_FROM = 1;

    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        info_id = Set.Load(HomeActivity2.this,"info_id",null);
        info_id = "135425414";

        /* Google map start */
        FragmentManager manager = getSupportFragmentManager();
        mapFragment = (SupportMapFragment)manager.findFragmentById(R.id.map_home);
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
        //For Input Views
        layout_inputs = (LinearLayout) findViewById(R.id.layout_inputs);
        view_spot_from = (InputCustomView) findViewById(R.id.view_spot_from);
        view_spot_to = (InputCustomView) findViewById(R.id.view_spot_to);
        view_time = (InputCustomView) findViewById(R.id.view_time);

        btn_home = (Button)  findViewById(R.id.btn_home);

        //toolbar & drawer & navi
        toolbar = (Toolbar) findViewById(R.id.toolbar_home2);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout2);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //Views in Hamburger
        View headerLayout = navigationView.getHeaderView(0);
        img_nav_info = (ImageView) headerLayout.findViewById(R.id.img_nav_info);
        tv_nav_infoname = (TextView) headerLayout.findViewById(R.id.tv_nav_infoname);
        tv_nav_info_nickandphone = (TextView) headerLayout.findViewById(R.id.tv_nav_info_nickandphone);

        //Marker
        view_point = (LinearLayout) findViewById(R.id.view_point);                              //전체
        layout_home_indicator = (LinearLayout) findViewById(R.id.layout_home_indicator);    //말풍선
        img_marker = (ImageView) findViewById(R.id.ic_place);
        tv_spotname = (TextView) findViewById(R.id.tv_spotname);
        tv_home_indicator  = (TextView) findViewById(R.id.tv_home_indicator);
    }

    private void setViews() {
        layout_home_indicator.post(new Runnable() {
            @Override
            public void run() {
                int height = layout_home_indicator.getHeight();
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

        //Set clicklistener about input
        view_spot_from.setOnClickListener(input_click_listener);
        view_spot_to.setOnClickListener(input_click_listener);

        //Set basic clicklistener
        view_time.setOnClickListener(basic_click_listener);
        btn_home.setOnClickListener(basic_click_listener);
        layout_home_indicator.setOnClickListener(basic_click_listener);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();

        // Custom Actionbar를 사용하기 위해 CustomEnabled을 true 시키고 필요 없는 것은 false 시킨다
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(false);         //액션바 아이콘을 업 네비게이션 형태(<)로 표시합니다.
        actionBar.setDisplayShowTitleEnabled(false);        //액션바에 표시되는 제목의 표시유무를 설정
        actionBar.setDisplayShowHomeEnabled(false);            //홈 아이콘을 숨김처리합니다.

        //layout을 가지고 와서 actionbar에 포팅을 시킵니다.
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);
        View actionbar = inflater.inflate(R.layout.actionbar_custom, null);

        actionBar.setCustomView(actionbar);
        ImageButton btn_right = (ImageButton) actionbar.findViewById(R.id.btn_right);
        btn_right.setOnClickListener(new View.OnClickListener(){
              @Override
              public void onClick(View v) {
                  openAutocompleteActivity(flag_fromto);
              }
          }
        );
        //액션바 양쪽 공백 없애기
        Toolbar parent = (Toolbar)actionbar.getParent();
        parent.setContentInsetsAbsolute(0,0);

        return true;
    }

    //get myinfo for hamberger menu
    private void setMyinfo() {
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
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.nav_sub_boardlist:
                break;
            case R.id.nav_sub_blocklist:
                Intent intent = new Intent(HomeActivity2.this, BlockActivity.class);
                startActivity(intent);
                break;
        }
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
        Bitmap bitmap_icon = util.getBitmap(HomeActivity2.this,R.drawable.ic_place_yellow_24dp);
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if(layout_inputs.getVisibility() == View.GONE){
            showInputs(true);
        }else{
            super.onBackPressed();
        }
    }

    private void openAutocompleteActivity(Boolean flag_from_to) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
            if(!flag_from_to){
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_FROM);
            }else{
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
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(place.getLatLng()));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));

            if(requestCode == PLACE_AUTOCOMPLETE_FROM){   //출발지 설정
                tv_home_indicator.setText("이 위치로 출발지 설정");
                tv_home_indicator.setTextColor(getResources().getColor(R.color.colorPink));
                img_marker.setImageResource(R.drawable.ic_place_yellow_24dp);
                flag_fromto = false;

            }else if (requestCode == PLACE_AUTOCOMPLETE_TO) {
                tv_home_indicator.setText("이 위치로 도착지 설정");
                tv_home_indicator.setTextColor(getResources().getColor(R.color.colorDart));
                flag_fromto = true;
            }

        }else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
            Status status = PlaceAutocomplete.getStatus(this, data);
        } else if (resultCode == RESULT_CANCELED) {
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    public View.OnClickListener input_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int id = v.getId();
            showInputs(false);
            Log.d("ddu","click");
            switch (id) {
                case R.id.view_spot_from:
                    Log.d("ddu","from click");
                    tv_home_indicator.setText("이 위치로 출발지 설정");
                    tv_home_indicator.setTextColor(getResources().getColor(R.color.colorPink));
                    break;
                case R.id.view_spot_to:
                    Log.d("ddu","to click");
                    tv_home_indicator.setText("이 위치로 도착지 설정");
                    tv_home_indicator.setTextColor(getResources().getColor(R.color.colorDart));
                    break;
            }
        }
    };

    public View.OnClickListener basic_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.view_time:
                    break;
//                case R.id.btn_place_auto2:
//                    openAutocompleteActivity(flag_fromto);
//                    break;
                case R.id.btn_home:
                    Intent intent = new Intent(HomeActivity2.this,RoomListActivity.class);
                    intent.putExtra("object",tmpRoom);
                    startActivity(intent);
                    break;
                case R.id.layout_home_indicator:
                    LatLng latLng = googleMap.getCameraPosition().target;
                    if(!flag_fromto){   //출발지 설정
                        flag_fromto = true;
                        view_spot_from.setRightLabel(tv_spotname.getText().toString());
                        tmpRoom.setStartSpot(tv_spotname.getText().toString());
                        tmpRoom.setStartLat(latLng.latitude);
                        tmpRoom.setStartLon(latLng.longitude);

                        markeropt_start
                                .position(latLng)
                                .snippet(tv_spotname.getText().toString());
                        hashMapMarker.put("start",markeropt_start);
                    }else {
                        flag_fromto = false;
                        view_spot_to.setRightLabel(tv_spotname.getText().toString());

                        tmpRoom.setEndSpot(tv_spotname.getText().toString());
                        tmpRoom.setEndLat(latLng.latitude);
                        tmpRoom.setEndLon(latLng.longitude);

                        markeropt_end
                                .position(latLng)
                                .snippet(tv_spotname.getText().toString());
                        hashMapMarker.put("end",markeropt_end);
                    }
                    googleMap.clear();
                    refreshMarkers();
                    showInputs(true);
                    break;
            }
        }
    };


//    custom methods
    private void showInputs(boolean flag){
        //true = VISIBLE, false = GONE
        if(flag){
            layout_inputs.setVisibility(View.VISIBLE);
            view_point.setVisibility(View.GONE);
        }else{
            layout_inputs.animate()
                    .translationY(layout_inputs.getHeight())
                    .alpha(0.0f)
                    .setDuration(300)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layout_inputs.setVisibility(View.GONE);
                        }
                    });
            view_point.setVisibility(View.VISIBLE);
        }
    }

    private void refreshMarkers(){
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
