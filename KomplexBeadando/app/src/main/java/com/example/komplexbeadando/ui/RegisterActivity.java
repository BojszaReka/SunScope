package com.example.komplexbeadando.ui;



import android.app.DatePickerDialog;
import android.content.Intent;
import android.icu.text.DateFormat;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.InputType;
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
import com.example.komplexbeadando.DatabaseServiceManager;
import com.example.komplexbeadando.R;
import com.example.komplexbeadando.User;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class RegisterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    DatabaseServiceManager dbManager;

    EditText txf_Usename;
    EditText txf_Password;
    private boolean showPassword = false;
    ImageButton btn_toggle;
    Button btn_pickDate;
    String selectedDate;
    CheckBox chb_remember;

    Boolean remember = false;
    String horoscope;

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

        dbManager = new DatabaseServiceManager(getApplicationContext());

        initializeActivity();
    }

    private void initializeActivity() {
        txf_Usename = findViewById(R.id.txf_Username);
        txf_Password = findViewById(R.id.txf_Password);
        btn_toggle = findViewById(R.id.btn_viewPsw);
        btn_pickDate = findViewById(R.id.btn_pickDate);
        chb_remember = findViewById(R.id.chb_remember);

        selectedDate = null;
        horoscope = null;
    }

    public void Register(View view) {
        String username = txf_Usename.getText().toString();
        String psw = txf_Password.getText().toString();
        if(username == null || psw == null || username.isEmpty() || psw.isEmpty() || username.equals(" ") || psw.equals(" ") || selectedDate == null || horoscope == null){
            Toast.makeText(this, getString(R.string.some_input_field_may_be_empty), Toast.LENGTH_LONG).show();
        }else{
            if(dbManager.checkUsernameFree(username)){
                AppData data = registerProcess(username, psw);
                Intent intent = new Intent(RegisterActivity.this, SunActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                SunActivity.data = data;
                startActivity(intent);
                finish();
            }else{
                Toast.makeText(this, getString(R.string.the_username_is_taken), Toast.LENGTH_LONG).show();
            }
        }
    }

    private AppData registerProcess(String username, String psw) {
        dbManager.addUser(new User(username, psw, horoscope));
        User u = dbManager.getUser(username);
        u.setRemembered(remember);
        dbManager.updateUser(u);
        return fillAppData(u);
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
        mDatePickerDialogFragment.show(getSupportFragmentManager(), getString(R.string.date_pick));
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(Calendar.YEAR, year);
        mCalendar.set(Calendar.MONTH, month);
        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(mCalendar.getTime());
        btn_pickDate.setText(selectedDate);
        horoscope = dateToHoroscope(month+1, dayOfMonth);
    }

    public AppData fillAppData(User u){
        ExecutorService executor = Executors.newFixedThreadPool(3);

        String horoscope = u.getHoroscope();
        ApiHandler api = new ApiHandler();

        Future<String> dailyFuture = executor.submit(() -> api.getDailyHoroscope(horoscope));
        Future<String> weeklyFuture = executor.submit(() -> api.getWeeklyHoroscope(horoscope));
        Future<String> monthlyFuture = executor.submit(() -> api.getMonthlyHoroscope(horoscope));

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

        return new AppData(u, 0, 0, new String[]{"", ""}, dailydata, weeklydata, monthlydata);
    }

    private String dateToHoroscope(int month, int day){
        if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) return "Aries";
        if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) return "Taurus";
        if ((month == 5 && day >= 21) || (month == 6 && day <= 20)) return "Gemini";
        if ((month == 6 && day >= 21) || (month == 7 && day <= 22)) return "Cancer";
        if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) return "Leo";
        if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) return "Virgo";
        if ((month == 9 && day >= 23) || (month == 10 && day <= 22)) return "Libra";
        if ((month == 10 && day >= 23) || (month == 11 && day <= 21)) return "Scorpio";
        if ((month == 11 && day >= 22) || (month == 12 && day <= 21)) return "Sagittarius";
        if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) return "Capricorn";
        if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) return "Aquarius";
        if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) return "Pisces";
        return "Values are out of bounds";
    }

    public void toggleRememeber(View view) {
        remember = !remember;
    }
}