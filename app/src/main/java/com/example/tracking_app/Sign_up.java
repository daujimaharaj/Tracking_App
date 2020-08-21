package com.example.tracking_app;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;

public class Sign_up extends AppCompatActivity {
    Uri resulturi;
    CircleImageView imageView;
    FirebaseAuth auth;
    EditText firstname,lastname,password,confrmpassword,email;
    String confirmpassword,myemail,mypassword,frstname,lstname,gender;
    RadioButton radioButtonmale,radioButtonfemale;
    ProgressDialog dialog;
    DatabaseReference reference;
    private StorageReference mref;
    private FirebaseUser firebaseUser;
    String code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        radioButtonmale=findViewById(R.id.gendermale);
        radioButtonfemale=findViewById(R.id.genderfemale);
        email=findViewById(R.id.email);
        firstname=findViewById(R.id.firstname);
        lastname=findViewById(R.id.lastname);
        password=findViewById(R.id.password);
        confrmpassword=findViewById(R.id.confrmpassword);
        imageView=findViewById(R.id.cirlceimg);
        mref= FirebaseStorage.getInstance().getReference("docs/");
        dialog=new ProgressDialog(this);
        reference= FirebaseDatabase.getInstance().getReference().child("users");
        auth=FirebaseAuth.getInstance();
    }


    public void selectimage( View v)
    {
        Intent intent1=new Intent();
        intent1.setAction(Intent.ACTION_GET_CONTENT);
        intent1.setType("image/*");
        startActivityForResult(intent1,12);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 12 && resultCode  == RESULT_OK && data != null) {
            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1,1)
                    .start(this);
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                resulturi = result.getUri();
                imageView.setImageURI(resulturi);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void register(View V){

        Random r=new Random();
        int n=100000+r.nextInt(900000);
        code=String.valueOf(n);

        myemail=email.getText().toString();
        mypassword=password.getText().toString();
        frstname=firstname.getText().toString();
        lstname=lastname.getText().toString();
        confirmpassword=confrmpassword.getText().toString();
        gender="";

        if(radioButtonmale.isChecked()){
            gender="Male";}

        if(radioButtonfemale.isChecked()){
            gender="female";}
          if(resulturi==null){
              Toast.makeText(this, "please select image", Toast.LENGTH_SHORT).show();
          }else
        if(firstname.length()==0)
            Toast.makeText(Sign_up.this, "please enter first name", Toast.LENGTH_SHORT).show();
        else if(lstname.length()==0)
            Toast.makeText(Sign_up.this, "please enter last name", Toast.LENGTH_SHORT).show();
        else if(email.length()==0)
            Toast.makeText(Sign_up.this, "please enter email", Toast.LENGTH_SHORT).show();
        else if(password.length()==0)
            Toast.makeText(Sign_up.this, "please enter password", Toast.LENGTH_SHORT).show();
        else if(confirmpassword.length()==0)
            Toast.makeText(Sign_up.this, "please enter confirm password", Toast.LENGTH_SHORT).show();
        else if(gender.length()==0)
            Toast.makeText(Sign_up.this, "please select gender", Toast.LENGTH_SHORT).show();
       else if(password.length()<6)
            Toast.makeText(this, "Enter password greater then 6 character", Toast.LENGTH_SHORT).show();
       else if(password.length()!=confirmpassword.length())
            Toast.makeText(this, "Password and confirm password are not same", Toast.LENGTH_SHORT).show();
       else{
     dialog.setMessage("wait while we are creating an account for you");
     dialog.show();
        auth.createUserWithEmailAndPassword(myemail, mypassword)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser=auth.getCurrentUser();
                            String userid=firebaseUser.getUid();
                            create_user create_user=new create_user(myemail,mypassword,confirmpassword,frstname,lstname,gender,"na",code,userid,"false","na","na");
                            reference.child(userid).setValue(create_user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull final Task<Void> task) {
                                            UploadTask uploadTask=mref.child("images/"+resulturi.getLastPathSegment())
                                                    .putFile(resulturi) ;
                                            uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                                @Override
                                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                               String uri= mref.child("images/"+resulturi.getLastPathSegment()).getDownloadUrl().toString();
                                                    reference.child(firebaseUser.getUid()).child("imageurl").setValue(uri)
                                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                @Override
                                                                public void onComplete(@NonNull Task<Void> task) {
                                                                   dialog.dismiss();
                                                                    sendverificationcode();
                                                                    Intent intent=new Intent(Sign_up.this,Login_Activity.class);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                }
                                            });

                                        }
                                    });

                        } else {
                            // If sign in fails, display a message to the user.
                            dialog.dismiss();
                            Toast.makeText(Sign_up.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }}
    public void sendverificationcode(){
        firebaseUser.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(), "Email sent for verification", Toast.LENGTH_SHORT).show();
                            auth.signOut();
                            finish();

                        }
                        else
                            Toast.makeText(getApplicationContext(), "Could not send email", Toast.LENGTH_SHORT).show();

    }});
    }
}
