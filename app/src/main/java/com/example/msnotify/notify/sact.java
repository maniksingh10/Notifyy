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
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class sact extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Recycle adap;
    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("notice");
    private List<Info> infoList = new ArrayList<>();
    private Spinner spn_branch;
    private View loadingIndicator;
    private CheckBox sEveryone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sact);

        loadingIndicator = findViewById(R.id.loading_spinner);
        loadingIndicator.setVisibility(View.VISIBLE);
        spn_branch = findViewById(R.id.spn_branch);

        sEveryone = findViewById(R.id.stcheckBoxisEveryone);
        recyclerView = findViewById(R.id.lists);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false));
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);
        loadingIndicator.setVisibility(View.GONE);
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
                recyclerView.smoothScrollToPosition(infoList.size());
                loadingIndicator.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        sEveryone.setChecked(true);
        if(sEveryone.isChecked()){
            searchNoti(true);
        }
        sEveryone.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    searchNoti(true);
                }else{
                   searchNoti(false);
                }
            }
        });
    }

    private void searchNoti(boolean is){
        if(is){
            spn_branch.setEnabled(false);
            Query query = databaseReference.orderByChild("branch").equalTo("Everyone");
            query.addValueEventListener(customEventListener);
        }else{
            spn_branch.setEnabled(true);
            Query query = databaseReference.orderByChild("branch").equalTo(spn_branch.getSelectedItem().toString());
            query.addValueEventListener(customEventListener);
            spn_branch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Query query = databaseReference.orderByChild("branch").equalTo(spn_branch.getItemAtPosition(position).toString());
                    query.addValueEventListener(customEventListener);
                }
                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });
        }
    }

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

    ValueEventListener customEventListener = new ValueEventListener() {
        @Override
        public void onDataChange(DataSnapshot dataSnapshot) {
            infoList.clear();
            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                Info info = snapshot.getValue(Info.class);
                infoList.add(info);
            }
            adap = new Recycle(getApplicationContext(), infoList);
            recyclerView.setAdapter(adap);
            recyclerView.smoothScrollToPosition(infoList.size());
            loadingIndicator.setVisibility(View.GONE);
        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };


    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
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

        @Override
        public int getSwipeDirs(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            Info pos = infoList.get(viewHolder.getAdapterPosition());
            if (pos.getUrl().isEmpty()) return 0;
            return super.getSwipeDirs(recyclerView, viewHolder);
        }
    };


  /*
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
*/
}



