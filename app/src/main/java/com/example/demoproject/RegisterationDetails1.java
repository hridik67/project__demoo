package com.example.demoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RegisterationDetails1 extends AppCompatActivity {

    static public String Email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_details1);
        Email=null;
        ImageView Backbutton=findViewById(R.id.backbutton);
        Backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        MaterialButton Next =findViewById(R.id.Next1);
        EditText email= findViewById(R.id.email);
        Next.setOnClickListener(view -> {
            if(email.getText().toString().length()>0) {
                Email=email.getText().toString();
                Intent intent = new Intent(RegisterationDetails1.this, RegisterationDetails2.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "Enter your email first to move further", Toast.LENGTH_SHORT).show();
            }

        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(RegisterationDetails1.this,RegisterationScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}