package com.example.msnotify.notify;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.util.Linkify;
import android.widget.TextView;

public class Syla_act extends AppCompatActivity {

    private TextView syllabus_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_syla_act);

        syllabus_tv = findViewById(R.id.tvappinfo);
        syllabus_tv.setText("Open this link for Syllabus\n\n\nhttp://hsbte.org.in/syllabus/revised-curriculum-w-e-f-session-2017-18");
        Linkify.addLinks(syllabus_tv, Linkify.WEB_URLS);
    }
}
