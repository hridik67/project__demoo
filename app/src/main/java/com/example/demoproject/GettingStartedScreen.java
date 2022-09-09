package com.example.demoproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;

public class GettingStartedScreen extends AppCompatActivity {
    ImageView userphoto;
    TextView username;
    MaterialButton NEXT7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getting_started_screen);
        userphoto=findViewById(R.id.userimage);
        userphoto.setImageURI(RegisterationDetails3.imageUri);
        username=findViewById(R.id.username);
        username.setText(RegisterationDetails3.Name);
        NEXT7=findViewById(R.id.NEXT7);
        NEXT7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(GettingStartedScreen.this, SwipingActivity.class);
                startActivity(intent);
            }
        });
    }
}