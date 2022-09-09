package com.example.demoproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.drawable.DrawableCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;

public class RegisterationDetails5 extends AppCompatActivity {
    int flag1=0,flag2=0,flag3=0,flag4=0,flag5=0,flag6=0,flag7=0,flag8=0,flag9=0,flag10=0;
    static String Work,study,Description;
    ImageView backbutton5;
    EditText work,studied,description;
    MaterialButton NEXT5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_details5);
        backbutton5=findViewById(R.id.backbutton5);
        backbutton5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        work=findViewById(R.id.work);
        studied=findViewById(R.id.studied);
        description=findViewById(R.id.description);
        NEXT5=findViewById(R.id.NEXT5);
        NEXT5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(work.getText().length()>0){
                    if (studied.getText().length()>0){
                        if (description.getText().length()>20){
                            Work=work.getText().toString();
                            study=studied.getText().toString();
                            Description=description.getText().toString();
                            Intent intent = new Intent(RegisterationDetails5.this, RegisterationDetails6.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RegisterationDetails5.this, "Your Description must be atleast 20 words", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(RegisterationDetails5.this, "Enter where have you Studied at", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterationDetails5.this, "Enter where have you worked at", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(this, RegisterationDetails4.class);
        startActivity(intent);
        finish();
    }
}