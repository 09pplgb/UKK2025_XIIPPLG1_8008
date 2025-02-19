package com.example.ukk;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Login extends AppCompatActivity {

    private TextView login1;
    private EditText email, password;
    private Button btn_login;
    private TextView register;
    private static final String LOGIN_URL = "http://172.16.0.203/Ukk_kasir/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        login1 = findViewById(R.id.login1);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        btn_login = findViewById(R.id.btn_login);
        register = findViewById(R.id.Register);

        btn_login.setText("Please log in to continue");

        email.setCompoundDrawablesWithIntrinsicBounds(R.drawable.user, 0, 0, 0);
    }
}
