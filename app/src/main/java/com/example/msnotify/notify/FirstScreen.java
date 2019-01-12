package com.example.msnotify.notify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class FirstScreen extends AppCompatActivity {

    private Button fstu,firteac;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);
        fstu =findViewById(R.id.studentbtnFirst);
        firteac = findViewById(R.id.teacherbtnFirst);



        fstu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this,LoginStudents.class);
                startActivity(intent);
            }
        });

        firteac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FirstScreen.this,Auth.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

        if (currentUser == null) {

            // No user is signed in

        } else {
            // User logged in
            Intent intent = new Intent(FirstScreen.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

    }
}
