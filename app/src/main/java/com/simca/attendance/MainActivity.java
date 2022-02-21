package com.simca.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simca.attendance.Model.Admins;
import com.simca.attendance.Prevalent.PrevalentAdmin;

import io.paperdb.Paper;

public class MainActivity extends AppCompatActivity {

    Button adminLogin,userLogin;
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Paper.init(this);
        loadingBar=new ProgressDialog(this);

        adminLogin = findViewById(R.id.main_admin_login_btn);
        userLogin = findViewById(R.id.main_user_login_btn);

        adminLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, com.simca.attendance.adminLogin.class));
            }
        });

        userLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), com.simca.attendance.userLogin.class));
            }
        });

    }

}