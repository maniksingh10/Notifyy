package com.example.msnotify.notify;

import android.content.Intent;
import androidx.annotation.NonNull;
import com.google.android.material.snackbar.Snackbar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Auth extends AppCompatActivity {

    private FirebaseAuth firebaseAuth;
    private EditText mail, pas;
    private ProgressBar progressBar;
    private Button signin,loogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        firebaseAuth = FirebaseAuth.getInstance();
        mail = findViewById(R.id.email);
        pas = findViewById(R.id.password);
        progressBar = findViewById(R.id.login_progress);
        signin = findViewById(R.id.email_sign_in_button);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });


    }

    private void attemptLogin() {


        // Store values at the time of the login attempt.
        String email = mail.getText().toString();
        String password = pas.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!isPasswordValid(password)) {
            pas.setError(getString(R.string.error_invalid_password));
            focusView = pas;
            cancel = true;
        } else if (TextUtils.isEmpty(email)) {
            pas.setError(getString(R.string.error_field_required));
            focusView = pas;
            cancel = true;
        }

        // Check for a valid email address.
        if (TextUtils.isEmpty(email)) {
            mail.setError(getString(R.string.error_field_required));
            focusView = mail;
            cancel = true;
        } else if (!isEmailValid(email)) {
            mail.setError(getString(R.string.error_invalid_email));
            focusView = mail;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            veri(email, password);
        }
    }

    private void veri(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Intent intent = new Intent(Auth.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                } else {
                    Snackbar.make(getCurrentFocus(), task.getException().getMessage(), Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser == null) {
            // No user is signed in

        } else {
            // User logged in
            Intent intent = new Intent(Auth.this, MainActivity.class);
            finish();
            startActivity(intent);
        }


    }

    private boolean isEmailValid(String email) {
        //TODO: Replace this with your own logic
        return email.contains("@");
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 5;
    }


}
