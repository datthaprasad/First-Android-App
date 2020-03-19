package com.example.android_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText user;
    private EditText pass;
    private TextView hint,forg;
    private Button but;
    private FirebaseAuth fire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        user=(EditText)findViewById(R.id.DPuser);
        pass=(EditText)findViewById(R.id.DPpw);
        but=(Button)findViewById(R.id.button);
        fire=FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        FirebaseUser userl=fire.getCurrentUser();
       if(userl !=null){
           finish();
           startActivity(new Intent(MainActivity.this,DATTHA_HOME.class));
       }
        forg=(TextView)findViewById(R.id.forgot);
       forg.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,reset_password.class));

           }
       });
        hint=(TextView)findViewById(R.id.DPtv);
        but.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View view){
                validate(user.getText().toString(),pass.getText().toString());
            }
        });
       hint.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               startActivity(new Intent(MainActivity.this,SecondActivity.class));
           }
       });

    }
    private void validate(String user,String pass){
        fire.signInWithEmailAndPassword(user,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                  //  Toast.makeText(MainActivity.this, "Login Successfull", Toast.LENGTH_SHORT).show();
                     checkEmailVerification();
                 }
                else {
                    Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();

                }
            } });
        }


    private void checkEmailVerification(){
        FirebaseUser fireuser=fire.getCurrentUser();
        Boolean emailflag=fireuser.isEmailVerified();
        if(emailflag){
            finish();
            Toast.makeText(MainActivity.this,"Login Succesfull",Toast.LENGTH_LONG).show();
            startActivity(new Intent(MainActivity.this,DATTHA_HOME.class));
        }
        else{
            Toast.makeText(this,"Verify the Email",Toast.LENGTH_LONG).show();
            fire.signOut();
        }
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
