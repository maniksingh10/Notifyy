package com.example.msnotify.notify;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {


    private Button btntimetable,btnteacher,btnstudent,btnsyll;
    private DatabaseReference appinfo,teachverify;
    private ConstraintLayout mainroot;
    private ProgressBar progressBar;
    private int stime;
    private int ttime;
    private String versionName;
    private String state;
    private String quote;
    private TextView alertyv;

    private List<String> uid = new ArrayList<String>();

    private int time;
    private DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appinfo = FirebaseDatabase.getInstance().getReference("AppInfoNotify");
        teachverify = FirebaseDatabase.getInstance().getReference("Admin");

        progressBar = findViewById(R.id.wait);
        btntimetable = findViewById(R.id.timetablebtn);
        btnstudent = findViewById(R.id.studentbtn);
        btnteacher = findViewById(R.id.teacherbtn);
        btnsyll = findViewById(R.id.syallabusbtn);


        mDrawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        mDrawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });
        teachverify.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot :dataSnapshot.getChildren()){
                    Teach teach1 = snapshot.getValue(Teach.class);
                    uid.add(teach1.getMuid());
                }
                FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                if (uid.contains(firebaseUser.getUid())) {
                    btnteacher.setVisibility(View.VISIBLE);
                } else {
                    btnteacher.setVisibility(View.GONE);
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        mainroot = findViewById(R.id.mainroot);
        alertyv = findViewById(R.id.tvappinfo);

        if (isNetworkAvailable()) {
            progressBar.setVisibility(View.VISIBLE);
            mainroot.setVisibility(View.GONE);
            alertyv.setVisibility(View.GONE);
        } else {
            mainroot.setVisibility(View.GONE);
            alertyv.setVisibility(View.VISIBLE);
            alertyv.setText("Hey, It looks Like you are Offline \n :(");
        }
        appinfo.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MyInfo myInfo = snapshot.getValue(MyInfo.class);
                    stime = 0001;
                    ttime = myInfo.getTill();
                    state = "on";
                    quote = myInfo.getQuote();
                    quote = myInfo.getQuote();
                    versionName = myInfo.getVer();
                }
                progressBar.setVisibility(View.GONE);
                CheckAppInfo();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
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
        String vename = BuildConfig.VERSION_NAME;

        if (versionName.equals(vename)) {
            if (time >= stime && time < ttime && state.equals("on")) {
                alertyv.setVisibility(View.GONE);
                mainroot.setVisibility(View.VISIBLE);
                btnteacher.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, tActivity.class);
                        startActivity(intent);
                    }
                });
                //

                btnstudent.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, sact.class);
                        startActivity(intent);
                    }
                });

                btnsyll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(MainActivity.this, Syla_act.class);
                        startActivity(intent);
                    }
                });
                btntimetable.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog windowAnimations;
                        AlertDialog.Builder imageDialog = new AlertDialog.Builder(MainActivity.this, android.R.style.Theme_Black_NoTitleBar);
                        LayoutInflater inflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);

                        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                                (ViewGroup) findViewById(R.id.layout_root));
                        PhotoView image = layout.findViewById(R.id.photo_view);
                        Glide.with(MainActivity.this).load("https://firebasestorage.googleapis.com/v0/b/notify-f6631.appspot.com/o/uploads%2F1539942491698.jpg?alt=media&token=e5724fcc-6833-4dad-a6de-3e87f0b23232").into(image);
                        imageDialog.setView(layout);
                        windowAnimations = imageDialog.create();
                        windowAnimations.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
                        windowAnimations.show();
                    }
                });
            } else {
                mainroot.setVisibility(View.GONE);
                alertyv.setVisibility(View.VISIBLE);
                alertyv.setText("App is Closed! \n \nPlease Come Back");
            }
        } else {
            mainroot.setVisibility(View.GONE);
            alertyv.setVisibility(View.VISIBLE);
            alertyv.setText("New Version of the app is available. \n Update it from the link \n \nhttps://drive.google.com/open?id=1ST_HBVMH5SVjp_GkBbxb9REgMvYE9NAl");
            Linkify.addLinks(alertyv, Linkify.WEB_URLS);
        }


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.about_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.about:
                // do something
                Intent intent = new Intent(MainActivity.this, AboutMe.class);
                startActivity(intent);
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }
}
