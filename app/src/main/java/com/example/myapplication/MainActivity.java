package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnViewRooms;
    private Button btnBookedRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupClickListeners();
    }

    private void setupViews() {
        btnViewRooms = findViewById(R.id.btnSelectRoom);
        btnBookedRooms = findViewById(R.id.btnBookedRooms);
    }

    private void setupClickListeners() {
        btnViewRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RoomManagementActivity.class);
                startActivity(intent);
            }
        });

        btnBookedRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, BookedRoomsActivity.class);
                startActivity(intent);
            }
        });
    }
}