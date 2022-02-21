package com.simca.attendance;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.simca.attendance.Prevalent.Prevalent;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class scannerView extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);

        Dexter.withContext(getApplicationContext())
                .withPermissions(Manifest.permission.CAMERA)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                        if (multiplePermissionsReport.areAllPermissionsGranted())
                        {
                            scannerView.startCamera();
                        }
                        else
                        {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();


    }

    private void showSettingsDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(scannerView.this);

        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivityForResult(intent, 101);
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                onBackPressed();
            }
        });
        builder.show();
    }

    @Override
    public void handleResult(Result rawResult) {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat currentDate = new SimpleDateFormat("dd-MMM-yyyy");
        SimpleDateFormat currentTime = new SimpleDateFormat("HH:mm:ss");
        String saveCurrentDate = currentDate.format(calendar.getTime());
        String saveCurrentTime = currentTime.format(calendar.getTime());

        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("attendance");

        String user_roll = Prevalent.currentOnlineUser.getRoll_no();
        String user_name = Prevalent.currentOnlineUser.getName();
        String user_stream = Prevalent.currentOnlineUser.getStream();
        String user_div = Prevalent.currentOnlineUser.getDiv();

        HashMap<String,Object> atnMap = new HashMap<>();

        atnMap.put("subject",rawResult.getText());
        atnMap.put("roll_no", user_roll);
        atnMap.put("name", user_name);
        atnMap.put("stream", user_stream);
        atnMap.put("div", user_div);
        atnMap.put("timespan", saveCurrentDate +" "+ saveCurrentTime);

        dbRef.child(saveCurrentDate).child(rawResult.getText()).child(user_roll).updateChildren(atnMap);

        Intent intent = new Intent(this, successSubmit.class);
        intent.putExtra("subject",rawResult.getText());
        startActivity(intent);

    }

    @Override
    protected void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }
}