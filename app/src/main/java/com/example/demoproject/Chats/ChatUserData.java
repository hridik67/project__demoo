package com.example.demoproject.Chats;

import android.graphics.Bitmap;

import com.example.demoproject.UserDetails;

import java.io.Serializable;

public class ChatUserData implements Serializable {
    Bitmap image;
    String userid;
    UserDetails userDetails;

    public ChatUserData(Bitmap image, UserDetails userDetails,String userid) {
        this.image = image;
        this.userDetails=userDetails;
        this.userid=userid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public UserDetails getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(UserDetails userDetails) {
        this.userDetails = userDetails;
    }
}
