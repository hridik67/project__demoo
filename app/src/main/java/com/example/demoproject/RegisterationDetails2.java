package com.example.demoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RegisterationDetails2 extends AppCompatActivity {

    EditText username;
    ImageView backbutton;
    MaterialButton next;
    static String usernName;

    static String Name=null,Description=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_details2);
        username=findViewById(R.id.username);
        backbutton=findViewById(R.id.backbutton3);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        next=findViewById(R.id.next3);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(username.getText().length() > 0){
                    usernName=username.getText().toString();
                    Intent intent= new Intent(RegisterationDetails2.this, RegisterationDetails3.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(RegisterationDetails2.this, "Enter your Username", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(this, RegisterationDetails1.class);
        startActivity(intent);
        finish();
    }

}