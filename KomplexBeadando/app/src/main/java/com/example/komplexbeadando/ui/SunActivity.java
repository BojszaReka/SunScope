package com.example.komplexbeadando.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.komplexbeadando.R;

import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SunActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    public static String loggedInUser;
    TextView txt_UserName;
    TextView txt_todaysDate;
    ImageView img_compass;

    //Compass variables
    private SensorManager sensorManager;
    private Sensor sensor;

    //location variables
    public static LocationManager locationManager;
    TextView txt_location;
    TextView txt_sunrise;
    TextView txt_sunset;
    double longitude = 0;
    double latitude = 0;
    String[] times;

    //user variables
    String username;
    String horoscope;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sun);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initializeActivity();
        compassInitialization();
        locationInitialization();
        new Thread(() -> apiDataRequestInitialization()).start();
    }



    //called methods
    public void initializeActivity(){
        userDataConversion();

        txt_UserName = findViewById(R.id.txt_UserName);
        txt_todaysDate = findViewById(R.id.txt_date);
        txt_location = findViewById(R.id.txt_location);
        txt_sunrise = findViewById(R.id.txt_sunriseTime);
        txt_sunset = findViewById(R.id.txt_sunsetTime);

        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d", d.getTime());

        txt_UserName.setText("Hello, "+username);
        txt_todaysDate.setText(s);
    }

    private void compassInitialization() {
        img_compass = findViewById(R.id.img_compass);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void locationInitialization() {
        requestPermissions();
        getLocation();
    }

    private void apiDataRequestInitialization() {
        ApiHandler api = new ApiHandler();
        while(longitude == 0 || latitude == 0){
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        String time = api.getSunriseSunsetTime(latitude, longitude);
        times = time.split(";");
        setTime();
    }

    public void setTime(){
        runOnUiThread(() -> {
            txt_sunrise.setText(times[0]);
            txt_sunset.setText(times[1]);
        });
    }

    private void userDataConversion() {
        String liu = loggedInUser.substring(1);
        String[] st = liu.split(";");
        username = st[0];
        horoscope = st[1];
    }

    public void redirectHoroscope(View view) {
        Intent intent = new Intent(SunActivity.this, HoroscopeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        HoroscopeActivity.loggedInUser = loggedInUser;
        startActivity(intent);
        finish();
    }


    //Compass methods
    @Override
    public void onSensorChanged(SensorEvent event) {
        int degree = Math.round(event.values[1]*360);
        img_compass.setRotation(-degree);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause(){
        super.onPause();
        sensorManager.unregisterListener(this);
    }


    //location handling
    private void requestPermissions() {
        Log.d(TAG, "Check location access");
        ActivityResultLauncher<String[]> locationPermissionRequest = registerForActivityResult(new ActivityResultContracts .RequestMultiplePermissions(), result -> {
            Boolean fineLocationGranted = null;
            Boolean coarseLocationGranted = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                fineLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false);
                coarseLocationGranted = result.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION,false);
            }
            if (fineLocationGranted != null && fineLocationGranted) {
                Log.d(TAG, "Fine location access granted");
            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                Log.d(TAG, "Coarses location access granted");
            } else {
                // No location access granted.
            }
        }
    );
        locationPermissionRequest.launch(new String[] {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @SuppressLint("MissingPermission")
    private void getLocation(){
        try {
            locationManager = (LocationManager) getApplicationContext().getSystemService(LOCATION_SERVICE);
            locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 5000, 5, SunActivity.this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        try {
            Geocoder geocoder = new Geocoder(SunActivity.this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            String address = addresses.get(0).getAddressLine(0);
            longitude = addresses.get(0).getLongitude();
            latitude = addresses.get(0).getLatitude();
            txt_location.setText("Your location is: "+address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> apiDataRequestInitialization()).start();
    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {
        LocationListener.super.onLocationChanged(locations);
    }

    @Override
    public void onFlushComplete(int requestCode) {
        LocationListener.super.onFlushComplete(requestCode);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
    }
}