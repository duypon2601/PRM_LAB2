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

public class FoodAdapter extends ArrayAdapter<Food> {
    private List<Food> foodList;
    private Context context;
    private OnRoomActionListener listener;
    private int selectedPosition = -1;

    public interface OnRoomActionListener {
        void onDeleteRoom(Food room);
        void onSelectRoom(Food room, int position);
    }

    public FoodAdapter(Context context, List<Food> foodList, OnRoomActionListener listener) {
        super(context, R.layout.list_item_food, foodList);
        this.context = context;
        this.foodList = foodList;
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.list_item_food, parent, false);
        }

        Food food = foodList.get(position);

        ImageView foodImage = convertView.findViewById(R.id.foodImage);
        TextView foodName = convertView.findViewById(R.id.foodName);
        TextView foodDescription = convertView.findViewById(R.id.foodDescription);
        TextView foodPrice = convertView.findViewById(R.id.foodPrice);
        Button btnDelete = convertView.findViewById(R.id.btnDelete);
        Button btnSelect = convertView.findViewById(R.id.btnSelect);

        foodImage.setImageResource(food.getImageResourceId());
        foodName.setText(food.getName());
        foodDescription.setText(food.getDescription());
        foodPrice.setText(String.format("%d VNĐ", food.getPrice()));

        // Set background color based on selection
        convertView.setBackgroundResource(position == selectedPosition ? 
            R.drawable.list_item_background_selected : R.drawable.list_item_background);

        btnDelete.setOnClickListener(v -> {
            if (listener != null) {
                listener.onDeleteRoom(food);
            }
        });

        btnSelect.setOnClickListener(v -> {
            if (listener != null) {
                selectedPosition = position;
                listener.onSelectRoom(food, position);
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
