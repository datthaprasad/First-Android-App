package com.example.android_1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.auth.FirebaseAuth;

public class DATTHA_HOME extends AppCompatActivity {
    FirebaseAuth fire;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dattha__home);
        fire=FirebaseAuth.getInstance();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
     private void Logout(){
        fire.signOut();
        finish();
        startActivity(new Intent(DATTHA_HOME.this,MainActivity.class));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.logoutmenu: Logout(); break;
            case R.id.profilemenu: startActivity(new Intent(this,userprofile_display.class)); break;
            case R.id.refresh: startActivity(new Intent(this,DATTHA_HOME.class)); break;
            case R.id.resetpass: startActivity(new Intent(this,reset_password.class)); break;
        }

        return true;
    }

}
