package com.example.demoproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.demoproject.FindMatchView.FindMatchBackendFragment;
import com.example.demoproject.HomeScreens.ChatFragment;
import com.example.demoproject.HomeScreens.ProfileFragment;
import com.example.demoproject.databinding.ActivityHomePageBinding;

import java.util.ArrayList;

public class HomePageActivity extends AppCompatActivity {

    ActivityHomePageBinding binding;
    static int startAge,endAge;
    ArrayList<String> genderSelected;
    ArrayMap<Float,UserDetails> distance = new ArrayMap<>();
    ArrayMap<Float,String> UserId = new ArrayMap<>();
    FrameLayout frameLayout;


    public ArrayMap<Float, UserDetails> getDistance() {
        return distance;
    }

    public void setDistance(ArrayMap<Float, UserDetails> distance) {
        this.distance = distance;
    }

    public ArrayMap<Float, String> getUserId() {
        return UserId;
    }

    public void setUserId(ArrayMap<Float, String> userId) {
        UserId = userId;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startAge=FilterPageActivity.StartAge;
        endAge=FilterPageActivity.EndAge;
        genderSelected=FilterPageActivity.genderSelected;
        binding =ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragement(new FindMatchBackendFragment());
        binding.bottomNavigationView.setSelectedItemId(R.id.findmatch);
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()){
                case R.id.profile:
                    replaceFragement(new ProfileFragment());
                    break;
                case R.id.findmatch:
                    replaceFragement(new FindMatchBackendFragment());
                    break;
                case R.id.chat:
                    replaceFragement(new ChatFragment());
                    break;
            }
            return true;
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent= new Intent(this, FilterPageActivity.class);
        startActivity(intent);
        finish();
    }

    public void replaceFragement(Fragment fragment){
        FragmentManager fragmentManager= getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }

    public int getStartAge() {
        return startAge;
    }

    public int getEndAge() {
        return endAge;
    }

    public ArrayList<String> getGenderSelected() {
        return genderSelected;
    }
}