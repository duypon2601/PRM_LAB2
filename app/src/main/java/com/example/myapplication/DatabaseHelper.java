package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "HotelDB";
    private static final int DATABASE_VERSION = 1;

    // Table names
    private static final String TABLE_FOOD = "food";
    private static final String TABLE_DRINK = "drink";

    // Common column names
    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_DESCRIPTION = "description";
    private static final String KEY_PRICE = "price";
    private static final String KEY_IMAGE = "image";
    private static final String KEY_NOTE = "note";
    private static final String KEY_IS_BOOKED = "is_booked";

    // Create table statements
    private static final String CREATE_TABLE_FOOD = "CREATE TABLE " + TABLE_FOOD + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_PRICE + " INTEGER,"
            + KEY_IMAGE + " INTEGER,"
            + KEY_NOTE + " TEXT,"
            + KEY_IS_BOOKED + " INTEGER DEFAULT 0"
            + ")";

    private static final String CREATE_TABLE_DRINK = "CREATE TABLE " + TABLE_DRINK + "("
            + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + KEY_NAME + " TEXT,"
            + KEY_DESCRIPTION + " TEXT,"
            + KEY_PRICE + " INTEGER,"
            + KEY_IMAGE + " INTEGER"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_FOOD);
        db.execSQL(CREATE_TABLE_DRINK);
        insertInitialData(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINK);
        onCreate(db);
    }

    private void insertInitialData(SQLiteDatabase db) {
        String[][] rooms = {
            {"Phòng Deluxe", "Phòng rộng rãi với view thành phố", "1500000", String.valueOf(R.drawable.phong1)},
            {"Phòng Suite", "Phòng cao cấp với phòng khách riêng", "2500000", String.valueOf(R.drawable.phong2)},
            {"Phòng Family", "Phòng rộng cho gia đình", "2000000", String.valueOf(R.drawable.phong3)},
            {"Phòng Standard", "Phòng tiêu chuẩn", "1000000", String.valueOf(R.drawable.phong4)},
            {"Phòng Executive", "Phòng dành cho doanh nhân", "3000000", String.valueOf(R.drawable.phong5)}
        };

        for (String[] room : rooms) {
            ContentValues values = new ContentValues();
            values.put(KEY_NAME, room[0]);
            values.put(KEY_DESCRIPTION, room[1]);
            values.put(KEY_PRICE, Integer.parseInt(room[2]));
            values.put(KEY_IMAGE, Integer.parseInt(room[3]));
            values.put(KEY_NOTE, "");
            values.put(KEY_IS_BOOKED, 0);
            db.insert(TABLE_FOOD, null, values);
        }
    }

    public List<Food> getAllFood() {
        List<Food> foodList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_FOOD, null, null, null, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                food.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
                food.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                food.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                food.setImageResource(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IMAGE)));
                food.setNote(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE)));
                food.setBooked(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IS_BOOKED)) == 1);
                foodList.add(food);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return foodList;
    }

    public List<Food> getBookedRooms() {
        List<Food> bookedRooms = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String selection = KEY_IS_BOOKED + " = ?";
        String[] selectionArgs = {"1"};
        Cursor cursor = db.query(TABLE_FOOD, null, selection, selectionArgs, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                Food food = new Food();
                food.setId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));
                food.setName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NAME)));
                food.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(KEY_DESCRIPTION)));
                food.setPrice(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_PRICE)));
                food.setImageResource(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_IMAGE)));
                food.setNote(cursor.getString(cursor.getColumnIndexOrThrow(KEY_NOTE)));
                food.setBooked(true);
                bookedRooms.add(food);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return bookedRooms;
    }

    public void bookRoom(Food room) {
        if (room == null) return;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_BOOKED, 1);
        values.put(KEY_NOTE, room.getNote());
        db.update(TABLE_FOOD, values, KEY_ID + " = ?", new String[]{String.valueOf(room.getId())});
    }

    public void unbookRoom(int roomId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_IS_BOOKED, 0);
        values.put(KEY_NOTE, "");
        db.update(TABLE_FOOD, values, KEY_ID + " = ?", new String[]{String.valueOf(roomId)});
    }

    public void updateRoomNote(Food room) {
        if (room == null) return;
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NOTE, room.getNote());
        db.update(TABLE_FOOD, values, KEY_ID + " = ?", new String[]{String.valueOf(room.getId())});
    }

    public void deleteFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_FOOD, KEY_ID + " = ?", new String[]{String.valueOf(id)});
    }
} 