package com.example.demoproject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class RegisterationDetails6 extends AppCompatActivity {

    LinearLayout addingphoto;
    private static final int REQUEST_IMAGE_CAPTURE = 2;
    MaterialButton next6;
    ImageView backbutton6;
    EditText height;
    Spinner zodiac,education,drinking,religion,pets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_details6);
        next6=findViewById(R.id.NEXT6);
        backbutton6=findViewById(R.id.backbutton6);
        backbutton6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        height=findViewById(R.id.height);
        pets=findViewById(R.id.pet);
        zodiac=findViewById(R.id.zodiac);
        education=findViewById(R.id.education);
        drinking=findViewById(R.id.drinking);
        religion=findViewById(R.id.religion);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(RegisterationDetails6.this, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.zodiac));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        zodiac.setAdapter(myAdapter);
        ArrayAdapter<String> myAdapter1 = new ArrayAdapter<>(RegisterationDetails6.this, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.education));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        education.setAdapter(myAdapter1);
        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<>(RegisterationDetails6.this, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.drinking));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        drinking.setAdapter(myAdapter2);
        ArrayAdapter<String> myAdapter3 = new ArrayAdapter<>(RegisterationDetails6.this, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.pets));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        pets.setAdapter(myAdapter3);
        ArrayAdapter<String> myAdapter4 = new ArrayAdapter<>(RegisterationDetails6.this, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.religion));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        religion.setAdapter(myAdapter4);
        next6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(RegisterationDetails6.this, RegisterationDetails7.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(this, RegisterationDetails5.class);
        startActivity(intent);
        finish();
    }

}