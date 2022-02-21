package com.simca.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class userLogin extends AppCompatActivity {

    EditText roll, pass;
    Button userLoginBtn;
    CheckBox rememberUser;
    TextView gotoSignup;

    ProgressDialog loadingBar;
    AwesomeValidation awesomeValidation;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_login);

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        roll = findViewById(R.id.user_login_roll);
        pass = findViewById(R.id.user_login_pass);
        userLoginBtn = findViewById(R.id.user_login_btn);
        rememberUser = findViewById(R.id.chk_box_user);
        gotoSignup = findViewById(R.id.login_goto_signup);

        gotoSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(userLogin.this,userSignup.class));
            }
        });

        Paper.init(this);

        loadingBar = new ProgressDialog(this);

        userLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginUser();
            }
        });

        String UserPhoneKey = Paper.book().read(Prevalent.UserNameKey);
        String UserPasswordKey = Paper.book().read(Prevalent.UserPasswordKey);

        if (UserPhoneKey != "" && UserPasswordKey != "") {
            if (!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)) {

                AllowAccessUser(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please Wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }

    }

    private void loginUser() {

        String uName = roll.getText().toString();
        String password = pass.getText().toString();

        awesomeValidation.addValidation(userLogin.this, R.id.user_login_roll, RegexTemplate.NOT_EMPTY, R.string.empty_username);
        awesomeValidation.addValidation(userLogin.this, R.id.user_login_pass, RegexTemplate.NOT_EMPTY, R.string.empty_password);

        if (awesomeValidation.validate()) {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait While We Check Your Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowUserToAccount(uName, password);
        } else {
            Toast.makeText(getApplicationContext(), "Error! Please Enter Username and Password", Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }

    private void AllowUserToAccount(String username, String password) {

        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("users").child(username).exists()) {

                    Users userData = snapshot.child("users").child(username).getValue(Users.class);

                    if (userData.getRoll_no().equals(username)) {
                        if (userData.getPassword().equals(password)) {

                            Toast.makeText(getApplicationContext(), "Welcome " + userData.getName(), Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(getApplicationContext(), Home.class);
                            Prevalent.currentOnlineUser = userData;
                            startActivity(intent);
                            if (rememberUser.isChecked()) {
                                Paper.book().write(Prevalent.UserNameKey, username);
                                Paper.book().write(Prevalent.UserPasswordKey, password);

                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "Password Is Incorrect!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Account Does Not Exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void AllowAccessUser(String username, String password) {


        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference();

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("users").child(username).exists()) {

                    Users userData = snapshot.child("users").child(username).getValue(Users.class);

                    if (userData.getRoll_no().equals(username)) {
                        if (userData.getPassword().equals(password)) {
                            Toast.makeText(userLogin.this, "Welcome Back " + userData.getName(), Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent = new Intent(userLogin.this, Home.class);
                            Prevalent.currentOnlineUser = userData;
                            startActivity(intent);
                        } else {
                            Toast.makeText(userLogin.this, "Password Is Incorrect!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                } else {
                    Toast.makeText(userLogin.this, "Account Does Not Exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}