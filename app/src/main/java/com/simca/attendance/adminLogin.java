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
import android.widget.Toast;

public class adminLogin extends AppCompatActivity {

    Button loginBtnAdmin;
    EditText adminUname,adminPass;
    CheckBox rememberAdmin;

    ProgressDialog loadingBar;
    AwesomeValidation awesomeValidation;

    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.admin_login);

        awesomeValidation= new AwesomeValidation(ValidationStyle.BASIC);

        loginBtnAdmin = findViewById(R.id.login_btn_admin);
        adminUname = findViewById(R.id.login_admin_uname);
        adminPass = findViewById(R.id.login_admin_pass);
        rememberAdmin = findViewById(R.id.chk_box_admin);

        Paper.init(this);

        loadingBar=new ProgressDialog(this);

        loginBtnAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loginAdmin();
            }
        });

        String UserPhoneKey= Paper.book().read(PrevalentAdmin.UserNameKey);
        String UserPasswordKey= Paper.book().read(PrevalentAdmin.UserPasswordKey);

        if(UserPhoneKey != "" && UserPasswordKey != ""){
            if(!TextUtils.isEmpty(UserPhoneKey) && !TextUtils.isEmpty(UserPasswordKey)){

                AllowAccessAdmin(UserPhoneKey,UserPasswordKey);

                loadingBar.setTitle("Already Logged In");
                loadingBar.setMessage("Please Wait.....");
                loadingBar.setCanceledOnTouchOutside(false);
                loadingBar.show();

            }
        }
    }

    private void loginAdmin() {

        String uName = adminUname.getText().toString();
        String password = adminPass.getText().toString();

        awesomeValidation.addValidation(adminLogin.this, R.id.login_admin_uname, RegexTemplate.NOT_EMPTY,R.string.empty_username);
        awesomeValidation.addValidation(adminLogin.this, R.id.login_admin_pass, RegexTemplate.NOT_EMPTY,R.string.empty_password);

        if(awesomeValidation.validate())
        {
            loadingBar.setTitle("Login Account");
            loadingBar.setMessage("Please Wait While We Check Your Credentials");
            loadingBar.setCanceledOnTouchOutside(false);
            loadingBar.show();

            AllowAdminToAccount(uName, password);
        }else{
            Toast.makeText(getApplicationContext(),"Error! Please Enter Username and Password",Toast.LENGTH_SHORT).show();
            loadingBar.dismiss();
        }
    }

    private void AllowAdminToAccount(String username, String password) {


        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("admin").child(username).exists()) {

                    Admins adminData = snapshot.child("admin").child(username).getValue(Admins.class);

                    if (adminData.getUsername().equals(username)) {
                        if (adminData.getPassword().equals(password)) {

                                Toast.makeText(getApplicationContext(), "Welcome "+adminData.getUsername(), Toast.LENGTH_SHORT).show();
                                loadingBar.dismiss();
                                Intent intent = new Intent(getApplicationContext(), adminHome.class);
                                PrevalentAdmin.currentOnlineAdmin=adminData;
                                startActivity(intent);
                                if(rememberAdmin.isChecked())
                                {
                                    Paper.book().write(PrevalentAdmin.UserNameKey,username);
                                    Paper.book().write(PrevalentAdmin.UserPasswordKey,password);

                                }
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "Password Is Incorrect!", Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Account Does Not Exist", Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    private void AllowAccessAdmin(String username, String password) {

        final DatabaseReference rootRef;
        rootRef= FirebaseDatabase.getInstance().getReference();


        rootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.child("admin").child(username).exists()){

                    Admins adminData =snapshot.child("admin").child(username).getValue(Admins.class);

                    if(adminData.getUsername().equals(username)){
                        if(adminData.getPassword().equals(password)){
                            Toast.makeText(adminLogin.this,"Welcome Back "+ adminData.getName(),Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                            Intent intent= new Intent(adminLogin.this,adminHome.class);
                            PrevalentAdmin.currentOnlineAdmin=adminData;
                            startActivity(intent);
                        }else{
                            Toast.makeText(adminLogin.this,"Password Is Incorrect!",Toast.LENGTH_SHORT).show();
                            loadingBar.dismiss();
                        }
                    }

                }else{
                    Toast.makeText(adminLogin.this,"Account Does Not Exist",Toast.LENGTH_SHORT).show();
                    loadingBar.dismiss();
//                    Toast.makeText(MainActivity.this,"Please Register A New Account",Toast.LENGTH_SHORT).show();
//                    Intent intent= new Intent(MainActivity.this,MainActivity.class);
//                    startActivity(intent);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}