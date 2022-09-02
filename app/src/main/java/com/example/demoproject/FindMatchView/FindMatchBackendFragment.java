package com.example.demoproject.FindMatchView;

import static com.facebook.FacebookSdk.getApplicationContext;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproject.HomePageActivity;
import com.example.demoproject.HomeScreens.FindMatchFragment;
import com.example.demoproject.HomeScreens.ProfileFragment;
import com.example.demoproject.R;
import com.example.demoproject.UserDetails;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.ArrayList;
import java.util.Objects;

public class FindMatchBackendFragment extends Fragment {


    final ArrayMap<Float,UserDetails> distance = new ArrayMap<>();
    final ArrayMap<Float,String> UserId = new ArrayMap<>();
    final ArrayList<String> matchlist=new ArrayList<>();
    //final ArrayList<Float,UserDetails> distance = new ArrayList<>();
    //final ArrayList<UserDetails> userDetailslist= new ArrayList<>();
    UserDetails userDetails;
    FusedLocationProviderClient fusedLocationProviderClient;
    Context context;
    TextView text;
    private final static int REQUEST_CODE = 100;
    static Location currentlocation;
    StorageReference storage;
    HomePageActivity getData;
    DatabaseReference matchesList;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    LocationRequest locationRequest;
    private static final int REQUEST_CHECK_SETTINGS = 10001;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_find_match_backend, container, false);
        context=getActivity();
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        matchesList = FirebaseDatabase.getInstance().getReference("Swipes");
        getData =(HomePageActivity) getActivity();
        text=view.findViewById(R.id.textView9);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);

        userDetails = new UserDetails();

        assert getData != null;
        int age=getData.getStartAge();
        //getLastLocation();
        getCurrentLocation();

            return view;
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
                Toast.makeText(context, "Give permission access the location", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(context, "gps is on", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            }
            else{
                Toast.makeText(context, "gps is required to turn on", Toast.LENGTH_SHORT).show();
                turnOnGPS();
            }
        }
    }

    private void getCurrentLocation() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(getData, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {


                    LocationServices.getFusedLocationProviderClient(getData)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(getData)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        currentlocation = locationResult.getLocations().get(index);
                                        Toast.makeText(context, "Latitude: "+ latitude + "\n" + "Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
                                        getmatchesFirst();

                                        //AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                    }
                                }
                            }, Looper.getMainLooper())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(context, "GPS is already tured on", Toast.LENGTH_SHORT).show();


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
            locationManager = (LocationManager) getData.getSystemService(Context.LOCATION_SERVICE);
        }

        isEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        return isEnabled;

    }

    /*private void getLastLocation(){

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        if (ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            if (location != null){
                                currentlocation=location;
                                getmatchesFirst();

                            }
                            else {
                                Toast.makeText(context, "unable to get location please allow permission in setting and switch on your location", Toast.LENGTH_SHORT).show();
                                text.setText("unable to get location please allow permission in setting and switch on your location");
                                GPS();
                                //getLastLocation();
                            }

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(context, e.toString(), Toast.LENGTH_SHORT).show();
                        }
                    });


        }else {

            askPermission();


        }


    }
    */

    private void getmatchesFirst() {
        if (currentUser!=null){
            matchesList.child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DataSnapshot> task) {
                            DataSnapshot snapshot=task.getResult();
                            for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                //Toast.makeText(context, dataSnapshot.getValue().toString()+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                if (dataSnapshot.getValue().toString().equals("Matched")){
                                  //  Toast.makeText(context, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                                    matchlist.add(dataSnapshot.getKey());
                                }
                            }
                            backendData();

                        }
            });
        }

    }
/*
    private void askPermission() {
        requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);
        //ActivityCompat.requestPermissions(requireActivity(),new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {

        if (requestCode == REQUEST_CODE){

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                Toast.makeText(context,"permission granted",Toast.LENGTH_SHORT).show();
                //getLastLocation();
                GPS();

            } else {
                Toast.makeText(context,"Please provide the location access from the settings to move forward",Toast.LENGTH_SHORT).show();
                text.setText("Please provide the location access from the settings to move forward");
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
                    Toast.makeText(context, "GPS is tured on", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getData,HomePageActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                } catch (ApiException e) {
                    switch (e.getStatusCode()) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:

                            try {
                                ResolvableApiException resolvableApiException = (ResolvableApiException)e;
                                startIntentSenderForResult(resolvableApiException.getResolution().getIntentSender(),REQUEST_CHECK_SETTINGS,null,0,0,0,null);
                                //resolvableApiException.startResolutionForResult(getData,REQUEST_CHECK_SETTINGS);
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
                    Toast.makeText(context, "GPS is tured on", Toast.LENGTH_SHORT).show();
                    getLastLocation();
                    break;

                case Activity.RESULT_CANCELED:
                    Toast.makeText(context, "GPS required to be tured on", Toast.LENGTH_SHORT).show();
                    text.setText("GPS required to be tured on");
                    GPS();
                    break;
            }
        }
    }
    */

    private void replaceFragement(Fragment fragment){
        FragmentManager fragmentManager= Objects.requireNonNull(getData).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (ProfileFragment.flag==1){
            fragmentTransaction.replace(R.id.frameLayout,new ProfileFragment());
        } else {
            fragmentTransaction.replace(R.id.frameLayout, fragment);
        }
        fragmentTransaction.commit();
    }

    private void backendData(){
        if (currentUser != null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");

            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                        if (!dataSnapshot.getKey().toString().equals(currentUser.getUid()) && !(matchlist.contains(dataSnapshot.getKey()))) {
                            userDetails = dataSnapshot.getValue(UserDetails.class);
                            assert userDetails != null;
                            if (userDetails.getAge()>=getData.getStartAge() && userDetails.getAge()<=getData.getEndAge() && getData.getGenderSelected().contains(userDetails.getGender()) ) {


                                Location startPoint = new Location("locationA");
                                startPoint.setLatitude(currentlocation.getLatitude());
                                startPoint.setLongitude(currentlocation.getLongitude());

                                Location endPoint = new Location("locationA");
                                endPoint.setLatitude(userDetails.getLattitude());
                                endPoint.setLongitude(userDetails.getLongitude());

                                float dist=startPoint.distanceTo(endPoint)/1000;
                                distance.put(dist,userDetails);
                                UserId.put(dist,dataSnapshot.getKey().toString());
                                // Log.e("info",distance.toString());


                            }


                            else {
                                Toast.makeText(context, "no data was found in according to your filters", Toast.LENGTH_SHORT).show();
                                text.setText("no data was found in according to your filters");
                            }


                        }
                    }
                    if (distance.size()==0 && UserId.size()==0){
                        Toast.makeText(context, "No User was found according to your filters", Toast.LENGTH_SHORT).show();
                        text.setText("No User was found according to your filters");
                    } else {
                        getData.setDistance(distance);
                        getData.setUserId(UserId);
                        replaceFragement(new FindMatchFragment());



                    }


                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {
                    Toast.makeText(context, error.toString(), Toast.LENGTH_SHORT).show();

                }
            });

        } else{
            Toast.makeText(context, "You are unauthorized", Toast.LENGTH_SHORT).show();
        }

    }
}