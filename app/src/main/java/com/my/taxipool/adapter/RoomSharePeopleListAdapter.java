package com.my.taxipool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.taxipool.R;
import com.my.taxipool.vo.CustomerInfo;

import java.util.ArrayList;

/**
 * Created by Hyeon on 2017-06-21.
 */
public class RoomSharePeopleListAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<CustomerInfo> data;
    private int layout;
    private Context cont;

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
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        final CustomerInfo listviewitem = data.get(position);
//        Log.d("RoomSharePeopleAdapter", listviewitem.getInfo_name());
        //profile set, 지금은 서버에서 프로필주소 안받아오므로 defalutimage로..
        ImageView profile = (ImageView)convertView.findViewById(R.id.img_roomshare_profile);
        //profile.setImageResource(R.drawable.defaultprofile);
        //profile.setImageResource(listviewitem.getProfile());
        //icon.setImageResource(listviewitem.getProfile());

        //nickname set
        TextView tx_block_id = (TextView)convertView.findViewById(R.id.tv_roomshare_id);
        tx_block_id.setText(listviewitem.getNickname());

//        Button bt_cancel = (Button)convertView.findViewById(R.id.block_cancel);
//        bt_cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.d("RoomSharePeopleAdapter",listviewitem.getInfo_name()+"클릭됨");
//                //Toast.makeText(BlockActivity.this, "", Toast.LENGTH_SHORT).show();
//                NetworkBlockCancel nbc = new NetworkBlockCancel();
//                nbc.sendData(listviewitem.getInfo_id(), listviewitem.getInfo_id());
//                ((BlockActivity) cont).refresh();
//                /*((BlockActivity) cont).runOnUiThread(new Runnable() {
//                    public void run() {
//                        notifyDataSetChanged();
//                    }
//                });*/
//
//            }
//        });
        return convertView;
    }

}

