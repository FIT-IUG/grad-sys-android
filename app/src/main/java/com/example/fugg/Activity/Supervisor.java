package com.example.fugg.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.fugg.Adapter.supervisor_adapter;
import com.example.fugg.R;
import com.example.fugg.classs.notifications;
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

public class Supervisor extends AppCompatActivity {
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
    String prj_name;
    String prj_desc;
    ArrayList<String> arrayList;
    ArrayList<String> arrayListlist1;
    ArrayList<String> arrayListlist2;
    String member_std;
    String tags;
    ImageView imgeview;
    String leader_id;
    String name_leader;
    String email_leader;

    private ArrayList<notifications>data;
    private RecyclerView recyclerView;
    private supervisor_adapter homeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_supervisor);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        data = new ArrayList<>();
        arrayListlist1 = new ArrayList<>();
        arrayListlist2 = new ArrayList<>();
        recyclerView = findViewById(R.id.recycler_view_super);
        imgeview = findViewById(R.id.imgeview);


        imgeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Supervisor.this, Request.class);
                startActivity(intent);
            }
        });

        mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id = String.valueOf(dataSnapshot.child("user_id").getValue());
                mDb.child("groups").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String member = String.valueOf(ds.child("teacher").getValue());
                            if (member.equals(user_id)) {

                                Log.e("message", "" + member);
                                Log.e("userid", "" + user_id);
                                key = ds.getKey();

                                mDb.child("groups").child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        prj_name = String.valueOf(ds.child("initialProjectTitle").getValue());
                                        leader_id = String.valueOf(ds.child("leaderStudentStd").getValue());
                                        for (DataSnapshot childSnapshot : ds.child("membersStd").getChildren()) {
                                            member_std = String.valueOf(childSnapshot.getValue());
                                            arrayListlist1.add(member_std);
                                        }
                                        for (DataSnapshot childSnapshot : ds.child("tags").getChildren()) {
                                            tags = String.valueOf(childSnapshot.getValue());
                                            arrayListlist2.add(tags);
                                        }

                                        mDb.child("users").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                                    String leader_search_inuser = String.valueOf(dataSnapshot1.child("user_id").getValue());
                                                    if (leader_id.equals(leader_search_inuser)) {
                                                        String keyee = dataSnapshot1.getKey();

                                                        mDb.child("users").child(keyee).addValueEventListener(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                name_leader = String.valueOf(dataSnapshot.child("name").getValue());
                                                                email_leader = String.valueOf(dataSnapshot.child("email").getValue());

                                                                System.out.println(keyee);
                                                                System.out.println(name_leader);
                                                                System.out.println(email_leader);

                                                                data.add(new com.example.fugg.classs.notifications(prj_name, name_leader, email_leader,arrayListlist1,arrayListlist2));

                                                                Set<notifications> set2 = new HashSet<>(data);
                                                                data.clear();
                                                                data.addAll(set2);

                                                                homeAdapter = new supervisor_adapter(Supervisor.this,data);
                                                                layoutManager = new LinearLayoutManager(Supervisor.this, RecyclerView.VERTICAL, false);
                                                                recyclerView.setLayoutManager(layoutManager);
                                                                recyclerView.setItemAnimator(new DefaultItemAnimator());
                                                                recyclerView.setAdapter(homeAdapter);
                                                            }

                                                            @Override
                                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                                            }
                                                        });
                                                    }
                                                }

                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError){
                                            }
                                        });
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
//                                Log.e("message", "" + massge);
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