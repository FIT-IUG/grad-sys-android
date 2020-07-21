package com.example.fugg.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
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

public class Add_supervisor extends AppCompatActivity {
    DatabaseReference mDb;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String userKey;
    String user_id;
    String user_name;
    String leader_id;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supervisor);

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        arrayList = new ArrayList<>();
        avg = new ArrayList<>();
        spinner1 = findViewById(R.id.spinner1);
        button = findViewById(R.id.create);
        myrefall = mDatabase.getReference("membersStd");
        pushkey = myrefall.push().getKey();
        checkgroup = new ArrayList<>();

        mDb.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user_id = String.valueOf(ds.child("user_id").getValue());
                    role = String.valueOf(ds.child("role").getValue());

                    if (role.equalsIgnoreCase("teacher")) {
                        avg.add(user_id);
                        mDb.child("androidStudentsStdInGroups").addValueEventListener(new ValueEventListener() {
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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
        spinner1.setAdapter(new ArrayAdapter<>(Add_supervisor.this, android.R.layout.simple_spinner_dropdown_item, avg));

        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Toast.makeText(Add_supervisor.this, "Plese Selesct Number", Toast.LENGTH_SHORT).show();
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
                        user_id = String.valueOf(dataSnapshot.child("user_id").getValue());
                        user_name = String.valueOf(dataSnapshot.child("name").getValue());
                        mDb.child("groups").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                                    leader_id = String.valueOf(dataSnapshot1.child("leaderStudentStd").getValue());
                                    if (user_id.equals(leader_id)) {
                                        key_group = dataSnapshot1.getKey();
                                        if (dataSnapshot1.hasChild("teacher")) {
                                            Toast.makeText(Add_supervisor.this, "لا يمكنك اختيار مشرف", Toast.LENGTH_SHORT).show();
                                        } else {
                                            System.out.println("ERREOr");
                                            DatabaseReference myref = mDatabase.getReference("notifications");
                                            String key1 = myref.push().getKey();

//                                            mDb.child("notifications").addValueEventListener(new ValueEventListener() {
//                                                @Override
//                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
//                                                        String leader = String.valueOf(ds.child("from").getValue());
//                                                        if (leader.equals(user_id)) {
//                                                            System.out.println(leader);
//                                                            System.out.println(user_id);
//                                                            String status = String.valueOf(ds.child("status").getValue());
//                                                            String type = String.valueOf(ds.child("type").getValue());
//                                                            if (type.equals("to_be_supervisor")) {
//                                                                System.out.println("status" + status);
//                                                                System.out.println("type" + type);
//                                                                System.out.println(status);
//                                                                System.out.println(type);
//                                                                Toast.makeText(Add_supervisor.this, "لديك طلب سابق لا يمكنك ارسال طلب جديد حاليا", Toast.LENGTH_SHORT).show();
//                                                                Intent intent = new Intent(Add_supervisor.this, MainActivity.class);
//                                                                startActivity(intent);
//                                                            }else{
//                                                                System.out.println("Hi mish baby");
//                                                            }
////                                                            if (status.equals("wait") && type.equals("to_be_supervisor")) {
////
////                                                                break;
////                                                            }
////                                                            else {
////                                                                if (number1 != null) {
////                                                                    mDb.child("notifications").child(key1).child("to").setValue(number1);
////                                                                    mDb.child("notifications").child(key1).child("from_name").setValue(user_name);
////                                                                    mDb.child("notifications").child(key1).child("from").setValue(user_id);
////                                                                    mDb.child("notifications").child(key1).child("status").setValue("wait");
////                                                                    mDb.child("notifications").child(key1).child("message").setValue("ان تكون مشرفا لفريقه" + user_name + "لقد طلب منك الطالب");
////                                                                    mDb.child("notifications").child(key1).child("type").setValue("to_be_supervisor");
////                                                                    break;
////                                                                }
////                                                                else {
////                                                                    Toast.makeText(Add_supervisor.this, "ادخل البيانات", Toast.LENGTH_SHORT).show();
////                                                                }
////                                                            }
//                                                        } else {
//                                                            Toast.makeText(Add_supervisor.this, "HI baby", Toast.LENGTH_SHORT).show();
//                                                        }
//                                                        break;
//                                                    }
//                                                }
//                                                @Override
//                                                public void onCancelled(@NonNull DatabaseError databaseError) {
//                                                }
//                                            });
                                            mDb.child("notifications").addValueEventListener(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                        String member = String.valueOf(ds.child("to").getValue());
                                                        String leader = String.valueOf(ds.child("from").getValue());
                                                        String status = String.valueOf(ds.child("status").getValue());
                                                        String type = String.valueOf(ds.child("to_be_supervisor").getValue());
                                                        if (leader.equals(user_id)&&status.equals("wait")&&type.equals("to_be_subervisor")) {
                                                            Toast.makeText(Add_supervisor.this, " لديك طلب سابق بحاله انتظار لا يمكنك اختيار مشرف جديد", Toast.LENGTH_SHORT).show();
                                                            Intent intent=new Intent(Add_supervisor.this,MainActivity.class);
                                                            startActivity(intent);
                                                        }else{
                                                            if(number1!=null) {
                                                                mDb.child("notifications").child(key1).child("to").setValue(number1);
                                                                mDb.child("notifications").child(key1).child("from_name").setValue(user_name);
                                                                mDb.child("notifications").child(key1).child("from").setValue(user_id);
                                                                mDb.child("notifications").child(key1).child("status").setValue("wait");
                                                                mDb.child("notifications").child(key1).child("message").setValue("ان تكون مشرفا لفريقه" + user_name + "لقد طلب منك الطالب");
                                                                mDb.child("notifications").child(key1).child("type").setValue("to_be_supervisor");
                                                            }else {
                                                                Toast.makeText(Add_supervisor.this, "ادخل البيانات", Toast.LENGTH_SHORT).show();
                                                            }
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