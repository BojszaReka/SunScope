package com.example.komplexbeadando;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.ArrayList;
import java.util.List;


@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private String horoscope;
    private boolean isRemembered;


    @TypeConverters(BitmapListConverter.class)
    private List<Bitmap> photos;

    @Ignore
    public User(){}

    public User(String username, String password, String horoscope) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.horoscope = horoscope;
        isRemembered = false;
        photos = new ArrayList<>();
    }

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public boolean getIsRemembered(){return isRemembered;}

    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    public void setRemembered(boolean remembered) {
        isRemembered = remembered;
    }

    public List<Bitmap> getPhotos() {
        return photos;
    }

    public void setPhotos(List<Bitmap> photos) {
        this.photos = photos;
    }

    @NonNull
    @Override
    public String toString() {
        return
                "{id =" + id +
                ", username=" + username +
                ", password=" + password +
                ", horoscope=" + horoscope +
                ",};";
    }


}
