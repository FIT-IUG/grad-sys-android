package com.example.fugg.ui.notifications;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fugg.Adapter.notifications_adapter_student;
import com.example.fugg.R;
import com.example.fugg.Adapter.notifications_adapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NotificationsFragment extends Fragment {
    DatabaseReference mDb;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String userKey;
    String key;
    DatabaseReference myref;
    String user_id;
    String massge;
    String key_group;
    String lead;
    String member;
    RecyclerView.LayoutManager layoutManager;
    String pushkey;
    String pushkey_gropus;
    DatabaseReference myrefall;
    DatabaseReference myrefall_gropus;

    private ArrayList<com.example.fugg.classs.notifications> data = new ArrayList<>();
    private RecyclerView recyclerView;
    private com.example.fugg.Adapter.notifications_adapter_student homeAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        myrefall = mDatabase.getReference("membersStd");
        myrefall_gropus = mDatabase.getReference("androidStudentsStdInGroups");

        pushkey = myrefall.push().getKey();
        pushkey_gropus = myrefall_gropus.push().getKey();

        myref = mDatabase.getReference("notifications");
        recyclerView = view.findViewById(R.id.recycler_viewnotifi);
        mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id = String.valueOf(dataSnapshot.child("user_id").getValue());
                mDb.child("notifications").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            member = String.valueOf(ds.child("to").getValue());
                            if (member.equals(user_id)) {
                                Log.e("message", "" + member);
                                Log.e("userid", "" + user_id);
                                key = ds.getKey();
                                mDb.child("notifications").child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        massge = String.valueOf(dataSnapshot.child("message").getValue());
                                        String status = String.valueOf(dataSnapshot.child("status").getValue());
                                        String leader = String.valueOf(dataSnapshot.child("from").getValue());

                                        if(status.equals("wait")) {
                                            data.add(new com.example.fugg.classs.notifications(massge));
                                        }
                                        homeAdapter = new notifications_adapter_student(data,new notifications_adapter_student.OnItemClick() {
                                            @Override
                                            public void accept() {
                                                mDb.child("groups").addValueEventListener(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        for (DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                                            lead =String.valueOf(dataSnapshot1.child("leaderStudentStd").getValue());//Validate necessary
                                                            if(lead.equals(leader)){
                                                                key_group =dataSnapshot1.getKey();
                                                                mDb.child("groups").child(key_group).child("membersStd").child(pushkey).setValue(user_id);
                                                                System.out.println("Sucess");
                                                                mDb.child("notifications").child(key).child("status").setValue("accept");
                                                                mDb.child("androidStudentsStdInGroups").child(pushkey_gropus).setValue(user_id);
                                                            }
                                                        }
                                                    }
                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                    }
                                                });
                                                System.out.println(leader);
                                            }
                                            @Override
                                            public void faild(String mass) {
                                                mDb.child("notifications").child(key).child("status").setValue("refuse");
                                            }
                                        });
                                        layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
                                        recyclerView.setLayoutManager(layoutManager);
                                        recyclerView.setItemAnimator(new DefaultItemAnimator());
                                        recyclerView.setAdapter(homeAdapter);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
//                                Log.e("message", "" + massge)
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