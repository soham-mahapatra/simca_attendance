package com.simca.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.util.ArrayList;
import java.util.List;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

public class qrGenerate extends AppCompatActivity {

    private String subject="";
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference();
    List<String> names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.qr_generate);

        Spinner subjSpin = findViewById(R.id.subsSpin);
        ImageView qrImage = findViewById(R.id.qr_image);
        TextView textQR = findViewById(R.id.qrText);
        Button qrGenBtn = findViewById(R.id.generateQRBtn);

        names = new ArrayList<>();

        dbRef.child("subjects").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot childSnap:snapshot.getChildren()) {
                    String spinnerName = childSnap.child("name").getValue(String.class);
                    names.add(spinnerName);
                }
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(qrGenerate.this, R.layout.selected_item,names);
                arrayAdapter.setDropDownViewResource( R.layout.drop_down_items);
                subjSpin.setAdapter(arrayAdapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        subjSpin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (!subjSpin.getItemAtPosition(i).toString().equals("Select subject"))
                {
                    subject = subjSpin.getItemAtPosition(i).toString();
                }
                else
                {
                    subject = "ss";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        qrGenBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if (!subject.equals("ss"))
                {
                    qrImage.setVisibility(View.VISIBLE);
                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        BitMatrix matrix = writer.encode(subject, BarcodeFormat.QR_CODE, 350, 350);
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        qrImage.setImageBitmap(bitmap);
                        textQR.setText("Take attendance for: " + subject);
                    } catch (WriterException e) {
                        e.printStackTrace();
                    }
                }
               else{
                   Toast.makeText(getApplicationContext(), "Please choose a subject", Toast.LENGTH_SHORT).show();
               }
            }
        });
    }
}