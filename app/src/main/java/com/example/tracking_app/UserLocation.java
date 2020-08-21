package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class UserLocation extends AppCompatActivity implements OnMapReadyCallback {

    String sharing;
    String uid;
    DatabaseReference mreference;
    Marker mCurrLocationMarker;
    FirebaseAuth auth;
    FirebaseUser muser;
    GoogleMap mgooglemap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_location);
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map_container, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
        mreference = FirebaseDatabase.getInstance().getReference().child("users");

        auth = FirebaseAuth.getInstance();
        muser = auth.getCurrentUser();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgooglemap = googleMap;
        if (mCurrLocationMarker != null) {
            mCurrLocationMarker.remove();
        }

        uid = getIntent().getStringExtra(MembersAdapter.Userid);
        ValueEventListener listener = mreference.child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                create_user createUser = dataSnapshot.getValue(create_user.class);
                Double latitude = Double.valueOf(createUser.lat);
                Double longitude = Double.valueOf(createUser.lng);
                LatLng mlatlng = new LatLng(latitude, longitude);
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(mlatlng, 16);
                mgooglemap.animateCamera(cameraUpdate, 4000, new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {

                    }

                    @Override
                    public void onCancel() {

                    }
                });
                MarkerOptions options = new MarkerOptions();
                options.position(mlatlng);
                options.title(createUser.firstname + " " + createUser.lstname + "  location");
                mCurrLocationMarker = mgooglemap.addMarker(options);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

}