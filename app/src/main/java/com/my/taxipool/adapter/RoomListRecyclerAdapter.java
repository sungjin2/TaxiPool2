package com.my.taxipool.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.my.taxipool.R;
import com.my.taxipool.vo.SimpleBoardData;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * Created by Hyeon on 2016-11-02.
 */

public class RoomListRecyclerAdapter extends RecyclerView.Adapter<RoomListRecyclerAdapter.ViewHolder> {

    private ArrayList<SimpleBoardData> list;
    private Context context;
    private RecyclerViewAdapterInterface.OnItemClickListener listener = new RecyclerViewAdapterInterface.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
        }
    };
    public RoomListRecyclerAdapter(ArrayList<SimpleBoardData> list, Context context, RecyclerViewAdapterInterface.OnItemClickListener listener){
        this.list = list;
        this.context = context;
        this.listener=listener;
    }

    @Override
    public int getItemCount() {
        //returns the number of elements the RecyclerView will display
        return list.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Inflate the layout, initialize the View Holder
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.customeview_roomlist, parent, false);
        ViewHolder holder = new ViewHolder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        final SimpleBoardData data = list.get(position);

        holder.root.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(v, position);
                Toast.makeText(context, "Recycle Click" + position, Toast.LENGTH_SHORT).show();
            }
        });

        //Use the provided View Holder on the onCreateViewHolder method to populate the current row on the RecyclerView
        holder.titleView.setText(data.getTitle());
        Log.d("info->", data.getTitle());
        ////////////////holder.levelView.setImageResource(R.drawable.background_tab);
        String info = String.format("%1$s | %2$s | 조회 %3$s",data.getmName(),data.getDateSimple(),String.valueOf(data.getReadCnt()));
        holder.infoView.setText(info);
        Log.d("info->", data.getDate() + "|" + data.getmName());
        holder.replyCntView.setText(String.valueOf(data.getReplyCnt()));
        //animate(holder);
    }



//    @Override
//    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
//        super.onAttachedToRecyclerView(recyclerView);
//    }

    // Insert a new item to the RecyclerView on a predefined position
    public void add(int position, SimpleBoardData data) {
        list.add(position, data);
        notifyItemInserted(position);
    }

    // Remove a RecyclerView item containing a specified Data object
    public void remove(SimpleBoardData data) {
        int position = list.indexOf(data);
        list.remove(position);
        notifyItemRemoved(position);
    }

    public SimpleBoardData getItem(final int position){
        return list.get(position);
    }

    public static final class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout root=null;
        TextView titleView=null;
        TextView infoView=null;
        TextView replyCntView=null;
        ImageView levelView=null;

        public ViewHolder(View itemView) {
            super(itemView);
            root=(LinearLayout)itemView.findViewById(R.id.boardview);
            titleView=(TextView)itemView.findViewById(R.id.title);
            infoView=(TextView)itemView.findViewById(R.id.info);
            replyCntView=(TextView)itemView.findViewById(R.id.repcount);
            levelView=(ImageView)itemView.findViewById(R.id.levelimage);
        }
    }


    public static final Comparator<SimpleBoardData> ALPHA_COMPARATOR = new Comparator<SimpleBoardData>() {
        private final Collator sCollator = Collator.getInstance();
        @Override
        public int compare(SimpleBoardData object1, SimpleBoardData object2) {
            long obj1 = object1.getDateLong();
            long obj2 = object2.getDateLong();
            if (obj1 > obj2) {
                return -1;
            } else if (obj1 < obj2) {
                return 1;
            } else
                return 0;
        }
    };      //얘는 어디다 쓰는거에여? 정렬?

}

class RecyclerViewAdapterInterface {
    public interface OnItemClickListener {
        public void onItemClick(View view, int position);
    }
    public interface OnItemLongClickListener{
        public boolean onItemLongClick(View view, int position);
    }
}
