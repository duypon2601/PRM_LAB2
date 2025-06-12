package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnSelectRoom;
    private Button btnBookedRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupViews();
        setupClickListeners();
    }

    private void setupViews() {
        btnSelectRoom = findViewById(R.id.btnSelectRoom);
        btnBookedRooms = findViewById(R.id.btnBookedRooms);
    }

    private void setupClickListeners() {
        btnSelectRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, FoodActivity.class);
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