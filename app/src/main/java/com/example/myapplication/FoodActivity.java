package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class FoodActivity extends AppCompatActivity implements FoodAdapter.OnRoomActionListener {
    private ListView lvFood;
    private Button btnOrderFood;
    private FoodAdapter adapter;
    private DatabaseHelper dbHelper;
    private Food selectedFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        dbHelper = new DatabaseHelper(this);
        lvFood = findViewById(R.id.lvFood);
        btnOrderFood = findViewById(R.id.btnOrderFood);
        btnOrderFood.setEnabled(false);

        setupFoodList();
        setupClickListeners();
    }

    private void setupFoodList() {
        List<Food> foodList = dbHelper.getAllFood();
        adapter = new FoodAdapter(this, foodList, this);
        lvFood.setAdapter(adapter);
    }

    private void setupClickListeners() {
        btnOrderFood.setOnClickListener(v -> {
            if (selectedFood != null) {
                // Book the room
                dbHelper.bookRoom(selectedFood);
                
                // Show confirmation
                Toast.makeText(this, "Đã đặt phòng thành công", Toast.LENGTH_SHORT).show();
                
                // Return to previous screen
                Intent resultIntent = new Intent();
                resultIntent.putExtra("selected_food", selectedFood);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
    }

    @Override
    public void onDeleteRoom(Food room) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa phòng này?")
            .setPositiveButton("Có", (dialog, which) -> {
                dbHelper.deleteFood(room.getId());
                setupFoodList();
                Toast.makeText(this, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
            })
            .setNegativeButton("Không", null)
            .show();
    }

    @Override
    public void onSelectRoom(Food room, int position) {
        selectedFood = room;
        btnOrderFood.setEnabled(true);
        adapter.setSelectedPosition(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupFoodList();
    }
}