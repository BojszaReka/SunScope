package com.example.komplexbeadando.ui;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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
        File directory = new File(_path, dataFile);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        FileInputStream stream = null;
        String data = "";
        try {
            stream = new FileInputStream(directory);
            byte[] content = new byte[(int) directory.length()];
            stream.read(content);
            data = new String(content);
        } catch (FileNotFoundException e) {
            //throw new RuntimeException(e);
            return e.toString();
        } catch (IOException e) {
            //throw new RuntimeException(e);
            return e.toString();
        }
        if(data.equals("")){
            return null; //empty file
        }else{
            users.clear();
            String[] st = data.split(";");
            for (String s: st) {
                users.add(new User(s));
            }
            return null;
        }
    }

    public static String writeFile(String file, String content){
        File directory = new File(_path, file);
        if (!directory.exists()) {
            directory.mkdirs(); // Create the directory if it doesn't exist
        }
        try {
            FileOutputStream writer = new FileOutputStream(directory);
            writer.write(content.getBytes());
            writer.close();
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
        String user = "@"+u.getUsername()+";"+u.getHoroscope();
        if(rememberUser){
            return writeFile(rememberFile, user);
        }
        return null;
    }




}
