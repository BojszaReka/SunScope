package com.example.komplexbeadando.ui;

import android.os.Build;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
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
        String times = deSerializeResponse(apiresponse);
        return times;
    }

    public static String getSunriseSunsetFromAPI(double lat, double lng, String date) {
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

            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();

        }
    }

    public static String getSunriseSunsetFromAPI(double lat, double lng) {
        try {
            String apiUrl = "https://api.sunrise-sunset.org/json?lat=" + lat + "&lng=" + lng+"&formatted=0";

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

            return response.toString();
        } catch (Exception e) {
            return "Error: " + e.getMessage();

        }
    }

    public String deSerializeResponse(String jsonResponse){
        Gson gson = new Gson();
        SunriseSunsetResponse response = gson.fromJson(jsonResponse, SunriseSunsetResponse.class);

        String sunrise = response.getResults().getSunrise();
        String sunset = response.getResults().getSunset();
        sunrise = formatTime(sunrise);
        sunset = formatTime(sunset);
        return sunrise+";"+sunset;
    }

    public static String formatTime(String inputDate) {
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
}