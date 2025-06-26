package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.graphics.Color;

import java.util.List;

public class RoomAdapter extends ArrayAdapter<Food> {
    private List<Food> roomList;
    private Context context;
    private OnRoomActionListener listener;
    private int selectedPosition = -1;

    public interface OnRoomActionListener {
        void onSelectRoom(Food room, int position);
    }

    public RoomAdapter(Context context, List<Food> roomList, OnRoomActionListener listener) {
        super(context, R.layout.list_item_room, roomList);
        this.context = context;
        this.roomList = roomList;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_room, parent, false);
        }

        Food room = roomList.get(position);

        ImageView roomImage = convertView.findViewById(R.id.roomImage);
        TextView roomName = convertView.findViewById(R.id.roomName);
        TextView roomDescription = convertView.findViewById(R.id.roomDescription);
        TextView roomPrice = convertView.findViewById(R.id.roomPrice);
        TextView roomStatus = convertView.findViewById(R.id.roomStatus);
        Button btnSelect = convertView.findViewById(R.id.btnSelect);

        roomImage.setImageResource(room.getImageResourceId());
        roomName.setText(room.getName());
        roomDescription.setText(room.getDescription());
        roomPrice.setText(String.format("%,d VNĐ/đêm", room.getPrice()));

        // Set background color based on selection
        convertView.setBackgroundResource(position == selectedPosition ? 
            R.drawable.list_item_background_selected : R.drawable.list_item_background);

        // Show/hide elements based on booking status
        if (room.isBooked()) {
            roomStatus.setVisibility(View.VISIBLE);
            roomStatus.setText("ĐÃ ĐẶT");
            roomStatus.setTextColor(Color.RED);
            btnSelect.setText("Đã đặt");
            btnSelect.setEnabled(false);
            btnSelect.setBackgroundColor(Color.GRAY);
        } else {
            roomStatus.setVisibility(View.GONE);
            btnSelect.setText("Chọn");
            btnSelect.setEnabled(true);
            btnSelect.setBackgroundColor(context.getResources().getColor(R.color.sea_green));
        }

        btnSelect.setOnClickListener(v -> {
            if (listener != null && !room.isBooked()) {
                selectedPosition = position;
                listener.onSelectRoom(room, position);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }

    public void setSelectedPosition(int position) {
        this.selectedPosition = position;
        notifyDataSetChanged();
    }

    public int getSelectedPosition() {
        return selectedPosition;
    }
} 