package com.example.msnotify.notify;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class sact extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Recycle adap;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notice");
    private List<Info> infoList = new ArrayList<>();

    private TextView mEmptyStateTextView;
    View loadingIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sact);
        loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.VISIBLE);
        mEmptyStateTextView = findViewById(R.id.empty_view);
        recyclerView = findViewById(R.id.lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

    if(isNetworkAvailable()){
        add();
    }else {
        loadingIndicator.setVisibility(View.GONE);
        mEmptyStateTextView.setVisibility(View.VISIBLE);
    }

    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void add() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Snackbar.make(getCurrentFocus(),"Notice Update",Snackbar.LENGTH_SHORT).show();
                infoList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Info info = snapshot.getValue(Info.class);
                    infoList.add(info);

                }
                adap = new Recycle(getApplicationContext(), infoList);
                recyclerView.setAdapter(adap);
                loadingIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


}
