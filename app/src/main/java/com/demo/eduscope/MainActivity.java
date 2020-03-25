package com.demo.eduscope;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    SharedPrefManager prefManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button create_acc = findViewById(R.id.createAcc_btn);
        Button sign_in = findViewById(R.id.signIn_btn);
        prefManager = new SharedPrefManager(this);

        //if the user is already logged in we will directly start the profile activity
        if (prefManager.isLoggedIn()) {
            finish();
            startActivity(new Intent(this, ProfileActivity.class));
        }

        create_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });

        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //finish();
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }
}
