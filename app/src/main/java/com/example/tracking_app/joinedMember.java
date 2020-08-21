package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class joinedMember extends AppCompatActivity {

    RecyclerView recyclerView;
    DatabaseReference reference ,userreference;
    create_user createUser;
    ArrayList<create_user> nameList;
    String joinedmemberid;
    MembersAdapter madapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joined_member);
        recyclerView=findViewById(R.id.recycler_member);
        nameList=new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        String userid=getIntent().getStringExtra(JoinedAdapter.USER_ID);
        userreference= FirebaseDatabase.getInstance().getReference().child("users");
        reference= FirebaseDatabase.getInstance().getReference().child("users").child(userid).child("circlemember");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                nameList.clear();
                if(dataSnapshot.exists()){
                    for(DataSnapshot dss:dataSnapshot.getChildren()){
                        joinedmemberid=dss.child("circlememberuserid").getValue(String.class);
                        userreference.child(joinedmemberid)
                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        createUser=dataSnapshot.getValue(create_user.class);
                                        nameList.add(createUser);
                                        madapter.notifyDataSetChanged();

                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(joinedMember.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(joinedMember.this, databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        madapter=new MembersAdapter(nameList,this);
        recyclerView.setAdapter(madapter);
        madapter.notifyDataSetChanged();

    }

}
