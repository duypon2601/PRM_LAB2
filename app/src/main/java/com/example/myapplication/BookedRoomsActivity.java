package com.example.myapplication;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class BookedRoomsActivity extends AppCompatActivity implements BookedRoomAdapter.OnRoomActionListener {
    private static final String TAG = "BookedRoomsActivity";
    private FoodRepository repository;
    private ListView listViewBookedRooms;
    private TextView tvEmpty;
    private BookedRoomAdapter adapter;
    private List<Food> bookedRooms;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_booked_rooms);
            Log.d(TAG, "onCreate: Setting up views");

            repository = new FoodRepository(getApplication());
            listViewBookedRooms = findViewById(R.id.listViewBookedRooms);
            tvEmpty = findViewById(R.id.tvEmpty);
            bookedRooms = new ArrayList<>();

            if (listViewBookedRooms == null) {
                Log.e(TAG, "onCreate: listViewBookedRooms is null");
                return;
            }

            adapter = new BookedRoomAdapter(this, bookedRooms, this);
            listViewBookedRooms.setAdapter(adapter);

            setupBookedRoomsList();
        } catch (Exception e) {
            Log.e(TAG, "onCreate: Error", e);
            Toast.makeText(this, "Error initializing: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void setupBookedRoomsList() {
        try {
            Log.d(TAG, "setupBookedRoomsList: Starting");
            // Load booked rooms with multi-threading
            repository.loadBookedRooms(new FoodRepository.DataCallback<List<Food>>() {
                @Override
                public void onResult(List<Food> rooms) {
                    runOnUiThread(() -> {
                        try {
                            bookedRooms.clear();
                            if (rooms != null) {
                                bookedRooms.addAll(rooms);
                                Log.d(TAG, "setupBookedRoomsList: Added " + rooms.size() + " rooms");
                            } else {
                                Log.w(TAG, "setupBookedRoomsList: rooms list is null");
                            }
                            adapter.notifyDataSetChanged();
                            updateEmptyView();
                        } catch (Exception e) {
                            Log.e(TAG, "setupBookedRoomsList: Error", e);
                            Toast.makeText(BookedRoomsActivity.this, "Error loading rooms: " + e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
                }
            });
        } catch (Exception e) {
            Log.e(TAG, "setupBookedRoomsList: Error", e);
            Toast.makeText(this, "Error loading rooms: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void updateEmptyView() {
        try {
            if (bookedRooms.isEmpty()) {
                tvEmpty.setVisibility(View.VISIBLE);
                listViewBookedRooms.setVisibility(View.GONE);
            } else {
                tvEmpty.setVisibility(View.GONE);
                listViewBookedRooms.setVisibility(View.VISIBLE);
            }
        } catch (Exception e) {
            Log.e(TAG, "updateEmptyView: Error", e);
        }
    }

    @Override
    public void onEditNote(Food room) {
        try {
            if (room == null) {
                Log.w(TAG, "onEditNote: room is null");
                return;
            }
            
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Thêm ghi chú cho phòng");

            final EditText input = new EditText(this);
            input.setText(room.getNote());
            input.setHint("Nhập ghi chú (tên khách, số điện thoại, yêu cầu đặc biệt...)");
            builder.setView(input);

            builder.setPositiveButton("Lưu", (dialog, which) -> {
                try {
                    String newNote = input.getText().toString();
                    room.setNote(newNote);
                    // Update with multi-threading
                    repository.updateFood(room, new FoodRepository.DataCallback<Void>() {
                        @Override
                        public void onResult(Void result) {
                            runOnUiThread(() -> {
                                adapter.notifyDataSetChanged();
                                Toast.makeText(BookedRoomsActivity.this, "Đã cập nhật ghi chú", Toast.LENGTH_SHORT).show();
                            });
                        }
                    });
                } catch (Exception e) {
                    Log.e(TAG, "onEditNote: Error saving note", e);
                    Toast.makeText(this, "Error saving note: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            });

            builder.setNegativeButton("Hủy", (dialog, which) -> dialog.cancel());
            builder.show();
        } catch (Exception e) {
            Log.e(TAG, "onEditNote: Error", e);
            Toast.makeText(this, "Error editing note: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onDeleteRoom(Food room) {
        try {
            if (room == null) {
                Log.w(TAG, "onDeleteRoom: room is null");
                return;
            }

            new AlertDialog.Builder(this)
                .setTitle("Xác nhận hủy đặt phòng")
                .setMessage("Bạn có chắc muốn hủy đặt phòng này?")
                .setPositiveButton("Hủy đặt", (dialog, which) -> {
                    try {
                        // Unbook room with multi-threading
                        repository.updateBookingStatus(room.getId(), false, "", new FoodRepository.DataCallback<Void>() {
                            @Override
                            public void onResult(Void result) {
                                runOnUiThread(() -> {
                                    setupBookedRoomsList();
                                    Toast.makeText(BookedRoomsActivity.this, "Đã hủy đặt phòng", Toast.LENGTH_SHORT).show();
                                });
                            }
                        });
                    } catch (Exception e) {
                        Log.e(TAG, "onDeleteRoom: Error deleting room", e);
                        Toast.makeText(this, "Error deleting room: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .setNegativeButton("Không", null)
                .show();
        } catch (Exception e) {
            Log.e(TAG, "onDeleteRoom: Error", e);
            Toast.makeText(this, "Error showing delete dialog: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            setupBookedRoomsList();
        } catch (Exception e) {
            Log.e(TAG, "onResume: Error", e);
            Toast.makeText(this, "Error refreshing rooms: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (repository != null) {
            repository.shutdown();
        }
    }
} 