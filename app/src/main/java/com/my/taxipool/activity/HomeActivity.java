package com.my.taxipool.activity;

import android.Manifest;
import android.content.ClipData;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
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
import com.google.android.gms.maps.model.LatLng;
import com.my.taxipool.R;
import com.my.taxipool.network.GetDistance;
import com.my.taxipool.vo.TmpRoom;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,GoogleMap.OnCameraIdleListener, OnMapReadyCallback,View.OnTouchListener
        ,GoogleApiClient.OnConnectionFailedListener {

    private SlidingUpPanelLayout mLayout;
    private NavigationView navigationView;
    private DrawerLayout drawer;
    private Toolbar toolbar;

    //For Input View
    private TextView tv_from;    private TextView tv_to;    private TextView tv_time;
    private Button btn;    private Spinner spinner_people;
    private RadioButton radio_bill; private RadioButton radio_point; private RadioButton radio_all;

    // Marker
    private TextView tv_spotname;       //Marker's textview
    private LinearLayout view_point;    //Marker
    private LinearLayout layout_spot;   //한칸 (출발지)

    private GoogleMap googleMap;
    private SupportMapFragment mapFragment;

    //Autocomplete
    private GoogleApiClient mGoogleApiClient;
    private static final int REQUEST_CODE_AUTOCOMPLETE = 1;
    private static final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;

    TmpRoom tmpRoom= new TmpRoom();

    long time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

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
    }

    private void setViewIds(){
        //Sliding Layout
        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);
        //toolbar & drawer & navi
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        //input리스트
        layout_spot = (LinearLayout) findViewById(R.id.layout_spot);
        view_point = (LinearLayout) findViewById(R.id.view_point);
        tv_from = (TextView) findViewById(R.id.tv_from);
        tv_to = (TextView) findViewById(R.id.tv_to);
        tv_time = (TextView) findViewById(R.id.tv_time);
        spinner_people = (Spinner) findViewById(R.id.spinner);
        radio_bill= (RadioButton) findViewById(R.id.radio_bill);
        radio_point= (RadioButton) findViewById(R.id.radio_point);
        radio_all= (RadioButton) findViewById(R.id.radio_all);

        //Marker
        view_point = (LinearLayout) findViewById(R.id.view_point);
        tv_spotname = (TextView) findViewById(R.id.tv_spotname);
        btn = (Button) findViewById(R.id.button_exam);
    }

    private void setViews() {
        view_point.post(new Runnable() {
            @Override
            public void run() {
                int height = view_point.getHeight();
                ImageView image = (ImageView) findViewById(R.id.ic_place);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(image.getLayoutParams());
                lp.setMargins(0, 0, 0, height);
                image.setLayoutParams(lp);
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
        tv_to.setOnTouchListener(this);
        tv_from.setOnTouchListener(this);
        tv_time.setOnTouchListener(this);

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

    private void initBottomDrawer() {
//        mLayout.setFadeOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//            }
//        });
        mLayout.setTouchEnabled(false);

        layout_spot.post(new Runnable() {
            @Override
            public void run() {
                int height = layout_spot.getHeight() * 2; //height is ready
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
                            GetDistance gd = new GetDistance();
                            final JSONObject distances = gd.getDistance(tmpRoom.getStartLat(),tmpRoom.getStartLon(),tmpRoom.getEndLat(),tmpRoom.getEndLon());
                            getdistanceJSON(distances);
                            Intent intent = new Intent(HomeActivity.this,RoomListActivity.class);
                            intent.putExtra("object",tmpRoom);
                            startActivity(intent);
                        }
                    }.start();
                    return true;

                } else if (v == tv_spotname) {
                    tmpRoom.setStartSpot(tv_spotname.getText().toString());
                    tv_from.setText(tv_spotname.getText().toString());
                    return true;

                } else if (v == tv_from) {
                    return true;

                } else if (v == tv_to) {
                    openAutocompleteActivity();
                    return true;
                }else if (v == tv_time){
//                    TimePickerDialog dialog = new TimePickerDialog(this,
//                            AlertDialog.THEME_HOLO_LIGHT,
//                            new TimePickerDialog.OnTimeSetListener() {
//                                @Override
//                                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
//                                    // 설정버튼 눌렀을 때
//                                    if(hourOfDay > 12){
//                                        tv_time.setText((hourOfDay - 12) + ":" + minute + " 오후  부터");
//                                    }else{
//                                        tv_time.setText(hourOfDay + ":" + minute + " 오전  부터");
//                                    }
//                                    tmpRoom.setTime(hourOfDay+":"+minute);
//                                }
//                            },15,24,false);
//                    dialog.show();
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
        tmpRoom.setStartLat(latLng.latitude);
        tmpRoom.setStartLon(latLng.longitude);
        tmpRoom.setStartSpot(title);
        tv_spotname.setText(title);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        LatLng init = new LatLng(37.56, 126.97);
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(init));
        googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        googleMap.setOnCameraIdleListener(this);

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
            Toast.makeText(this, "지오코더 서비스 사용불가", Toast.LENGTH_LONG).show();
            return "지오코더 서비스 사용불가";
        } catch (IllegalArgumentException illegalArgumentException) {
            Toast.makeText(this, "잘못된 GPS 좌표", Toast.LENGTH_LONG).show();
            return "잘못된 GPS 좌표";
        }

        if (addresses == null || addresses.size() == 0) {
            Toast.makeText(this, "주소 미발견", Toast.LENGTH_LONG).show();
            return "주소 미발견";
        } else {
            Address address = addresses.get(0);
            return address.getAddressLine(0).toString();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }else if (mLayout != null &&
                (mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || mLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }else{
            super.onBackPressed();
        }
    }

    private void openAutocompleteActivity() {
        try {
            Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_OVERLAY)
                            .build(this);
            startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);
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

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = PlaceAutocomplete.getPlace(this, data);
                tv_to.setText(place.getId());
                tv_to.setText(formatPlaceDetails(getResources(), place.getName(),
                        place.getId(), place.getAddress(), place.getPhoneNumber(),
                        place.getWebsiteUri()));
                tmpRoom.setEndLat(place.getLatLng().latitude);
                tmpRoom.setEndLon(place.getLatLng().longitude);
                tmpRoom.setEndSpot(place.getAddress().toString());
            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                Status status = PlaceAutocomplete.getStatus(this, data);
            } else if (resultCode == RESULT_CANCELED) {
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
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
}
