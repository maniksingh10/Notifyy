package com.example.msnotify.notify;

import android.content.Intent;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
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

        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        firebaseAuth = FirebaseAuth.getInstance();
        nickname_et = findViewById(R.id.username);
        course_et = findViewById(R.id.spinner2);
        mobile_et = findViewById(R.id.phone);
        send_bt = findViewById(R.id.sendcodebt);
        progressBar = findViewById(R.id.progressBar);
        root = findViewById(R.id.studentsroot);

        send_bt.setOnClickListener(new View.OnClickListener() {
                                       @Override
                                       public void onClick(View v) {
                                           progressBar.setVisibility(View.VISIBLE);
                                           saveInfo();
                                       }
                                   }
        );

    }

    private void saveInfo(){
        String name= nickname_et.getText().toString();
        String couse = course_et.getSelectedItem().toString();
        String mobile = mobile_et.getText().toString();

        if(name.isEmpty() ||couse.isEmpty()||mobile.isEmpty() ){
            showError();
            progressBar.setVisibility(View.GONE);
        } else{
            sendverifycode("+91"+mobile);
            progressBar.setVisibility(View.VISIBLE);
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

    private void signinwiithcreditndial(PhoneAuthCredential phoneAuthCredential) {
        firebaseAuth.signInWithCredential(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete( Task<AuthResult> task) {

                String name= nickname_et.getText().toString();
                String couse = course_et.getSelectedItem().toString();
                String mobile = mobile_et.getText().toString();

                if(task.isSuccessful()){
                    String key = databaseReference.push().getKey();
                    UserInfo userInfo = new UserInfo(name, couse,mobile);
                    databaseReference.child(key).setValue(userInfo);

                    progressBar.setVisibility(View.VISIBLE);
                    Intent intent = new Intent(LoginStudents.this, sact.class);
                    startActivity(intent);
                    finish();
                    progressBar.setVisibility(View.GONE);
                }else{
                    //Toast.makeText(LoginStudents.this, "Not Success"+ task.getException().getMessage(), Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void sendverifycode(String phone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phone, 60, TimeUnit.SECONDS, TaskExecutors.MAIN_THREAD, mCallBack


        );
    }

    private String verifyID;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallBack = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            Toast.makeText(LoginStudents.this, "Code Sent to Mobile", Toast.LENGTH_SHORT).show();
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
           // Toast.makeText(LoginStudents.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            progressBar.setVisibility(View.GONE);
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
            Intent intent = new Intent(LoginStudents.this, sact.class);
            finish();
            startActivity(intent);
        }

    }


}
