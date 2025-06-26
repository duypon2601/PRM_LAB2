package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Button;

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
        Button btnEditNote = convertView.findViewById(R.id.btnEditNote);
        Button btnDeleteRoom = convertView.findViewById(R.id.btnDeleteRoom);

        if (ivRoom != null) ivRoom.setImageResource(room.getImageResourceId());
        if (tvRoomName != null) tvRoomName.setText(room.getName());
        if (tvRoomDescription != null) tvRoomDescription.setText(room.getDescription());
        if (tvRoomPrice != null) tvRoomPrice.setText(String.format("%,d VNĐ/đêm", room.getPrice()));
        
        // Format booking note display
        if (tvRoomNote != null) {
            String note = room.getNote();
            if (note.isEmpty()) {
                tvRoomNote.setText("Chưa có thông tin đặt phòng");
                tvRoomNote.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            } else {
                tvRoomNote.setText(note);
                tvRoomNote.setTextColor(context.getResources().getColor(R.color.sea_green));
            }
        }

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