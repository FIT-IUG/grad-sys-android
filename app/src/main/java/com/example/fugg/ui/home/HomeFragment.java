package com.example.fugg.ui.home;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fugg.Activity.MainActivity;
import com.example.fugg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeFragment extends Fragment {
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
    DatabaseReference myrefall;
    ArrayList<String>arrayList;
    ArrayList<String>arrayList2;
    ArrayList<String>arrayList_tag;
    TextView proj_super,proj_name,proj_leader;
    ListView list1,list2;
    String member_std,tags;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        arrayList=new ArrayList<>();
        arrayList2=new ArrayList<>();
        arrayList_tag=new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDb = mDatabase.getReference();
        user = firebaseAuth.getCurrentUser();
        userKey = user.getUid();
        myrefall = mDatabase.getReference("membersStd");
        pushkey = myrefall.push().getKey();

        proj_super=view.findViewById(R.id.proj_super);
        proj_name=view.findViewById(R.id.proj_name);
        proj_leader=view.findViewById(R.id.proj_leader);
        list1=view.findViewById(R.id.list1);
        list2=view.findViewById(R.id.list2);
        mDb.child("users").child(userKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user_id=String.valueOf(dataSnapshot.child("user_id").getValue());
                mDb.child("groups").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for(DataSnapshot dataSnapshot1:dataSnapshot.getChildren()){
                            for(DataSnapshot dataSnapshot2:dataSnapshot1.child("membersStd").getChildren()){
                            member=String.valueOf(dataSnapshot2.getValue());
                                System.out.println("member"+member);
                            if(member.equals(user_id)){
                                System.out.println("member"+member);
                                System.out.println("user_id"+user_id);

                                key=dataSnapshot1.getKey();

                                System.out.println(key);

                                mDb.child("groups").child(key).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        String proje_name=String.valueOf(dataSnapshot.child("initialProjectTitle").getValue());

                                        proj_name.setText(proje_name);
                                        if(proje_name==null||proje_name.equals("null")||proje_name.equals("")) {
                                            proj_name.setText("لا يوجد حتى هذه اللحظه عنوان يرجى الانتظار");
                                        }
                                        String leaderb=String.valueOf(dataSnapshot.child("leaderStudentStd").getValue());
                                        proj_leader.setText(leaderb);
                                        String teacher=String.valueOf(dataSnapshot.child("teacher").getValue());
                                        proj_super.setText(teacher);
                                        String check=proj_super.getText().toString();
                                        if(check==null||check.equals("null")||check.equals("")) {
                                            proj_super.setText("لا يوجد حتى هذه اللحظه اي مشرف يرجى الانتظار");
                                        }
                                        for (DataSnapshot childSnapshot: dataSnapshot.child("membersStd").getChildren()) {
                                            member_std=String.valueOf(childSnapshot.getValue());
                                            arrayList.add(member_std);
                                        }
                                        for (DataSnapshot childSnapshot: dataSnapshot.child("tags").getChildren()) {
                                            tags=String.valueOf(childSnapshot.getValue());
                                            arrayList2.add(tags);
                                        }
                                        ArrayAdapter<String> adapter1=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,arrayList);
                                        list1.setAdapter(adapter1);
                                        ArrayAdapter<String>adapter2=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_expandable_list_item_1,arrayList2);
                                        list2.setAdapter(adapter2);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                    }
                                });
                            }else{
                                System.out.println("faild ya bahaa");
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
}