package com.simca.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class viewAtnSelectSub extends AppCompatActivity {

    TextView dsa,java,oose,nt,osc,apti,ss,rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_atn_select_sub);

        dsa = findViewById(R.id.view_atn_select_DSA);
        java = findViewById(R.id.view_atn_select_JAVA);
        oose = findViewById(R.id.view_atn_select_OOSE);
        nt = findViewById(R.id.view_atn_select_NT);
        osc = findViewById(R.id.view_atn_select_OSC);
        apti = findViewById(R.id.view_atn_select_APTI);
        ss = findViewById(R.id.view_atn_select_SS);
        rnd = findViewById(R.id.view_atn_select_rnd);

        dsa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAtnSelectSub.this,viewAttendance.class);
                intent.putExtra("subject","DSA");
                startActivity(intent);
            }
        });

        java.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAtnSelectSub.this,viewAttendance.class);
                intent.putExtra("subject","JAVA");
                startActivity(intent);
            }
        });

        oose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAtnSelectSub.this,viewAttendance.class);
                intent.putExtra("subject","OOSE");
                startActivity(intent);
            }
        });

        nt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAtnSelectSub.this,viewAttendance.class);
                intent.putExtra("subject","NT");
                startActivity(intent);
            }
        });

        osc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAtnSelectSub.this,viewAttendance.class);
                intent.putExtra("subject","OSC");
                startActivity(intent);
            }
        });

        apti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAtnSelectSub.this,viewAttendance.class);
                intent.putExtra("subject","Aptitude");
                startActivity(intent);
            }
        });

        ss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAtnSelectSub.this,viewAttendance.class);
                intent.putExtra("subject","Soft skills");
                startActivity(intent);
            }
        });

        rnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(viewAtnSelectSub.this,viewAttendance.class);
                intent.putExtra("subject","R &amp; D");
                startActivity(intent);
            }
        });
    }
}