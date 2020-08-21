package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.goodiebag.pinview.Pinview;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class JoincircleActivity extends AppCompatActivity {

    Pinview pinview;
    DatabaseReference reference,currentreference;
    FirebaseAuth mauth;
    FirebaseUser muser;
    String current_userid,join_userid;
    DatabaseReference circlereference,joinedrefernce;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joincircle);
    pinview=findViewById(R.id.pinview);
    mauth=FirebaseAuth.getInstance();
    muser=mauth.getCurrentUser();
    reference= FirebaseDatabase.getInstance().getReference().child("users");
    currentreference= FirebaseDatabase.getInstance().getReference().child("users").child(muser.getUid());
    current_userid= muser.getUid();
    }
    public void submit(View v){
        Query query=reference.orderByChild("code").equalTo(pinview.getValue());
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    create_user createuser = null;
                    for (DataSnapshot childss : dataSnapshot.getChildren()) {
                        createuser = childss.getValue(create_user.class);
                    }
                    join_userid = createuser.userid;
                    circlereference = FirebaseDatabase.getInstance().getReference().child("users").child(join_userid).child("circlemember");
                    joinedrefernce = FirebaseDatabase.getInstance().getReference().child("users").child(muser.getUid()).child("joinedCircle");

                    Circlejoin circlejoin = new Circlejoin(current_userid);
                    Circlejoin circlejoin1 = new Circlejoin(join_userid);

                    circlereference.child(muser.getUid()).setValue(circlejoin)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(JoincircleActivity.this, "you joined cicle successfully", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                    joinedrefernce.child(join_userid).setValue(circlejoin1);

                }else
                Toast.makeText(JoincircleActivity.this, "Circle code is invalid", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
