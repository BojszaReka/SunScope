package com.example.komplexbeadando.ui;

import android.content.Intent;
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

import com.example.komplexbeadando.ApiHandler;
import com.example.komplexbeadando.AppData;
import com.example.komplexbeadando.DatabaseServiceManager;
import com.example.komplexbeadando.R;

import java.util.Date;

public class HoroscopeActivity extends AppCompatActivity {

    public static AppData data;

    DatabaseServiceManager dbManager;

    TextView txt_UserName;
    TextView txt_todaysDate;
    TextView txt_zodiacSign;
    TextView txt_typeIndicator;
    TextView txt_horoscopeDescription;

    ImageView tab1;
    ImageView tab2;
    ImageView tab3;
    ImageView img_zodiacSign;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_horoscope);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbManager = new DatabaseServiceManager(getApplicationContext());

        initializeActivity();
        setZodiacLogo();
        new Thread(() -> intializeAPIdata()).start();
    }

    public void initializeActivity(){

        txt_UserName = findViewById(R.id.txt_UserName);
        txt_todaysDate = findViewById(R.id.txt_date);
        txt_zodiacSign = findViewById(R.id.txt_zodiacSign);
        txt_typeIndicator = findViewById(R.id.txt_typeIndicator);
        txt_horoscopeDescription = findViewById(R.id.txt_horoscopeDescription);

        tab1 = findViewById(R.id.img_tab1);
        tab2 = findViewById(R.id.img_tab2);
        tab3 = findViewById(R.id.img_tab3);
        img_zodiacSign = findViewById(R.id.img_zodiacSign);

        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d", d.getTime());

        setupUI(txt_UserName, "Hello, "+data.getUsername());
        setupUI(txt_todaysDate, s.toString());
        setupUI(txt_zodiacSign, data.getHoroscope());
    }

    private void intializeAPIdata() {
        ApiHandler api = new ApiHandler();
        data.setDailydata(api.getDailyHoroscope(data.getHoroscope()));
        data.setWeeklydata(api.getWeeklyHoroscope(data.getHoroscope()));
        data.setMonthlydata(api.getMonthlyHoroscope(data.getHoroscope()));
        displayDailyHoroscope();
    }

    private void setZodiacLogo(){
        switch(data.getHoroscope()){
            case "Aries": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac1)); break;
            case "Taurus": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac2)); break;
            case "Gemini": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac3)); break;
            case "Cancer": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac4)); break;
            case "Leo": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac5)); break;
            case "Virgo": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac6)); break;
            case "Libra": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac7)); break;
            case "Scorpio": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac8)); break;
            case "Saggitarius": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac9)); break;
            case "Capricorn": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac10)); break;
            case "Aquarius": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac11)); break;
            case "Pisces": img_zodiacSign.setImageDrawable(getResources().getDrawable(R.drawable.zodiac12)); break;
        }

    }

    private void setupUI(TextView element, String content){
        element.setText(content);
        element.requestLayout();
    }

    public void redirectSundial(View view) {
        Intent intent = new Intent(HoroscopeActivity.this, SunActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SunActivity.data = data;
        startActivity(intent);
        finish();
    }

    public void settingButtonClicked(View view) {
        boolean result = dbManager.updateUserOnLogout(data.getUsername());
        if(!result){
            Intent intent = new Intent(HoroscopeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }

    public void displayDailyHoroscope(View view) {
        displayDailyHoroscope();
    }
    public void displayDailyHoroscope() {
        runOnUiThread(() -> {
            setupUI(txt_typeIndicator, "Daily");
            setupUI(txt_horoscopeDescription, data.getDailydata());
            tab1.getLayoutParams().width = 175;
            tab1.requestLayout();
            tab2.getLayoutParams().width = 150;
            tab2.requestLayout();
            tab3.getLayoutParams().width = 150;
            tab3.requestLayout();
        });
    }

    public void displayWeeklyHoroscope(View view) {
        setupUI(txt_typeIndicator, "Weekly");
        setupUI(txt_horoscopeDescription, data.getWeeklydata());
        tab2.getLayoutParams().width = 175;
        tab2.requestLayout();
        tab1.getLayoutParams().width = 150;
        tab1.requestLayout();
        tab3.getLayoutParams().width = 150;
        tab3.requestLayout();
    }

    public void displayMonthlyHoroscope(View view) {
        setupUI(txt_typeIndicator, "Monthly");
        setupUI(txt_horoscopeDescription, data.getMonthlydata());
        tab3.getLayoutParams().width = 175;
        tab3.requestLayout();
        tab1.getLayoutParams().width = 150;
        tab1.requestLayout();
        tab2.getLayoutParams().width = 150;
        tab2.requestLayout();
    }
}