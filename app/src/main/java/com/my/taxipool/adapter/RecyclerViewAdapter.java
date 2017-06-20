package com.my.taxipool.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.my.taxipool.R;
import com.my.taxipool.vo.Room;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by KITRI on 2017-06-07.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>{

    //private List<String> labels;
    private ArrayList<Room> room_labels;
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    //생성자
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView start_spot, end_spot, start_time, cnt, current_cnt;
        public ViewHolder(View itemView) {
            super(itemView);
            start_spot = (TextView) itemView.findViewById(R.id.start_spot);
            end_spot = (TextView) itemView.findViewById(R.id.end_spot);
            start_time = (TextView) itemView.findViewById(R.id.start_time);
            cnt = (TextView) itemView.findViewById(R.id.cnt);
            current_cnt= (TextView) itemView.findViewById(R.id.current_cnt);
        }
    }

    /*public RecyclerViewAdapter(List<String> labels) {
        this.labels = labels; //외부에서 List데이터 만들어 받아서 사용
    }*/
    public RecyclerViewAdapter(ArrayList<Room> labels) {
        this.room_labels = labels; //외부에서 List데이터 만들어 받아서 사용
        Log.d("room_labes",room_labels.toString());
    }
    @Override
    public int getItemCount() {
         return room_labels.size();
    }

    //레이아웃을 만들어 holder에 저장
    //레이아웃 폴더의 a_item.xml 파일을 가져와서 ViewHolder에 담아주는 셈
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.b_item, parent, false);
        return new ViewHolder(view);
    }

    //넘겨받은 데이터를 화면에 출력하는 역할
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        //holder.start_spot.setText(labels.get(position));
        //출발지
        holder.start_spot.setText(room_labels.get(position).getStart_spot());
        holder.start_spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), room_labels.get(position).getStart_spot(), Toast.LENGTH_SHORT).show();
            }
        });
        //도착지
        holder.end_spot.setText(room_labels.get(position).getEnd_spot());
        holder.end_spot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), room_labels.get(position).getEnd_spot(), Toast.LENGTH_SHORT).show();
            }
        });
        //출발시간
        /*holder.start_time.setText(sdf.format(room_labels.get(position).getStart_time()));
        holder.start_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), sdf.format(room_labels.get(position).getStart_time()), Toast.LENGTH_SHORT).show();
            }
        });*/
        //현재인원
        holder.cnt.setText(String.valueOf(room_labels.get(position).getMax_cnt()));
        holder.current_cnt.setText(String.valueOf(room_labels.get(position).getCurrent_cnt()));
        holder.cnt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),String.valueOf(room_labels.get(position).getMax_cnt()), Toast.LENGTH_SHORT).show();
            }
        });
        /*//남녀방 구분
        if(room_labels.get(position).getRoom_gender().equals("f")){

        }else{

        }
        //결제방법
        if(room_labels.get(position).getPayment().equals("p")){

        }else{ //cash인경우

        }
        //음주여부
        if(room_labels.get(position).equals("y")){

        }else{

        }
*/
    }

    /**
     * 뷰 재활용을 위한 viewHolder
     */
/*    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView img;
        public TextView textTitle;

        public ViewHolder(View itemView){
            super(itemView);

            *//*img = (ImageView) itemView.findViewById(R.id.imgProfile);
            textTitle = (TextView) itemView.findViewById(R.id.textTitle);*//*
        }
    }*/

}
