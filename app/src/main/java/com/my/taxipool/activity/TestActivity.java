package com.my.taxipool.activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.my.taxipool.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Hyeon on 2017-06-16.
 */

public class TestActivity extends AppCompatActivity{
    long time;
    TextView timeanddate;
    View dialogView;
    AlertDialog alertDialog;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
//        timeanddate = (TextView) findViewById(R.id.tv_timeanddate);
    }

    public void onBe(View view){
        View dialogView = View.inflate(TestActivity.this, R.layout.dialog_date_time, null);
        timeanddate = (TextView) dialogView.findViewById(R.id.tv_timeanddate);
        timePicker = (TimePicker) dialogView.findViewById(R.id.time_picker);
        alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setView(dialogView);
        alertDialog.show();
        timePicker.setIs24HourView(true);

        SimpleDateFormat format_date = new SimpleDateFormat("yyyy/MM/dd");
        SimpleDateFormat format_time = new SimpleDateFormat("HHmm");

        final String str_date = format_date.format(new Date());
        Calendar calendar = Calendar.getInstance();
        try{
            calendar.setTime(format_date.parse(str_date));
            calendar.add(Calendar.DATE, 1);
        }catch(ParseException e){
            e.printStackTrace();
        }
        final String str_date_next = format_date.format(calendar.getTime()).toString();
        Log.d("dates",str_date+str_date_next);

        final int str_time = Integer.parseInt(format_time.format(new Date()));

        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                timeanddate.setText(hourOfDay+":"+minute);
                int setted_time = Integer.parseInt( String.valueOf(hourOfDay)+ String.valueOf(minute) );
//                if(str_time < setted_time){
//                    timeanddate.setText(str_date_next);
//                }else{
//                    timeanddate.setText(str_date);
//                }
            }
        });

        dialogView.findViewById(R.id.date_time_set).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                Calendar calendar = new GregorianCalendar(
                        date.getYear(),
                        date.getDay(),
                        date.getDate(),
                        timePicker.getCurrentHour(),
                        timePicker.getCurrentMinute());
                time = calendar.getTimeInMillis();
                alertDialog.dismiss();
            }
        });
    }
}
