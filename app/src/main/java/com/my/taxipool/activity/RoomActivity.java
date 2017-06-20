package com.my.taxipool.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.taxipool.R;
import com.my.taxipool.adapter.MyPagerAdapter;
import com.my.taxipool.util.Set;

/**
 * Created by Hyeon on 2017-05-28.
 */
public class RoomActivity extends AppCompatActivity{
    //Dialog dialog;
    TextView tv_dialog_name;    ImageView image_dialog;
    TextView btn_dialog_yes;    TextView btn_dialog_no;

    //viewPager
    private ViewPager viewPager;
    private TabLayout tab;
    //status
    Boolean isBangjang;
    static final int NO_MEMBER = 0;
    static final int REQUIRING = 10;
//    static final int YES_MEMBER = 20;
    static final int WAIT_TOGO = 20;
    static final int FINISH = 50;
    int status;
    String roomNum;

    //views
    private Button btn_in_room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        isBangjang = getIntent().getBooleanExtra("isBangjang",false);
        status = Set.Load(RoomActivity.this,"status",NO_MEMBER);

        setViewIds();
        setViews();
        if(status >= 20){
            setContentView(R.layout.activity_room_yes_member);
            set_TabViews();
        }else{
            setContentView(R.layout.activity_room_no_member);
        }
    }
    private void setViewIds() {
//        btn_in_room = (Button) findViewById(R.id.btn_in_room);
    }
    private void setViews() {
//        if (status == NO_MEMBER && !isBangjang){
//            btn_in_room.setBackgroundColor(getResources().getColor(R.color.darkGray));
//            btn_in_room.setTextColor(getResources().getColor(R.color.white));
//            btn_in_room.setText("동승 신청");
//
//        }
//        else if(status == REQUIRING){
//            btn_in_room.setBackgroundColor(getResources().getColor(R.color.darkGray));
//            btn_in_room.setTextColor(getResources().getColor(R.color.white));
//            btn_in_room.setText("동승신청 취소");
//
//        }
//        else if(status == WAIT_TOGO){
//            btn_in_room.setBackgroundColor(getResources().getColor(R.color.colorPoint));
//            btn_in_room.setTextColor(getResources().getColor(R.color.white));
//            btn_in_room.setText("출발 하기");
//        }
//
//        btn_in_room.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                switch(status){
//                    case NO_MEMBER :
//                        //합승 신청
//                        Set.Save(RoomActivity.this,"status",status+10);
//                        status += 10;
//                        break;
//
//                    case REQUIRING :
//                        //합승신청 취소
//                        Set.Save(RoomActivity.this,"status",status-10);
//                        status -= 10;
//                        break;
//
//                    case WAIT_TOGO :
//                        //출발하기
//                        Set.Save(RoomActivity.this,"status",status+10);
//                        status += 10;
////                        Intent intent = new Intent(RoomActivity.this,);
////            startActivity(intent);
////            finish();
//                        break;
//                }
//            }
//        });
//
//        if(isBangjang){
//            //setOnClickListener ->  showDialog();
//        }
    }

    private void set_TabViews() {
        tab =  (TabLayout) findViewById(R.id.main_tab);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tab.setupWithViewPager(viewPager);
        tab.getTabAt(0).setText("합승정보");
        tab.getTabAt(1).setText("채팅방");
    }
    private void setupViewPager(ViewPager viewPager) {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new TabInfo(), "합승정보");
        adapter.addFragment(new TabChat(), "채팅방");
        viewPager.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void showDialog(){
        final View innerView = getLayoutInflater().inflate(R.layout.dialog, null);
        Dialog mDialog = new Dialog(this);
        mDialog.setContentView(innerView);

        tv_dialog_name = (TextView) findViewById(R.id.dialog_name);
        image_dialog = (ImageView) findViewById(R.id.dialog_image);
        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mDialog.show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}