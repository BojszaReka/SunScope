package com.example.komplexbeadando.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.komplexbeadando.R;

import java.util.Date;

public class SunActivity extends AppCompatActivity implements SensorEventListener {

    public static String loggedInUser;
    TextView txt_UserName;
    TextView txt_todaysDate;
    ImageView img_compass;

    //Compass variables
    private SensorManager sensorManager;
    private Sensor sensor;

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
        initialize();
        txt_UserName = findViewById(R.id.txt_UserName);
        txt_UserName.setText("Hello, "+username);


        txt_todaysDate = findViewById(R.id.txt_date);
        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d", d.getTime());
        txt_todaysDate.setText(s);

        //compass
        img_compass = findViewById(R.id.img_compass);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    public void redirectHoroscope(View view) {
        Intent intent = new Intent(SunActivity.this, HoroscopeActivity.class);
        SunActivity.loggedInUser = loggedInUser;
        startActivity(intent);
    }

    public void initialize(){
        String liu = loggedInUser.substring(1);
        String[] st = liu.split(";");
        username = st[0];
        horoscope = st[1];
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
}