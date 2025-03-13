package com.example.komplexbeadando;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<User> users);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addUser(User user);

    @Delete
    void delete(User user);

    @Update
    void updateUsers(List<User> users);

    @Update
    void updateUser(User user);

    @Query("SELECT * FROM users")
    List<User> getAll();

    @Query("SELECT * FROM users WHERE id == :id LIMIT 1")
    User getUser(int id);

    @Query("SELECT * FROM users WHERE username == :username LIMIT 1")
    User getUser(String username);
}
