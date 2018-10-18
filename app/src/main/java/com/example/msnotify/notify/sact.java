package com.example.msnotify.notify;

import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

        if (isNetworkAvailable()) {
            add();
        } else {
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setVisibility(View.VISIBLE);
        }
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                if (direction == ItemTouchHelper.RIGHT) {
                    Info pos = infoList.get(viewHolder.getAdapterPosition());
                    loadPhoto(pos.getUrl());
                    adap.notifyDataSetChanged();
                }
            }
        }).attachToRecyclerView(recyclerView);
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
                Snackbar.make(recyclerView, "Notice Update", Snackbar.LENGTH_SHORT).show();
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


//    int noticepostiton;
//    @Override
//    public void onNoticeClicked(int noticePos) {
//
//    }

    private void loadPhoto(String url) {
        AlertDialog windowAnimations;
        AlertDialog.Builder imageDialog = new AlertDialog.Builder(this, android.R.style.Theme_Black_NoTitleBar);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        View layout = inflater.inflate(R.layout.custom_fullimage_dialog,
                (ViewGroup) findViewById(R.id.layout_root));
        PhotoView image = layout.findViewById(R.id.photo_view);
        Glide.with(this).load(url).into(image);

        imageDialog.setView(layout);
        windowAnimations = imageDialog.create();
        windowAnimations.getWindow().getAttributes().windowAnimations = R.style.dialog_animation;
        windowAnimations.show();
    }


}



