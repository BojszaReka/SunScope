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

import com.example.komplexbeadando.R;

import java.io.File;

public class LoginActivity extends AppCompatActivity {

    EditText txf_Usename;
    EditText txf_Password;
    private boolean showPassword = false;
    ImageButton btn_toggle;
    CheckBox chb_remember;
    Boolean remember = false;
    private File path;

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
        initializeActivity();
    }

    private void initializeActivity() {
        txf_Usename = findViewById(R.id.txf_Username);
        txf_Password = findViewById(R.id.txf_Password);
        btn_toggle = findViewById(R.id.btn_viewPsw);
        chb_remember = findViewById(R.id.chb_remember);

        path = getApplicationContext().getFilesDir();

        if(FileHandler.isRememberedUser(path)){
            String userdata = FileHandler.rememberedUser();
            userdata = userdata.substring(1);
            LoginProcess(userdata);
        }
    }

    public void Login(View view) {
        String username = txf_Usename.getText().toString();
        String psw = txf_Password.getText().toString();
        if( username.equals(null) || psw.equals(null) || username.equals("") || psw.equals("") || username.equals(" ") || psw.equals(" ")){
            Toast.makeText(this, "Beviteli mező(k) üres(ek)!", Toast.LENGTH_LONG).show();
        }else{
            String result = FileHandler.Login(username, psw, path, remember);
            if(result.charAt(0) == '@'){
                LoginProcess(result);
            }else{
                Toast.makeText(this, result, Toast.LENGTH_LONG).show();
            }
        }
    }

    public void LoginProcess(String userdata){
        Log.d(TAG, "Logged in: "+userdata);

        Intent intent = new Intent(LoginActivity.this, SunActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        SunActivity.loggedInUser = userdata;
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
}