package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Invitemember extends AppCompatActivity {
    String invitecode;
    FirebaseAuth auth;
    FirebaseUser user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invitemember);
        auth=FirebaseAuth.getInstance();
        final TextView invitemember=findViewById(R.id.textinvite);
        user=auth.getCurrentUser();
        String userid=user.getUid();
        DatabaseReference reference= FirebaseDatabase.getInstance()
                .getReference().child("users").child(userid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                invitecode = dataSnapshot.child("code").getValue(String.class);
                invitemember.setText(invitecode);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void Send(View view) {
        Intent i1=new Intent(Intent.ACTION_SEND);
        i1.setType("text/plain");
        i1.putExtra(Intent.EXTRA_TEXT,"Join my circle with this invitecode"+invitecode);
        startActivity(i1.createChooser(i1,"share using"));

    }
}
