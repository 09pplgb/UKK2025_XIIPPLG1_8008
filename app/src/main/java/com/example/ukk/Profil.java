package com.example.ukk;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

    public class Profil extends AppCompatActivity {

        private TextView nameTextView, imageTextView;
        private String userId;
        private static final String API_URL = "http://172.16.0.203/ukk2/login.php";

        @SuppressLint({"WrongViewCast", "MissingInflatedId"})
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.profil);


            nameTextView = findViewById(R.id.Tv);
            imageTextView = findViewById(R.id.imageView);

            Button logoutButton = findViewById(R.id.btnlogout);


            SharedPreferences loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            userId = loginPrefs.getString("idL", null);

            if (userId == null) {
                SharedPreferences regisPrefs = getSharedPreferences("RegisPrefs", MODE_PRIVATE);
                userId = regisPrefs.getString("idR", null);
            }

            if (userId != null) {
                fetchUserData(userId);
            }


            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    logout();
                }
            });
        }

        private void fetchUserData(String id) {
            String url = API_URL + id;


            RequestQueue queue = Volley.newRequestQueue(this);
            JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {

                                String name = response.getString("name");
                                String email = response.getString("email");
                                String phone = response.getString("phone");


                                nameTextView.setText(name);
                                imageTextView.setText(email);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                    Toast.makeText(Profil.this, "Error fetching data", Toast.LENGTH_SHORT).show();
                }
            });


            queue.add(request);
        }


        private void logout() {

            SharedPreferences loginPrefs = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences regisPrefs = getSharedPreferences("RegisPrefs", MODE_PRIVATE);

            SharedPreferences.Editor loginEditor = loginPrefs.edit();
            SharedPreferences.Editor regisEditor = regisPrefs.edit();

            loginEditor.clear();
            regisEditor.clear();

            loginEditor.apply();
            regisEditor.apply();


            Intent intent = new Intent(Profil.this, Login.class);
            startActivity(intent);
            finish();
        }
    }

