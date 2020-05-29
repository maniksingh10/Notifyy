package com.example.msnotify.notify;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Attendance extends AppCompatActivity {

    private FloatingActionButton addAttend;
    private TextView tv_stu_attend, tv_total_attend, tv_attend_percent, tv_updatedon,tv_attend_msg;
    private int student_total_atten, total_attend;
    private DatabaseReference usersRef;
    private final String stu_mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Checking Register....");
        progressDialog.show();
        addAttend = findViewById(R.id.fabattend);
        tv_stu_attend = findViewById(R.id.tv_stu_attend);
        tv_total_attend = findViewById(R.id.tv_total_attend);
        tv_attend_percent = findViewById(R.id.tv_percent);
        tv_updatedon = findViewById(R.id.tv_updated_on);
        tv_attend_msg=findViewById(R.id.tv_attend_msg);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Total_Attendance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    TotalAttend totalAttend = snapshot.getValue(TotalAttend.class);
                    total_attend = totalAttend.getTotalAtttendd();
                    tv_total_attend.setText(String.valueOf(total_attend));
                }
                gotTotalAttend();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        usersRef = FirebaseDatabase.getInstance().getReference("users");


        addAttend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAddDialog();
            }
        });
    }

    private void gotTotalAttend() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("users").child(stu_mobile);
        if (reference != null) {
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    UserInfo stuAttendance = dataSnapshot.getValue(UserInfo.class);
                    if (stuAttendance != null) {
                        String up = stuAttendance.getDate_attendace();
                        student_total_atten = stuAttendance.getAttendance();
                        double percentage = (double) (student_total_atten * 100) / total_attend;

                        if (student_total_atten <= total_attend) {
                            tv_stu_attend.setText(String.valueOf(student_total_atten));
                            tv_attend_percent.setText(String.valueOf(percentage));
                            if (percentage < 75.0) {
                                tv_attend_percent.setTextColor(getResources().getColor(R.color.low_attend));
                            } else {
                                tv_attend_percent.setTextColor(getResources().getColor(R.color.full_attend));
                            }
                        } else {
                            tv_stu_attend.setText(String.valueOf(0));
                            tv_attend_percent.setText(String.valueOf(0));
                            tv_attend_msg.setVisibility(View.VISIBLE);
                            tv_attend_msg.setText("Contact us.......Go to about page");
                        }
                        tv_updatedon.setText(up);
                    }
                    progressDialog.dismiss();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_add_attendance, null);
        dialogBuilder.setView(dialogView);
        final EditText editText = dialogView.findViewById(R.id.editText);
        editText.requestFocus();
        Button button = dialogView.findViewById(R.id.btn_attend);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int add = Integer.valueOf(editText.getText().toString());
                int c_updated_attend = student_total_atten + add;

                if(c_updated_attend>total_attend){
                    Toast.makeText(Attendance.this,"Not possible",Toast.LENGTH_SHORT).show();
                }else{
                    int updated_attend = student_total_atten + add;
                    usersRef.child(stu_mobile).child("attendance").setValue(updated_attend);
                    usersRef.child(stu_mobile).child("date_attendace").setValue(new SimpleDateFormat("dd-MMM HH:mm", Locale.getDefault()).format(new Date()));
                }
                alertDialog.dismiss();
            }
        });

    }
}
