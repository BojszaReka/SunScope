package com.example.komplexbeadando;

import androidx.room.RoomDatabase;


@androidx.room.Database(entities = {User.class}, version = 3)
public abstract class Database extends RoomDatabase {

    public abstract UserDao userDao();


}
