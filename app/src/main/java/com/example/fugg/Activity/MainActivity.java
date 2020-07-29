package com.example.fugg.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fugg.R;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawer;
    NavigationView nvigation;
    View hview;
    TextView name;
    TextView textView1, textView2, textView3;
    DatabaseReference mDb;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase mDatabase;
    FirebaseUser user;
    String userKey;
    String user_id;
    String leader_id;
    String teacher_id;
    String  title;
    String  member_std;
    String  tags;
    ArrayList <String>arrayList;
    ArrayList <String>arrayList2;
    ListView list1,list2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        nvigation = findViewById(R.id.nav);
        hview = nvigation.getHeaderView(0);
        name = hview.findViewById(R.id.name);
//        name.setText(shared.getString("nikname",""));
        textView1 = findViewById(R.id.proj_supermain);
        textView2 = findViewById(R.id.proj_name);
        textView3 = findViewById(R.id.proj_leader);
        drawer = findViewById(R.id.drawer);
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        arrayList=new ArrayList<>();
        arrayList2=new ArrayList<>();
        list1=findViewById(R.id.list1);
        list2=findViewById(R.id.list2);
        ImageView imageView4 = drawer.findViewById(R.id.imageView4);

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(nvigation);
            }
        });
        nvigation = findViewById(R.id.nav);
        nvigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint("WrongConstant")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawer.closeDrawer(Gravity.START);
                switch (menuItem.getItemId()) {

                    case R.id.addSupervisor:
                        Intent intent1 = new Intent(MainActivity.this, Add_supervisor.class);
                        startActivity(intent1);
                        break;
                    case R.id.addStudent:
                        Intent intent2 = new Intent(MainActivity.this, Add_student.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });
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
                                title=String.valueOf(dataSnapshot1.child("initialProjectTitle").getValue());
                                teacher_id=String.valueOf(dataSnapshot1.child("teacher").getValue());

                                for (DataSnapshot childSnapshot: dataSnapshot1.child("membersStd").getChildren()) {
                                    member_std=String.valueOf(childSnapshot.getValue());
                                    arrayList.add(member_std);
                                }
                                for (DataSnapshot childSnapshot: dataSnapshot1.child("tags").getChildren()) {
                                    tags=String.valueOf(childSnapshot.getValue());
                                    arrayList2.add(tags);
                                }

                                textView1.setText(teacher_id);
                                String check=textView1.getText().toString();
                                Toast.makeText(MainActivity.this, "check::"+check, Toast.LENGTH_SHORT).show();
                                if(check==null||check.equals("null")||check.equals("")) {
                                    textView1.setText("لا يوجد حتى هذه اللحظه اي مشرف يرجى الانتظار");
                                }

                                textView2.setText(title);
                                textView3.setText(leader_id);

                                ArrayAdapter<String> adapter1=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,arrayList);
                                        list1.setAdapter(adapter1);
                                ArrayAdapter<String>adapter2=new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_expandable_list_item_1,arrayList2);
                                        list2.setAdapter(adapter2);
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
