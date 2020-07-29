package com.example.fugg.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
    String teacher_name;
    String massge;
    String from;
    RecyclerView.LayoutManager layoutManager;
    String key_group;
    String lead;
    ArrayList<String> arrayList;
    ArrayList<String> arrayList2;
    String member_std;
    String tags;
    String initproj;

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
        arrayList = new ArrayList<>();
        arrayList2 = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_viewrequest);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        recyclerView.setHasFixedSize(true);

        mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id = String.valueOf(dataSnapshot.child("user_id").getValue());
                teacher_name = String.valueOf(dataSnapshot.child("name").getValue());
                mDb.child("notifications").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String member = String.valueOf(ds.child("to").getValue());
                            String leader = String.valueOf(ds.child("from").getValue());
                            String status = String.valueOf(ds.child("status").getValue());
                            if (member.equals(user_id) && status.equals("wait")) {

                                from = String.valueOf(ds.child("from").getValue());
                                massge = String.valueOf(ds.child("message").getValue());
                                key = ds.getKey();


                                mDb.child("groups").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            lead = String.valueOf(ds.child("leaderStudentStd").getValue());
                                            if (lead.equals(from)) {
                                                initproj = String.valueOf(ds.child("initialProjectTitle").getValue());
                                                for (DataSnapshot childSnapshot : ds.child("membersStd").getChildren()) {
                                                    member_std = String.valueOf(childSnapshot.getValue());
                                                    arrayList.add(member_std);
                                                }
                                                for (DataSnapshot childSnapshot : ds.child("tags").getChildren()) {
                                                    tags = String.valueOf(childSnapshot.getValue());
                                                    arrayList2.add(tags);
                                                }

                                                Log.e("massage", "" + massge);
                                                Log.e("member", "" + member);
                                                Log.e("userid", "" + user_id);
                                                Log.e("lead", "" + lead);
                                                Log.e("from", "" + from);
                                                Log.e("initialProjectTitle", "" + initproj);
                                                Log.e("arrayList", "" + arrayList);
                                                Log.e("arrayList2", "" + arrayList2);
                                                data.add(new com.example.fugg.classs.notifications(massge, initproj, arrayList, arrayList2));

                                                homeAdapter = new notifications_adapter(data, Request.this, new notifications_adapter.OnItemClick() {
                                                    @Override
                                                    public void accept() {
                                                        System.out.println(massge + "Done");
                                                        mDb.child("groups").addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                                    String leader_groups = String.valueOf(dataSnapshot1.child("leaderStudentStd").getValue());
                                                                    System.out.println("sucess1");
                                                                    System.out.println(leader_groups);
                                                                    System.out.println(leader);
                                                                    System.out.println(status);

                                                    if (leader_groups.equals(leader)) {
                                                        System.out.println("sucess2");
                                                        String keyes = dataSnapshot1.getKey();
                                                        mDb.child("groups").child(keyes).child("teacher").setValue(user_id);
                                                        mDb.child("notifications").child(key).child("status").setValue("accept");
                                                        Intent intent=new Intent(Request.this,Supervisor.class);
                                                        startActivity(intent);
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
                                                        Intent intent=new Intent(Request.this,Supervisor.class);
                                                        startActivity(intent);
                                                    }
                                                });
                                                recyclerView.setAdapter(homeAdapter);
                                            }


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