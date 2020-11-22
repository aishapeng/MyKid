package com.example.mykid;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {Activity.class}, version = 1, exportSchema = false)
public abstract class ActivityRoomDatabase extends RoomDatabase {
    public abstract ActivityDao activityDao();

    private static ActivityRoomDatabase INSTANCE;

    public static ActivityRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (ActivityRoomDatabase.class) {
                if (INSTANCE == null) {
                    //create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ActivityRoomDatabase.class,
                            "activity_database")
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

