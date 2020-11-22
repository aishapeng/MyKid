package com.example.mykid;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface ActivityDao {
    @Insert
    void insert(Activity activity);

    @Delete
    void delete(Activity activity);

}
