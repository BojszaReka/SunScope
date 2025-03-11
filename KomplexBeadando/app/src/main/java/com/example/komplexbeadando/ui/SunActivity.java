package com.example.komplexbeadando.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
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
import android.provider.MediaStore;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.komplexbeadando.ApiHandler;
import com.example.komplexbeadando.AppData;
import com.example.komplexbeadando.DatabaseServiceManager;
import com.example.komplexbeadando.R;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SunActivity extends AppCompatActivity implements SensorEventListener, LocationListener {

    public static AppData data;
    DatabaseServiceManager dbManager;

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

        dbManager = new DatabaseServiceManager(getApplicationContext());

        initializeActivity();
        compassInitialization();
        locationInitialization();
        requestNotificationPermissions();
        new Thread(() -> apiDataRequestInitialization()).start();
    }



    //called methods
    public void initializeActivity(){
        txt_UserName = findViewById(R.id.txt_UserName);
        txt_todaysDate = findViewById(R.id.txt_date);
        txt_location = findViewById(R.id.txt_location);
        txt_sunrise = findViewById(R.id.txt_sunriseTime);
        txt_sunset = findViewById(R.id.txt_sunsetTime);

        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d", d.getTime());

        txt_UserName.setText("Hello, "+data.getUsername());
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
         while(data.getLatitude() == 0 || data.getLongitude() == 0){
             try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
         String time = api.getSunriseSunsetTime(data.getLatitude(), data.getLongitude());
         Date[] dates = api.getDates(data.getLatitude(), data.getLongitude());
         data.setTimes(time.split(";"));
         data.setDates(dates);
         setTime();
    }

    public void setTime(){
        runOnUiThread(() -> {
            txt_sunrise.setText(data.getTimes()[0]);
            txt_sunset.setText(data.getTimes()[1]);
        });
    }

    public void redirectHoroscope(View view) {
        Intent intent = new Intent(SunActivity.this, HoroscopeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        HoroscopeActivity.data = data;
        startActivity(intent);
        finish();
    }

    public void settingButtonClicked(View view) {
        boolean result = dbManager.updateUserOnLogout(data.getUsername());
        if(!result){
            Intent intent = new Intent(SunActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
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
            data.setLatitude(addresses.get(0).getLatitude());
            data.setLongitude(addresses.get(0).getLongitude());
            txt_location.setText("Your location is: "+address);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        new Thread(() -> apiDataRequestInitialization()).start();
    }

    //notification event handler
    public void notificationButtonClick(View view) {
        if(data.dates != null && data.dates[0]!=null && data.dates[1] != null){
            toggleButtons(false);
            Log.d(TAG, "Notification button clicked");
            LayoutInflater l = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
            View popupview = l.inflate(R.layout.popup_notification, null);
            final PopupWindow pop = new PopupWindow(popupview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            ImageButton btn_close = popupview.findViewById(R.id.btn_closePopup);
            ImageButton btn_save = popupview.findViewById(R.id.btn_save);
            RadioButton rbtn_sunrise = popupview.findViewById(R.id.rbtn_sunrise);
            RadioButton rbtn_sunset = popupview.findViewById(R.id.rbtn_sunset);

            Spinner dropdown = popupview.findViewById(R.id.spn_dropwon);
            String[] items = new String[] {"5 mins before", "10 mins before", "15 mins before", "30 mins before", "45 mins before", "60 mins before"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
            dropdown.setAdapter(adapter);
            pop.setWidth(800);
            dropdown.getLayoutParams().width = 600;

            btn_close.setOnClickListener((OnClickListener) v -> {
                toggleButtons(true);
                pop.dismiss();
            });

            btn_save.setOnClickListener((OnClickListener) v -> {
                Log.d(TAG, "Save button clicked");
                int mins = Integer.parseInt(dropdown.getSelectedItem().toString().split(" ")[0]);

                Log.d(TAG, "Selected dropdown item: "+dropdown.getSelectedItem().toString());


                if(rbtn_sunrise.isChecked() && !rbtn_sunset.isChecked()){
                    Log.d(TAG, "Sunrise selected, dropdown item: "+dropdown.getSelectedItem().toString());
                    setNotification(data.dates[0],mins);

                    sendTestNotification();

                }
                if(!rbtn_sunrise.isChecked() && rbtn_sunset.isChecked()){
                    Log.d(TAG, "Sunrise selected, dropdown item: "+dropdown.getSelectedItem().toString());
                    setNotification(data.dates[1],mins);

                    sendTestNotification();
                }
                toggleButtons(true);
                pop.dismiss();
            });
            pop.showAsDropDown(txt_sunrise, 50, -600);
        }

    }

    @SuppressLint("ScheduleExactAlarm")
    public void setNotification(Date time, int mins){
        Context context = getApplicationContext();
        Log.d(TAG, "Setting notification "+mins+" before: "+time.toString());
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        // Calculate the trigger time
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.add(Calendar.MINUTE, -mins);

        // Set the alarm
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
        Toast.makeText(this, "Set notification "+mins+" before: "+time.toString(), Toast.LENGTH_LONG).show();
    }

    public void toggleButtons(boolean boo){
        findViewById(R.id.btn_Sundial).setClickable(boo);
        findViewById(R.id.btn_Horoscope).setClickable(boo);
        findViewById(R.id.btn_notification).setClickable(boo);
        findViewById(R.id.btn_gallery).setClickable(boo);
        findViewById(R.id.btn_camera).setClickable(boo);
    }

    public void sendTestNotification(){
        Date currentDate = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        calendar.add(Calendar.MINUTE, 1);
        Date newDate = calendar.getTime();

        setNotification(newDate,0);
    }

    private void requestNotificationPermissions() {
        Log.d(TAG, "Check notification permission:");
        ActivityResultLauncher<String[]> notificationPermissionAccess = registerForActivityResult(new ActivityResultContracts .RequestMultiplePermissions(), result -> {
                    Boolean scheduleExactAlarmGranted = null;
                    Boolean useExactAlarmGranted = null;
                    Boolean postNotficitaionGranted = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        scheduleExactAlarmGranted = result.getOrDefault(Manifest.permission.SCHEDULE_EXACT_ALARM, false);
                        useExactAlarmGranted = result.getOrDefault(Manifest.permission.USE_EXACT_ALARM,false);
                        postNotficitaionGranted = result.getOrDefault(Manifest.permission.POST_NOTIFICATIONS,false);
                    }
                    if (scheduleExactAlarmGranted != null && scheduleExactAlarmGranted) {
                        Log.d(TAG, "Schedule exact alarm permission granted");
                    }
                    if (useExactAlarmGranted != null && useExactAlarmGranted) {
                        Log.d(TAG, "Use exact alarm permission granted");
                    }
                    if (postNotficitaionGranted != null && postNotficitaionGranted) {
                         Log.d(TAG, "Post notification permission granted");
                    }
                }
        );
        notificationPermissionAccess.launch(new String[] {
                Manifest.permission.SCHEDULE_EXACT_ALARM,
                Manifest.permission.USE_EXACT_ALARM,
                Manifest.permission.POST_NOTIFICATIONS
        });
    }

    public void openCamera(View view) {
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivity(intent);
    }
}