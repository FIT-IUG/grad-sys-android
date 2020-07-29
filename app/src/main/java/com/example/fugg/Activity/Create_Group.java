package com.example.fugg.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Create_Group extends AppCompatActivity {
    Spinner spinner1, spinner2, spinner3;
    List<String> group;
    String number1, number2, number3;
    Button button;
    DatabaseReference mDb;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String userKey;

    List<String> avg;
    String user_id;
    String role;
    String std;
    String name;
    DatabaseReference myref;
    DatabaseReference myrefall;
    DatabaseReference myrefagroup;
    ArrayList<String> arraylist;

    String mmrber;
    String key;
    String key1;
    String key2;
    String key3;
    String keygroup;
    String pushkey;
    String memberStd;
    ArrayList<String> checkgroup;
    String leader_id;
    String leader_name;
    String max;
    String min;
    int maxi;
    int mini;
    ProgressDialog progress;
    RadioGroup radioGroup;
    RadioButton radioButton;
    ArrayList<String>arrayList1234;
    ListView listView123;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create__group);

        button = findViewById(R.id.create);
        group = new ArrayList<>();
        checkgroup = new ArrayList<>();
        arrayList1234=new ArrayList<>();
        listView123=findViewById(R.id.list123456);

        progress = new ProgressDialog(this);
        progress.setMessage("Loading.........");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setIndeterminate(true);
        progress.show();

        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        avg = new ArrayList<>();
        myref = mDatabase.getReference("notifications");
        myrefall = mDatabase.getReference("androidStudentsStdInGroups");
        myrefagroup = mDatabase.getReference("groups");
        pushkey = myrefall.push().getKey();
        key1 = myref.push().getKey();
        keygroup = myrefagroup.push().getKey();
        arraylist = new ArrayList<>();
        radioGroup = findViewById(R.id.groub);



        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = (RadioButton) findViewById(checkedId);
                Toast.makeText(getBaseContext(), radioButton.getText(), Toast.LENGTH_SHORT).show();
            }
        });

        mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                std = String.valueOf(dataSnapshot.child("user_id").getValue());
                name = String.valueOf(dataSnapshot.child("name").getValue());

                mDb.child("groups").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            String leader = String.valueOf(ds.child("leaderStudentStd").getValue());
                            if (std.equals(leader)) {
                                Log.e("Leader", "" + leader);
                                key = ds.getKey();
                                Log.e("Keyy", "" + key);
                                mDb.child("groups").child(key).child("membersStd").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        if (dataSnapshot.hasChild("membersStd")) {
                                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                            String membergroup = String.valueOf(ds.getValue());
                                            arraylist.add(membergroup);
                                        }
                                        for (int i = 0; i < arraylist.size(); i++) {
                                            Log.e("membergroupxxx", "" + arraylist.size());
                                        }
                                        mDb.child("settings").addValueEventListener(new ValueEventListener() {

                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                                max = String.valueOf(dataSnapshot.child("max_group_members").getValue());
                                                min = String.valueOf(dataSnapshot.child("min_group_members").getValue());
                                                maxi = Integer.parseInt(max);
                                                mini = Integer.parseInt(min);
                                                Log.e("ssds", "" + arraylist);

                                                if (arraylist.size() <= maxi && arraylist.size() >= mini) {
                                                    Intent intent = new Intent(Create_Group.this, GroupsDetails.class);
                                                    startActivity(intent);
                                                    progress.dismiss();
                                                    finish();

                                                }else{

                                                }
                                            }
                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                        Log.e("membergroupxxx", "" + arraylist);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                Log.e("test", "sucess");
                                break;
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                    }
                });
                progress.dismiss();
                mDb.child("notifications").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            String to=String.valueOf(dataSnapshot1.child("to").getValue());
                            String type=String.valueOf(dataSnapshot1.child("type").getValue());
                            if(std.equals(to)&&type.equals("join_group")){
                                Intent intent=new Intent(Create_Group.this, Home_Fragment.class);
                                startActivity(intent);
                                Log.e("to",""+to);
                                finish();
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
        spinner1 = findViewById(R.id.spinner11);


        spinner1.setAdapter(new ArrayAdapter<>(Create_Group.this, android.R.layout.simple_spinner_dropdown_item, avg));
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                  number1 = parent.getItemAtPosition(position).toString();
                arrayList1234.add(number1);

                AlertDialog.Builder builder1 = new AlertDialog.Builder(Create_Group.this);
                builder1.setMessage(number1+"هل انت متاكد من ارسال طلب ل ");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                mDb.child("settings").addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        max = String.valueOf(dataSnapshot.child("max_group_members").getValue());
                                        min = String.valueOf(dataSnapshot.child("min_group_members").getValue());
                                        maxi = Integer.parseInt(max);
                                        mini = Integer.parseInt(min);
                                        group.add(number1);

                                        Set<String> set = new HashSet<>(group);
                                        group.clear();
                                        group.addAll(set);
                                            if (group.size()<= maxi) {
                                                Log.e("number1out",""+number1);
                                                if (number1 != null) {
                                                    ArrayAdapter<String> adapter1=new ArrayAdapter<String>(Create_Group.this,android.R.layout.simple_expandable_list_item_1,arrayList1234);
                                                    listView123.setAdapter(adapter1);
                                                    Log.e("number1",""+number1);
                                                    key1 = myref.push().getKey();

                                                    Map<String,Object> taskMap = new HashMap<>();
                                                    taskMap.put("from", std);
                                                    taskMap.put("to", number1);
                                                    taskMap.put("from_name", name);
                                                    taskMap.put("message", "بالانضمام الى فريق التخرج الخاص" + name + "لقد طلب منك الطالب");
                                                    taskMap.put("status", "wait");
                                                    taskMap.put("type", "join_group");
                                                    myref.push().setValue(taskMap);

                                                }
                                                myrefagroup.child(keygroup).child("leaderStudentStd").setValue(std);
                                                myrefall.child(pushkey).setValue(std);
                                            }
                                            else {
                                                Toast.makeText(Create_Group.this, "لقد تجاوزت الحد المسموح به أو هناك تكرار في ارقام الفريق", Toast.LENGTH_SHORT).show();
                                            }

                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                                dialog.cancel();
                            }
                        });
                builder1.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        button.setEnabled(true);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String gender = radioButton.getText().toString();
                myrefagroup.child(keygroup).child("graduateInFirstSemester").setValue(gender);
                button.setEnabled(false);
                progress.dismiss();
                AlertDialog.Builder builder = new AlertDialog.Builder(Create_Group.this);
                builder.setCancelable(true);
                builder.setMessage("لا يمكنك الاستفاده من التطبيق الا عند موافقة الحد الادنى من الطلبه");
                builder.setNegativeButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                        System.exit(0);
                    }
                });
                builder.show();
            }
        });
    }
}