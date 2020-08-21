package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST_CODE=9001;
    FirebaseAuth mauth;
    FirebaseUser muser;
    private boolean prmissionGranted;
    String [] apppermissions={ Manifest.permission.WRITE_EXTERNAL_STORAGE,
                              Manifest.permission.READ_EXTERNAL_STORAGE,
                               Manifest.permission.ACCESS_FINE_LOCATION};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mauth=FirebaseAuth.getInstance();
        muser=mauth.getCurrentUser();
        if(muser==null) {
            setContentView(R.layout.activity_main);
            checkandrequestpermission();
           if(prmissionGranted){
               Intent intentty=new Intent(MainActivity.this,NavigationDrawer.class);
               startActivity(intentty);
               finish();

           }
        }else
        {
            Intent intentty=new Intent(MainActivity.this,NavigationDrawer.class);
            startActivity(intentty);
            finish();
        }
    }

    private boolean checkandrequestpermission() {
        List<String> listpermissionneeded=new ArrayList<>();
        for(String perm:apppermissions){
            if(ContextCompat.checkSelfPermission(this,perm)!=PackageManager.PERMISSION_GRANTED){
                listpermissionneeded.add(perm);
            }
        }

        if(!listpermissionneeded.isEmpty()){
            ActivityCompat.requestPermissions(this,
                    listpermissionneeded.toArray(new String[listpermissionneeded.size()]),PERMISSION_REQUEST_CODE);
       return false;
        }
    return true;
    }

    public void gotosignup(View view) {

        Intent intent=new Intent(MainActivity.this,Sign_up.class);
        startActivity(intent);


    }

    public void gotologin(View view) {

        Intent intent=new Intent(MainActivity.this,Login_Activity.class);
        startActivity(intent);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(requestCode==PERMISSION_REQUEST_CODE&&grantResults[0]==PackageManager.PERMISSION_GRANTED
                && grantResults[1]==PackageManager.PERMISSION_GRANTED&&grantResults[2]==PackageManager.PERMISSION_GRANTED)
        {
            prmissionGranted=true;
            Toast.makeText(this, "permission granted", Toast.LENGTH_SHORT).show();
        }else
        {
            Toast.makeText(this, "Permission not granted ", Toast.LENGTH_SHORT).show();
        }
    }


}
