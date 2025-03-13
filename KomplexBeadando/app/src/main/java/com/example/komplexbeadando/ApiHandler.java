package com.example.komplexbeadando;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class ApiHandler {

    public ApiHandler() {

    }

    public String getSunriseSunsetTime(double latitude, double longitude){
        String apiresponse = getSunriseSunsetFromAPI(latitude, longitude);
        return deSerializeSunsetResponse(apiresponse);
    }

    public String getSunriseSunsetTime(double latitude, double longitude, String date){
        String apiresponse = getSunriseSunsetFromAPI(latitude, longitude, date);
        return deSerializeSunsetResponse(apiresponse);
    }

    public Date[] getDates(double latitude, double longitude) {
        String apiresponse = getSunriseSunsetFromAPI(latitude, longitude);
        Gson gson = new Gson();
        SunriseSunsetResponse response = gson.fromJson(apiresponse, SunriseSunsetResponse.class);
        Date[] dates = new Date[2];
        if(stringToDate(response.getResults().getSunrise()) != null){
            dates[0]= stringToDate(response.getResults().getSunrise());
        }
        if(stringToDate(response.getResults().getSunset()) != null){
            dates[1] = stringToDate(response.getResults().getSunset());
        }
        return dates;
    }

    public Date[] getDates(double latitude, double longitude, String date) {
        String apiresponse = getSunriseSunsetFromAPI(latitude, longitude, date);
        Gson gson = new Gson();
        SunriseSunsetResponse response = gson.fromJson(apiresponse, SunriseSunsetResponse.class);
        Date[] dates = new Date[2];
        if(stringToDate(response.getResults().getSunrise()) != null){
            dates[0]= stringToDate(response.getResults().getSunrise());
        }
        if(stringToDate(response.getResults().getSunset()) != null){
            dates[1] = stringToDate(response.getResults().getSunset());
        }
        return dates;
    }

    public String getDailyHoroscope(String sign){
        String apiresponse = getDailyHoroscopeAPI(sign);
        String horoscopedata = null;
        if(apiresponse != null){
            horoscopedata = deSerializeDailyHoroscopeResponse(apiresponse);
        }
        return  horoscopedata;
    }

    public String getWeeklyHoroscope(String sign){
        String apiresponse = getWeeklyHoroscopeAPI(sign);
        String horoscopedata = null;
        if(apiresponse != null){
            horoscopedata = deSerializeWeeklyHoroscopeResponse(apiresponse);
        }
        return  horoscopedata;
    }

    public String getMonthlyHoroscope(String sign){
        String apiresponse = getMonthlyHoroscopeAPI(sign);
        String horoscopedata = null;
        if(apiresponse != null){
            horoscopedata = deSerializeMonthlyHoroscopeResponse(apiresponse);
        }
        return  horoscopedata;
    }

    private static String getSunriseSunsetFromAPI(double lat, double lng, String date) {
        Log.d(TAG, "Starting API call: get sunset and sunrise data");
        Log.d(TAG, "With date?: "+(date != null ? "&date=" + date : ""));
        String apiUrl = "https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + lng + (date != null ? "&date=" + date : "")+"&formatted=0";
        try {
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                throw new RuntimeException("HTTP GET Request Failed with Error Code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();
            Log.d(TAG, "sunset and sunrise api response: "+response);
            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String getSunriseSunsetFromAPI(double lat, double lng) {
        Log.d(TAG, "Starting API call: get sunset and sunrise data");
        try {
            String apiUrl = "https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + lng+"&formatted=0";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                Log.d(TAG, "HTTP GET Request Failed with Error Code : " + conn.getResponseCode());
                throw new RuntimeException("HTTP GET Request Failed with Error Code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();
            Log.d(TAG, "sunset and sunrise api response: "+response);
            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String getDailyHoroscopeAPI(String sign) {
        Log.d(TAG, "Starting API call: get daily horoscope data for sign: "+sign);
        try {
            String apiUrl = "https://horoscope-app-api.vercel.app/api/v1/get-horoscope/daily?sign="+sign+"&day=TODAY";
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                Log.d(TAG, "HTTP GET Request Failed with Error Code : " + conn.getResponseCode());
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();
            Log.d(TAG, "Daily api response: "+response);
            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String getWeeklyHoroscopeAPI(String sign) {
        Log.d(TAG, "Starting API call: get weekly data for sign: "+sign);
        try {
            String apiUrl = "https://horoscope-app-api.vercel.app/api/v1/get-horoscope/weekly?sign="+sign;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                Log.d(TAG, "HTTP GET Request Failed with Error Code : " + conn.getResponseCode());
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();
            Log.d(TAG, "Weekly api response: "+response);
            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

    private static String getMonthlyHoroscopeAPI(String sign) {
        Log.d(TAG, "Starting API call: get monthly horoscope data for sign: "+sign);
        try {
            String apiUrl = "https://horoscope-app-api.vercel.app/api/v1/get-horoscope/monthly?sign="+sign;
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                Log.d(TAG, "HTTP GET Request Failed with Error Code : " + conn.getResponseCode());
                return null;
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String output;
            while ((output = br.readLine()) != null) {
                response.append(output);
            }
            conn.disconnect();
            Log.d(TAG, "Monthly api response: "+response);
            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }


    private String deSerializeDailyHoroscopeResponse(String jsonResponse){
        Gson gson = new Gson();
        Log.d(TAG, "Deserialized: "+jsonResponse);
        if(!jsonResponse.contains("Error")){
            Log.d(TAG, "Got proper response for daily horoscope");
            DailyHoroscopeDataResponse horoscopeData = gson.fromJson(jsonResponse, DailyHoroscopeDataResponse.class);
            return horoscopeData.getData().getHoroscopeData();
        }else{
            Log.d(TAG, "The was an error in getting daily horoscope");
            return "There was an Errror, check if your wifi or data is on";
        }

    }

    private String deSerializeWeeklyHoroscopeResponse(String jsonResponse){
        Gson gson = new Gson();
        if(!jsonResponse.contains("Error")){
            Log.d(TAG, "Got proper response for weekly horoscope");
            WeeklyHoroscopeDataResponse horoscopeData = gson.fromJson(jsonResponse, WeeklyHoroscopeDataResponse.class);
            return horoscopeData.getData().getHoroscopeData();
        }else{
            Log.d(TAG, "The was an error in getting weekly horoscope");
            return "There was an Errror, check if your wifi or data is on";
        }
    }

    private String deSerializeMonthlyHoroscopeResponse(String jsonResponse){
        Gson gson = new Gson();
        if(!jsonResponse.contains("Error")){
            Log.d(TAG, "Got proper response for monthly horoscope");
            MonthlyHoroscopeDataResponse horoscopeData = gson.fromJson(jsonResponse, MonthlyHoroscopeDataResponse.class);
            return horoscopeData.getData().getHoroscopeData();
        }else{
            Log.d(TAG, "The was an error in getting monthly horoscope");
            return "There was an Errror, check if your wifi or data is on";
        }
    }

    private String deSerializeSunsetResponse(String jsonResponse){
        Gson gson = new Gson();
        if(!jsonResponse.contains("Error")){
            SunriseSunsetResponse response = gson.fromJson(jsonResponse, SunriseSunsetResponse.class);
            String sunrise = response.getResults().getSunrise();
            String sunset = response.getResults().getSunset();
            sunrise = formatTime(sunrise);
            sunset = formatTime(sunset);
            return sunrise+";"+sunset;
        }else{
            return "There was an error while getting the response";
        }

    }

    private static String formatTime(String inputDate) {
        DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
        OffsetDateTime dateTime = OffsetDateTime.parse(inputDate, inputFormatter).plusHours(1);
        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm");
        return dateTime.format(outputFormatter);
    }

    private Date stringToDate(String dateString){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        try {
            return sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}