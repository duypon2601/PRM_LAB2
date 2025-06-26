package com.example.myapplication;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.List;

public class FoodActivity extends AppCompatActivity implements FoodAdapter.OnRoomActionListener {
    private ListView lvFood;
    private Button btnOrderFood;
    private FoodAdapter adapter;
    private FoodRepository repository;
    private Food selectedFood;
    private FloatingActionButton fabAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        repository = new FoodRepository(getApplication());
        lvFood = findViewById(R.id.lvFood);
        btnOrderFood = findViewById(R.id.btnOrderFood);
        fabAddRoom = findViewById(R.id.fabAddRoom);
        btnOrderFood.setEnabled(false);

        setupFoodList();
        setupClickListeners();
    }

    private void setupFoodList() {
        // Load data with multi-threading
        repository.loadAllFood(new FoodRepository.DataCallback<List<Food>>() {
            @Override
            public void onResult(List<Food> foodList) {
                runOnUiThread(() -> {
                    adapter = new FoodAdapter(FoodActivity.this, foodList, FoodActivity.this);
                    lvFood.setAdapter(adapter);
                });
            }
        });
    }

    private void setupClickListeners() {
        btnOrderFood.setOnClickListener(v -> {
            if (selectedFood != null) {
                // Book the room with multi-threading
                repository.updateBookingStatus(selectedFood.getId(), true, selectedFood.getNote(), 
                    new FoodRepository.DataCallback<Void>() {
                        @Override
                        public void onResult(Void result) {
                            runOnUiThread(() -> {
                                Toast.makeText(FoodActivity.this, "Đã đặt phòng thành công", Toast.LENGTH_SHORT).show();
                                
                                // Return to previous screen
                                Intent resultIntent = new Intent();
                                resultIntent.putExtra("selected_food", selectedFood);
                                setResult(RESULT_OK, resultIntent);
                                finish();
                            });
                        }
                    });
            }
        });

        fabAddRoom.setOnClickListener(v -> showAddRoomDialog());
    }

    private void showAddRoomDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Thêm phòng mới");

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_add_edit_item, null);
        EditText etName = dialogView.findViewById(R.id.etName);
        EditText etDescription = dialogView.findViewById(R.id.etDescription);
        EditText etPrice = dialogView.findViewById(R.id.etPrice);

        builder.setView(dialogView);

        builder.setPositiveButton("Thêm", (dialog, which) -> {
            String name = etName.getText().toString().trim();
            String description = etDescription.getText().toString().trim();
            String priceStr = etPrice.getText().toString().trim();

            if (name.isEmpty() || description.isEmpty() || priceStr.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                int price = Integer.parseInt(priceStr);
                Food newRoom = new Food();
                newRoom.setName(name);
                newRoom.setDescription(description);
                newRoom.setPrice(price);
                newRoom.setImageResource(R.drawable.phong1); // Default image

                // Add room with multi-threading
                repository.addFood(newRoom, new FoodRepository.DataCallback<Long>() {
                    @Override
                    public void onResult(Long result) {
                        runOnUiThread(() -> {
                            setupFoodList();
                            Toast.makeText(FoodActivity.this, "Đã thêm phòng mới", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
            } catch (NumberFormatException e) {
                Toast.makeText(this, "Giá phòng không hợp lệ", Toast.LENGTH_SHORT).show();
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    @Override
    public void onDeleteRoom(Food room) {
        new AlertDialog.Builder(this)
            .setTitle("Xác nhận xóa")
            .setMessage("Bạn có chắc muốn xóa phòng này?")
            .setPositiveButton("Có", (dialog, which) -> {
                // Delete with multi-threading
                repository.deleteFood(room, new FoodRepository.DataCallback<Void>() {
                    @Override
                    public void onResult(Void result) {
                        runOnUiThread(() -> {
                            setupFoodList();
                            Toast.makeText(FoodActivity.this, "Đã xóa phòng", Toast.LENGTH_SHORT).show();
                        });
                    }
                });
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (repository != null) {
            repository.shutdown();
        }
    }
}