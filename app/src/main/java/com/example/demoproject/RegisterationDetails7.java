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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.Spinner;
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
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class RegisterationDetails7 extends AppCompatActivity {

    Spinner filtergender;
    ImageView backbutton7;
    SeekBar agerange,distance;
    TextView agerange_view,distance_view;
    LocationRequest locationRequest;
    static int filterage,filterdistance;
    MaterialButton next7;
    DatabaseReference reference;
    static double Longitude,Lattitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registeration_details7);
        reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        getCurrentLocation();
        agerange=findViewById(R.id.age);
        agerange_view=findViewById(R.id.age_view);
        next7=findViewById(R.id.NEXT7);
        filterage=18;
        filterdistance=25;
        agerange.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                agerange_view.setText("18-"+Integer.toString(i));
                filterage=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        distance=findViewById(R.id.distance);
        distance_view=findViewById(R.id.distance_view);
        distance.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                distance_view.setText(Integer.toString(i)+"KM");
                filterdistance=i;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        backbutton7=findViewById(R.id.backbutton7);
        backbutton7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        filtergender=findViewById(R.id.filter_gender);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(RegisterationDetails7.this, R.layout.gender_dropdown_list,getResources().getStringArray(R.array.filtergender));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        filtergender.setAdapter(myAdapter);
        next7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!(filtergender.getSelectedItem().toString().equals("Select"))){
                    if (filterdistance==25){
                        Toast.makeText(RegisterationDetails7.this, "Select the range above 25km", Toast.LENGTH_SHORT).show();
                    } else {
                        if (filterage==18){
                            Toast.makeText(RegisterationDetails7.this, "Select age range between 18 to 75", Toast.LENGTH_SHORT).show();
                        } else {
                            sendDatatobackend();
                            Intent intent = new Intent(RegisterationDetails7.this, GettingStartedScreen.class);
                            startActivity(intent);
                        }
                    }
                } else {
                    Toast.makeText(RegisterationDetails7.this, "Select a gender preferrence", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(this, RegisterationDetails6.class);
        startActivity(intent);
        finish();
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
                Toast.makeText(RegisterationDetails7.this, "Give permission access the location", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(RegisterationDetails7.this, "gps is on", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            }
            else{
                Toast.makeText(RegisterationDetails7.this, "gps is required to turn on", Toast.LENGTH_SHORT).show();
                turnOnGPS();
            }
        }
    }

    private void getCurrentLocation() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(RegisterationDetails7.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {


                    LocationServices.getFusedLocationProviderClient(RegisterationDetails7.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(RegisterationDetails7.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        Lattitude=latitude;
                                        Longitude=longitude;
                                        Toast.makeText(RegisterationDetails7.this, "Latitude: "+ latitude + "\n" + "Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
                                        //getmatchesFirst();

                                        //AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                    }
                                }
                            }, Looper.getMainLooper())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterationDetails7.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(RegisterationDetails7.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();


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

    private void sendDatatobackend() {
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(new OnCompleteListener<String>() {
            @Override
            public void onComplete(@NonNull Task<String> task) {

                UserDetails userDetails=new UserDetails();
                userDetails.setEmail(RegisterationDetails1.Email);
                userDetails.setUsername(RegisterationDetails2.usernName);
                userDetails.setName(RegisterationDetails3.Name);
                userDetails.setDob(RegisterationDetails3.dob);
                userDetails.setAge(RegisterationDetails3.age);
                userDetails.setGender(RegisterationDetails3.gender);
                userDetails.setNoOfImage(RegisterationDetails4.noofphotos);
                userDetails.setWork(RegisterationDetails5.Work);
                userDetails.setStudy(RegisterationDetails5.study);
                userDetails.setDescription(RegisterationDetails5.Description);
                userDetails.setHeight(RegisterationDetails6.Height);
                userDetails.setZodiac(RegisterationDetails6.Zodiac);
                userDetails.setEducation(RegisterationDetails6.Education);
                userDetails.setDrinking(RegisterationDetails6.Drinking);
                userDetails.setPets(RegisterationDetails6.Pets);
                userDetails.setReligion(RegisterationDetails6.Religion);
                userDetails.setLongitude(Longitude);
                userDetails.setHidelocation("no");
                userDetails.setHideage("no");
                userDetails.setLattitude(Lattitude);
                userDetails.setChattoken(task.getResult());
                userDetails.setFilterGender(filtergender.getSelectedItem().toString());
                userDetails.setAgeRange(filterage);
                userDetails.setDistance(filterdistance);
                userDetails.setHideage("no");
                userDetails.setHidelocation("no");


                String currentUid= FirebaseAuth.getInstance().getCurrentUser().getUid();
                reference.child(currentUid).setValue(userDetails);
                Toast.makeText(RegisterationDetails7.this, "Profile Created", Toast.LENGTH_SHORT).show();
            }
        });
    }


}