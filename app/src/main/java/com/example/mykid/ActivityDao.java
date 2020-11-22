package com.example.mykid;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ActivityDao {
    @Insert
    void insert(Activity activity);

    @Delete
    void delete(Activity activity);

    @Update
    void update(Activity activity);

    @Query("SELECT * from activity_table ORDER BY activityDate DESC")
    LiveData<List<Activity>> getAllActivity();
}
