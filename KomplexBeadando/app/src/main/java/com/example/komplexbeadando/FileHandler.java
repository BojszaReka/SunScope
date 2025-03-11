package com.example.komplexbeadando;


import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class FileHandler {

    private static String dataFile = "data.txt";
    private static String rememberFile = "remember.txt";
    private static ArrayList<User> users = new ArrayList<User>();
    private static File _path;

    public static void writeEmpty(){
        //writeFile(dataFile, "");
        //writeFile(rememberFile, "");
    }

    public static String Register(String username, String password, String date, File path, Boolean rememberUser){
        _path = path;
        String response = dataToList();
        if(response != null){
            return response;
        }else if(checkIfExists(username)){
            return "This username is already in use, pick a different one!";
        }else{
            response = SaveData(username, password, date);
            if(response.charAt(0) == '@'){
                response = response.substring(1);
                int newUserId = Integer.parseInt(response);
                User u = GetData(newUserId);
                response = rememberUser(u, rememberUser);
                if(response == null){
                    return "@"+u.getUsername()+";"+u.getHoroscope();
                }else{
                    return response;
                }
            }else{
                return response;
            }
        }
        //return null;
    }

    public static String Login(String username, String password, File path, Boolean rememberUser){
        _path = path;
        String response = dataToList();
        if(response != null){
            return response;
        }else if(checkIfExists(username)){
            User u = GetData(username);
            response = rememberUser(u, rememberUser);
            if(response != null){
                return response;
            }else{
                if(password.equals(u.getPassword())){
                    return "@"+u.getUsername()+";"+u.getHoroscope();
                }else{
                    return "Username and password does not match";
                }
            }
        }else{
            return "This user does not exists!";
        }
        //return null;
    }

    public static boolean checkIfExists(String username){
        for (int i = 0; i< users.size(); i++){
            if(users.get(i).getUsername().equals(username)){
                return true;
            }
        }
        return false;
    }

    public static String SaveData(String username, String password, String date){
        int id;
        if(users.size() == 0){
            id = 1;
        }else{
            id = users.get(users.size() -1).getId() +1;
        }
        User u = new User(id, username, password, date);
        users.add(u);

        String content = "";
        for (User us: users) {
            content += us.toString();
        }

        String response = writeFile(dataFile, content);
        if(response != null){
            return response;
        }
        return "@"+u.getId(); //return userid
    }

    public static User GetData(String username){
        User u = new User();
        for (int i = 0; i< users.size(); i++){
            if(users.get(i).getUsername().equals(username)){
                u = users.get(i);
            }
        }
        return u;
    }

    public static User GetData(int id){
        User u = new User();
        for (int i = 0; i< users.size(); i++){
            if(users.get(i).getId() == id){
                u = users.get(i);
            }
        }
        return u;
    }

    public static String dataToList(){

        String data = readFile(dataFile);
        if(data.charAt(0) == '@'){
            data = data.substring(1);
            if(data == null || data.equals("")){
                return "File is empty"; //empty file
            }else{
                users.clear();
                String[] st = data.split(";");
                for (String s: st) {
                    users.add(new User(s));
                }
                return null;
            }

        }else{
            return data;
        }
    }

    private static String readFile(String file) {
        File directory = new File(_path, file);
        if (!directory.exists()) {
            directory.mkdirs();
            Log.d(TAG, file+" file got created");
        }
        String data = "";
        try {

            FileInputStream stream = new FileInputStream(directory);
            byte[] content = new byte[(int) directory.length()];
            stream.read(content);
            Log.d(TAG, file+" Read: "+new String(content));
            String returned = "@"+new String(content);
            return returned;
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
            return e.toString();
        } catch (IOException e) {
            //throw new RuntimeException(e);
            return e.toString();
        }
    }


    public static String writeFile(String file, String content){
        File directory = new File(_path, file);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
            Log.d(TAG, file+" file got created");
        }
        try {
            FileOutputStream writer = new FileOutputStream(directory);
            writer.write(content.getBytes());
            writer.close();
            Log.d(TAG, file+" wrote: "+content);
            return null;
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
            return e.toString();
        } catch (IOException e) {
            //throw new RuntimeException(e);
            return e.toString();
        }
    }

    public static String rememberUser(User u, Boolean rememberUser){
        if(rememberUser){
            Log.d(TAG, "RememberUser method: "+rememberUser+" "+u.toString());
            String user = "@"+u.getUsername()+";"+u.getHoroscope();
            return writeFile(rememberFile, user);
        }
        return null;
    }

    public static boolean isRememberedUser(File path){
        _path = path;
        String filecontent = readFile(rememberFile);
        if(filecontent == null){
            return  false;
        }else if(filecontent.charAt(0) == '@'){
            if(filecontent.substring(1)!= null){
                return true;
            }else{
                return false;
            }
        }
        return false;
    }

    public static String rememberedUser(){
        String filecontent = readFile(rememberFile);
        if(filecontent == null || filecontent.isEmpty() || filecontent.isBlank()){
            return  null;
        }else if(filecontent.charAt(0) == '@'){
            if(filecontent.substring(1)!= null){
                return filecontent;
            }else{
                return null;
            }
        }
        return null;
    }


    public static void LogOut() {
        writeFile(rememberFile, "");
    }
}
