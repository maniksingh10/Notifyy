package com.example.msnotify.notify;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddingInfo extends AppCompatActivity {

    private FloatingActionButton floatingActionButton;
    private Spinner spinner;
    private EditText editText;
    private DatabaseReference databaseReference;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private String date =new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault()).format(new Date());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adding_info);

        spinner = findViewById(R.id.spinner2);
        editText = findViewById(R.id.notice);
        radioGroup = findViewById(R.id.radioGroup);
        TextView datetv= findViewById(R.id.date);
        datetv.setText(date);
        databaseReference = FirebaseDatabase.getInstance().getReference("notice");
        floatingActionButton = findViewById(R.id.floatingActionButton);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                done();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    private void done() {
        String not = editText.getText().toString();
        String bra = spinner.getSelectedItem().toString();
        int selectedId = radioGroup.getCheckedRadioButtonId();
        radioButton = (RadioButton) findViewById(selectedId);

        if (bra.isEmpty() || not.isEmpty()|| selectedId == -1){
            Snackbar snackbar = Snackbar.make(getCurrentFocus(),"Error",Snackbar.LENGTH_LONG);
            View snackBarView = snackbar.getView();
            snackBarView.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
            snackBarView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            snackbar.show();


        }else{
            String yer = radioButton.getText().toString();
            String sdate =new SimpleDateFormat("dd-MMM-yy hh:mm aa", Locale.getDefault()).format(new Date());


            String key = databaseReference.push().getKey();
            Info info = new Info(yer, not, bra, FirebaseAuth.getInstance().getCurrentUser().getEmail(),sdate);
            databaseReference.child(key).setValue(info);

            Intent returnIntent = new Intent();
            setResult(Activity.RESULT_OK, returnIntent);
            finish();
        }


    }

}
