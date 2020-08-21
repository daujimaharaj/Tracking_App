package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.JobIntentService;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class NavigationDrawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, OnMapReadyCallback {

    Switch simpleswitch;
    Marker mCurrLocationMarker;
    LocationRequest locationRequest;
    FirebaseUser muser;
    GoogleMap mgooglemap;
    FirebaseAuth auth;
    private DrawerLayout drawerLayout;
    private LocationCallback mlocationcallback;
    private FusedLocationProviderClient providerClient;
    LatLng latLng;
    String current_userfrstname, current_userlstname, current_useremail, current_imageurl;
    DatabaseReference mreference;
    TextView userfrstname, useremail, userlstname;
    ImageView userimage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        SupportMapFragment supportMapFragment = SupportMapFragment.newInstance();
        getSupportFragmentManager().beginTransaction().add(R.id.map_fragment_container, supportMapFragment).commit();
        supportMapFragment.getMapAsync(this);
        simpleswitch = findViewById(R.id.simpleswitch);
        mreference = FirebaseDatabase.getInstance().getReference().child("users");
        auth = FirebaseAuth.getInstance();
        muser = auth.getCurrentUser();
        NavigationView navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle
                (this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(this);
        providerClient = LocationServices.getFusedLocationProviderClient(this);
        mlocationcallback = new LocationCallback() {

            @Override
            public void onLocationResult(LocationResult locationResult) {

                if (locationResult == null)
                    return;
                if (mCurrLocationMarker != null) {
                    mCurrLocationMarker.remove();
                }
                Location location = locationResult.getLastLocation();
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
                String latitude = String.valueOf(latLng.latitude);
                String longitude = String.valueOf(latLng.longitude);
                mreference.child(muser.getUid()).child("lat").setValue(latitude);
                mreference.child(muser.getUid()).child("lng").setValue(longitude);
                gotolocationplace(latLng);
            }
        };
        View header = navigationView.getHeaderView(0);
        userfrstname = header.findViewById(R.id.user_firstname);
        userlstname = header.findViewById(R.id.user_lstname);
        useremail = header.findViewById(R.id.user_email);
        userimage = header.findViewById(R.id.user_image);

        mreference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                current_userfrstname = dataSnapshot.child(muser.getUid()).child("firstname").getValue(String.class);
                current_userlstname = dataSnapshot.child(muser.getUid()).child("lstname").getValue(String.class);
                current_useremail = dataSnapshot.child(muser.getUid()).child("email").getValue(String.class);
                current_imageurl = dataSnapshot.child(muser.getUid()).child("imageurl").getValue(String.class);

                userfrstname.setText(current_userfrstname);
                userlstname.setText(current_userlstname);
                useremail.setText(current_useremail);
                Picasso.get().load(current_imageurl).into(userimage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void gotolocationplace(LatLng latLng) {
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 16);
        mgooglemap.animateCamera(cameraUpdate, 4000, new GoogleMap.CancelableCallback() {
            @Override
            public void onFinish() {

            }

            @Override
            public void onCancel() {

            }
        });
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.title("current location");
        mCurrLocationMarker = mgooglemap.addMarker(options);
        Toast.makeText(getApplicationContext(), "new location", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else
            super.onBackPressed();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.nav_signOut) {
            auth.signOut();
            Intent intent = new Intent(NavigationDrawer.this, MainActivity.class);
            startActivity(intent);
            finish();
        } else if (id == R.id.nav_inviteMembers) {
            Intent inviteintent = new Intent(NavigationDrawer.this, Invitemember.class);
            startActivity(inviteintent);

        } else if (id == R.id.nav_joinCircle) {
            Intent intentjoin = new Intent(NavigationDrawer.this, JoincircleActivity.class);
            startActivity(intentjoin);

        } else if (id == R.id.nav_joinedCircle) {
            Intent myjoinedintent = new Intent(NavigationDrawer.this, JoinedCircle.class);
            startActivity(myjoinedintent);

        } else if (id == R.id.nav_shareLoc) {
            Intent i1 = new Intent(Intent.ACTION_SEND);
            i1.setType("text/plain");
            i1.putExtra(Intent.EXTRA_TEXT, "My location is :" + "https://wwww.google.com/maps/@" + latLng.latitude + "," + latLng.longitude + ",17z");
            startActivity(i1.createChooser(i1, "share using"));
        } else if (id == R.id.nav_myCircle) {
            Intent mycircleintent = new Intent(NavigationDrawer.this, MyCircle.class);
            startActivity(mycircleintent);
        }
        return true;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mgooglemap = googleMap;
        getLocationUpdates();
        Toast.makeText(this, "map is ready", Toast.LENGTH_SHORT).show();

    }

    private void getLocationUpdates() {

        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);

        providerClient.requestLocationUpdates(locationRequest, mlocationcallback, getMainLooper());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mlocationcallback != null)
            providerClient.removeLocationUpdates(mlocationcallback);

    }


    @Override
    protected void onResume() {
        super.onResume();
        if (mlocationcallback != null)
            providerClient.requestLocationUpdates(locationRequest, mlocationcallback, getMainLooper());
    }

    public void checkedswitch(View view) {
        if (simpleswitch.isChecked())
            mreference.child(muser.getUid()).child("isharing").setValue("true");
        else
            mreference.child(muser.getUid()).child("isharing").setValue("false");

        if (simpleswitch.isChecked()) {
            SharedPreferences.Editor editor = getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
            editor.putBoolean("NameOfThingToSave", true);
            editor.commit();
        } else {
            SharedPreferences.Editor editor = getSharedPreferences("com.example.xyz", MODE_PRIVATE).edit();
            editor.putBoolean("NameOfThingToSave", false);
            editor.commit();
        }

    }
}