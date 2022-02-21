package com.simca.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class codeGenerate extends AppCompatActivity {

    List<String> names;

    String otp = "",selectedSubject="";
    CountDownTimer cTimer = null;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_generate);

        names = new ArrayList<>();

        Spinner subjectSpinner = findViewById(R.id.subjectSpin);
        TextView codeYourTxt = findViewById(R.id.code_gen);
        TextView codeNumber = findViewById(R.id.code_gen_number);
        TextView codeExp = findViewById(R.id.code_gen_expiry);

        dbRef.child("subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnap:snapshot.getChildren()) {
                    String spinnerName = childSnap.child("name").getValue(String.class);
                    names.add(spinnerName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(codeGenerate.this, R.layout.selected_item,names);
                arrayAdapter.setDropDownViewResource( R.layout.drop_down_items);
                subjectSpinner.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        subjectSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!subjectSpinner.getItemAtPosition(i).toString().equals("Select subject"))
                {
                    selectedSubject = subjectSpinner.getItemAtPosition(i).toString();

                    int randomPin = (int) (Math.random()*9000)+1000;
                    otp = String.valueOf(randomPin);
                    codeYourTxt.setVisibility(View.VISIBLE);
                    codeNumber.setText(otp);
                    subjectSpinner.setEnabled(false);

                    sendCodeToDB();
                    startTimer(codeExp);
                }
                else
                {
                    selectedSubject = "ss";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void sendCodeToDB() {

        HashMap<String,Object> timeMap = new HashMap<>();

        timeMap.put("subject",selectedSubject);
        timeMap.put("code",otp);

        dbRef.child("code").child(otp).updateChildren(timeMap);
    }

    void startTimer(TextView expiry) {
        cTimer = new CountDownTimer(60000, 1000) {
            public void onTick(long millisUntilFinished) {
                expiry.setText("Will expire in: " + millisUntilFinished / 1000);
            }
            public void onFinish() {
                expiry.setText("done!");
                onBackPressed();
                Toast.makeText(getApplicationContext(), "Code Expired", Toast.LENGTH_SHORT).show();
                dbRef.child("code").removeValue();
            }
        };
        cTimer.start();
    }


    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        cancelTimer();
        dbRef.child("code").removeValue();
    }
}