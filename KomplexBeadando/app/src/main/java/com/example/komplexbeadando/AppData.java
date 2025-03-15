package com.example.komplexbeadando;

import android.graphics.Bitmap;
import androidx.annotation.NonNull;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppData {
    public String username;
    public String horoscope;
    public double longitude = 0;
    public double latitude = 0;
    public String[] times;
    public Date[] dates;
    public String dailydata = "Getting data...";
    public String weeklydata = "Getting data...";
    public String monthlydata = "Getting data...";
    List<Bitmap> photos;

    public AppData() {}

    public AppData(User user){
        username = user.getUsername();
        horoscope = user.getHoroscope();
        setPhotos(user.getPhotos());
    }

    public AppData(User user, double longitude, double latitude, String[] times, String dailydata, String weeklydata, String monthlydata){
        username = user.getUsername();
        horoscope = user.getHoroscope();
        this.longitude = longitude;
        this.latitude = latitude;
        this.times = times;
        this.dailydata = dailydata;
        this.weeklydata = weeklydata;
        this.monthlydata = monthlydata;
        setPhotos(user.getPhotos());
    }

    // Getters and Setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String[] getTimes() {
        return times;
    }

    public void setTimes(String[] times) {
        this.times = times;
    }

    public Date[] getDates() {
        return dates;
    }

    public void setDates(Date[] dates) {
        this.dates = dates;
    }

    public String getDailydata() {
        return dailydata;
    }

    public void setDailydata(String dailydata) {
        this.dailydata = dailydata;
    }

    public String getWeeklydata() {
        return weeklydata;
    }

    public void setWeeklydata(String weeklydata) {
        this.weeklydata = weeklydata;
    }

    public String getMonthlydata() {
        return monthlydata;
    }

    public void setMonthlydata(String monthlydata) {
        this.monthlydata = monthlydata;
    }

    public void addPhoto(Bitmap photo){
        photos.add(photo);
    }

    public void setPhotos(List<Bitmap> photos){
        this.photos = new ArrayList<>();
        this.photos.addAll(photos);
    }

    public List<Bitmap> getPhotos(){
        return photos;
    }

    // toString method
    @NonNull
    @Override
    public String toString() {
        return "AppData{" +
                "username='" + username + '\'' +
                ", horoscope='" + horoscope + '\'' +
                ", longitude=" + longitude +
                ", latitude=" + latitude +
                ", times=" + (times != null ? String.join(", ", times) : "null") +
                ", dailydata='" + dailydata + '\'' +
                ", weeklydata='" + weeklydata + '\'' +
                ", monthlydata='" + monthlydata + '\'' +
                '}';
    }
}
