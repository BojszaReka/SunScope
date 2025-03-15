package com.example.komplexbeadando.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.komplexbeadando.ApiHandler;
import com.example.komplexbeadando.AppData;
import com.example.komplexbeadando.DatabaseServiceManager;
import com.example.komplexbeadando.R;
import com.example.komplexbeadando.User;

import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class LoginActivity extends AppCompatActivity {

    DatabaseServiceManager dbManager;

    EditText txf_Usename;
    EditText txf_Password;
    private boolean showPassword = false;
    ImageButton btn_toggle;
    CheckBox chb_remember;
    Boolean remember = false;

    String appLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        loadLocale(this);

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbManager = new DatabaseServiceManager(getApplicationContext());

        initializeActivity();
        checkRememberedUser();
    }

    private void initializeActivity() {
        txf_Usename = findViewById(R.id.txf_Username);
        txf_Password = findViewById(R.id.txf_Password);
        btn_toggle = findViewById(R.id.btn_viewPsw);
        chb_remember = findViewById(R.id.chb_remember);
    }

    private void checkRememberedUser() {
        User u = dbManager.getRememberedUser();
        if(u != null){
            LoginProcess(u);
        }
    }

    public void loadLocale(Activity activity) {
        SharedPreferences prefs = activity.getSharedPreferences("Settings", Activity.MODE_PRIVATE);
        String language = prefs.getString("language", "en"); // Default to English if not set
        appLang = language;
        Log.d(TAG, "App language set: "+appLang);
        setLocal(activity, language);
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

    public void Login(View view) {
        String username = txf_Usename.getText().toString();
        String psw = txf_Password.getText().toString();
        if( username.equals(null) || psw.equals(null) || username.isEmpty() || psw.isEmpty() || username.equals(" ") || psw.equals(" ")){
            Toast.makeText(this, getString(R.string.some_input_field_may_be_empty), Toast.LENGTH_LONG).show();
        }else{
            if(dbManager.authenticateUser(username,psw)){
                User u = dbManager.getUser(username);
                u.setRemembered(remember);
                dbManager.updateUser(u);
                LoginProcess(u);
            }else{
                Toast.makeText(this, getString(R.string.wrong_login_information), Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void LoginProcess(User u){
        if(u.getHoroscope().equals("Saggitarius")){
            u.setHoroscope("Sagittarius");
        }
        Log.d(TAG, "Logged in: "+u.getUsername());
        Intent intent = new Intent(LoginActivity.this, SunActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SunActivity.data = fillAppData(u);
        startActivity(intent);
        finish();
    }

    public void Register(View view) {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
    }

    public void togglePsw(View view) {
        showPassword = !showPassword;
        if (showPassword) {
            txf_Password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            btn_toggle.setImageResource(android.R.drawable.ic_menu_close_clear_cancel);
        } else {
            txf_Password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            btn_toggle.setImageResource(android.R.drawable.ic_menu_view);
        }
        txf_Password.setSelection(txf_Password.length());
    }

    public void toggleRememeber(View view) {
        remember = !remember;
    }

    public AppData fillAppData(User u){
        ExecutorService executor = Executors.newFixedThreadPool(3);

        String horoscope = u.getHoroscope();
        ApiHandler api = new ApiHandler();

        Future<String> dailyFuture = executor.submit(() -> api.getDailyHoroscope(horoscope, appLang));
        Future<String> weeklyFuture = executor.submit(() -> api.getWeeklyHoroscope(horoscope, appLang));
        Future<String> monthlyFuture = executor.submit(() -> api.getMonthlyHoroscope(horoscope, appLang));

        String dailydata = "";
        String weeklydata = "";
        String monthlydata = "";

        try {
            dailydata = dailyFuture.get();
            weeklydata = weeklyFuture.get();
            monthlydata = monthlyFuture.get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return new AppData(u, 0, 0, new String[]{"", ""}, dailydata, weeklydata, monthlydata, appLang);
    }
}