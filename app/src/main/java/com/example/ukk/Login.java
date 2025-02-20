package com.example.ukk;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    private TextView Login;
    private EditText user, pass;
    private Button btn_login;
    private TextView register;
    private static final String URL = "http://172.16.0.203/ukk2/login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        user = findViewById(R.id.Email);
        pass = findViewById(R.id.Password);
        btn_login = findViewById(R.id.btn_login);
        register = findViewById(R.id.register);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = user.getText().toString().trim();
                String password = pass.getText().toString().trim();

                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(Login.this, "Username dan password tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    login(username, password);
                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(com.example.ukk.Login.this, Register.class);
                startActivity(intent);
            }
        });


    }

    private void login(String username, String password) {
        RequestQueue queue = Volley.newRequestQueue(this);

        JSONObject postData = new JSONObject();
        try {
            postData.put("username", username);
            postData.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                URL + "?username=" + username + "&password=" + password,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            if (success) {
                                Toast.makeText(Login.this, "Login Berhasil", Toast.LENGTH_SHORT).show();

                                JSONObject usersData = response.getJSONObject("data");
                                String idUser = usersData.getString("id");

                                SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString("idL", idUser);
                                editor.apply();

                                Log.d("SharedPreferences", "id pengguna: " + idUser);

                                Intent intent = new Intent(Login.this, MainActivity.class);
                                intent.putExtra("idL", idUser);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(Login.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Login.this, "Error parsing response.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Login.this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
        );

        queue.add(jsonObjectRequest);
    }
}
