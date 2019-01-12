package com.example.msnotify.notify;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;

import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

public class LoginStudents extends AppCompatActivity {

    private Button send_bt;
    private TextInputEditText nickname_et, mobile_et;
    private FirebaseAuth firebaseAuth;
    private Spinner course_et;
    private ProgressBar progressBar;
    private ConstraintLayout root;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_students);


        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        nickname_et = findViewById(R.id.username);
        course_et = findViewById(R.id.spinner2);
        mobile_et = findViewById(R.id.phone);
        send_bt = findViewById(R.id.sendcodebt);

        root = findViewById(R.id.studentsroot);

        send_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {

                                           saveInfo();
                                       }
                                   }
        );

        mobile_et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() == 10){
                    send_bt.setEnabled(true);
                }else{
                    send_bt.setEnabled(false);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void saveInfo(){
        String name= nickname_et.getText().toString();
        String couse = course_et.getSelectedItem().toString();
        String mobile = mobile_et.getText().toString();

        if(name.isEmpty() ||couse.isEmpty()||mobile.isEmpty() ){
            showError();
        } else{
          sendverifycode("+91"+mobile);
        }
    }

    private void showError() {
        progressBar.setVisibility(View.GONE);
        Snackbar snackbar = Snackbar.make(root, "Error", Snackbar.LENGTH_LONG);
        View snackBarView = snackbar.getView();
        snackBarView.setBackgroundColor(getApplicationContext().getResources().getColor(R.color.colorAccent));
        snackBarView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snackbar.show();
    }

    private void verifyCode(String code) {
        PhoneAuthCredential phoneAuthCredential = PhoneAuthProvider.getCredential(verifyID,code);
        signinwiithcreditndial(phoneAuthCredential);
    }

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private void signinwiithcreditndial(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {

                final String name= nickname_et.getText().toString();
                final String couse = course_et.getSelectedItem().toString();
                final String mobile = mobile_et.getText().toString();
                final String yer;
                radioGroup = findViewById(R.id.logradioGroup);
                int selectedId = radioGroup.getCheckedRadioButtonId();
                if(selectedId == -1){
                   showError();
                }else{
                    radioButton = (RadioButton) findViewById(selectedId);
                    yer = radioButton.getText().toString();

                    if(task.isSuccessful()){
                        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                            @Override
                            public void onComplete(Task<InstanceIdResult> task) {
                                if(task.isSuccessful()){
                                    int iyer = 0;
                                    if(yer.equals("1st Year")){
                                        iyer = 1;
                                    }else if(yer.equals("2nd Year")){
                                        iyer = 2;
                                    }else if(yer.equals("3rd Year")){
                                        iyer = 3;
                                    }
                                    String key = databaseReference.push().getKey();
                                    UserInfo userInfo = new UserInfo(name, couse,"+91"+mobile,task.getResult().getToken(),Build.MODEL,String.valueOf(iyer),
                                            System.currentTimeMillis(),0,new SimpleDateFormat("dd-MMM HH:mm", Locale.getDefault()).format(new Date()));

                                    databaseReference.child("+91"+mobile).setValue(userInfo);

                                    Intent intent = new Intent(LoginStudents.this, MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }
                            }
                        });
                    }else{
                        Toast.makeText(LoginStudents.this, "Not Success"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void sendverifycode(String phone) {

        progressDialog.setTitle("Please Wait.....Don't Tap or Close!!!");
        progressDialog.show();
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack
        );
    }

    private AlertDialog alertDialog;
    private ProgressDialog progressDialog;
    private String verifyID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {


        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(LoginStudents.this, "Code Sent to Mobile", Toast.LENGTH_LONG).show();
            verifyID = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            if(code != null){
                verifyCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(LoginStudents.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
    };

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();
        if (currentUser == null) {
            // No user is signed in

        } else {
            // User logged in
            Intent intent = new Intent(LoginStudents.this, MainActivity.class);
            finish();
            startActivity(intent);
        }

    }


}
