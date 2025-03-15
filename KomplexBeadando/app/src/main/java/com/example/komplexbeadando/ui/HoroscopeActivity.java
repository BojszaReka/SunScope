package com.example.komplexbeadando.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.komplexbeadando.ApiHandler;
import com.example.komplexbeadando.AppData;
import com.example.komplexbeadando.DatabaseServiceManager;
import com.example.komplexbeadando.R;

import java.util.Date;
import java.util.Locale;

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
        new Thread(this::intializeAPIdata).start();
    }

    public void initializeActivity(){

        txt_UserName = findViewById(R.id.txt_username);
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

        setupUI(txt_UserName, data.getUsername());
        setupUI(txt_todaysDate, s.toString());
        setupUI(txt_zodiacSign, getZodiac());
    }

    private String getZodiac() {
        switch(data.getHoroscope()){
            case "Aries": return getString(R.string.aries);
            case "Taurus": return getString(R.string.taurus);
            case "Gemini": return getString(R.string.gemini);
            case "Cancer": return getString(R.string.cancer);
            case "Leo": return getString(R.string.leo);
            case "Virgo": return getString(R.string.virgo);
            case "Libra": return getString(R.string.libra);
            case "Scorpio": return getString(R.string.scorpio);
            case "Sagittarius": return getString(R.string.sagittarius);
            case "Capricorn": return getString(R.string.capricorn);
            case "Aquarius": return getString(R.string.aquarius);
            case "Pisces": return getString(R.string.pisces);
            default: return "";
        }
    }

    private void intializeAPIdata() {
        ApiHandler api = new ApiHandler();
        data.setDailydata(api.getDailyHoroscope(data.getHoroscope(), data.getAppLang()));
        data.setWeeklydata(api.getWeeklyHoroscope(data.getHoroscope(), data.getAppLang()));
        data.setMonthlydata(api.getMonthlyHoroscope(data.getHoroscope(), data.getAppLang()));
        displayDailyHoroscope();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void setZodiacLogo(){
        switch(data.getHoroscope()){
            case "Aries": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac1, null)); break;
            case "Taurus": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac2, null)); break;
            case "Gemini": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac3, null)); break;
            case "Cancer": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac4, null)); break;
            case "Leo": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac5, null)); break;
            case "Virgo": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac6, null)); break;
            case "Libra": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac7, null)); break;
            case "Scorpio": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac8, null)); break;
            case "Sagittarius": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac9, null)); break;
            case "Capricorn": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac10, null)); break;
            case "Aquarius": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac11, null)); break;
            case "Pisces": img_zodiacSign.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.zodiac12, null)); break;
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

    @SuppressLint("InflateParams")
    public void settingButtonClicked(View view) {
        toggleButtons(false);
        Log.d(TAG, "Settings button clicked");
        LayoutInflater l = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupview = l.inflate(R.layout.popup_settings, null);
        final PopupWindow pop = new PopupWindow(popupview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        ImageButton btn_close = popupview.findViewById(R.id.btn_closePopup);
        Button btn_save = popupview.findViewById(R.id.btn_saveSetting);
        Button btn_logOut = popupview.findViewById(R.id.btn_logOut);

        Spinner dropdown = popupview.findViewById(R.id.spn_langDropwon);
        String[] items = new String[] {getString(R.string.english), getString(R.string.hungarian)};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        dropdown.setAdapter(adapter);
        pop.setWidth(800);
        dropdown.getLayoutParams().width = 600;

        btn_close.setOnClickListener(v -> {
            toggleButtons(true);
            pop.dismiss();
        });

        btn_save.setOnClickListener(v -> {
            Log.d(TAG, "Save button clicked");
            Log.d(TAG, "Selected dropdown item: "+dropdown.getSelectedItem().toString());
            setAppLanguage(dropdown.getSelectedItem().toString());
            toggleButtons(true);
            pop.dismiss();
            finish();
            startActivity(getIntent());
        });

        btn_logOut.setOnClickListener(v -> {
            toggleButtons(true);
            pop.dismiss();
            logOutUser();
        });

        pop.showAsDropDown(txt_UserName, 30, 600);
    }

    public void logOutUser(){
        boolean result = dbManager.updateUserOnLogout(data.getUsername());
        if(!result){
            Intent intent = new Intent(HoroscopeActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
            startActivity(intent);
            finish();
        }
    }

    public void toggleButtons(boolean boo){
        findViewById(R.id.btn_Sundial).setClickable(boo);
        findViewById(R.id.btn_Horoscope).setClickable(boo);
        findViewById(R.id.btn_settings).setClickable(boo);
        findViewById(R.id.btn_DailyTab).setClickable(boo);
        findViewById(R.id.btn_WeeklyTab).setClickable(boo);
        findViewById(R.id.btn_MonthlyTab).setClickable(boo);
    }

    public void setAppLanguage(String language){
        Log.d(TAG, "Setting app language to: "+language);
        if(language.equals("English") || language.equals("Angol")){
            Log.d(TAG, "Eng true");
            setLocal(HoroscopeActivity.this, "en");
        }
        if(language.equals("Hungarian") || language.equals("Magyar")){
            Log.d(TAG, "Hu true");
            setLocal(HoroscopeActivity.this, "hu");
        }
    }

    public void setLocal(Activity activity, String langcode){
        Locale locale = new Locale(langcode);
        locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());

        SharedPreferences prefs = activity.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("language", langcode);
        editor.apply();
    }

    public void displayDailyHoroscope(View view) {
        displayDailyHoroscope();
    }

    public void displayDailyHoroscope() {
        runOnUiThread(() -> {
            setupUI(txt_typeIndicator, getString(R.string.daily));
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
        setupUI(txt_typeIndicator, getString(R.string.weekly));
        setupUI(txt_horoscopeDescription, data.getWeeklydata());
        tab2.getLayoutParams().width = 175;
        tab2.requestLayout();
        tab1.getLayoutParams().width = 150;
        tab1.requestLayout();
        tab3.getLayoutParams().width = 150;
        tab3.requestLayout();
    }

    public void displayMonthlyHoroscope(View view) {
        setupUI(txt_typeIndicator, getString(R.string.monthly));
        setupUI(txt_horoscopeDescription, data.getMonthlydata());
        tab3.getLayoutParams().width = 175;
        tab3.requestLayout();
        tab1.getLayoutParams().width = 150;
        tab1.requestLayout();
        tab2.getLayoutParams().width = 150;
        tab2.requestLayout();
    }
}