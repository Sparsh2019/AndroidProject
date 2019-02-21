package com.sparsh2k19.geekhub;

import android.net.Uri;

import java.net.URL;

public class Message {
    private String name;
    private String message;
    private String uid;
    private long time;
    private String imageUri = null;
    private Code code = null;

    Message(){}

    Message(String name, String message, long time, String uid){
        this.name = name;
        this.message = message;
        this.time = time;
        this.uid = uid;
    }

    public Code getCode() {
        return code;
    }

    public void setCode(Code code) {
        this.code = code;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public String getImageUri() {return imageUri; }

    public String getName() {
        return name;
    }

    public String getMessage() {
        return message;
    }

    public long getTime() {
        return time;
    }

    public String getUid() {
        return uid;
    }
}
