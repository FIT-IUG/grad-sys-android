package com.example.fugg.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fugg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Add_student extends AppCompatActivity {
    DatabaseReference mDb;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String userKey;
    String user_id;
    String leader_id;
    String teacher_id;
    String  member_std;
    ArrayList<String> arrayList;
    Spinner spinner1;
    String number1;
    Button button;
    String key_group;
    String pushkey;
    DatabaseReference myrefall;
    List<String> avg;
    String memberStd;
    ArrayList<String> checkgroup;
    String role;
    DatabaseReference myref;
    String key1;
    String user_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_student);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        arrayList=new ArrayList<>();
        avg=new ArrayList<>();
        spinner1 = findViewById(R.id.spinner1);
        button = findViewById(R.id.create);
        myrefall = mDatabase.getReference("membersStd");
        pushkey = myrefall.push().getKey();
        checkgroup = new ArrayList<>();
        myref = mDatabase.getReference("notifications");
        key1 = myref.push().getKey();

        mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id=String.valueOf(dataSnapshot.child("user_id").getValue());
                mDb.child("groups").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            leader_id= String.valueOf(dataSnapshot1.child("leaderStudentStd").getValue());
                            if (user_id.equals(leader_id)){
                                String key=dataSnapshot1.getKey();
                                for (DataSnapshot childSnapshot: dataSnapshot1.child("membersStd").getChildren()) {
                                    member_std=String.valueOf(childSnapshot.getValue());
                                    arrayList.add(member_std);
                                }
                                if(arrayList.size()<3) {
                                    Log.d("arrrrr", "" + arrayList.size());
                                }
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
        mDb.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user_id = String.valueOf(ds.child("user_id").getValue());
                    role = String.valueOf(ds.child("role").getValue());

                    char g = user_id.charAt(0);
                    System.out.println(g);
                    if (role.equalsIgnoreCase("student") && g == '1') {
                        avg.add(user_id);
                        mDb.child("androidStudentsStdInGroups").addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot)     {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    memberStd = String.valueOf(dataSnapshot1.getValue());
                                    checkgroup.add(memberStd);
                                }
                                avg.removeAll(checkgroup);
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        spinner1.setAdapter(new ArrayAdapter<>(Add_student.this, android.R.layout.simple_spinner_dropdown_item, avg));

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(Add_student.this, "Plese Selesct Number", Toast.LENGTH_SHORT).show();
                } else {
                    number1 = parent.getItemAtPosition(position).toString();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user_id=String.valueOf(dataSnapshot.child("user_id").getValue());
                        user_name=String.valueOf(dataSnapshot.child("name").getValue());
                        mDb.child("groups").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                                    leader_id= String.valueOf(dataSnapshot1.child("leaderStudentStd").getValue());
                                    if (user_id.equals(leader_id)){
                                         key_group=dataSnapshot1.getKey();
                                        for (DataSnapshot childSnapshot: dataSnapshot1.child("membersStd").getChildren()) {
                                            member_std=String.valueOf(childSnapshot.getValue());
                                            arrayList.add(member_std);
                                        }
                                        if(arrayList.size()<3) {
                                            myref.child(key1).child("from").setValue(user_id);
                                            myref.child(key1).child("to").setValue(number1);
                                            myref.child(key1).child("from_name").setValue(user_name);
                                            myref.child(key1).child("message").setValue("بالانضمام الى فريق التخرج الخاص" + user_name + "لقد طلب منك الطالب");
                                            myref.child(key1).child("status").setValue("wait");
                                            myref.child(key1).child("type").setValue("join_group");
                                        }else{
                                            Toast.makeText(Add_student.this, "لا يمكنك اضافه اعضاء جدد الفريق ممتلئ", Toast.LENGTH_SHORT).show();
                                        }
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
        });
    }
}