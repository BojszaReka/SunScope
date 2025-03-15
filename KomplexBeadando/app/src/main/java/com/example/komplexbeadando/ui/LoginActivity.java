package com.example.komplexbeadando.ui;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.content.Intent;
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

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    DatabaseServiceManager dbManager;

    EditText txf_Usename;
    EditText txf_Password;
    private boolean showPassword = false;
    ImageButton btn_toggle;
    CheckBox chb_remember;
    Boolean remember = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dbManager = new DatabaseServiceManager(getApplicationContext());

        /*
        List<User> users  = dbManager.getAllUsers();
        String usernames = "";
        for (User u: users) {
            u.setPhotos(new ArrayList<>());
            dbManager.updateUser(u);
        }
        Log.d(TAG, "Users: "+usernames);
        */
        //bogyo user user2

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

    public void Login(View view) {
        String username = txf_Usename.getText().toString();
        String psw = txf_Password.getText().toString();
        if( username.equals(null) || psw.equals(null) || username.isEmpty() || psw.isEmpty() || username.equals(" ") || psw.equals(" ")){
            Toast.makeText(this, "Beviteli mező(k) üres(ek)!", Toast.LENGTH_LONG).show();
        }else{
            if(dbManager.authenticateUser(username,psw)){
                User u = dbManager.getUser(username);
                u.setRemembered(remember);
                dbManager.updateUser(u);
                LoginProcess(u);
            }else{
                Toast.makeText(this, "Rossz bejelentkezési adatok!", Toast.LENGTH_SHORT).show();
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
        String horoscope = u.getHoroscope();
        ApiHandler api = new ApiHandler();
        String dailydata = api.getDailyHoroscope(horoscope);
        String weeklydata = api.getWeeklyHoroscope(horoscope);
        String monthlydata = api.getMonthlyHoroscope(horoscope);
        String[] times = new String[2];
        times[0] = "";
        times[1] = "";
        return new AppData(u, 0, 0, times, dailydata, weeklydata, monthlydata);
    }
}