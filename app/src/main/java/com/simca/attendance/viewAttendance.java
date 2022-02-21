package com.simca.attendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class viewAttendance extends AppCompatActivity {

    private DatabaseReference attendanceRef,dateRef;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_attendance);

        recyclerView = findViewById(R.id.recycler_menu_ViewAten);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        String subjects = getIntent().getStringExtra("subject");



        dateRef = FirebaseDatabase.getInstance().getReference();
        dateRef.child("fetch").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnap : snapshot.getChildren()) {

                    String fetchedDate = childSnap.getValue(String.class);
                    attendanceRef = FirebaseDatabase.getInstance().getReference().child("attendance").child(fetchedDate).child(subjects);
                    FirebaseRecyclerOptions<Attendance> options = new FirebaseRecyclerOptions
                            .Builder<Attendance>().setQuery(attendanceRef, Attendance.class).build();
                    FirebaseRecyclerAdapter<Attendance, AttenHolder> adapter = new FirebaseRecyclerAdapter<Attendance, AttenHolder>(options) {
                        @Override
                        protected void onBindViewHolder(@NonNull AttenHolder holder, int position, @NonNull Attendance model) {

                            holder.rollNo.setText(model.getRoll_no() + " - " + model.getName());

                        }

                        @NonNull
                        @Override
                        public AttenHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.attended_layout, parent, false);
                            AttenHolder holder = new AttenHolder(v);
                            return holder;
                        }
                    };
                    recyclerView.setAdapter(adapter);
                    adapter.startListening();

                    Toolbar toolbar = findViewById(R.id.toolbar_view_atn);
                    setSupportActionBar(toolbar);
                    getSupportActionBar().setTitle("Attendance for " + subjects + " (" + fetchedDate + ")");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}