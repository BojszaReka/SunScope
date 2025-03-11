package com.example.komplexbeadando.ui;



import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
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
import com.example.komplexbeadando.FileHandler;
import com.example.komplexbeadando.R;

import java.io.File;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText txf_Usename;
    EditText txf_Password;
    private boolean showPassword = false;
    ImageButton btn_toggle;
    Button btn_pickDate;
    String selectedDate;
    CheckBox chb_remember;

    Boolean remember = false;

    private File path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        initializeActivity();
    }

    private void initializeActivity() {
        txf_Usename = findViewById(R.id.txf_Username);
        txf_Password = findViewById(R.id.txf_Password);
        btn_toggle = findViewById(R.id.btn_viewPsw);
        btn_pickDate = findViewById(R.id.btn_pickDate);
        chb_remember = findViewById(R.id.chb_remember);

        path = getApplicationContext().getFilesDir();

        selectedDate = null;
    }

    public void Register(View view) {
        String username = txf_Usename.getText().toString();
        String psw = txf_Password.getText().toString();
        if( username.equals(null) || psw.equals(null) || username.equals("") || psw.equals("") || username.equals(" ") || psw.equals(" ") || selectedDate.equals(null)){
            Toast.makeText(this, "Beviteli mező(k) üres(ek)!", Toast.LENGTH_LONG).show();
        }else{
            String result = FileHandler.Register(username, psw, selectedDate, path, remember);
            if(result.charAt(0) == '@'){
                Log.d(TAG, "Registered: "+result);
                Intent intent = new Intent(RegisterActivity.this, SunActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                SunActivity.data = fillAppData(result);
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void Login(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
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

    public void selectDate(View view) {
        com.example.komplexbeadando.ui.DatePicker mDatePickerDialogFragment;
        mDatePickerDialogFragment = new com.example.komplexbeadando.ui.DatePicker();
        mDatePickerDialogFragment.show(getSupportFragmentManager(), "DATE PICK");
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        btn_pickDate.setText(selectedDate);

    }

    public AppData fillAppData(String loggedinuser){
        String liu = loggedinuser.substring(1);
        String[] st = liu.split(";");
        String username = st[0];
        String horoscope = st[1];
        ApiHandler api = new ApiHandler();
        String dailydata = api.getDailyHoroscope(horoscope);
        String weeklydata = api.getWeeklyHoroscope(horoscope);
        String monthlydata = api.getMonthlyHoroscope(horoscope);
        String[] times = new String[2];
        times[0] = "";
        times[1]="";
        AppData data = new AppData(username, horoscope, 0, 0, times, dailydata, weeklydata, monthlydata);
        return data;
    }

    public void toggleRememeber(View view) {
        remember = !remember;
    }
}