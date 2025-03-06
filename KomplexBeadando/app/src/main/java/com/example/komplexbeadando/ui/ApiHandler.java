package com.example.komplexbeadando.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.os.Build;
import android.util.Log;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;

class ApiHandler {


    public ApiHandler() {

    }

    public String getSunriseSunsetTime(double latitude, double longitude){
        String apiresponse = getSunriseSunsetFromAPI(latitude, longitude);
        String times = deSerializeSunsetResponse(apiresponse);
        return times;
    }

    public String getSunriseSunsetTime(double latitude, double longitude, String date){
        String apiresponse = getSunriseSunsetFromAPI(latitude, longitude, date);
        String times = deSerializeSunsetResponse(apiresponse);
        return times;
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
        try {
            String apiUrl = "https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + lng +
                    (date != null ? "&date=" + date : "")+"&formatted=0";

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
            Log.d(TAG, "sunset and sunrise api response: "+response.toString());
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
            Log.d(TAG, "sunset and sunrise api response: "+response.toString());
            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();

        }
    }

    private String deSerializeSunsetResponse(String jsonResponse){
        Gson gson = new Gson();
        SunriseSunsetResponse response = gson.fromJson(jsonResponse, SunriseSunsetResponse.class);

        String sunrise = response.getResults().getSunrise();
        String sunset = response.getResults().getSunset();
        sunrise = formatTime(sunrise);
        sunset = formatTime(sunset);
        return sunrise+";"+sunset;
    }

    private static String formatTime(String inputDate) {
        OffsetDateTime dateTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_OFFSET_DATE_TIME;
            dateTime = OffsetDateTime.parse(inputDate, inputFormatter);
            DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("HH:mm");
            String formattedTime = dateTime.format(outputFormatter);
            return formattedTime;
        }
        return inputDate;
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
            Log.d(TAG, "Daily api response: "+response.toString());
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
            Log.d(TAG, "Weekly api response: "+response.toString());
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
            Log.d(TAG, "Monthly api response: "+response.toString());
            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();

        }
    }


    private String deSerializeDailyHoroscopeResponse(String jsonResponse){

        Gson gson = new Gson();
        DailyHoroscopeDataResponse horoscopeData = gson.fromJson(jsonResponse, DailyHoroscopeDataResponse.class);
        Log.d(TAG, "Deserialized: "+horoscopeData.isSuccess());
        Log.d(TAG, "Deserialized: "+horoscopeData.getData().toString());
        return horoscopeData.getData().getHoroscopeData();
    }

    private String deSerializeWeeklyHoroscopeResponse(String jsonResponse){
        Gson gson = new Gson();
        WeeklyHoroscopeDataResponse horoscopeData = gson.fromJson(jsonResponse, WeeklyHoroscopeDataResponse.class);
        return horoscopeData.getData().getHoroscopeData();
    }

    private String deSerializeMonthlyHoroscopeResponse(String jsonResponse){
        Gson gson = new Gson();
        MonthlyHoroscopeDataResponse horoscopeData = gson.fromJson(jsonResponse, MonthlyHoroscopeDataResponse.class);
        return horoscopeData.getData().getHoroscopeData();
    }
}