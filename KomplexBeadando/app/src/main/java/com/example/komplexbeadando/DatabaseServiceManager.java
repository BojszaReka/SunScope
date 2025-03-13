package com.example.komplexbeadando;

import android.content.Context;
import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;
import java.util.List;

public class DatabaseServiceManager {
    Database db;
    Context _context;

    UserDao userDao;

    public DatabaseServiceManager(Context context){
        _context = context;
        RoomDatabase.Callback myCallback = new RoomDatabase.Callback() {
            @Override
            public void onCreate(@NonNull SupportSQLiteDatabase db) {
                super.onCreate(db);
            }

            @Override
            public void onOpen(@NonNull SupportSQLiteDatabase db) {
                super.onOpen(db);
            }
        };
        db = Room.databaseBuilder(_context, Database.class, "SunScopeDB").addCallback(myCallback).fallbackToDestructiveMigration().allowMainThreadQueries().build();
        userDao = db.userDao();
    }

    public List<User> getAllUsers(){
        List<User> users = userDao.getAll();
        return users;
    }

    public void addUsers(List<User> users){
        userDao.insertAll(users);
    }

    public void addUser(User user){
        userDao.addUser(user);
    }

    public void updateUser(User user){
        userDao.updateUser(user);
    }

    public User getUser(int id){
        return userDao.getUser(id);
    }

    public User getUser(String username){
        return userDao.getUser(username);
    }

    public boolean authenticateUser(String username, String password){
        User u = getUser(username);
        return u != null && u.getPassword().equals(password);
    }

    public boolean checkUsernameFree(String username){
        List<User> users = getAllUsers();
        if(!users.isEmpty()){
            for (User u:users) {
                if(u.getUsername().equals(username)){
                    return false;
                }
            }
        }
        return true;
    }

    public User getRememberedUser(){
        List<User> users = userDao.getAll();
        for (User u: users) {
            if(u.getIsRemembered()){
                return u;
            }
        }
        return null;
    }

    public boolean updateUserOnLogout(String username){
        User u = getUser(username);
        u.setRemembered(false);
        userDao.updateUser(u);
        u = getUser(username);
        return u.getIsRemembered();
    }

    public void addPhoto(String username, Bitmap photo){
        User u = userDao.getUser(username);
        List<Bitmap> p = u.getPhotos();
        p.add(photo);
        u.setPhotos(p);
        userDao.updateUser(u);
    }

    public void deleteUser(String username){
        userDao.delete(userDao.getUser(username));
    }

}
