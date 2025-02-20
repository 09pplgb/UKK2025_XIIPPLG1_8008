package com.example.ukk;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    private EditText eml, usr, ps, crp, nm;
    private Button rgs;
    private TextView lgn;
    private static final String REGISTER_URL = "http://172.16.0.203/ukk2/register.php";

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

        eml = findViewById(R.id.email);
        usr = findViewById(R.id.username);
        ps = findViewById(R.id.password);
        crp = findViewById(R.id.crPassword);
        nm = findViewById(R.id.name);

        rgs = findViewById(R.id.btn_register);

        lgn = findViewById(R.id.login);

        lgn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this, Login.class);
                startActivity(intent);
            }
        });

        rgs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        String username = usr.getText().toString().trim();
        String password = ps.getText().toString().trim();
        String email = eml.getText().toString().trim();
        String name = nm.getText().toString().trim();
        String confirmPassword = crp.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || email.isEmpty() || name.isEmpty()) {
            Toast.makeText(this, "Semua data harus diisi", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Password tidak cocok", Toast.LENGTH_SHORT).show();
            return;
        }

        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Mendaftarkan...");
        progressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Registrasi berhasil!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(Register.this, Login.class));
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                        Toast.makeText(Register.this, "Gagal mendaftar: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("email", email);
                params.put("name", name);
                params.put("confirmation", confirmPassword);
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}