package com.example.viewpager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignIn extends AppCompatActivity {
    private Activity act;
    TextView switchScreen;
    Button acceptInput;
    EditText username, password, confirmPassword;
    private FirebaseAuth mAuth;

    private void innitLogIn(){
        setContentView(R.layout.fragment_login);
        username = findViewById(R.id.lgUsername);
        password = findViewById(R.id.lgPassword);
        switchScreen = findViewById(R.id.lgSwitchScreen);
        acceptInput = findViewById(R.id.loginBtn);
        acceptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = username.getText().toString();
                String pass = password.getText().toString();
                if (email.equals("") || pass.equals("")) {
                    return;
                }
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(act, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });

            }
        });
        switchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                innitRegister();
            }
        });

    }

    private void innitRegister(){
        setContentView(R.layout.fragment_register);
        username = findViewById(R.id.rgEmail);
        password = findViewById(R.id.rgPassword);
        confirmPassword = findViewById(R.id.rgConfirmPassword);
        acceptInput = findViewById(R.id.registerBtn);
        acceptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = username.getText().toString();
                String pass = password.getText().toString();
                String cfmPass = confirmPassword.getText().toString();
                if (email.equals("") || pass.equals("") || cfmPass.equals("") || !pass.equals(cfmPass)) {
                    return;
                }
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            startActivity(new Intent(act, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                        }
                    }
                });

            }
        });
        switchScreen = findViewById(R.id.rgSwitchScreen);
        switchScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                innitLogIn();
            }
        });
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        act = this;
        mAuth = FirebaseAuth.getInstance();

        innitLogIn();
    }


    @Override
    public void onBackPressed() {
        startActivity(new Intent(act, MainActivity.class));
        finish();
    }
}
