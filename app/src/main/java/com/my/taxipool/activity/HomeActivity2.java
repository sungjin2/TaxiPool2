package com.my.taxipool.activity;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.Handler;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.my.taxipool.vo.Room;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class HomeActivity2 extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnCameraIdleListener, OnMapReadyCallback
        ,GoogleApiClient.OnConnectionFailedListener {

    //for data
    public static CustomerInfo myInfo;
    private Room room= new Room();
    private String info_id;

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private ActionBar actionbar;
    private Toolbar toolbar;
    private ImageButton btn_right;  //in toolbar;

    //For Input Views
    private LinearLayout layout_inputs;
    private InputCustomView
            view_spot_from,
            view_spot_to,
            view_time;
    private Button btn_home;

    private Boolean[] issetted = new Boolean[2];
    //0은 출발 inputview, 1은 도착 inputview
    private  Boolean flag_fromto = false;

    //For Hamburger Views
    private ImageView img_nav_info;
    private TextView tv_nav_infoname, tv_nav_info_nickandphone, tv_point;
    private Bitmap bitmap;
    private Button bt_point;

    // Marker and Map
    MarkerOptions markeropt_start;
    MarkerOptions markeropt_end;
    HashMap<String,MarkerOptions> hashMapMarker = new HashMap<>();

    ImageView img_marker;

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

    //Autocomplete
    private GoogleApiClient mGoogleApiClient;
    private static final int PLACE_AUTOCOMPLETE_TO = 0;
    private static final int PLACE_AUTOCOMPLETE_FROM = 1;

    //Hidden View
    private RelativeLayout layout_hidden_spot;
    private InputCustomView view_hidden_spot;
    private Button btn_hidden;
    private RelativeLayout layout_input_showmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);

        info_id = Set.Load(HomeActivity2.this,"info_id",null);
//        info_id = "135425414";

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

        issetted[0] = false;
        issetted[1] = false;

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
                        myInfo.setPoint(object.getInt("point"));
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
        tv_point = (TextView) headerLayout.findViewById(R.id.tv_point);
        bt_point = (Button) headerLayout.findViewById(R.id.bt_point);


        //Marker
        img_marker = (ImageView) findViewById(R.id.img_pin);

        //Hidden Views
        layout_hidden_spot = (RelativeLayout) findViewById(R.id.layout_hidden_spot);
        view_hidden_spot = (InputCustomView) findViewById(R.id.view_hidden_spot);
        btn_hidden = (Button) findViewById(R.id.btn_hidden);
        layout_input_showmap = (RelativeLayout) findViewById(R.id.layout_input_showmap);
    }

    private void setViews() {
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

        btn_hidden.setOnClickListener(basic_click_listener);
        bt_point.setOnClickListener(basic_click_listener);
        layout_input_showmap.setOnClickListener(basic_click_listener);
        view_hidden_spot.setOnClickListener(basic_click_listener);

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
        btn_right = (ImageButton) actionbar.findViewById(R.id.btn_right);
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
            tv_point.setText(myInfo.getPoint());
            tv_nav_info_nickandphone.setText(myInfo.getInfo_name()+" / "+myInfo.getPhone_no());
        }catch(InterruptedException e) {
            e.printStackTrace();
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Intent intent;
        switch (id) {
            case R.id.nav_sub_boardlist:
                intent = new Intent(HomeActivity2.this, RoomRecordActiviy.class);
                startActivity(intent);
                break;
            case R.id.nav_sub_blocklist:
                intent = new Intent(HomeActivity2.this, BlockActivity.class);
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
        view_hidden_spot.setRightLabel(title);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng init = new LatLng(37.56, 126.97);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(init));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.setOnCameraIdleListener(this);

        ImageResourceUtil util = new ImageResourceUtil();
        Bitmap bitmap_icon_from = util.getBitmap(HomeActivity2.this,R.mipmap.ic_from);
        bitmap_icon_from = resizeMapIcons(bitmap_icon_from,100,100);
        Bitmap bitmap_icon_to = util.getBitmap(HomeActivity2.this,R.mipmap.ic_to);
        bitmap_icon_to = resizeMapIcons(bitmap_icon_to,100,100);

        markeropt_start = new MarkerOptions();
        markeropt_end = new MarkerOptions();
        markeropt_start.title("출발지")
                .snippet(room.getStart_spot())
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap_icon_from));
        markeropt_end.title("도착지")
                .snippet(room.getEnd_spot())
                .icon(BitmapDescriptorFactory.fromBitmap(bitmap_icon_to));

        /* for current location */
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            view_hidden_spot.setRightLabel("GPS 권한설정이 필요합니다");
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


    private boolean backPressedToExitOnce = false;
    @Override
    public void onBackPressed() {
        if (backPressedToExitOnce) {
            super.onBackPressed();
        } else {
            this.backPressedToExitOnce = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    backPressedToExitOnce = false;
                }
            }, 500);
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            }else if(layout_inputs.getVisibility() == View.GONE){
                googleMap.clear();
                showInputs(true);
            }else if(layout_inputs.getVisibility() == View.VISIBLE){
                showInputs(false);
            }else{
                super.onBackPressed();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        this.backPressedToExitOnce = false;
    }

    private void openAutocompleteActivity(Boolean flag_from_to) {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY).build(this);
//            if(!flag_from_to){
//                img_marker.setImageResource(R.mipmap.ic_from);
                startActivityForResult(intent, PLACE_AUTOCOMPLETE_FROM);
//            }else{
//                img_marker.setImageResource(R.mipmap.ic_to);
//                startActivityForResult(intent, PLACE_AUTOCOMPLETE_TO);
//            }
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
            switch (id) {
                case R.id.view_spot_from:
                    flag_fromto = false;
                    if(issetted[0]){
                        showDialogForChangePin(HomeActivity2.this);
                    }else{
                        showInputs(false);
                    }
                    break;
                case R.id.view_spot_to:
                    flag_fromto = true;
                    if(issetted[1]){
                        showDialogForChangePin(HomeActivity2.this);
                    }else{
                        showInputs(false);
                    }
                    break;
            }
        }
    };

    public View.OnClickListener basic_click_listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent;
            switch (v.getId()) {
                case R.id.view_time:
                    final int DIALOG_TIME = 2;
                    showDialog(DIALOG_TIME);
                    break;
                case R.id.btn_home:
                    intent = new Intent(HomeActivity2.this,RoomListActivity.class);
                    intent.putExtra("object",room);
                    startActivity(intent);
                    break;
                case R.id.btn_hidden:
                    LatLng latLng = googleMap.getCameraPosition().target;
                    if(!flag_fromto){   //출발지 설정
                        room.setStart_spot(view_hidden_spot.getRightLabel());
                        room.setStart_lat(latLng.latitude);
                        room.setStart_lon(latLng.longitude);
                        markeropt_start
                                .position(latLng)
                                .snippet(room.getStart_spot());
                        hashMapMarker.put("start",markeropt_start);
                        view_spot_from.setRightLabel(room.getStart_spot());
                        view_spot_from.setRightColor(R.color.black);
                        issetted[0]= true;
                    }else{
                        room.setEnd_spot(view_hidden_spot.getRightLabel());
                        room.setEnd_lat(latLng.latitude);
                        room.setEnd_lon(latLng.longitude);
                        markeropt_end
                                .position(latLng)
                                .snippet(room.getEnd_spot());
                        hashMapMarker.put("end",markeropt_end);
                        view_spot_to.setRightLabel(room.getEnd_spot());
                        view_spot_to.setRightColor(R.color.black);
                        issetted[1]= true;
                    }
                    showInputs(true);
                    break;
                case R.id.bt_point:
                    intent = new Intent(HomeActivity2.this,PointActivity.class);
                    startActivity(intent);
                    break;
                case R.id.view_hidden_spot:
                    openAutocompleteActivity(flag_fromto);
                    break;
                case R.id.layout_input_showmap:
                    showMap();
                    break;
            }
        }
    };


//    custom methods
    private void showInputs(boolean flag){
        //true = VISIBLE, false = GONE
        if(flag){
            layout_inputs.setTranslationY(layout_inputs.getHeight());
            layout_inputs.setVisibility(View.GONE);
            btn_right.setVisibility(View.GONE);
            layout_inputs.animate()
                    .translationYBy(-layout_inputs.getHeight())
                    .setDuration(500)
                    .setStartDelay(400)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationStart(final Animator animation) {
                            layout_inputs.setVisibility(View.VISIBLE);
                            layout_hidden_spot.setVisibility(View.GONE);
                            if(issetted[0]&&issetted[1]){
                                layout_input_showmap.setVisibility(View.VISIBLE);
                            }
                        }
                    })
                    .start();
        }else{
            LatLng latLng = googleMap.getCameraPosition().target;
            String title = getmarkerAddress(latLng);
            view_hidden_spot.setRightLabel(title);
            layout_inputs.animate()
                    .translationY(layout_inputs.getHeight())
                    .setDuration(400)
                    .setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            super.onAnimationEnd(animation);
                            layout_inputs.setVisibility(View.GONE);
                            layout_hidden_spot.setVisibility(View.VISIBLE);
                            btn_right.setVisibility(View.VISIBLE);
                            if(!flag_fromto){
                                img_marker.setImageResource(R.mipmap.ic_from);
                                view_hidden_spot.setLeftLabel("출발지");
                            }else{
                                img_marker.setImageResource(R.mipmap.ic_to);
                                view_hidden_spot.setLeftLabel("도착지");
                            }
                        }
                    });
        }
    }

    private void showMap(){
        layout_inputs.animate()
                .translationY(layout_inputs.getHeight())
                .setDuration(400)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        layout_inputs.setVisibility(View.GONE);
                        layout_hidden_spot.setVisibility(View.GONE);
                    }
                });

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
            googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 200));
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

    private void showDialogForChangePin(Context context){
        //AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("마커를 변경합니다.");
        if(!flag_fromto){
            builder.setMessage("출발지를 변경하시겠습니까?");
        }else{
            builder.setMessage("도착지를 변경하시겠습니까?");
        }
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch(which){
                    case DialogInterface.BUTTON_POSITIVE:
                        showInputs(false);
                    case DialogInterface.BUTTON_NEGATIVE:
                        dialog.cancel();
                        break;
                }
            }
        };

        builder.setPositiveButton("네", dialogClickListener);
        builder.setNegativeButton("실수에요",dialogClickListener);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    @Deprecated
    protected Dialog onCreateDialog(int id) {
        TimePickerDialog tpd =
                new TimePickerDialog(this, AlertDialog.THEME_HOLO_LIGHT,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view,
                                                  int hourOfDay, int minute) {
                                Calendar calendar = Calendar.getInstance();
                                long now = System.currentTimeMillis();

                                Date sysdate = new Date(now);

                                SimpleDateFormat sdfday = new SimpleDateFormat("yyyy-MM-dd");
                                SimpleDateFormat starthour = new SimpleDateFormat("HH");
                                SimpleDateFormat startiminute = new SimpleDateFormat("mm");
                                SimpleDateFormat transFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

                                int hour = Integer.parseInt(starthour.format(sysdate));
                                int iminute = Integer.parseInt(startiminute.format(sysdate));

                                calendar.setTime(sysdate);

                                if( hourOfDay < hour || (hourOfDay==hour && minute < iminute) ){
                                    Toast.makeText(getApplicationContext(), "시간설정은 24시간 제한이므로 내일로 설정됩니다.", Toast.LENGTH_LONG).show();
                                    calendar.add(Calendar.DATE, 1);
                                        Toast.makeText(getApplicationContext(), "시간설정은 24시간 제한이므로 내일로 설정됩니다.", Toast.LENGTH_LONG).show();
                                        calendar.add(Calendar.DATE, 1);
                                    if(hourOfDay>12) {
                                        view_time.setRightLabel("내일 오후" + (hourOfDay-12) + "시 " + minute + "분");
                                    }else if(hourOfDay==0){
                                        view_time.setRightLabel("내일 오전" + (hourOfDay+12) + "시 " + minute + "분");
                                    }else{
                                        view_time.setRightLabel("내일 오전" + hourOfDay + "시 " + minute + "분");
                                    }
                                }else{
                                    if(hourOfDay>12) {
                                        view_time.setRightLabel("오늘 오후" + (hourOfDay-12) + "시 " + minute + "분");
                                    }else if(hourOfDay==0){
                                        view_time.setRightLabel("오늘 오전" + (hourOfDay+12) + "시 " + minute + "분");
                                    }else{
                                        view_time.setRightLabel("오늘 오전" + hourOfDay + "시 " + minute + "분");
                                    }
                                }

                                view_time.setRightColor(R.color.black);
                                String strstart_time = sdfday.format(calendar.getTimeInMillis())+" "+hourOfDay+":"+""+minute;
                                try {
                                    room.setStart_time(transFormat.parse(strstart_time));
                                }catch (ParseException e){

                                }
                            }
                        }, // 값설정시 호출될 리스너 등록
                        4,19, false); // 기본값 시분 등록
        // true : 24 시간(0~23) 표시
        // false : 오전/오후 항목이 생김
        return tpd;
    }
}
