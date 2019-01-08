package com.example.msnotify.notify;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    private TextView tv_stu_attend, tv_total_attend,tv_attend_percent,tv_updatedon;
    private int student_total_atten, total_attend;
    private DatabaseReference stuAttendReferen;
    private final String stu_mobile = FirebaseAuth.getInstance().getCurrentUser().getPhoneNumber();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance);

        addAttend = findViewById(R.id.fabattend);
        tv_stu_attend=findViewById(R.id.tv_stu_attend);
        tv_total_attend=findViewById(R.id.tv_total_attend);
        tv_attend_percent= findViewById(R.id.tv_percent);
        tv_updatedon = findViewById(R.id.tv_updated_on);

        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Total_Attendance");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    TotalAttend totalAttend = snapshot.getValue(TotalAttend.class);
                    total_attend = totalAttend.getTotalAtttendd();
                    tv_total_attend.setText(String.valueOf(total_attend));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        stuAttendReferen = FirebaseDatabase.getInstance().getReference("students_attend");

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("students_attend").child(stu_mobile);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    StuAttendance stuAttendance = dataSnapshot.getValue(StuAttendance.class);
                    String a = stuAttendance.getOneStuTotalAttend();
                    String up = stuAttendance.getDate();
                    if(a != null){
                        student_total_atten = Integer.valueOf(a);
                        double percentage = (student_total_atten*100)/total_attend;
                        if(student_total_atten<total_attend){
                            tv_stu_attend.setText(String.valueOf(student_total_atten));
                            tv_attend_percent.setText(String.valueOf(percentage));
                        }else{
                            tv_stu_attend.setText(String.valueOf(0));
                            tv_attend_percent.setText(String.valueOf(0));
                        }
                        tv_updatedon.setText(up);
                    }else{
                        tv_stu_attend.setText(String.valueOf("0"));
                    }
            }

            @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });

        addAttend.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            showAddDialog();
        }
    });
    }


    private void showAddDialog() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView= inflater.inflate(R.layout.dialog_add_attendance, null);
        dialogBuilder.setView(dialogView);
        final EditText editText = (EditText)
                dialogView.findViewById(R.id.editText);
        Button button = dialogView.findViewById(R.id.btn_attend);
        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int add = Integer.valueOf(editText.getText().toString());
                int updated_attend = student_total_atten + add;

                stuAttendReferen.child(stu_mobile).setValue(new StuAttendance(add,new SimpleDateFormat("dd-MMM HH:mm", Locale.getDefault()).format(new Date()),String.valueOf(updated_attend)));
alertDialog.dismiss();
            }
        });

    }
}
