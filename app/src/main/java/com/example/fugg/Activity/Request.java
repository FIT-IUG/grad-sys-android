package com.example.fugg.Activity;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.fugg.Adapter.notifications_adapter;
import com.example.fugg.Adapter.supervisor_adapter;
import com.example.fugg.R;
import com.example.fugg.classs.notifications;
import com.example.fugg.ui.notifications.NotificationsFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Request extends AppCompatActivity {
    DatabaseReference mDb;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String userKey;
    String key;
    DatabaseReference myref;
    String user_id;
    String massge;
    RecyclerView.LayoutManager layoutManager;
    String key_group;
    String lead;

    private ArrayList<com.example.fugg.classs.notifications> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private notifications_adapter homeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_request);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        myref = mDatabase.getReference("notifications");
        recyclerView = findViewById(R.id.recycler_viewrequest);
        mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id = String.valueOf(dataSnapshot.child("user_id").getValue());
                mDb.child("notifications").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String member = String.valueOf(ds.child("to").getValue());
                            String leader = String.valueOf(ds.child("from").getValue());
                            String status = String.valueOf(ds.child("status").getValue());
                            if (member.equals(user_id)) {
                                Log.e("message", "" + member);
                                Log.e("userid", "" + user_id);
                                key = ds.getKey();
                                mDb.child("notifications").child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        massge = String.valueOf(dataSnapshot.child("message").getValue());
                                        String status = String.valueOf(dataSnapshot.child("status").getValue());
                                        if(status.equals("wait")) {
                                            data.add(new com.example.fugg.classs.notifications(massge));
                                            homeAdapter = new notifications_adapter(data, new notifications_adapter.OnItemClick() {
                                                @Override
                                                public void accept() {
                                                    System.out.println(massge+"Done");
                                                    mDb.child("groups").addValueEventListener(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                            for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                                String leader_groups=String.valueOf(dataSnapshot1.child("leaderStudentStd").getValue());
                                                                System.out.println("sucess1");
                                                                System.out.println(leader_groups);
                                                                System.out.println(leader);
                                                                System.out.println(status);

                                                                if(leader_groups.equals(leader)&&status.equals("wait")){
                                                                    System.out.println("sucess2");
                                                                    String keyes=dataSnapshot1.getKey();
                                                                    mDb.child("groups").child(keyes).child("teacher").setValue(user_id);
                                                                    mDb.child("notifications").child(key).child("status").setValue("accept");
                                                                    System.out.println("sucess2");
                                                                }
                                                            }
                                                        }
                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        }
                                                    });
                                                }
                                                @Override
                                                public void faild(String mass) {
                                                    mDb.child("notifications").child(key).child("status").setValue("refuse");
                                                }
                                            });
                                            layoutManager = new LinearLayoutManager(Request.this, RecyclerView.VERTICAL, false);
                                            recyclerView.setLayoutManager(layoutManager);
                                            recyclerView.setItemAnimator(new DefaultItemAnimator());
                                            recyclerView.setAdapter(homeAdapter);
                                        }else {
                                            System.out.println("maybe error");
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }
}