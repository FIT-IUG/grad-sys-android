package com.example.fugg.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fugg.R;

import java.util.ArrayList;

public class notifications_adapter extends RecyclerView.Adapter<notifications_adapter.myHolder> {
    static ArrayList<com.example.fugg.classs.notifications> card;

    static OnItemClick onItemClick;

   // public void setOnItemClick(OnItemClick onItemClick) {
//        this.onItemClick = onItemClick;
//    }

    public notifications_adapter( ArrayList<com.example.fugg.classs.notifications> card, OnItemClick onItemClick) {
        this.onItemClick=onItemClick;
        this.card = card;
    }
    @NonNull
    @Override
    public myHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.notifi_design, parent, false);
        myHolder h = new myHolder(v);
        return h;
    }
    @Override
    public void onBindViewHolder(@NonNull myHolder holder, int i) {
        final com.example.fugg.classs.notifications item = card.get(i);
        myHolder.textView.setText(item.getMessage());


        holder.bind(onItemClick,i);


//        myHolder.buttonaccept.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//            }
//        });
//        myHolder.buttonrefuse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }
    @Override
    public int getItemCount() {
        return card.size();
    }

    public static class myHolder extends RecyclerView.ViewHolder {

        static TextView textView;
        static Button buttonaccept;
        static Button buttonrefuse;
        public myHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.text1);
            buttonaccept=itemView.findViewById(R.id.buttonaccept);
            buttonrefuse=itemView.findViewById(R.id.buttonrefuse);

        }

     public  void bind(OnItemClick lintsne,int posation){
         buttonaccept.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.e("accept","accept");
                 onItemClick.accept();
                 buttonaccept.setEnabled(false);

             }
         });
         buttonrefuse.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Log.e("refuse","refuse");

                 onItemClick.faild(card.get(posation).getMessage());
             }
         });

     }

    }


    public interface OnItemClick {
      //  void item(com.example.fugg.classs.notifications notiClass, int position);
        void accept();
        void  faild(String mass);
    }


}