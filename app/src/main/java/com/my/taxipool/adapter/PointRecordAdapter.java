package com.my.taxipool.adapter;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.my.taxipool.R;
import com.my.taxipool.vo.PointRecordInfo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by sungjin on 2017-06-27.
 */

public class PointRecordAdapter extends RecyclerView.Adapter<PointRecordAdapter.ViewHolder> {

    private List<PointRecordInfo> pointRecordList;
    private int itemLayout;

    public PointRecordAdapter(List<PointRecordInfo> items, int itemLayout) {

        this.pointRecordList = items;
        this.itemLayout = itemLayout;
    }

    @Override
    public int getItemCount() {
        return pointRecordList.size();
    }

    //Holder에 저장
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(itemLayout, viewGroup, false);
        return new ViewHolder(view);
    }

    //뿌려주는 작업
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        PointRecordInfo item = pointRecordList.get(position);

        SimpleDateFormat transFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        String date = transFormat.format(item.getDate());

        viewHolder.tv_date.setText(date);
        viewHolder.tv_point.setText(String.valueOf(item.getPoint()));
        //tv_type의 값에 따라 (계좌충전, 계좌출금, 광고충전, 합승 등 변환필요)
        viewHolder.tv_type.setText(String.valueOf(item.getType()));

        if(item.getType()>0) {
            viewHolder.tv_point.setTextColor(Color.BLUE);
        }else{
            viewHolder.tv_point.setTextColor(Color.RED);
        }
    }

    //재활용을 위한 ViewHolder
    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView tv_date;
        public TextView tv_type;
        public TextView tv_point;

        public ViewHolder(View itemView) {
            super(itemView);

            tv_date = (TextView) itemView.findViewById(R.id.tv_date);
            tv_type = (TextView) itemView.findViewById(R.id.tv_type);
            tv_point = (TextView) itemView.findViewById(R.id.tv_point);

        }
    }
}
