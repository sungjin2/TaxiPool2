package com.my.taxipool.adapter;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.my.taxipool.R;
import com.my.taxipool.util.CommuServer;
import com.my.taxipool.util.ImageHelper;
import com.my.taxipool.vo.CustomerInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by Hyeon on 2017-06-21.
 */

public class RoomSharePeopleListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<CustomerInfo> data;
    private int layout;
    private Context cont;

    //view
    TextView tv_roomshare_state;
    Bitmap bitmap;

    //item
    CustomerInfo listviewitem;


    public RoomSharePeopleListAdapter(Context context, int layout, ArrayList<CustomerInfo> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
        this.cont = context;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getInfo_name();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        listviewitem = data.get(position);

        //Image set
        ImageView img_roomshare_profile = (ImageView)convertView.findViewById(R.id.img_roomshare_profile);
        Thread mThread = new Thread(){
            @Override
            public void run(){
                try{
                    URL url = new URL(listviewitem.getProfile_pic());
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
            img_roomshare_profile.setImageBitmap(bitmap);
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        //profile.setImageResource(R.drawable.defaultprofile);
        //profile.setImageResource(listviewitem.getProfile());
        //icon.setImageResource(listviewitem.getProfile());

        //Nickname set
        TextView tv_roomshare_nickname = (TextView)convertView.findViewById(R.id.tv_roomshare_nickname);
        tv_roomshare_nickname.setText(listviewitem.getNickname());

        //Ratingbar set
        RatingBar rating_roomshare = (RatingBar) convertView.findViewById(R.id.rating_roomshare);
        rating_roomshare.setRating((float)listviewitem.getResultscore());

        //State set
        TextView tv_roomshare_state = (TextView)convertView.findViewById(R.id.tv_roomshare_state);
        tv_roomshare_nickname.setText(listviewitem.getNickname());
        if(listviewitem.getState().equals("r")){
            tv_roomshare_state.setText("요청중");
            tv_roomshare_state.setTextColor(cont.getResources().getColor(R.color.colorRed));
        }else if(listviewitem.getState().equals("a")){
            tv_roomshare_state.setTextColor(cont.getResources().getColor(R.color.darkGray));
            tv_roomshare_state.setText("승낙");
        }

        tv_roomshare_state.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(position);
            }
        });
        return convertView;
    }

    public void showDialog(final int position){
        final View innerView = LayoutInflater.from(cont).inflate(R.layout.dialog,null);
        TextView tv_dialog_name = (TextView) innerView.findViewById(R.id.tv_dialog_infoname);
        ImageView image_dialog = (ImageView) innerView.findViewById(R.id.dialog_image);
        TextView tv_dialog_no = (TextView) innerView.findViewById(R.id.tv_dialog_no);
        TextView tv_dialog_yes = (TextView) innerView.findViewById(R.id.tv_dialog_yes);

        tv_dialog_name.setText(listviewitem.getNickname());
        final Dialog mDialog = new Dialog(cont);
        mDialog.setContentView(innerView);

        tv_dialog_yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               new CommuServer(CommuServer.UPDATE_STATE, new CommuServer.OnCommuListener() {
                        @Override
                        public void onSuccess(JSONObject object, JSONArray arr, String str) {
                            listviewitem.setState("a");
                            notifyDataSetChanged();
                            mDialog.dismiss();
                        }
                        @Override
                        public void onFailed(Error error) {
                        }
                    }).addParam("room_no", 2)
                       .addParam("share_info_id",listviewitem.getInfo_id())
                       .addParam("state","a")
                       .start();

            }
        });
        tv_dialog_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                  new CommuServer(CommuServer.UPDATE_STATE, new CommuServer.OnCommuListener() {
                        @Override
                        public void onSuccess(JSONObject object, JSONArray arr, String str) {
                            data.remove(position);
                            notifyDataSetChanged();
                            mDialog.dismiss();
                        }
                        @Override
                        public void onFailed(Error error) {
                        }
                    }).addParam("room_no", 2)
                          .addParam("share_info_id",listviewitem.getInfo_id())
                          .addParam("state","d")
                          .start();
            }
        });

        WindowManager.LayoutParams params = mDialog.getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        mDialog.getWindow().setAttributes(params);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        mDialog.getWindow().setGravity(Gravity.BOTTOM);
        mDialog.show();
    }

}