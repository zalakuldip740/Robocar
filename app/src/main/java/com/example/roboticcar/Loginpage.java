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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Loginpage extends AppCompatActivity {
    EditText usernametext, passwordtext,login_passcode;
    Button loginsubmit;
    FirebaseAuth firebaseAuth;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpage);

        usernametext = findViewById(R.id.login_username);
        passwordtext = findViewById(R.id.login_password);
        login_passcode=findViewById(R.id.login_passcode);
        loginsubmit = findViewById(R.id.login_submit);

        firebaseAuth = FirebaseAuth.getInstance();


        sharedPreferences = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);


        // if(firebaseAuth.getCurrentUser() != null){
        //   finish();

        // startActivity(new Intent(getApplicationContext(), Passcode.class));
        //}


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
                }else if(TextUtils.isEmpty(passcode)){
                    Toast.makeText(getApplicationContext(), "Please Enter Pin", Toast.LENGTH_SHORT).show();
                    return;
                }else if(passcode.length()<4){
                    Toast.makeText(getApplicationContext(), "Set 4 digit pin", Toast.LENGTH_SHORT).show();
                    return;
                }
                String emailformat=username + "@gmail.com";

                Toast.makeText(getApplicationContext(), "please Wait !", Toast.LENGTH_SHORT).show();

                firebaseAuth.signInWithEmailAndPassword(emailformat, password).addOnCompleteListener(Loginpage.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getApplicationContext(), "Successful login", Toast.LENGTH_SHORT).show();

                            editor = sharedPreferences.edit();
                            editor.putString("username", username);
                            editor.putString("passcode", passcode);
                            editor.apply();

                            Intent intent = new Intent(Loginpage.this, Passcode.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Invalid username or password", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }
}