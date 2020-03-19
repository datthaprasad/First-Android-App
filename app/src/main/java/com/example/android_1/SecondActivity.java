package com.example.android_1;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
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

public class SecondActivity extends AppCompatActivity {
    private EditText name,email,password,age;
    private Button but;
    private ImageView image;
    private FirebaseAuth firebase;
    private FirebaseStorage firebaseStorage;
    private TextView log;
    private static int PICK_IMAGE=123;
    Uri imagepath;
    StorageReference storageReference;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==PICK_IMAGE && resultCode==RESULT_OK && data.getData()!=null){
            imagepath=data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imagepath);
                image.setImageBitmap(bitmap);
            }catch(Exception e){

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        views();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);




        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");//doc->application/pdf or application/*
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"SELECT IAMGE"),PICK_IMAGE); ;
            }
        });


        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SecondActivity.this,MainActivity.class));
            }
        });
        but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //upload data
                    firebase.createUserWithEmailAndPassword(email.getText().toString().trim(),password.getText().toString().trim()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful())
                                sendEmail();

                                else
                                Toast.makeText(SecondActivity.this,"Registration faild",Toast.LENGTH_SHORT).show();


                        }
                    });
                }
            }
        });
    }
    private void views(){
        image=findViewById(R.id.ivprofile);
        name=(EditText)findViewById(R.id.etName);
        age=(EditText)findViewById(R.id.Agetv);
        email=(EditText)findViewById(R.id.etEmail);
        password=(EditText)findViewById(R.id.etPassword);
        but=(Button) findViewById(R.id.register);
        firebase=FirebaseAuth.getInstance();
        firebaseStorage=FirebaseStorage.getInstance();
        storageReference=firebaseStorage.getReference();
        log=(TextView)findViewById(R.id.tvlogin);
    }
    private Boolean validate() {
        if (name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || password.getText().toString().isEmpty()||imagepath==null) {
            Toast.makeText(this,"please enter all field",Toast.LENGTH_SHORT).show();
            return false;
    }else return true;
    }

    private void sendEmail(){
        FirebaseUser fireuser=firebase.getCurrentUser();
        if(fireuser!=null){
            fireuser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(SecondActivity.this,"Successfully registered, chaeck your Email",Toast.LENGTH_SHORT).show();
                        senduserdata();
                        firebase.signOut();
                        finish();
                        startActivity(new Intent(SecondActivity.this,MainActivity.class));
                    }
                    else{
                        Toast.makeText(SecondActivity.this,"Verification Mail not sent",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    //add name age mail t databse using java class
   private void senduserdata(){
       FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
       DatabaseReference myref=firebaseDatabase.getReference(firebase.getUid());
       StorageReference myref1=storageReference.child(firebase.getUid()).child("Images").child("Profile Pic");//images etc
       UploadTask uploadTask=myref1.putFile(imagepath);
       uploadTask.addOnFailureListener(new OnFailureListener() {
           @Override
           public void onFailure(@NonNull Exception e) {
               Toast.makeText(SecondActivity.this,"Failed",Toast.LENGTH_LONG).show();
           }
       });
       uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
           @Override
           public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
               Toast.makeText(SecondActivity.this,"Suceesfull",Toast.LENGTH_LONG).show();
           }
       });

       userprofile up=new userprofile(age.getText().toString().trim(),email.getText().toString().trim(),name.getText().toString().trim());
       myref.setValue(up);
   }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
