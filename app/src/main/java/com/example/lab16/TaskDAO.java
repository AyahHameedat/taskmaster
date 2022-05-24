package com.example.lab16;


import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.lab16.data.TaskData;

import java.util.List;

@Dao
public interface TaskDAO {


    @Query("SELECT * FROM TaskData")
    List<TaskData> getAll();

    @Query("SELECT * FROM TaskData WHERE id = :id")
    TaskData getTaskByID(Long id);

    @Insert
    Long insertTask(TaskData task);

    @Delete
    void delete(TaskData task);

}
