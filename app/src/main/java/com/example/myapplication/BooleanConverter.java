package com.example.myapplication;

import androidx.room.TypeConverter;

public class BooleanConverter {
    @TypeConverter
    public static Boolean toBoolean(Integer value) {
        return value == null ? null : value == 1;
    }

    @TypeConverter
    public static Integer toInteger(Boolean value) {
        return value == null ? null : value ? 1 : 0;
    }
} 