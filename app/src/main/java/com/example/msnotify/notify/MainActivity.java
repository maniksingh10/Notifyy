package com.example.msnotify.notify;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private Button btnstudent, btnteacher;
    private DatabaseReference appinfo;
    private ConstraintLayout mainroot;
    private ProgressBar progressBar;
    private int stime;
    private int ttime;
    private String versionName;
    private String state;
    private String quote;
    private TextView alertyv;
    private int time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appinfo = FirebaseDatabase.getInstance().getReference("AppInfoNotify");
        progressBar = findViewById(R.id.wait);

        mainroot = findViewById(R.id.mainroot);
        alertyv = findViewById(R.id.tvappinfo);
        btnstudent= findViewById(R.id.studentbtn);
        btnteacher = findViewById(R.id.teacherbtn);
        if(isNetworkAvailable()){
            progressBar.setVisibility(View.VISIBLE);
            mainroot.setVisibility(View.GONE);
            alertyv.setVisibility(View.GONE);
        }else{
            mainroot.setVisibility(View.GONE);
            alertyv.setVisibility(View.VISIBLE);
            alertyv.setText("Hey, It looks Like you are Offline \n :(");
        }


        appinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyInfo myInfo = snapshot.getValue(MyInfo.class);
                    stime = myInfo.getStart();
                    ttime = myInfo.getTill();
                    state = myInfo.getState();
                    quote = myInfo.getQuote();
                    versionName=myInfo.getVer();
                }
                progressBar.setVisibility(View.GONE);
                CheckAppInfo();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
        btnteacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,Auth.class);
                startActivity(intent);
            }
        });
        //

        btnstudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,sact.class);
                startActivity(intent);
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    private void CheckAppInfo() {
        String currentDateAndTime = new SimpleDateFormat("HHmm", Locale.getDefault()).format(new Date());
        time = Integer.parseInt(currentDateAndTime);
        String vename= BuildConfig.VERSION_NAME;
        Log.d("cjeck", String.valueOf(time)+String.valueOf(stime) + String.valueOf(ttime) +state + versionName);

        if(versionName.equals(vename)){
            if (time >= stime && time < ttime && state.equals("on")) {
                alertyv.setVisibility(View.GONE);
                mainroot.setVisibility(View.VISIBLE);

            }
            else {
                mainroot.setVisibility(View.GONE);
                alertyv.setVisibility(View.VISIBLE);
                alertyv.setText("App is Closed! \n \nPlease Come Back");
            }
        }
        else {
            mainroot.setVisibility(View.GONE);
            alertyv.setVisibility(View.VISIBLE);
            alertyv.setText("New Version of the app is available. \n Update it from the link \n \nhttps://drive.google.com/open?id=1ST_HBVMH5SVjp_GkBbxb9REgMvYE9NAl");
            Linkify.addLinks(alertyv, Linkify.WEB_URLS);
        }


    }
}
