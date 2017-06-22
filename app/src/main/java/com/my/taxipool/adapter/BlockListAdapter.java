package com.my.taxipool.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.my.taxipool.R;
import com.my.taxipool.activity.BlockActivity;
import com.my.taxipool.network.NetworkBlockCancel;
import com.my.taxipool.util.CommuServer;
import com.my.taxipool.vo.BlockCustomerInfo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by sungjin on 2017-06-20.
 */

public class BlockListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<BlockCustomerInfo> data;
    private int layout;
    private Context cont;


    public BlockListAdapter(Context context, int layout, ArrayList<BlockCustomerInfo> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
        this.cont = context;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getBlockNickname();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(final int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        final BlockCustomerInfo listviewitem = data.get(position);
        Log.d("BlockListAdapter", listviewitem.getBlockNickname());


        //profile set, 지금은 서버에서 프로필주소 안받아오므로 defalutimage로..
        ImageView profile = (ImageView)convertView.findViewById(R.id.block_profile);
        //profile.setImageResource(R.drawable.defaultprofile);
        //profile.setImageResource(listviewitem.getProfile());
        //icon.setImageResource(listviewitem.getProfile());

        //nickname set
        TextView tx_block_id = (TextView)convertView.findViewById(R.id.block_id);
        tx_block_id.setText(listviewitem.getBlockNickname());

        Button bt_cancel = (Button)convertView.findViewById(R.id.block_cancel);
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CommuServer("http://192.168.12.30:8888/taxi_db_test2/blocklistcancel.do", new CommuServer.OnCommuListener() {
                    @Override
                    public void onSuccess(JSONObject object, JSONArray arr, String str) {
                        Log.d("ddu result!!",str.toString());
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                    @Override
                    public void onFailed(Error error) {
                    }
                }).addParam("info_id",listviewitem.getInfo_id())
                  .addParam("block_info_id", listviewitem.getBlockInfo_id()).start();
            }
        });
        return convertView;
    }
}

