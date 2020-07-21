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
    String str_title;
    String key;
    List<String> supervis;
    String number1;
    Button button;
    String role;
    String user_id;
    ArrayList<String>multispinner;
    String user_name;
    String user_id_leader;
    String leader;
    String init;
    RadioGroup radioGroup;
    ListView listView;

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
        multispinner = new ArrayList<>();
        titleedit = findViewById(R.id.title_group);
        spinner1 = findViewById(R.id.spinner13);
        button=findViewById(R.id.button);
        radioGroup = findViewById(R.id.groub);
        DatabaseReference myref = mDatabase.getReference("notifications");
        String key1 = myref.push().getKey();
        selectedItems=new ArrayList<String>();
        listView=findViewById(R.id.checkable_list);

        mDb.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    user_id = String.valueOf(ds.child("user_id").getValue());
                    role = String.valueOf(ds.child("role").getValue());
                    if (role.equalsIgnoreCase("teacher")) {
                        supervis.add(user_id);
                    }
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
                                Log.e("HII",""+key);
                                mDb.child("groups").child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                                        init=String.valueOf(dataSnapshot.child("tag").getValue());
//                                        selectedItems.add(init);
//                                        ArrayAdapter<String>adapter=new ArrayAdapter<>(GroupsDetails.this,R.layout.checkable_list_layout,selectedItems);
//                                        listView.setAdapter(adapter);
//                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                                            @Override
//                                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                                                String selected=view
//                                            }
//                                        });
                                        Log.e("keyyyyyyyyyyy2",""+init);
                                        if (dataSnapshot.hasChild("initialProjectTitle")){
                                            Intent intent=new Intent(GroupsDetails.this,MainActivity.class);
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
        Log.d("show number1",""+number1);


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
//                                        String keyes = getIntent().getStringExtra("keyee");

                                    }
                                }
                                str_title = titleedit.getText().toString();
                                System.out.println("str_title   "+str_title);;
                                if (str_title != null) {
                                    mDb.child("groups").child(key).child("initialProjectTitle").setValue(str_title);
                                    ArrayList<String> answersStringArray= new ArrayList<String>();

                                    if (radioGroup.getCheckedRadioButtonId() != -1) {
                                        RadioButton uans = (RadioButton) findViewById(radioGroup.getCheckedRadioButtonId());
                                        String ansText = uans.getText().toString();
                                        answersStringArray.add(ansText);
                                        mDb.child("groups").child(key).child("tags").setValue(answersStringArray);
                                    }
                                    Log.d("numberKhar",""+number1);

                                }else{
                                    Toast.makeText(GroupsDetails.this, "خانة العنوان مطلوبه", Toast.LENGTH_SHORT).show();
                                }
                                if(number1!=null) {
                                    mDb.child("notifications").child(key1).child("to").setValue(number1);
                                    mDb.child("notifications").child(key1).child("from_name").setValue(user_name);
                                    mDb.child("notifications").child(key1).child("from").setValue(user_id_leader);
                                    mDb.child("notifications").child(key1).child("status").setValue("wait");
                                    mDb.child("notifications").child(key1).child("message").setValue("ان تكون مشرفا لفريقه" + user_name + "لقد طلب منك الطالب");
                                    mDb.child("notifications").child(key1).child("type").setValue("to_be_supervisor");
                                    Intent intent=new Intent(GroupsDetails.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }else {
                                    Toast.makeText(GroupsDetails.this, "ادخل البيانات", Toast.LENGTH_SHORT).show();
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