package com.example.mykid;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Report.class}, version = 1, exportSchema = false)
public abstract class ActivityRoomDatabase extends RoomDatabase {
    public abstract ReportDao activityDao();

    private static ActivityRoomDatabase INSTANCE;

    private static RoomDatabase.Callback callback =
            new RoomDatabase.Callback(){
                @Override
                public void onOpen(@NonNull SupportSQLiteDatabase db) {
                    super.onOpen(db);
                }
            };

    //TODO: not sure need populate or not

    public static ActivityRoomDatabase getInstance(final Context context) {
        if (INSTANCE == null) {
            synchronized (ActivityRoomDatabase.class) {
                if (INSTANCE == null) {
                    //create database
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ActivityRoomDatabase.class,
                            "report_database")
                            .addCallback(callback)
                            .fallbackToDestructiveMigration()
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}

