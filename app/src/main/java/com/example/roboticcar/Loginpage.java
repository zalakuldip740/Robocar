package com.example.roboticcar;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Loginpage extends AppCompatActivity {
    EditText usernametext, passwordtext, login_passcode;
    Button loginsubmit;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String datares;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        usernametext = findViewById(R.id.login_username);
        passwordtext = findViewById(R.id.login_password);
        login_passcode = findViewById(R.id.login_passcode);
        loginsubmit = findViewById(R.id.login_submit);


        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);





        loginsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String username = usernametext.getText().toString();
                String password = passwordtext.getText().toString();
                final String passcode = login_passcode.getText().toString();


                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Username", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(passcode)) {
                    Toast.makeText(getApplicationContext(), "Please Enter Pin", Toast.LENGTH_SHORT).show();
                    return;
                } else if (passcode.length() < 4) {
                    Toast.makeText(getApplicationContext(), "Set 4 digit pin", Toast.LENGTH_SHORT).show();
                    return;
                }
                String emailformat = username + "@gmail.com";

                Toast.makeText(getApplicationContext(), "please Wait !", Toast.LENGTH_SHORT).show();


                OkHttpClient client = new OkHttpClient();

                JSONObject postdata = new JSONObject();
                try {
                    postdata.put("email", emailformat);
                    postdata.put("password", password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                MediaType JSON = MediaType.parse("application/json; charset=utf-8");
                RequestBody requestBody = RequestBody.create(postdata.toString(), JSON);

                Request request = new Request.Builder()
                        .url("http://192.168.43.213:5000/api/token")
                        .post(requestBody)
                        // .header("Accept", "application/json")
                        .header("Content-Type", "application/json")
                        .build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(Loginpage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                    @Override
                    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                        if (response.isSuccessful()) {
                            String responsedata = response.body().string();
                            try {
                                datares = new JSONObject(responsedata).getString("data");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            Loginpage.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {



                                    if (datares.equalsIgnoreCase("There was an error logging in")) {

                                        Toast.makeText(getApplicationContext(), datares, Toast.LENGTH_SHORT).show();
                                    } else {

                                        Toast.makeText(getApplicationContext(), "Successful login", Toast.LENGTH_SHORT).show();

                                        editor = sharedPreferences.edit();
                                        editor.putString("username", username);
                                        editor.putString("passcode", passcode);
                                        editor.putString("token", datares);
                                        editor.putString("is_sign", "true");
                                        editor.apply();

                                        Intent intent = new Intent(Loginpage.this, Passcode.class);
                                        startActivity(intent);
                                        finish();
                                    }


                                }
                            });
                        }



                    }
                });


            }
        });


    }
}