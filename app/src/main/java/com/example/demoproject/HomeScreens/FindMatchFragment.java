package com.example.demoproject.HomeScreens;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.ArrayMap;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.demoproject.FindMatchView.OnSwipeTouchListener;
import com.example.demoproject.HomePageActivity;
import com.example.demoproject.Notification.SendNotification;
import com.example.demoproject.R;
import com.example.demoproject.UserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class FindMatchFragment extends Fragment {

    ArrayMap<Float, UserDetails> distance = new ArrayMap<>();
    ArrayMap<Float,String> UserId = new ArrayMap<>();
    DatabaseReference swipeReference,MatchList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    ImageView nope,like,heart;
    String choices;
    TextView name,description,dist,gender;
    FrameLayout frameLayout;
    StorageReference storage;
    int i;
    Context context;
    TextView nousertext;
    ScrollView scrollView;
    RelativeLayout relativeLayout;
    LinearLayout bottomlinearLayout;
    LinearLayout linearLayout;



    @SuppressLint("ClickableViewAccessibility")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_find_match, container, false);
        context=getActivity();
        frameLayout=view.findViewById(R.id.frame_layout);

        swipeReference = FirebaseDatabase.getInstance().getReference("Swipes");
        MatchList = FirebaseDatabase.getInstance().getReference("Swipes");
        nousertext=view.findViewById(R.id.nousertext);
        scrollView=view.findViewById(R.id.scollview);
        relativeLayout=view.findViewById(R.id.rl);
        bottomlinearLayout=view.findViewById(R.id.linearlayout);
        name = view.findViewById(R.id.Name);
        description=view.findViewById(R.id.description);
        linearLayout=view.findViewById(R.id.linear_layout);
        dist=view.findViewById(R.id.Distance);
        gender=view.findViewById(R.id.gender);
        nope=view.findViewById(R.id.nope);
        like=view.findViewById(R.id.like);
        heart=view.findViewById(R.id.heart);
        HomePageActivity getData =(HomePageActivity) getActivity();
        distance=getData.getDistance();
        UserId=getData.getUserId();
        i=0;
        storage= FirebaseStorage.getInstance().getReference("UsersProfilePhotos");
        update(i);

        scrollView.setOnTouchListener(new OnSwipeTouchListener(){
            @Override
            public boolean onSwipeRight() {
                //Toast.makeText(getData, "swipe right", Toast.LENGTH_SHORT).show();
                choices="like";
                swipeUpdate(choices);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
                //Toast.makeText(getContext(), choices, Toast.LENGTH_SHORT).show();
                return super.onSwipeRight();
            }

            @Override
            public boolean onSwipeLeft() {
                //Toast.makeText(getData, "swipe left", Toast.LENGTH_SHORT).show();
                choices = "nope";
                swipeUpdate(choices);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
                //Toast.makeText(getContext(), choices, Toast.LENGTH_SHORT).show();
                return super.onSwipeLeft();
            }
        });
        /*scrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                //Toast.makeText(getData, "touched", Toast.LENGTH_SHORT).show();
                return false;
            }
        });

         */
        nope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choices = "nope";
                swipeUpdate(choices);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
                //Toast.makeText(getContext(), choices, Toast.LENGTH_SHORT).show();
            }
        });
        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choices="like";
                swipeUpdate(choices);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
                //Toast.makeText(getContext(), choices, Toast.LENGTH_SHORT).show();
            }
        });
        heart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                choices="heart";
                swipeUpdate(choices);
                linearLayout.removeAllViews();
                linearLayout.invalidate();
                //Toast.makeText(getContext(), choices, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }

    public interface IsAvailableCallback {
        void onAvailableCallback(boolean isAvailable);
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
                            //Toast.makeText(context, "its a match", Toast.LENGTH_SHORT).show();
                            MatchList.child(currentUser).child(UserId.valueAt(i)).setValue("Matched");
                            MatchList.child(UserId.valueAt(i)).child(currentUser).setValue("Matched");
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
            nousertext.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.INVISIBLE);
            bottomlinearLayout.setVisibility(View.INVISIBLE);

            //Toast.makeText(context, "No further users to show according to filters", Toast.LENGTH_SHORT).show();
        }
    }


    public void update(int j){

        //Toast.makeText(context, String.valueOf(distance.size()), Toast.LENGTH_SHORT).show();
        if (j<distance.size()) {
            getPhots(UserId.valueAt(j).toString(),distance.valueAt(j).getNoOfImage());
            dist.setText(distance.keyAt(j).toString() + "KM");
            name.setText(distance.valueAt(j).getName());
            gender.setText(distance.valueAt(j).getGender());
            description.setText("Bio-\n"+distance.valueAt(j).getDescription());
        } else{

            nousertext.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.INVISIBLE);
            relativeLayout.setVisibility(View.INVISIBLE);
            bottomlinearLayout.setVisibility(View.INVISIBLE);
            //Toast.makeText(context, "No further users to show according to filters", Toast.LENGTH_SHORT).show();
        }

    }

    private void getPhots(String toString, int noOfImage) {
        for (int i=0;i<noOfImage;i++){
            StorageReference reference=storage.child(toString+"Profile Picture"+(i+1));
            try {
                File localfile= File.createTempFile(toString+"Profile Picture"+i,".jpg");
                reference.getFile(localfile)
                        .addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                Bitmap bitmap = BitmapFactory.decodeFile(localfile.getAbsolutePath());
                                ImageView imageView = new ImageView(context);
                                imageView.setImageResource(R.mipmap.ic_launcher);
                                addvieW1(imageView,bitmap);
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
    }

    private void addvieW1(ImageView imageView, Bitmap bitmap) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,   ViewGroup.LayoutParams.MATCH_PARENT);

        // setting the margin in linearlayout
        params.setMargins(0, 10, 0, 10);
        imageView.setLayoutParams(params);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setImageBitmap(bitmap);

        // adding the image in layout
        linearLayout.addView(imageView);
    }
}