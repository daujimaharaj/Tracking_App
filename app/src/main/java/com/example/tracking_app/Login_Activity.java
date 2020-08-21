package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login_Activity extends AppCompatActivity {

    EditText edt1, edt2;
    FirebaseAuth auth;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_);
        dialog = new ProgressDialog(this);
        edt1 = findViewById(R.id.edit1);
        edt2 = findViewById(R.id.edit2);
        auth = FirebaseAuth.getInstance();

    }

    public void login(View view) {
        if (edt1.length() < 0) {
            Toast.makeText(this, "Enter Email", Toast.LENGTH_SHORT).show();
        }
        if (edt2.length() < 0) {
            Toast.makeText(this, "Enter Password", Toast.LENGTH_SHORT).show();
        } else {
            dialog.setMessage("please wait");
            dialog.show();
            String email = edt1.getText().toString();
            String password = edt2.getText().toString();
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(Login_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                FirebaseUser user = auth.getCurrentUser();
                                if (user.isEmailVerified()) {
                                    dialog.dismiss();
                                    Toast.makeText(Login_Activity.this, "login sucessfully", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login_Activity.this, NavigationDrawer.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    dialog.dismiss();
                                    Toast.makeText(getApplicationContext(), "Email is not verified", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                dialog.dismiss();
                                Toast.makeText(Login_Activity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();

                            }
                        }
                    });
        }
    }
}