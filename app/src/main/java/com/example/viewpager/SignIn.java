package com.example.viewpager;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignIn extends AppCompatActivity {
    private Activity act;
    TextView switchScreen;
    Button acceptInput;

    TextInputLayout email, password, confirmPassword, username;
    private FirebaseAuth mAuth;
    private boolean isLogIn = true;

    private boolean validateEmail() {
        String val = email.getEditText().getText().toString();
        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()) {
            email.setError("Field must not be empty");
            return false;
        } else if (!val.matches(emailPattern)) {
            email.setError("Invalid email address");
            return false;
        } else {
            email.setError(null);
            email.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validatePassword() {
        String val = password.getEditText().getText().toString();
        String passwordVal = "^" +
                //"(?=.*[0-9])" +         //at least 1 digit
                //"(?=.*[a-z])" +         //at least 1 lower case letter
                //"(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])" +      //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{6,}" +               //at least 6 characters
                "$";
        if (val.isEmpty()) {
            password.setError("Field must not be empty");
            return false;
        } else if (!val.matches(passwordVal)) {
            password.setError("Password is too weak");
            return false;
        } else {
            password.setError(null);
            password.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateReconfirmPassword() {
        String val = confirmPassword.getEditText().getText().toString();
        String tPassword = email.getEditText().getText().toString();
        if (val.isEmpty()) {
            confirmPassword.setError("Field must not be empty");
            return false;
        } else if (!val.equals(tPassword)) {
            confirmPassword.setError("Must match password");
            return false;
        } else {
            confirmPassword.setError(null);
            confirmPassword.setErrorEnabled(false);
            return true;
        }
    }

    private boolean validateUsername() {
        String val = username.getEditText().getText().toString();

        if (val.isEmpty()) {
            username.setError("Field must not be empty");
            return false;
        } else {
            username.setError(null);
            username.setErrorEnabled(false);
            return true;
        }

    }

    private void innitLogIn() {
        setContentView(R.layout.fragment_login);
        isLogIn = true;
        email = findViewById(R.id.lgEmail);
        password = findViewById(R.id.lgPassword);
        switchScreen = findViewById(R.id.lgSwitchScreen);
        acceptInput = findViewById(R.id.loginBtn);
        acceptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String _email = email.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();
                if (_email.isEmpty() || pass.isEmpty()) {
                    return;
                }
                mAuth.signInWithEmailAndPassword(_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
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

    private void innitRegister() {
        setContentView(R.layout.fragment_register);
        email = findViewById(R.id.rgEmail);
        password = findViewById(R.id.rgPassword);
        confirmPassword = findViewById(R.id.rgConfirmPassword);
        acceptInput = findViewById(R.id.registerBtn);
        username = findViewById(R.id.rgUsername);
        acceptInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!validateEmail() | !validateUsername() | !validatePassword() | !validateReconfirmPassword()) {
                    return;
                }
                String _email = email.getEditText().getText().toString();
                String pass = password.getEditText().getText().toString();
                String cfmPass = confirmPassword.getEditText().getText().toString();
                String name = username.getEditText().getText().toString();
                mAuth.createUserWithEmailAndPassword(_email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information.
                            FirebaseUser mUser = mAuth.getCurrentUser();
                            FirebaseFirestore db = FirebaseFirestore.getInstance();

                            Map<String, Object> data = new HashMap<>();
                            data.put("username", name);
                            db.collection("users").document(mUser.getUid()).set(data);

                            startActivity(new Intent(act, MainActivity.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Toast.makeText(getApplicationContext(), "Password must be at least 6 characters", Toast.LENGTH_LONG).show();
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
