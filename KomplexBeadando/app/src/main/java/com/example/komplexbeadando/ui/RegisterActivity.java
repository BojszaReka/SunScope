package com.example.komplexbeadando.ui;



import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;
import static androidx.core.content.ContextCompat.startActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
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
        Boolean remember = chb_remember.isActivated();
        if( username.equals(null) || psw.equals(null) || username.equals("") || psw.equals("") || username.equals(" ") || psw.equals(" ") || selectedDate.equals(null)){
            Toast.makeText(this, "Beviteli mező(k) üres(ek)!", Toast.LENGTH_LONG).show();
        }else{
            String result = FileHandler.Register(username, psw, selectedDate, path, remember);
            if(result.charAt(0) == '@'){
                Log.d(TAG, "Registered: "+result);
                Intent intent = new Intent(RegisterActivity.this, SunActivity.class);
                SunActivity.loggedInUser = result;
                startActivity(intent);
            }else{
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
        }

    }

    public void Login(View view) {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
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
}