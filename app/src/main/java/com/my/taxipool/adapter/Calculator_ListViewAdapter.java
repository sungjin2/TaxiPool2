package com.my.taxipool.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.taxipool.R;
import com.my.taxipool.activity.PeopleListItem;

import java.util.ArrayList;

/**
 * Created by KITRI on 2017-06-21.
 */

public class Calculator_ListViewAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<PeopleListItem> data;
    private int layout;
    public Calculator_ListViewAdapter(Context context, int layout, ArrayList<PeopleListItem> data){
        this.inflater=(LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.data=data;
        this.layout=layout;
    }
    @Override
    public int getCount(){return data.size();}
    @Override
    public String getItem(int position){return data.get(position).getinfo_id();}
    @Override
    public long getItemId(int position){return position;}
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        if(convertView==null){
            convertView=inflater.inflate(layout,parent,false);
        }
        PeopleListItem listviewitem=data.get(position);
        ImageView tv_calculator_profile=(ImageView)convertView.findViewById(R.id.tv_calculator_profile);
        TextView tv_calculator_name=(TextView)convertView.findViewById(R.id.tv_calculator_name);
        tv_calculator_name.setText(listviewitem.getinfo_id());
        return convertView;
    }
}
