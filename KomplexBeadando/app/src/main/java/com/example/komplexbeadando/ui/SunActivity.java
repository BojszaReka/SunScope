package com.example.komplexbeadando.ui;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.komplexbeadando.R;

import java.util.Date;

public class SunActivity extends AppCompatActivity {

    public static String loggedInUser;
    TextView txt_UserName;
    TextView txt_todaysDate;
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
        parse();
        txt_UserName = findViewById(R.id.txt_UserName);
        txt_UserName.setText("Hello, "+username);

        txt_todaysDate = findViewById(R.id.txt_date);
        Date d = new Date();
        CharSequence s  = DateFormat.format("MMMM d", d.getTime());
        txt_todaysDate.setText(s);

    }

    public void redirectHoroscope(View view) {
        Intent intent = new Intent(SunActivity.this, HoroscopeActivity.class);
        SunActivity.loggedInUser = loggedInUser;
        startActivity(intent);
    }

    public void parse(){
        String liu = loggedInUser.substring(1);
        String[] st = liu.split(";");
        username = st[0];
        horoscope = st[1];
    }
}