package com.example.roboticcar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.hanks.passcodeview.PasscodeView;

import java.util.concurrent.Executor;

public class Passcode extends AppCompatActivity {
    PasscodeView passcodeView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passcode);
        passcodeView=findViewById(R.id.passcode_view);
        passcodeView.setLocalPasscode("1234")
                .setListener(new PasscodeView.PasscodeViewListener() {
                    @Override
                    public void onFail() {

                    }

                    @Override
                    public void onSuccess(String number) {
                        Intent intent = new Intent(Passcode.this,MainActivity.class);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
                    }
                });



        Executor executor = ContextCompat.getMainExecutor(this);

        final BiometricPrompt biometricPrompt = new BiometricPrompt(this, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(getApplicationContext(), "Use Passcode", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);

                Intent intent = new Intent(Passcode.this,MainActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(getApplicationContext(), "Successful", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();

                Toast.makeText(getApplicationContext(), " Auth Failed! Use Passcode or retry", Toast.LENGTH_SHORT).show();

            }
        });

        BiometricPrompt.PromptInfo promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Unlock Robocar")
                .setSubtitle("Confirm your screen Face Lock or Fingerprint")
                .setNegativeButtonText("Use Passcode")
                .setConfirmationRequired(false)
                .build();


        biometricPrompt.authenticate(promptInfo);





    }
}