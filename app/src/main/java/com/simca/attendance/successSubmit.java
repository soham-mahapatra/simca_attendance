package com.simca.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

public class successSubmit extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_submit);

        String curr_subject = getIntent().getStringExtra("subject");

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(), "Attendance submitted for: " + curr_subject, Toast.LENGTH_SHORT).show();
                    }
                },500);
            }
        },1000);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(successSubmit.this,Home.class));
            }
        },3000);



    }
}