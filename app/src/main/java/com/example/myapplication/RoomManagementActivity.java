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

public class RoomManagementActivity extends AppCompatActivity implements RoomAdapter.OnRoomActionListener {
    private ListView lvRooms;
    private Button btnBookRoom;
    private RoomAdapter adapter;
    private FoodRepository repository;
    private Food selectedRoom;
    private FloatingActionButton fabAddRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room_management);

        repository = new FoodRepository(getApplication());
        lvRooms = findViewById(R.id.lvRooms);
        btnBookRoom = findViewById(R.id.btnBookRoom);
        fabAddRoom = findViewById(R.id.fabAddRoom);
        btnBookRoom.setEnabled(false);

        setupRoomList();
        setupClickListeners();
    }

    private void setupRoomList() {
        // Load data with multi-threading
        repository.loadAllFood(new FoodRepository.DataCallback<List<Food>>() {
            @Override
            public void onResult(List<Food> roomList) {
                runOnUiThread(() -> {
                    adapter = new RoomAdapter(RoomManagementActivity.this, roomList, RoomManagementActivity.this);
                    lvRooms.setAdapter(adapter);
                });
            }
        });
    }

    private void setupClickListeners() {
        btnBookRoom.setOnClickListener(v -> {
            if (selectedRoom != null) {
                showBookingDialog();
            }
        });

        fabAddRoom.setOnClickListener(v -> showAddRoomDialog());
    }

    private void showBookingDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Đặt phòng - " + selectedRoom.getName());

        View dialogView = getLayoutInflater().inflate(R.layout.dialog_booking, null);
        EditText etGuestName = dialogView.findViewById(R.id.etGuestName);
        EditText etPhoneNumber = dialogView.findViewById(R.id.etPhoneNumber);
        EditText etCheckInDate = dialogView.findViewById(R.id.etCheckInDate);
        EditText etCheckOutDate = dialogView.findViewById(R.id.etCheckOutDate);
        EditText etSpecialRequest = dialogView.findViewById(R.id.etSpecialRequest);

        builder.setView(dialogView);

        builder.setPositiveButton("Đặt phòng", (dialog, which) -> {
            String guestName = etGuestName.getText().toString().trim();
            String phoneNumber = etPhoneNumber.getText().toString().trim();
            String checkInDate = etCheckInDate.getText().toString().trim();
            String checkOutDate = etCheckOutDate.getText().toString().trim();
            String specialRequest = etSpecialRequest.getText().toString().trim();

            if (guestName.isEmpty() || phoneNumber.isEmpty() || checkInDate.isEmpty() || checkOutDate.isEmpty()) {
                Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create booking note
            String bookingNote = String.format("Khách: %s\nSĐT: %s\nCheck-in: %s\nCheck-out: %s\nYêu cầu: %s", 
                guestName, phoneNumber, checkInDate, checkOutDate, specialRequest);

            // Book the room with multi-threading
            repository.updateBookingStatus(selectedRoom.getId(), true, bookingNote, 
                new FoodRepository.DataCallback<Void>() {
                    @Override
                    public void onResult(Void result) {
                        runOnUiThread(() -> {
                            Toast.makeText(RoomManagementActivity.this, "Đã đặt phòng thành công", Toast.LENGTH_SHORT).show();
                            setupRoomList(); // Refresh list
                        });
                    }
                });
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
        builder.show();
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
                            setupRoomList();
                            Toast.makeText(RoomManagementActivity.this, "Đã thêm phòng mới", Toast.LENGTH_SHORT).show();
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
    public void onSelectRoom(Food room, int position) {
        selectedRoom = room;
        btnBookRoom.setEnabled(true);
        adapter.setSelectedPosition(position);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupRoomList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (repository != null) {
            repository.shutdown();
        }
    }
} 