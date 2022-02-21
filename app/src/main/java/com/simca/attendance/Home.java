package com.simca.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.simca.attendance.Model.Code;
import com.simca.attendance.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import io.paperdb.Paper;

public class Home extends AppCompatActivity {

    Button scanBtn,codeBtn;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        scanBtn = findViewById(R.id.home_qr_scan);
        codeBtn = findViewById(R.id.home_code);

        loadingBar = new ProgressDialog(this);

        Toolbar toolbar = findViewById(R.id.toolbar_user_home);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Simca Attendance");

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),scannerView.class));
            }
        });

        codeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                EditText txtCode = new EditText(Home.this);
                txtCode.setHint("eg. 1234");

                new AlertDialog.Builder(Home.this).setTitle("Enter code for attendance")
                        .setView(txtCode).setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (txtCode.getText().toString().isEmpty())
                        {
                            Toast.makeText(Home.this, "Please enter code", Toast.LENGTH_SHORT).show();
                        }
                        else{
                            String codeGet = txtCode.getText().toString();
                            checkCodeInDb(codeGet);
                            loadingBar.setTitle("Checking code");
                            loadingBar.setMessage("Please Wait.....");
                            loadingBar.setCanceledOnTouchOutside(false);
                            loadingBar.show();
                        }
                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId())
        {
            case R.id.viewProf:

                Dialog profDialog = new Dialog(Home.this);
                profDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                profDialog.setContentView(R.layout.profile_details);
                profDialog.setCancelable(true);
                profDialog.setCanceledOnTouchOutside(true);

                TextView name,email,phone,div,roll;

                name = profDialog.findViewById(R.id.view_user_name);
                email = profDialog.findViewById(R.id.view_user_email);
                phone = profDialog.findViewById(R.id.view_user_phone);
                div = profDialog.findViewById(R.id.view_user_div);
                roll = profDialog.findViewById(R.id.view_user_roll);

                name.setText("Name: " + Prevalent.currentOnlineUser.getName());
                email.setText("Email: " + Prevalent.currentOnlineUser.getEmail());
                phone.setText("Phone: " + Prevalent.currentOnlineUser.getPhone());
                div.setText("Div: " + Prevalent.currentOnlineUser.getDiv());
                roll.setText("Roll No.: " + Prevalent.currentOnlineUser.getRoll_no());

                profDialog.show();
                return true;

            case R.id.logout:

                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Logout")
                        .setMessage("Are you sure you want to logout ?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Paper.book().destroy();
                                Intent intent= new Intent(Home.this,MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                Toast.makeText(Home.this, "Successfully Logged Out !", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
                return true;

            case R.id.editProf:
                startActivity(new Intent(Home.this,editProfile.class));
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    boolean doubleBackToExitPressedOnce = false;
    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    private void checkCodeInDb(String codeCheck) {
        dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("code").child(codeCheck).exists())
                {
                    Code codeData = snapshot.child("code").child(codeCheck).getValue(Code.class);

                    Calendar calendar = Calendar.getInstance();
                    SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
                    SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
                    String saveCurrentDate = currentDate.format(calendar.getTime());
                    String saveCurrentTime = currentTime.format(calendar.getTime());

                    String user_roll = Prevalent.currentOnlineUser.getRoll_no();
                    String user_name = Prevalent.currentOnlineUser.getName();
                    String user_stream = Prevalent.currentOnlineUser.getStream();
                    String user_div = Prevalent.currentOnlineUser.getDiv();

                    HashMap<String,Object> atnMap = new HashMap<>();

                    atnMap.put("subject",codeData.getSubject());
                    atnMap.put("roll_no", user_roll);
                    atnMap.put("name", user_name);
                    atnMap.put("stream", user_stream);
                    atnMap.put("div", user_div);
                    atnMap.put("timespan", saveCurrentDate +" "+ saveCurrentTime);

                    dbRef.child("attendance").child(saveCurrentDate).child(codeData.getSubject()).child(user_roll).updateChildren(atnMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    loadingBar.dismiss();
                                    Intent intent = new Intent(Home.this, successSubmit.class);
                                    intent.putExtra("subject",codeData.getSubject());
                                    startActivity(intent);
                                }
                            });

                }
                else{
                    loadingBar.dismiss();
                    Toast.makeText(Home.this, "Incorrect code !!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}