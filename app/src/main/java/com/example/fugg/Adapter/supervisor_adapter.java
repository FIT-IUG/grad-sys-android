package com.example.fugg.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fugg.R;
import com.example.fugg.classs.notifications;

import java.util.ArrayList;

public class supervisor_adapter extends RecyclerView.Adapter<supervisor_adapter.myHolder> {
    static ArrayList<com.example.fugg.classs.notifications> card;
    Context context;
    static OnItemClick onItemClick;

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public supervisor_adapter(Context context, ArrayList<com.example.fugg.classs.notifications> card) {
        this.card = card;
        this.context = context;
    }

//    public supervisor_adapter( ArrayList<com.example.fugg.classs.notifications> card, OnItemClick onItemClick) {
//     this.onItemClick=onItemClick;
//        this.card = card;
//    }
    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.supervisor_item, parent, false);
        myHolder h = new myHolder(v);
        return h;
    }
    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int i) {
        final com.example.fugg.classs.notifications item = card.get(i);
        myHolder.textView1.setText(item.getProject_name());
        myHolder.textView2.setText(item.getLeader_name());
        myHolder.textView3.setText(item.getLeader_email());

        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,item.getArrayListlist1());
        ArrayAdapter<String> adapter2=new ArrayAdapter<String>(context,android.R.layout.simple_expandable_list_item_1,item.getArrayListlist2());
        supervisor_adapter.myHolder.listView1.setAdapter(adapter1);
        supervisor_adapter.myHolder.listView2.setAdapter(adapter2);

//        holder.bind(onItemClick,card.get(i),i);
    }
    @Override
    public int getItemCount() {
        return card.size();
    }
    public static class myHolder extends RecyclerView.ViewHolder {

        static TextView textView1;
        static TextView textView2;
        static TextView textView3;
        static ListView listView1;
        static ListView listView2;

        public myHolder(@NonNull View itemView) {
            super(itemView);
            textView1 = itemView.findViewById(R.id.text1);
            textView2 = itemView.findViewById(R.id.text2);
            textView3 = itemView.findViewById(R.id.text3);
            listView1=itemView.findViewById(R.id.listsuper1);
            listView2=itemView.findViewById(R.id.listsuper2);
        }
//        public void   bind(OnItemClick lis, notifications data, int position){
//
//
//            itemView.setOnClickListener(v -> lis.item(data,position));
//
//
//        }
    }
    public interface OnItemClick {
        void item(com.example.fugg.classs.notifications notiClass, int position);
    }


}