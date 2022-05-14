package com.example.lab16;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.lab16.data.TaskData;

@Database(entities = {TaskData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TaskDAO taskDao();


    //1
    private static AppDatabase appDatabase; //declaration for the instance

    //2
    public AppDatabase() {

    }

    //3
    public static synchronized AppDatabase getInstance(Context context) {

        if(appDatabase == null)
        {
            appDatabase = Room.databaseBuilder(context,
                    AppDatabase.class, "AppDatabase").allowMainThreadQueries().build();
        }
        return appDatabase;
    }


}
