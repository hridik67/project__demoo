package com.example.demoproject;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.List;
import java.util.Locale;

public class RegisterationDetails8 extends AppCompatActivity {

    DatabaseReference reference;
    StorageReference storage;
    EditText username,password;


    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    static Location currentlocation;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;
    static double Lattitude;
    static double Longitude;
    static Location getCurrentlocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_details8);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        getCurrentLocation();
        username= findViewById(R.id.username);
        MaterialButton createAccount;
        createAccount=findViewById(R.id.create_account);
        getCurrentlocation=null;
        reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
        username.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                username.setBackgroundResource(R.color.buttoncolorchanged);
                return false;
            }
        });

        password= findViewById(R.id.password);
        password.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                password.setBackgroundResource(R.color.buttoncolorchanged);
                return false;
            }
        });

        EditText confirmpassword= findViewById(R.id.confirm_password);
        confirmpassword.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                confirmpassword.setBackgroundResource(R.color.buttoncolorchanged);
                return false;
            }
        });
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (username.getText().toString().length()<=0){
                    Toast.makeText(RegisterationDetails8.this, "Enter your Username", Toast.LENGTH_SHORT).show();
                } else if (!password.getText().toString().equals(confirmpassword.getText().toString())){
                    Toast.makeText(RegisterationDetails8.this, "Your password and confirm password does not match", Toast.LENGTH_SHORT).show();
                } else if (password.getText().toString().length()<=0){
                    Toast.makeText(RegisterationDetails8.this, "Enter your Password", Toast.LENGTH_SHORT).show();
                } else if (getCurrentlocation==null){
                    Toast.makeText(RegisterationDetails8.this, "unable to get location please allow permission in setting and switch on your location", Toast.LENGTH_SHORT).show();
                }
                else {
                    sendDatatobackend();
                    Intent intent= new Intent(RegisterationDetails8.this, FilterPageActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1){
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED){

                if (isGPSEnabled()) {
                    getCurrentLocation();

                }else {

                    turnOnGPS();
                }
            }
            else {
                Toast.makeText(RegisterationDetails8.this, "Give permission access the location", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(RegisterationDetails8.this, "gps is on", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            }
            else{
                Toast.makeText(RegisterationDetails8.this, "gps is required to turn on", Toast.LENGTH_SHORT).show();
                turnOnGPS();
            }
        }
    }

    private void getCurrentLocation() {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(RegisterationDetails8.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {


                    LocationServices.getFusedLocationProviderClient(RegisterationDetails8.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(RegisterationDetails8.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        getCurrentlocation=locationResult.getLocations().get(index);
                                        Lattitude=latitude;
                                        Longitude=longitude;
                                        Toast.makeText(RegisterationDetails8.this, "Latitude: "+ latitude + "\n" + "Longitude: "+ longitude, Toast.LENGTH_SHORT).show();

                                        //AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                    }
                                }
                            }, Looper.getMainLooper())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterationDetails8.this, e.toString(), Toast.LENGTH_SHORT).show();
                                }
                            });

                } else {
                    turnOnGPS();
                }

            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }
    }

    private void turnOnGPS() {



        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(RegisterationDetails8.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();


                } catch (ApiException e) {

                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException) e;
                                startIntentSenderForResult(resolvableApiException.getResolution().getIntentSender(),2,null,0,0,0,null);
                                //resolvableApiException.startResolutionForResult(getData, 2);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });

    }

    private boolean isGPSEnabled() {
        LocationManager locationManager = null;
        boolean isEnabled = false;

        if (locationManager == null) {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    /*private void getLastLocation(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(RegisterationDetails8.this);

        if (ContextCompat.checkSelfPermission(RegisterationDetails8.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                getCurrentlocation=location;
                                Lattitude=location.getLatitude();
                                Longitude=location.getLongitude();

                            }
                            else {
                                Toast.makeText(RegisterationDetails8.this, "unable to get location please allow permission in setting and switch on your location", Toast.LENGTH_SHORT).show();
                                GPS();
                                //getLastLocation();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(RegisterationDetails8.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }else {

            askPermission();


        }


    }

    private void askPermission() {
        //requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(RegisterationDetails8.this,"permission granted",Toast.LENGTH_SHORT).show();
                //getLastLocation();
                GPS();

            } else {
                Toast.makeText(RegisterationDetails8.this,"Please provide the location access from the settings to move forward",Toast.LENGTH_SHORT).show();
                askPermission();

            }



        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void GPS(){

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest);
        builder.setAlwaysShow(true);

        Task<LocationSettingsResponse> result = LocationServices.getSettingsClient(getApplicationContext())
                .checkLocationSettings(builder.build());

        result.addOnCompleteListener(new OnCompleteListener<LocationSettingsResponse>() {
            @Override
            public void onComplete(@NonNull Task<LocationSettingsResponse> task) {

                try {
                    LocationSettingsResponse response = task.getResult(ApiException.class);
                    Toast.makeText(RegisterationDetails8.this, "GPS is tured on", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(RegisterationDetails8.this,RegisterationDetails8.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                //startIntentSenderForResult(resolvableApiException.getResolution().getIntentSender(),REQUEST_CHECK_SETTINGS,null,0,0,0,null);
                                resolvableApiException.startResolutionForResult(RegisterationDetails8.this,REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException ex) {
                                ex.printStackTrace();
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            //Device does not have location
                            break;
                    }
                }
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {

            switch (resultCode) {
                case Activity.RESULT_OK:
                    Toast.makeText(RegisterationDetails8.this, "GPS is tured on", Toast.LENGTH_SHORT).show();
                    getLastLocation();
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(RegisterationDetails8.this, "GPS required to be tured on", Toast.LENGTH_SHORT).show();
                    GPS();
                    break;
            }
        }
    }

     */


    private void sendDatatobackend() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                UserDetails userDetails=new UserDetails();
                userDetails.setEmail(RegisterationDetails1.Email);
                userDetails.setName(RegisterationDetails2.Name);
                userDetails.setDescription(RegisterationDetails2.Description);
                userDetails.setDob(RegisterationDetails3.dob);
                userDetails.setAge(RegisterationDetails3.age);
                userDetails.setGender(RegisterationDetails4.gender);
                userDetails.setOrigin(RegisterationDetails5.origin);
                //userDetails.setNoOfImage(RegisterationDetails6.noOfImage);
                userDetails.setLongitude(Longitude);
                userDetails.setLattitude(Lattitude);
                userDetails.setChattoken(task.getResult());
                userDetails.setUsername(username.getText().toString());
                userDetails.setPassword(password.getText().toString());
                String currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                reference.child(currentUid).setValue(userDetails);
                Toast.makeText(RegisterationDetails8.this, "Profile Created", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(this, RegisterationDetails7.class);
        startActivity(intent);
        finish();
    }
}