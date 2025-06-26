package com.example.myapplication;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface FoodDao {
    @Query("SELECT * FROM food")
    List<Food> getAllFood();

    @Query("SELECT * FROM food WHERE is_booked = 1")
    List<Food> getBookedRooms();

    @Insert
    long insertFood(Food food);

    @Update
    void updateFood(Food food);

    @Delete
    void deleteFood(Food food);

    @Query("DELETE FROM food WHERE id = :id")
    void deleteFoodById(int id);

    @Query("UPDATE food SET is_booked = :isBooked, note = :note WHERE id = :id")
    void updateBookingStatus(int id, boolean isBooked, String note);
} 