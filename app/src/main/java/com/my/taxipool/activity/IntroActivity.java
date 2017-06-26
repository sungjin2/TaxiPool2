package com.my.taxipool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.my.taxipool.R;

/**
 * Created by Hyeon on 2016-05-25.
 */

public class IntroActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
               /* Intent intent = null;
                int info_id = Set.Load(IntroActivity.this,"info_id",-1);
                if( -1 != info_id){
                    //id있을 때 - 어플 받고 로그인이력 1번 이상
                    String room_no = Set.Load(IntroActivity.this, "room_no", "NO_ROOM");
                    String state = Set.Load(IntroActivity.this, "state", "NO_STATE");

                    if(room_no.equals("NO_ROOM")){
                        intent = new Intent(IntroActivity.this,HomeActivity.class);
                    }else{
                        intent = new Intent(IntroActivity.this,RoomActivity.class);
                        intent.putExtra("state", state);
                        intent.putExtra("room_no_from_intro", room_no);
                    }
                    intent.putExtra("info_id", info_id);
                }else{
                    intent = new Intent(IntroActivity.this, LoginActivity.class);
                }
                startActivity(intent);
                finish();*/

                int info_id = 135425414;
                startActivity(new Intent(IntroActivity.this,HomeActivity.class)
                        .putExtra("info_id", info_id));
                finish();
            }
        }, 3000);
    }
}
