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
    public void updateUsers(List<User> users);

    @Update
    public void updateUser(User user);

    @Query("SELECT * FROM users")
    public List<User> getAll();

    @Query("SELECT * FROM users WHERE id == :id LIMIT 1")
    public User getUser(int id);

    @Query("SELECT * FROM users WHERE username == :username LIMIT 1")
    public User getUser(String username);
}
