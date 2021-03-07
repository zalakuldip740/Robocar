package com.example.roboticcar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

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
    EditText usernametext, passwordtext, setpasscode1,setpasscode2;
    Button loginsubmit,done;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String datares;
    LottieAnimationView animationloading,animationsuccess;
    String username,passcode1,passcode2;

    TextView step,stepdescription;

    LinearLayout logininputpage,setpasscodeinputpage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        usernametext = findViewById(R.id.login_username);
        passwordtext = findViewById(R.id.login_password);
        setpasscode1 = findViewById(R.id.setpasscode1);
        setpasscode2 = findViewById(R.id.setpasscode2);

        logininputpage=findViewById(R.id.logininputpage);
        setpasscodeinputpage=findViewById(R.id.setpasscodeinputpage);


        loginsubmit = findViewById(R.id.login_submit);
        done = findViewById(R.id.passcode_done);


        step=findViewById(R.id.step);
        stepdescription=findViewById(R.id.stepdescription);


        animationloading=findViewById(R.id.animation_loading);
        animationsuccess=findViewById(R.id.animation_success);


        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);








        loginsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 username = usernametext.getText().toString();
                String password = passwordtext.getText().toString();



                if (TextUtils.isEmpty(username)) {
                    usernametext.setError("Ip Can't be empty");
                    usernametext.requestFocus();
                    return;
                }else if(!Patterns.IP_ADDRESS.matcher(username).matches()){
                    usernametext.setError("Please Enter Valid IP");
                    usernametext.requestFocus();
                    return;
                }
                else if (TextUtils.isEmpty(password)) {
                    passwordtext.setError("Password Can't be empty");
                    passwordtext.requestFocus();
                    return;
                } else {
                    animationloading.setVisibility(View.VISIBLE);
                    animationloading.playAnimation();
                }
                String emailformat = username + "@gmail.com";



                //Toast.makeText(getApplicationContext(), "please Wait !", Toast.LENGTH_SHORT).show();


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
                                animationloading.pauseAnimation();
                                animationloading.setVisibility(View.GONE);
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
                                @SuppressLint("SetTextI18n")
                                @Override
                                public void run() {


                                    animationloading.pauseAnimation();
                                    animationloading.setVisibility(View.GONE);
                                    if (datares.equalsIgnoreCase("There was an error logging in")) {

                                        Toast.makeText(getApplicationContext(), datares, Toast.LENGTH_SHORT).show();
                                    } else {

                                        loginsuccessdialog();

                                    }


                                }
                            });
                        }



                    }
                });


            }
        });


        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 passcode1 = setpasscode1.getText().toString();
                passcode2 = setpasscode2.getText().toString();

                if (TextUtils.isEmpty(passcode1)) {
                    setpasscode1.setError("PIN Can't be empty");
                    setpasscode1.requestFocus();
                } else if (passcode1.length() < 4) {
                    setpasscode1.setError("PIN must have 4 digit");
                    setpasscode1.requestFocus();
                }else  if (TextUtils.isEmpty(passcode2)) {
                    setpasscode2.setError("Confirm PIN Can't be empty");
                    setpasscode2.requestFocus();
                }else if(!passcode1.equals(passcode2)){
                    setpasscode2.setError("Confirm PIN not match");
                    setpasscode2.requestFocus();
                }else  {

                    animationsuccess.setVisibility(View.VISIBLE);
                    animationsuccess.playAnimation();

                    Toast.makeText(getApplicationContext(), "All Done", Toast.LENGTH_SHORT).show();

                    editor = sharedPreferences.edit();
                    editor.putString("username", username);
                    editor.putString("passcode",passcode1);
                    editor.putString("token", datares);
                    editor.putString("is_sign", "true");
                    editor.apply();


                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {

                            Intent intent = new Intent(Loginpage.this, Passcode.class);
                            startActivity(intent);
                            finish();

                        }
                    }, 1000);

                }

            }
        });


    }

    private void loginsuccessdialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(Loginpage.this).create();
        alertDialog.setTitle("Login Successful");
        alertDialog.setMessage("Now Set 4 digit passcode for unlock app");
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                new DialogInterface.OnClickListener() {
                    @SuppressLint("SetTextI18n")
                    public void onClick(DialogInterface dialog, int which) {
                        logininputpage.setVisibility(View.GONE);
                        setpasscodeinputpage.setVisibility(View.VISIBLE);
                        step.setText("Step : 2");
                        stepdescription.setText("Set 4 digit pin for unlock the app.");
                        dialog.dismiss();
                    }
                });
        alertDialog.show();
    }
}