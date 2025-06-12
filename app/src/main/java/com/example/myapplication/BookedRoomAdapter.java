package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

public class BookedRoomAdapter extends ArrayAdapter<Food> {
    private Context context;
    private List<Food> bookedRooms;
    private OnRoomActionListener listener;

    public interface OnRoomActionListener {
        void onEditNote(Food room);
        void onDeleteRoom(Food room);
    }

    public BookedRoomAdapter(Context context, List<Food> bookedRooms, OnRoomActionListener listener) {
        super(context, R.layout.list_item_booked_room, bookedRooms);
        this.context = context;
        this.bookedRooms = bookedRooms;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_booked_room, parent, false);
        }

        Food room = bookedRooms.get(position);
        if (room == null) return convertView;

        ImageView ivRoom = convertView.findViewById(R.id.ivRoom);
        TextView tvRoomName = convertView.findViewById(R.id.tvRoomName);
        TextView tvRoomDescription = convertView.findViewById(R.id.tvRoomDescription);
        TextView tvRoomPrice = convertView.findViewById(R.id.tvRoomPrice);
        TextView tvRoomNote = convertView.findViewById(R.id.tvRoomNote);
        View btnEditNote = convertView.findViewById(R.id.btnEditNote);
        View btnDeleteRoom = convertView.findViewById(R.id.btnDeleteRoom);

        if (ivRoom != null) ivRoom.setImageResource(room.getImageResourceId());
        if (tvRoomName != null) tvRoomName.setText(room.getName());
        if (tvRoomDescription != null) tvRoomDescription.setText(room.getDescription());
        if (tvRoomPrice != null) tvRoomPrice.setText(String.format("Giá: %d VNĐ", room.getPrice()));
        if (tvRoomNote != null) tvRoomNote.setText(room.getNote().isEmpty() ? "Không có ghi chú" : room.getNote());

        if (btnEditNote != null) {
            btnEditNote.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onEditNote(room);
                }
            });
        }

        if (btnDeleteRoom != null) {
            btnDeleteRoom.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDeleteRoom(room);
                }
            });
        }

        return convertView;
    }
} 