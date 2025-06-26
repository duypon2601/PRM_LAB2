package com.example.myapplication;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Food.class}, version = 1, exportSchema = false)
@TypeConverters({BooleanConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract FoodDao foodDao();

    private static volatile AppDatabase INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "hotel_database")
                            .addCallback(sRoomDatabaseCallback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(SupportSQLiteDatabase db) {
            super.onCreate(db);

            databaseWriteExecutor.execute(() -> {
                FoodDao dao = INSTANCE.foodDao();
                
                // Insert initial data
                Food[] rooms = {
                    createRoom("Phòng Deluxe", "Phòng rộng rãi với view thành phố", 1500000, R.drawable.phong1),
                    createRoom("Phòng Suite", "Phòng cao cấp với phòng khách riêng", 2500000, R.drawable.phong2),
                    createRoom("Phòng Family", "Phòng rộng cho gia đình", 2000000, R.drawable.phong3),
                    createRoom("Phòng Standard", "Phòng tiêu chuẩn", 1000000, R.drawable.phong4),
                    createRoom("Phòng Executive", "Phòng dành cho doanh nhân", 3000000, R.drawable.phong5)
                };

                for (Food room : rooms) {
                    dao.insertFood(room);
                }
            });
        }
    };

    private static Food createRoom(String name, String description, int price, int imageResource) {
        Food room = new Food();
        room.setName(name);
        room.setDescription(description);
        room.setPrice(price);
        room.setImageResource(imageResource);
        room.setNote("");
        room.setBooked(false);
        return room;
    }
} 