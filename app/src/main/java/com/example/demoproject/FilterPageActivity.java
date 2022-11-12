package com.example.demoproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class FilterPageActivity extends AppCompatActivity {

    int flag1=0,flag2=0,flag3=0,flag4=0,flag5=0,flag6=0,flag7=0;
    EditText startAge,endAge;
    static int StartAge,EndAge;
    MaterialButton getProfile;
    static ArrayList<String> genderSelected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter_page);
        Button man = findViewById(R.id.Man);
        Button transman = findViewById(R.id.transman);
        Button cisman = findViewById(R.id.cisman);
        Button ciswoman = findViewById(R.id.Ciswoman);
        Button woman = findViewById(R.id.Woman);
        Button Nonbinary = findViewById(R.id.Nonbinary);
        Button transwoman = findViewById(R.id.Tranwoman);

        startAge=findViewById(R.id.startAge);
        endAge=findViewById(R.id.endage);
        getProfile=findViewById(R.id.fetch);
        getProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genderSelected=new ArrayList<>();
                StartAge=Integer.parseInt(startAge.getText().toString());
                EndAge=Integer.parseInt(endAge.getText().toString());
                if (StartAge>=18 && EndAge<=75 ){
                    if (StartAge <EndAge) {
                        if (flag1==0 && flag2==0  && flag3==0 && flag4==0 && flag5==0 && flag6==0 && flag7==0){
                            //Toast.makeText(FilterPageActivity.this, "select Atleast one of the gender Preferrence", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            if (flag1 == 1) {
                                genderSelected.add(man.getText().toString());
                            }
                            if (flag2 == 1) {
                                genderSelected.add(transman.getText().toString());
                            }
                            if (flag3 == 1) {
                                genderSelected.add(transwoman.getText().toString());
                            }
                            if (flag4 == 1) {
                                genderSelected.add(woman.getText().toString());
                            }
                            if (flag5 == 1) {
                                genderSelected.add(cisman.getText().toString());
                            }
                            if (flag6 == 1) {
                                genderSelected.add(ciswoman.getText().toString());
                            }
                            if (flag7 == 1) {
                                genderSelected.add(Nonbinary.getText().toString());
                            }
                            //Toast.makeText(FilterPageActivity.this, StartAge + "-" + EndAge, Toast.LENGTH_SHORT).show();
                            //Toast.makeText(FilterPageActivity.this, "Gender Selected" + genderSelected.toString(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(FilterPageActivity.this, HomePageActivity.class);
                            startActivity(intent);
                        }
                    }
                    else {
                        //Toast.makeText(FilterPageActivity.this, "Age should be in format- Starting Age - Ending Age" ,Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    //Toast.makeText(FilterPageActivity.this, "Age should be in range of 18 - 75", Toast.LENGTH_SHORT).show();
                }
            }
        });
        StartAge=18;
        EndAge=75;
        man.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag1==0) {
                   //genderSelected.add(0,man.getText().toString());
                    Drawable buttonDrawable = man.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable,getColor(R.color.buttoncolorchanged));
                    man.setBackground(buttonDrawable);
                    flag1=1;
                }
                else if (flag1==1){
                    Drawable buttonDrawable = man.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolor));
                    man.setBackground(buttonDrawable);
                    flag1=0;
                }
            }
        });
        transman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag2==0) {
                   // genderSelected.add(1,transman.getText().toString());
                    Drawable buttonDrawable = transman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolorchanged));
                    transman.setBackground(buttonDrawable);
                    flag2=1;
                }
                else if (flag2==1){
              //      genderSelected.remove(1);
                    Drawable buttonDrawable = transman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolor));
                    transman.setBackground(buttonDrawable);
                    flag2=0;
                }
            }
        });

        transwoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag3==0) {
                    //genderSelected.add(2,transwoman.getText().toString());
                    Drawable buttonDrawable = transwoman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolorchanged));
                    transwoman.setBackground(buttonDrawable);
                    flag3=1;
                }
                else if (flag3==1){
                   // genderSelected.remove(2);
                    Drawable buttonDrawable = transwoman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolor));
                    transwoman.setBackground(buttonDrawable);
                    flag3=0;
                }
            }
        });
        woman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag4==0) {
                    //genderSelected.add(3,woman.getText().toString());
                    Drawable buttonDrawable = woman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolorchanged));
                    woman.setBackground(buttonDrawable);
                    flag4=1;
                }
                else if (flag4==1){
                  // genderSelected.remove(3);
                    Drawable buttonDrawable = woman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolor));
                    woman.setBackground(buttonDrawable);
                    flag4=0;
                }
            }
        });
        cisman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag5==0) {
                    //genderSelected.add(4,cisman.getText().toString());
                    Drawable buttonDrawable = cisman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolorchanged));
                    cisman.setBackground(buttonDrawable);
                    flag5=1;
                }
                else if (flag5==1){
                   // genderSelected.remove(4);
                    Drawable buttonDrawable = cisman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolor));
                    cisman.setBackground(buttonDrawable);
                    flag5=0;
                }
            }
        });
        ciswoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag6==0) {
                    //genderSelected.add(5,ciswoman.getText().toString());
                    Drawable buttonDrawable = ciswoman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolorchanged));
                    ciswoman.setBackground(buttonDrawable);
                    flag6=1;
                }
                else if (flag6==1){
          //          genderSelected.remove(5);
                    Drawable buttonDrawable = ciswoman.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolor));
                    ciswoman.setBackground(buttonDrawable);
                    flag6=0;
                }
            }
        });
        Nonbinary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (flag7==0) {
                    //genderSelected.add(6,Nonbinary.getText().toString());
                    Drawable buttonDrawable = Nonbinary.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolorchanged));
                    Nonbinary.setBackground(buttonDrawable);
                    flag7=1;
                }
                else if (flag7==1){
              //      genderSelected.remove(6);
                    Drawable buttonDrawable = Nonbinary.getBackground();
                    buttonDrawable = DrawableCompat.wrap(buttonDrawable);
                    //the color is a direct color int and not a color resource
                    DrawableCompat.setTint(buttonDrawable, getColor(R.color.buttoncolor));
                    Nonbinary.setBackground(buttonDrawable);
                    flag7=0;
                }
            }
        });

    }
}