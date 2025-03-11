package com.example.komplexbeadando;

import java.util.Date;

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

    // Empty constructor
    public AppData() {}
    public AppData(User user){
        username = user.getUsername();
        horoscope = user.getHoroscope();
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
    }

    // Constructor with parameters
    public AppData(String username, String horoscope, double longitude, double latitude, String[] times, String dailydata, String weeklydata, String monthlydata) {
        this.username = username;
        this.horoscope = horoscope;
        this.longitude = longitude;
        this.latitude = latitude;
        this.times = times;
        this.dailydata = dailydata;
        this.weeklydata = weeklydata;
        this.monthlydata = monthlydata;
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

    // toString method
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
