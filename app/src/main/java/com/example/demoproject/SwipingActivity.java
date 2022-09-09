package com.example.demoproject;

import static com.facebook.FacebookSdk.getApplicationContext;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.util.ArrayMap;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproject.FindMatchView.OnSwipeTouchListener;
import com.example.demoproject.FindMatchView.SliderAdapter;
import com.example.demoproject.HomeScreens.FindMatchFragment;
import com.example.demoproject.Notification.SendNotification;
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
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.smarteist.autoimageslider.IndicatorView.animation.type.IndicatorAnimationType;
import com.smarteist.autoimageslider.SliderAnimations;
import com.smarteist.autoimageslider.SliderView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;

public class SwipingActivity extends AppCompatActivity {

    final ArrayMap<Float,UserDetails> distance = new ArrayMap<>();
    final ArrayMap<Float,String> UserId = new ArrayMap<>();
    final ArrayList<String> matchlist=new ArrayList<>();
    //final ArrayList<Float,UserDetails> distance = new ArrayList<>();
    //final ArrayList<UserDetails> userDetailslist= new ArrayList<>();
    UserDetails userDetails,currentuserDetails;
    FusedLocationProviderClient fusedLocationProviderClient;
    private final static int REQUEST_CODE = 100;
    static Location currentlocation;
    StorageReference storage;
    DatabaseReference matchesList,swipeReference;
    FirebaseUser currentUser;
    LocationRequest locationRequest;
    static int swipe_view_flag=0;
    int i;
    RelativeLayout swipe_view;
    ImageView swipe_profile_photo,swipe_profile_rewind,heart,nope,like,backbuttonswipe2;
    TextView swipe_profile_distance,swipe_profile_name,swipe_profile_age,nousertext,skip;
    TextView swipe_profile_name2,swipe_profile_age2,swipe_profile_study,swipe_profile_work,swipe_profile_description,swipe_profile_zodiac_sign,swipe_profile_favourite_drink,swipe_profile_pets;
    CardView match_swipe;
    String choices;

    ImageView userimage,otheruserimage;

    SliderView sliderView;
    Bitmap[] images;

    ConstraintLayout swipe1,swipe2,swipe3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swiping);
        skip=findViewById(R.id.skipped);
        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        userimage=findViewById(R.id.userimage);
        otheruserimage=findViewById(R.id.other_userimage);
        backbuttonswipe2=findViewById(R.id.backbuttonswipe2);
        backbuttonswipe2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        match_swipe=findViewById(R.id.match_swipe);
        swipe_profile_name2=findViewById(R.id.swipe_profile_name2);
        swipe_profile_age2=findViewById(R.id.swipe_profile_age2);
        swipe_profile_study=findViewById(R.id.swipe_profile_study);
        swipe_profile_work=findViewById(R.id.swipe_profile_work);
        swipe_profile_description=findViewById(R.id.swipe_profile_description);
        swipe_profile_zodiac_sign=findViewById(R.id.swipe_profile_zodiac_sign);
        swipe_profile_favourite_drink=findViewById(R.id.swipe_profile_favourite_drink);
        swipe_profile_pets=findViewById(R.id.swipe_profile_pets);
        swipe1=findViewById(R.id.swipe1);
        swipe2=findViewById(R.id.swipe2);
        swipe3=findViewById(R.id.swipe3);
        sliderView=findViewById(R.id.image_slider);
        swipeReference = FirebaseDatabase.getInstance().getReference("Swipes");
        swipe_profile_photo=findViewById(R.id.swipe_profile_image);
        swipe_profile_rewind=findViewById(R.id.swipe_profile_rewind);
        swipe_profile_rewind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                i=0;
                update(i);
            }
        });
        nousertext=findViewById(R.id.nousertext);
        heart=findViewById(R.id.heart);
        like=findViewById(R.id.like);
        nope=findViewById(R.id.nope);
        swipe_profile_distance=findViewById(R.id.swipe_profile_distance);
        swipe_profile_name=findViewById(R.id.swipe_profile_name);
        swipe_profile_age=findViewById(R.id.swipe_profile_age);
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        swipe_view=findViewById(R.id.swipe_profile_view);
        swipe_view.setOnTouchListener(new OnSwipeTouchListener(){
            @Override
            public boolean onSwipeRight() {
                Toast.makeText(SwipingActivity.this, "swipe right", Toast.LENGTH_SHORT).show();
                choices="heart";
                swipeUpdate(choices);
                //linearLayout.removeAllViews();
                //linearLayout.invalidate();
                Toast.makeText(SwipingActivity.this, choices, Toast.LENGTH_SHORT).show();
                return super.onSwipeRight();
            }

            @Override
            public boolean onSwipeLeft() {

                Toast.makeText(SwipingActivity.this, "swipe left", Toast.LENGTH_SHORT).show();
                choices = "nope";
                swipeUpdate(choices);
                //linearLayout.removeAllViews();
                //linearLayout.invalidate();
                Toast.makeText(SwipingActivity.this, choices, Toast.LENGTH_SHORT).show();
                return super.onSwipeLeft();

            }

            @Override
            public boolean onSwipeTop() {
                Toast.makeText(SwipingActivity.this, "swipe left", Toast.LENGTH_SHORT).show();
                choices = "like";
                swipeUpdate(choices);
                //linearLayout.removeAllViews();
                //linearLayout.invalidate();
                Toast.makeText(SwipingActivity.this, choices, Toast.LENGTH_SHORT).show();
                return super.onSwipeTop();
            }
        });
        swipe_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipe_view_flag=1;
                getPhots(UserId.valueAt(i).toString(),distance.valueAt(i).getNoOfImage(),"profile");
                swipe_profile_name2.setText(distance.valueAt(i).getName()+",");
                swipe_profile_age2.setText(String.valueOf(distance.valueAt(i).getAge()));
                swipe_profile_study.setText("Study at "+distance.valueAt(i).getStudy());
                swipe_profile_work.setText("Works at "+distance.valueAt(i).getWork());
                swipe_profile_description.setText(distance.valueAt(i).getDescription());
                swipe_profile_zodiac_sign.setText(distance.valueAt(i).getZodiac());
                swipe_profile_favourite_drink.setText(distance.valueAt(i).getDrinking());
                swipe_profile_pets.setText(distance.valueAt(i).getPets());
                swipe1.setVisibility(View.INVISIBLE);
                swipe2.setVisibility(View.VISIBLE);
            }
        });
        match_swipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                swipe1.setVisibility(View.VISIBLE);
                swipe2.setVisibility(View.INVISIBLE);
                choices="heart";
                swipeUpdate(choices);
                //linearLayout.removeAllViews();
                //linearLayout.invalidate();
                Toast.makeText(SwipingActivity.this, choices, Toast.LENGTH_SHORT).show();
            }
        });
        nope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choices = "nope";
                swipeUpdate(choices);
                //linearLayout.removeAllViews();
                //linearLayout.invalidate();
                Toast.makeText(SwipingActivity.this, choices, Toast.LENGTH_SHORT).show();
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choices="like";
                swipeUpdate(choices);
                //linearLayout.removeAllViews();
                //linearLayout.invalidate();
                Toast.makeText(SwipingActivity.this, choices, Toast.LENGTH_SHORT).show();
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choices="heart";
                swipeUpdate(choices);
                //linearLayout.removeAllViews();
                //linearLayout.invalidate();
                Toast.makeText(SwipingActivity.this, choices, Toast.LENGTH_SHORT).show();
            }
        });

        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        matchesList = FirebaseDatabase.getInstance().getReference("Swipes");
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(2000);
        getCurrentLocation();
        i=0;
        //update(i);
    }


    public void swipeUpdate(String choices){
        FirebaseUser currentUser1 = FirebaseAuth.getInstance().getCurrentUser();
        String currentUser=currentUser1.getUid();
        //check if this user has also liked you or not
        if (i<distance.size()) {
            assert currentUser != null;
            swipeReference.child(currentUser).child(UserId.valueAt(i)).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    DataSnapshot snapshot= task.getResult();
                    if (snapshot.exists()) {
                        if ((Objects.requireNonNull(snapshot.getValue()).toString().equals("like") || snapshot.getValue().toString().equals("heart")) && !(choices.equals("nope"))) {
                            Toast.makeText(SwipingActivity.this, "its a match", Toast.LENGTH_SHORT).show();
                            matchesList.child(currentUser).child(UserId.valueAt(i)).setValue("Matched");
                            matchesList.child(UserId.valueAt(i)).child(currentUser).setValue("Matched");
                            swipe_view_flag=1;
                            swipe3.setVisibility(View.VISIBLE);
                            swipe2.setVisibility(View.INVISIBLE);
                            swipe1.setVisibility(View.INVISIBLE);
                            showmatcheduserPhoto(currentUser,UserId.valueAt(i));
                            SendNotification.Companion.send("Matched",distance.valueAt(i).getChattoken(),"You have matched check who it is",currentUser);

                        } else {
                            //updating user liked swipes om database
                            swipeReference.child(UserId.valueAt(i)).child(currentUser).setValue(choices);
                        }
                    } else {
                        //updating user liked swipes om database
                        swipeReference.child(UserId.valueAt(i)).child(currentUser).setValue(choices);
                    }
                    i++;
                    update(i);

                }
            });

        }
        else {
            //nousertext.setVisibility(View.VISIBLE);
            //scrollView.setVisibility(View.INVISIBLE);
            //relativeLayout.setVisibility(View.INVISIBLE);
            //bottomlinearLayout.setVisibility(View.INVISIBLE);

            Toast.makeText(SwipingActivity.this, "No further users to show according to filters", Toast.LENGTH_SHORT).show();
        }
    }

    private void showmatcheduserPhoto(String currentUser3, String otheruser3) {
        StorageReference reference=storage.child(currentUser3+"Profile Picture1");
        try {
            File localfile = File.createTempFile(currentUser3 + "Profile Picture1", ".jpg");
            int finalI = i;
            reference.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            userimage.setImageBitmap(bitmap);
                            //ImageView imageView = new ImageView(context);
                            //imageView.setImageResource(R.mipmap.ic_launcher);
                            //addvieW1(imageView,bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
        StorageReference reference1=storage.child(otheruser3+"Profile Picture1");
        try {
            File localfile = File.createTempFile(otheruser3 + "Profile Picture1", ".jpg");
            int finalI = i;
            reference1.getFile(localfile)
                    .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                            Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                            otheruserimage.setImageBitmap(bitmap);
                            //ImageView imageView = new ImageView(context);
                            //imageView.setImageResource(R.mipmap.ic_launcher);
                            //addvieW1(imageView,bitmap);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void update(int j){

        //Toast.makeText(SwipingActivity.this, String.valueOf(distance.size()), Toast.LENGTH_SHORT).show();
        if (j<distance.size()) {
            getPhots(UserId.valueAt(j).toString(),1,"swipe");
            swipe_profile_distance.setText(distance.keyAt(j).toString() + " KM away");
            swipe_profile_name.setText(distance.valueAt(j).getName()+",");
            swipe_profile_age.setText(String.valueOf(distance.valueAt(j).getAge()));
            //gender.setText(distance.valueAt(j).getGender());
            //description.setText("Bio-\n"+distance.valueAt(j).getDescription());
        } else{

            nousertext.setVisibility(View.VISIBLE);
            //scrollView.setVisibility(View.INVISIBLE);
            swipe_view.setVisibility(View.INVISIBLE);
            nope.setVisibility(View.INVISIBLE);
            heart.setVisibility(View.INVISIBLE);
            like.setVisibility(View.INVISIBLE);
            //relativeLayout.setVisibility(View.INVISIBLE);
            //bottomlinearLayout.setVisibility(View.INVISIBLE);
            Toast.makeText(SwipingActivity.this, "No further users to show according to filters", Toast.LENGTH_SHORT).show();
        }

    }

    private void getPhots(String toString, int noOfImage,String s) {
            for (int i=0;i<noOfImage;i++) {
                if (s.equals("profile")){
                    images=new Bitmap[noOfImage];
                }
                StorageReference reference=storage.child(toString+"Profile Picture"+(i+1));
                try {
                    File localfile = File.createTempFile(toString + "Profile Picture" + (i+1), ".jpg");
                    int finalI = i;
                    reference.getFile(localfile)
                            .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                    Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                    if (s.equals("swipe")) {
                                        swipe_profile_photo.setImageBitmap(bitmap);
                                    } else if (s.equals("profile")) {
                                        images[finalI]=bitmap;

                                    }
                                    //ImageView imageView = new ImageView(context);
                                    //imageView.setImageResource(R.mipmap.ic_launcher);
                                    //addvieW1(imageView,bitmap);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {

                                }
                            });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (s.equals("profile")){

                SliderAdapter sliderAdapter = new SliderAdapter(images);
                sliderView.setSliderAdapter(sliderAdapter);
                sliderView.setIndicatorAnimation(IndicatorAnimationType.SLIDE);
                sliderView.setSliderTransformAnimation(SliderAnimations.DEPTHTRANSFORMATION);
                sliderView.startAutoCycle();
            }
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
                Toast.makeText(SwipingActivity.this, "Give permission access the location", Toast.LENGTH_SHORT).show();
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2) {
            if (resultCode == Activity.RESULT_OK) {

                Toast.makeText(SwipingActivity.this, "gps is on", Toast.LENGTH_SHORT).show();
                getCurrentLocation();
            }
            else{
                Toast.makeText(SwipingActivity.this, "gps is required to turn on", Toast.LENGTH_SHORT).show();
                turnOnGPS();
            }
        }
    }

    private void getCurrentLocation() {



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ActivityCompat.checkSelfPermission(SwipingActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (isGPSEnabled()) {


                    LocationServices.getFusedLocationProviderClient(SwipingActivity.this)
                            .requestLocationUpdates(locationRequest, new LocationCallback() {
                                @Override
                                public void onLocationResult(@NonNull LocationResult locationResult) {
                                    super.onLocationResult(locationResult);

                                    LocationServices.getFusedLocationProviderClient(SwipingActivity.this)
                                            .removeLocationUpdates(this);

                                    if (locationResult != null && locationResult.getLocations().size() >0){

                                        int index = locationResult.getLocations().size() - 1;
                                        double latitude = locationResult.getLocations().get(index).getLatitude();
                                        double longitude = locationResult.getLocations().get(index).getLongitude();
                                        currentlocation=locationResult.getLocations().get(index);
                                        Toast.makeText(SwipingActivity.this, "Latitude: "+ latitude + "\n" + "Longitude: "+ longitude, Toast.LENGTH_SHORT).show();
                                        getmatchesFirst();


                                        //getmatchesFirst();

                                        //AddressText.setText("Latitude: "+ latitude + "\n" + "Longitude: "+ longitude);
                                    }


                                }
                            }, Looper.getMainLooper())
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(SwipingActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
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
                    Toast.makeText(SwipingActivity.this, "GPS is already tured on", Toast.LENGTH_SHORT).show();


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




    private void getmatchesFirst() {
        if (currentUser.getUid()!=null){
            DatabaseReference match = FirebaseDatabase.getInstance().getReference("Swipes");
            match.child(currentUser.getUid()).get().addOnCompleteListener(task -> {
                DataSnapshot snapshot=task.getResult();
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        //Toast.makeText(context, dataSnapshot.getValue().toString()+dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                        if (Objects.requireNonNull(dataSnapshot.getValue()).toString().equals("Matched")) {
                            Toast.makeText(SwipingActivity.this, dataSnapshot.getKey(), Toast.LENGTH_SHORT).show();
                            matchlist.add(dataSnapshot.getKey());
                        }
                    }
                }
                Toast.makeText(SwipingActivity.this, String.valueOf(matchlist.size()), Toast.LENGTH_SHORT).show();
                Toast.makeText(SwipingActivity.this, "Please wait for a second or two loading the profiles as per your details", Toast.LENGTH_SHORT).show();
                backendData();

            });
        }


    }

    private void backendData(){
        if (currentUser.getUid()!=null) {
            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("UserProfileDetails");
            reference.child(currentUser.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DataSnapshot> task) {
                    if (task.getResult().exists()) {
                        currentuserDetails = task.getResult().getValue(UserDetails.class);
                        reference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot dataSnapshot:snapshot.getChildren()){
                                    if (!(dataSnapshot.getKey().toString().equals(currentUser.getUid()))) {
                                        userDetails = dataSnapshot.getValue(UserDetails.class);
                                        Toast.makeText(SwipingActivity.this, userDetails.getName(), Toast.LENGTH_SHORT).show();
                                        if (matchlist.size()==0 || (!(matchlist.contains(dataSnapshot.getKey()))) ) {
                                            userDetails = dataSnapshot.getValue(UserDetails.class);
                                            assert userDetails != null;
                                            if (userDetails.getAge() >= 18 && userDetails.getAge() <= currentuserDetails.getAgeRange() && userDetails.getGender().equals(currentuserDetails.getFilterGender())) {
                                                Location startPoint = new Location("locationA");
                                                startPoint.setLatitude(currentlocation.getLatitude());
                                                startPoint.setLongitude(currentlocation.getLongitude());

                                                Location endPoint = new Location("locationA");
                                                endPoint.setLatitude(userDetails.getLattitude());
                                                endPoint.setLongitude(userDetails.getLongitude());

                                                float dist = startPoint.distanceTo(endPoint) / 1000;
                                                distance.put(dist, userDetails);
                                                UserId.put(dist, dataSnapshot.getKey().toString());
                                                if (i == 0) {
                                                    update(i);
                                                    swipe_view.setVisibility(View.VISIBLE);
                                                    nope.setVisibility(View.VISIBLE);
                                                    heart.setVisibility(View.VISIBLE);
                                                    like.setVisibility(View.VISIBLE);
                                                }
                                                //Log.e("infofuck",distance.toString());
                                                Toast.makeText(SwipingActivity.this, "hello -" + distance.size(), Toast.LENGTH_SHORT).show();


                                            } else {
                                                Toast.makeText(SwipingActivity.this, "no data was found in according to your filters", Toast.LENGTH_SHORT).show();
                                                //text.setText("no data was found in according to your filters");
                                            }
                                        }


                                    }

                                }
                                if (distance.size()==0 && UserId.size()==0){
                                    nousertext.setVisibility(View.VISIBLE);
                                    //scrollView.setVisibility(View.INVISIBLE);
                                    swipe_view.setVisibility(View.INVISIBLE);
                                    nope.setVisibility(View.INVISIBLE);
                                    heart.setVisibility(View.INVISIBLE);
                                    like.setVisibility(View.INVISIBLE);
                                    Toast.makeText(SwipingActivity.this, "No User was found according to your filters", Toast.LENGTH_SHORT).show();
                                    //text.setText("No User was found according to your filters");
                                } else {
                                    //getData.setDistance(distance);
                                    //getData.setUserId(UserId);



                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(SwipingActivity.this, error.toString(), Toast.LENGTH_SHORT).show();

                            }
                        });
                    }
                }
            });

        } else{
            Toast.makeText(SwipingActivity.this, "You are unauthorized", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onBackPressed() {
        if (swipe_view_flag==1){
            swipe_view_flag=0;
            swipe1.setVisibility(View.VISIBLE);
            swipe2.setVisibility(View.INVISIBLE);
            swipe3.setVisibility(View.INVISIBLE);
        }
        else {
            super.onBackPressed();
        }
    }
}