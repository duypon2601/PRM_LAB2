package com.example.myapplication;

import android.app.Application;
import android.os.Handler;
import android.os.Looper;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FoodRepository {
    private FoodDao foodDao;
    private ExecutorService executorService;
    private Handler mainHandler;

    public FoodRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        foodDao = db.foodDao();
        executorService = Executors.newFixedThreadPool(4);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    // Load data with multi-threading
    public void loadAllFood(DataCallback<List<Food>> callback) {
        executorService.execute(() -> {
            List<Food> foodList = foodDao.getAllFood();
            mainHandler.post(() -> callback.onResult(foodList));
        });
    }

    public void loadBookedRooms(DataCallback<List<Food>> callback) {
        executorService.execute(() -> {
            List<Food> bookedList = foodDao.getBookedRooms();
            mainHandler.post(() -> callback.onResult(bookedList));
        });
    }

    // Add with multi-threading
    public void addFood(Food food, DataCallback<Long> callback) {
        executorService.execute(() -> {
            long id = foodDao.insertFood(food);
            mainHandler.post(() -> callback.onResult(id));
        });
    }

    // Update with multi-threading
    public void updateFood(Food food, DataCallback<Void> callback) {
        executorService.execute(() -> {
            foodDao.updateFood(food);
            mainHandler.post(() -> callback.onResult(null));
        });
    }

    // Delete with multi-threading
    public void deleteFood(Food food, DataCallback<Void> callback) {
        executorService.execute(() -> {
            foodDao.deleteFood(food);
            mainHandler.post(() -> callback.onResult(null));
        });
    }

    public void deleteFoodById(int id, DataCallback<Void> callback) {
        executorService.execute(() -> {
            foodDao.deleteFoodById(id);
            mainHandler.post(() -> callback.onResult(null));
        });
    }

    // Update booking status with multi-threading
    public void updateBookingStatus(int id, boolean isBooked, String note, DataCallback<Void> callback) {
        executorService.execute(() -> {
            foodDao.updateBookingStatus(id, isBooked, note);
            mainHandler.post(() -> callback.onResult(null));
        });
    }

    // Callback interface for async operations
    public interface DataCallback<T> {
        void onResult(T result);
    }

    public void shutdown() {
        executorService.shutdown();
    }
} 