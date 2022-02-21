package com.simca.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simca.attendance.Prevalent.Prevalent;

import java.util.HashMap;

public class editProfile extends AppCompatActivity {

    EditText email,name;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_profile);

        email = findViewById(R.id.edit_email);
        name = findViewById(R.id.edit_name);
        Button updateBtn = findViewById(R.id.edit_submit_btn);

        userInfoDisplay(email,name);

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDetails();
            }
        });
    }

    private void userInfoDisplay(EditText u_email, EditText u_name) {

        DatabaseReference usersRef= FirebaseDatabase.getInstance().getReference().child("users").child(Prevalent.currentOnlineUser.getRoll_no());
        usersRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                {

                    String user_name = snapshot.child("name").getValue().toString();
                    String user_email = snapshot.child("email").getValue().toString();

                    u_name.setText(user_name);
                    u_email.setText(user_email);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void updateDetails() {

        DatabaseReference ref= FirebaseDatabase.getInstance().getReference().child("users");
        if(TextUtils.isEmpty(name.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(email.getText().toString()))
        {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_SHORT).show();
        }
        else {
            HashMap<String, Object> userMap = new HashMap<>();

            userMap.put("name", name.getText().toString());
            userMap.put("email", email.getText().toString());

            ref.child(Prevalent.currentOnlineUser.getRoll_no()).updateChildren(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(editProfile.this, Home.class));
                        Toast.makeText(editProfile.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }
}