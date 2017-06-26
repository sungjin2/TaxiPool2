package com.my.taxipool.activity;

/**
 * by GY on 2017-06-19.
 * by Hyeon on 2017-06-19.
 */

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.my.taxipool.R;
import com.my.taxipool.util.CommuServer;
import com.my.taxipool.util.ImageResourceUtil;
import com.my.taxipool.util.Set;
import com.my.taxipool.vo.Room;
import com.my.taxipool.vo.TmpRoom;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class RoomRegistActivity extends AppCompatActivity implements OnMapReadyCallback{
    TextView tv_regist_start_spot,tv_regist_end_spot;
    MarkerOptions startMarkerOption, endMarkerOption;
    GoogleMap googlemapTmp;

    final int DIALOG_TIME = 2;

    //InputView
    private RadioGroup payment;    private RadioButton cash;    private RadioButton point;
    private CheckBox[] ck_choice_option = new CheckBox[4];
    private TextView[] tv_choice = new TextView[4];
    private TextView tv_regist_time;
    private Spinner spin_regist_peoplenum;
    private Button bt_regist;
    private RadioButton rd;

    //inputValues
    private String can_alcohol="y";    private String gender="0";
    private String room_no;
    private Date time;
    TmpRoom tmpRoom;
    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        FragmentManager manager = getSupportFragmentManager();
        SupportMapFragment mapFragment = (SupportMapFragment)manager.findFragmentById(R.id.map_regist);
        mapFragment.getMapAsync(this);
        //지도준비 -> onMapReady() 자동호출

        setViewIds();
        setViews();
    }

    public void setViewIds(){
        ck_choice_option[0] = (CheckBox)findViewById(R.id.chAlcohol);
        ck_choice_option[1]= (CheckBox)findViewById(R.id.chGender);
        ck_choice_option[2] = (CheckBox)findViewById(R.id.chWoman);
        ck_choice_option[3] = (CheckBox)findViewById(R.id.chMan);

        tv_choice[0] = (TextView)findViewById(R.id.txalcohol);
        tv_choice[1] = (TextView)findViewById(R.id.txGender);
        tv_choice[2] = (TextView)findViewById(R.id.txWoman);
        tv_choice[3] = (TextView)findViewById(R.id.txMan);

        cash = (RadioButton)findViewById(R.id.cash);
        point = (RadioButton)findViewById(R.id.point);

        payment=(RadioGroup)findViewById(R.id.payment);
        bt_regist=(Button)findViewById(R.id.btRegist);

        tv_regist_start_spot = (TextView)findViewById(R.id.tv_regist_start_spot);
        tv_regist_end_spot = (TextView)findViewById(R.id.tv_regist_end_spot);

        spin_regist_peoplenum = (Spinner)findViewById(R.id.spin_regist_peoplenum);
        tv_regist_time = (TextView)findViewById(R.id.tv_regist_time);
        tmpRoom = (TmpRoom) getIntent().getSerializableExtra("object");
    }

    public void setViews(){
        //선택옵션 체크박스 체크 선택 조절하기
        for(int i = 1;i<4;i++) {
            final int index = i;
            ck_choice_option[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for(int j=1;j<4;j++){
                        ck_choice_option[j].setChecked(false);
                    }
                    ck_choice_option[index].setChecked(true);
                }
            });
        }

        tv_regist_start_spot.setText(tmpRoom.getStartSpot());
        tv_regist_end_spot.setText(tmpRoom.getEndSpot());
        tv_regist_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(DIALOG_TIME);
            }
        });

        bt_regist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //gender_alcohol
                for(int i = 0;i<4;i++){
                    if(ck_choice_option[i].isChecked()==true){
                        if(i==0){
                            can_alcohol ="n";
                        }
                        if(i==2){
                            gender="f";
                        }else if(i==3){
                            gender="m";
                        }
                    }
                }

                //payment
                String pay ="";
                if(point.getId() == (payment.getCheckedRadioButtonId())){
                    rd = (RadioButton) findViewById(payment.getCheckedRadioButtonId());
                    pay ="p";
                }else if(cash.getId() == (payment.getCheckedRadioButtonId())){
                    rd = (RadioButton) findViewById(payment.getCheckedRadioButtonId());
                    pay = "c";
                }

                room = new Room(0,"135425414",
                        spin_regist_peoplenum.getSelectedItemPosition()+1,
                        pay,gender,can_alcohol,tv_regist_start_spot.getText().toString(),
                        tv_regist_end_spot.getText().toString(),
                        String.valueOf(tmpRoom.getStartLat()),String.valueOf(tmpRoom.getStartLon()),
                        String.valueOf(tmpRoom.getEndLat()),String.valueOf(tmpRoom.getEndLon()),
                        time, "a");

                //AlertDialog
                AlertDialog.Builder builder = new AlertDialog.Builder(RoomRegistActivity.this);
                builder.setTitle("이제부터 방장이 됩니다.");
                builder.setMessage("입력한 정보대로 합승방을 만들거에요");

                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch(which){
                            case DialogInterface.BUTTON_POSITIVE:
                                new CommuServer(CommuServer.REGIST_ROOM, new CommuServer.OnCommuListener() {
                                    @Override
                                    public void onSuccess(JSONObject object, JSONArray arr, String str) {
                                        Log.d("ddu result!!",str);
                                        room_no = str;
                                        Intent intent = new Intent(RoomRegistActivity.this, RoomActivity.class);
                                        Set.Save(RoomRegistActivity.this,"room_no",room_no);
                                        intent.putExtra("state",Room.WAIT_TOGO);
                                        startActivity(intent);
                                        finish();
                                    }
                                    @Override
                                    public void onFailed(Error error) {
                                    }
                                }).addParam("admin_id", room.getAdmin_id())
                                        .addParam("max_cnt", room.getMax_cnt())
                                        .addParam("payment", room.getPayment())
                                        .addParam("room_gender", room.getRoom_gender())
                                        .addParam("alcohol", room.getAlcohol())
                                        .addParam("start_spot", room.getStart_spot())
                                        .addParam("end_spot", room.getEnd_spot())
                                        .addParam("start_x", room.getStart_x())
                                        .addParam("start_y", room.getStart_y())
                                        .addParam("end_x", room.getEnd_x())
                                        .addParam("end_y", room.getEnd_y()).start();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.cancel();
                                break;
                        }
                    }
                };

                builder.setPositiveButton("네", dialogClickListener);
                builder.setNegativeButton("잠깐만요",dialogClickListener);
                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
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

                                if(hourOfDay>12) {
                                    tv_regist_time.setText("오후" + (hourOfDay-12) + "시 " + minute + "분");
                                }else if(hourOfDay==0){
                                    tv_regist_time.setText("오전" + (hourOfDay+12) + "시 " + minute + "분");
                                }else{
                                    tv_regist_time.setText("오전" + hourOfDay + "시 " + minute + "분");
                                }

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

                                if(hourOfDay<hour){
                                    Toast.makeText(getApplicationContext(), "현재시간보다 이전시간을 선택하셨습니다. 다음 날짜로 변경합니다.", Toast.LENGTH_LONG).show();
                                    calendar.add(Calendar.DATE, 1);
                                }else if(hourOfDay==hour){
                                    if(minute<iminute) {
                                        Toast.makeText(getApplicationContext(), "현재시간보다 이전시간을 선택하셨습니다. 다음 날짜로 변경합니다.", Toast.LENGTH_LONG).show();
                                        calendar.add(Calendar.DATE, 1);
                                    }
                                }
                                String strstart_time = sdfday.format(calendar.getTimeInMillis())+" "+hourOfDay+":"+""+minute;
                                try {
                                    time = transFormat.parse(strstart_time);
                                }catch (ParseException e){

                                }
                            }
                        }, // 값설정시 호출될 리스너 등록
                        4,19, false); // 기본값 시분 등록
        // true : 24 시간(0~23) 표시
        // false : 오전/오후 항목이 생김
        return tpd;

    }

    @Override
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void onMapReady(final GoogleMap googleMap) {
        startMarkerOption = new MarkerOptions();
        endMarkerOption = new MarkerOptions();
        LatLng start_latlon = new LatLng(tmpRoom.getStartLat(), tmpRoom.getStartLon());
        LatLng end_latlon = new LatLng(tmpRoom.getEndLat(), tmpRoom.getEndLon());

        ImageResourceUtil util = new ImageResourceUtil();
        Bitmap bitmap_icon = util.getBitmap(RoomRegistActivity.this,R.drawable.ic_place_yellow_24dp);
        bitmap_icon = resizeMapIcons(bitmap_icon,130,130);

        startMarkerOption.position(start_latlon)
            .title("출발지")
            .snippet(tmpRoom.getStartSpot())
            .icon(BitmapDescriptorFactory.fromBitmap(bitmap_icon));
        endMarkerOption.position(end_latlon)
            .title("도착지")
            .snippet(tmpRoom.getEndSpot())
            .icon(BitmapDescriptorFactory.fromBitmap(bitmap_icon));

        googleMap.addMarker(startMarkerOption);
        googleMap.addMarker(endMarkerOption);
        googleMap.addPolyline(new PolylineOptions()
                .add(start_latlon, end_latlon)
                .width(4)
                .color(getResources().getColor(R.color.colorDart)));

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(start_latlon);
        builder.include(end_latlon);
        LatLngBounds bounds = builder.build();

//        googleMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 80));
    }

    public Bitmap resizeMapIcons(Bitmap imageBitmap,int width, int height){
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }
}