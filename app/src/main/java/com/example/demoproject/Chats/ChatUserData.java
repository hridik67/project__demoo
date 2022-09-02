package com.example.demoproject.Chats;

import android.graphics.Bitmap;

public class ChatUserData {
    Bitmap image;
    String name,desc,userid;

    public ChatUserData(Bitmap image, String name, String desc,String userid) {
        this.image = image;
        this.name = name;
        this.desc = desc;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
