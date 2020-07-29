package com.example.fugg.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
import java.util.List;


public class GroupsDetails extends AppCompatActivity {
    DatabaseReference mDb;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    ArrayList<String> selectedItems;


    FirebaseUser user;
    String userKey;
    EditText titleedit;
    Spinner spinner1;
    Spinner spinner255;
    //    String str_title;
    String key;
    List<String> supervis;
    List<String> tags;
    List<String> tags255;
    String number1;
    String number255;
    Button button;
    String role;
    String user_id;
    String user_names;
    ArrayList<String> multispinner;
    String user_name;
    String user_id_leader;
    String leader;
    String init;

    ListView listView255;
    String user_nametest;
    String id_number;
    String tagsstring;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_groups_details);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        supervis = new ArrayList<>();
        tags = new ArrayList<>();
        tags255 = new ArrayList<>();
        multispinner = new ArrayList<>();
        titleedit = findViewById(R.id.title_group);
        spinner1 = findViewById(R.id.spinner13);
        spinner255 = findViewById(R.id.spinner255);
        button = findViewById(R.id.button);
        
        DatabaseReference myref = mDatabase.getReference("notifications");
        String key1 = myref.push().getKey();
        selectedItems = new ArrayList<>();
        listView255=findViewById(R.id.list255);




        mDb.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user_id = String.valueOf(ds.child("user_id").getValue());
                    user_names = String.valueOf(ds.child("name").getValue());
                    role = String.valueOf(ds.child("role").getValue());
                    if (role.equalsIgnoreCase("teacher")) {
                        supervis.add(user_names);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDb.child("tags").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    tagsstring = String.valueOf(ds.getValue());
                        tags.add(tagsstring);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id_leader = dataSnapshot.child("user_id").getValue(String.class);
                user_name = dataSnapshot.child("name").getValue(String.class);
                mDb.child("groups").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot ds : dataSnapshot.getChildren()) {
                            leader = String.valueOf(ds.child("leaderStudentStd").getValue());
                            Log.e("Leader22", "" + user_id_leader);
                            Log.e("Leader33", "" + leader);

                            if (user_id_leader.equals(leader)) {
                                Log.e("Leader55", "" + leader);
                                key = ds.getKey();
                                Log.e("HII", "" + key);
                                mDb.child("groups").child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        Log.e("keyyyyyyyyyyy2", "" + init);
                                        if (dataSnapshot.hasChild("initialProjectTitle")) {
                                            Intent intent = new Intent(GroupsDetails.this, MainActivity.class);
                                            startActivity(intent);
                                            finish();
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
        spinner1.setAdapter(new ArrayAdapter<>(GroupsDetails.this, android.R.layout.simple_spinner_dropdown_item, supervis));
        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                number1 = parent.getItemAtPosition(position).toString();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Log.d("show number1", "" + number1);

        spinner255.setAdapter(new ArrayAdapter<>(GroupsDetails.this, android.R.layout.simple_spinner_dropdown_item, tags));
        spinner255.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                number255 = parent.getItemAtPosition(position).toString();
                tags255.add(number255);
                if (number255 != null) {
                    ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(GroupsDetails.this, android.R.layout.simple_expandable_list_item_1, tags255);
                    listView255.setAdapter(adapter1);
                    Log.e("number1", "" + number1);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        Log.d("show number1", "" + number255);




        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        user_id_leader = dataSnapshot.child("user_id").getValue(String.class);
                        user_name = dataSnapshot.child("name").getValue(String.class);
                        mDb.child("groups").addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String leader = String.valueOf(ds.child("leaderStudentStd").getValue());
                                    if (user_id_leader.equals(leader)) {
                                        key = ds.getKey();
                                    }
                                }
                                String str_title = titleedit.getText().toString();
                                System.out.println("str_title" + str_title);
                                if (str_title == null || str_title.equals("null") || str_title.equals("")) {
                                    Toast.makeText(GroupsDetails.this, "خانة العنوان مطلوبه", Toast.LENGTH_SHORT).show();
                                    Log.d("numberKhar", "" + number1);
                                } else {
                                    mDb.child("groups").child(key).child("initialProjectTitle").setValue(str_title);




                                        mDb.child("groups").child(key).child("tags").setValue(tags255);


                                    if (number1 != null) {
                                        mDb.child("users").addValueEventListener(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                                    user_nametest = String.valueOf(ds.child("name").getValue());
                                                    if (number1.equals(user_nametest)) {
                                                        id_number = String.valueOf(ds.child("user_id").getValue());
                                                        mDb.child("notifications").child(key1).child("to").setValue(id_number);
                                                    }
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                            }
                                        });
                                        mDb.child("notifications").child(key1).child("from_name").setValue(user_name);
                                        mDb.child("notifications").child(key1).child("from").setValue(user_id_leader);
                                        mDb.child("notifications").child(key1).child("status").setValue("wait");
                                        mDb.child("notifications").child(key1).child("message").setValue("ان تكون مشرفا لفريقه" + user_name + "لقد طلب منك الطالب");
                                        mDb.child("notifications").child(key1).child("type").setValue("to_be_supervisor");
                                        Intent intent = new Intent(GroupsDetails.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else {
                                        Toast.makeText(GroupsDetails.this, "ادخل البيانات", Toast.LENGTH_SHORT).show();
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