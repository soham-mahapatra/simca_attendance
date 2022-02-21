package com.simca.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class userSignup extends AppCompatActivity {

    EditText fullName,email,phone,password,rollNo;
    Button btnSubmit;

    ProgressDialog loadingBar;
    AwesomeValidation awesomeValidation;
    private String division="";

    FirebaseAuth fAuth;
    FirebaseFirestore fStore = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_signup);

        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);
        loadingBar=new ProgressDialog(userSignup.this);

        fAuth = FirebaseAuth.getInstance();

        fullName = findViewById(R.id.signup_name);
        email = findViewById(R.id.signup_email);
        phone = findViewById(R.id.signup_phone);
        password = findViewById(R.id.signup_passwd);
        rollNo = findViewById(R.id.signup_rollno);
        btnSubmit = findViewById(R.id.signup_submit_btn);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccount();
            }
        });

        Spinner divSpin = findViewById(R.id.signup_div_spin);
        ArrayAdapter<String> adapter=new ArrayAdapter<String>(getApplicationContext(),
                R.layout.selected_item,getResources()
                .getStringArray(R.array.division));
        adapter.setDropDownViewResource(R.layout.drop_down_items);
        divSpin.setAdapter(adapter);

        divSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!divSpin.getItemAtPosition(i).toString().equals("Select division"))
                {
                    division = divSpin.getItemAtPosition(i).toString();
                }
                else
                {
                    division = "dd";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void createAccount() {

        String user_full_Name=fullName.getText().toString();
        String user_phone = phone.getText().toString();
        String user_email = email.getText().toString();
        String user_password = password.getText().toString();
        String user_roll = rollNo.getText().toString();

        awesomeValidation.addValidation(userSignup.this, R.id.signup_name, RegexTemplate.NOT_EMPTY,R.string.invalid_name);
        awesomeValidation.addValidation(userSignup.this, R.id.signup_phone, "[5-9]{1}[0-9]{9}",R.string.invalid_mobile);
        awesomeValidation.addValidation(userSignup.this, R.id.signup_email, Patterns.EMAIL_ADDRESS,R.string.invalid_email);
        awesomeValidation.addValidation(userSignup.this, R.id.signup_rollno, RegexTemplate.NOT_EMPTY,R.string.invalid_roll);
        awesomeValidation.addValidation(userSignup.this, R.id.signup_passwd, ".{6,}",R.string.invalid_password);
        if(awesomeValidation.validate()) {

                if (!division.equals("dd") ) {
                    loadingBar.setTitle("Create Account");
                    loadingBar.setMessage("Please Wait While We Confirm All Details");
                    loadingBar.setCanceledOnTouchOutside(false);
                    loadingBar.show();

                    ValidateUser(user_full_Name,user_phone,user_email,user_password,user_roll);

                } else {
                    Toast.makeText(userSignup.this, "Please select division", Toast.LENGTH_SHORT).show();
                }
        }else {
            Toast.makeText(userSignup.this,"Validation Failed",Toast.LENGTH_SHORT).show();
        }
    }

    private void ValidateUser(String user_full_name, String user_phone, String user_email, String user_password, String user_roll) {

        final DatabaseReference rootRef;
        rootRef = FirebaseDatabase.getInstance().getReference().child("users");

        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if (!(snapshot.child(user_roll).exists())) {

                    rootRef.orderByChild("phone").equalTo(user_phone).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.getValue()!=null)
                            {
                                Toast.makeText(userSignup.this, "This Phone Number Already Exists", Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                            }else {

                                fAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            Calendar calendar = Calendar.getInstance();
                                            SimpleDateFormat currDate = new SimpleDateFormat("dd-MMM-yyyy");
                                            SimpleDateFormat currTime = new SimpleDateFormat("HH:mm:ss");
                                            String saveCurrentDate = currDate.format(calendar.getTime());
                                            String saveCurrentTime = currTime.format(calendar.getTime());
                                            String expDt = saveCurrentDate;

                                            HashMap<String, Object> userdataMap = new HashMap<>();

                                            userdataMap.put("phone", user_phone);
                                            userdataMap.put("email", user_email);
                                            userdataMap.put("name", user_full_name+" ");
                                            userdataMap.put("password", user_password);
                                            userdataMap.put("roll_no", user_roll);
                                            userdataMap.put("div", division);
                                            userdataMap.put("Date", saveCurrentDate);
                                            userdataMap.put("Time", saveCurrentTime);

                                            rootRef.child(user_roll).updateChildren(userdataMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(userSignup.this, "Congratulations, Your Account Has Been Created ! Login to access your account.", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();
                                                        startActivity(new Intent(userSignup.this,userLogin.class));

                                                    } else {
                                                        Toast.makeText(userSignup.this, "Error! Please Try Again", Toast.LENGTH_SHORT).show();
                                                        loadingBar.dismiss();

                                                    }
                                                }
                                            });
                                        }
                                        else{
                                            if(task.getException().getMessage().equals("The email address is already in use by another account."))
                                            {
                                                loadingBar.dismiss();
                                                Toast.makeText(userSignup.this, "Email already exist", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                });
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });

                } else {

                    Toast.makeText(userSignup.this, "This roll no. already exists", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}