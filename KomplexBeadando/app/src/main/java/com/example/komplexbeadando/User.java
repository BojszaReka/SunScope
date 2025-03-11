package com.example.komplexbeadando;

import androidx.room.Entity;
import androidx.room.Fts4;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;


@Entity(tableName = "users")
public class User {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String username;
    private String password;
    private String horoscope;
    private boolean isRemembered;


    public User(){}

    public User(String username, String password, String date) {
        this.id = 0;
        this.username = username;
        this.password = password;
        this.horoscope = dateToHoroscope(date);
        isRemembered = false;
    }

    /*
    public User(String read){
        String[] st = read.split(",");
        String[] data = new String[4];
        int index = 0;
        for (String a : st) {
            String[] st2 = a.split("=");
            if(st2.length == 2){
                data[index] = st2[1];
                index++;
            }
        }
        if(data[0].charAt(0) == ' '){
            data[0] = data[0].substring(1);
        }
        this.id = Integer.parseInt(data[0]);
        this.username = data[1];
        this.password = data[2];
        this.horoscope = data[3];
    }
     */

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getHoroscope() {
        return horoscope;
    }

    public boolean getIsRemembered(){return isRemembered;}


    public void setId(int id) {
        this.id = id;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setHoroscope(String horoscope) {
        this.horoscope = horoscope;
    }

    public void setRemembered(boolean remembered) {
        isRemembered = remembered;
    }

    private String dateToHoroscope(String date){
        String[] months = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};

        try {
            String[] parts = date.split(",? ");
            String month = parts[1];
            int day = Integer.parseInt(parts[2]);

            int monthNumber = -1;
            for (int i = 0; i < months.length; i++) {
                if (months[i].equalsIgnoreCase(month)) {
                    monthNumber = i + 1;
                    break;
                }
            }

            if(monthNumber != -1){
                if( (monthNumber==3 && day>20)  || (monthNumber==4 && day<20) ){
                    return "Aries";
                }
                if( (monthNumber==4 && day>19)  || (monthNumber==5 && day<21) ){
                    return "Taurus";
                }
                if( (monthNumber==5 && day>20)  || (monthNumber==6 && day<21) ){
                    return "Gemini";
                }
                if( (monthNumber==6 && day>20)  || (monthNumber==7 && day<23) ){
                    return "Cancer";
                }
                if( (monthNumber==7 && day>22)  || (monthNumber==8 && day<23) ){
                    return "Leo";
                }
                if( (monthNumber==8 && day>22)  || (monthNumber==4 && day<23) ){
                    return "Virgo";
                }
                if( (monthNumber==9 && day>22)  || (monthNumber==10 && day<23) ){
                    return "Libra";
                }
                if( (monthNumber==10 && day>22)  || (monthNumber==11 && day<22) ){
                    return "Scorpio";
                }
                if( (monthNumber==11 && day>21)  || (monthNumber==12 && day<22) ){
                    return "Saggitarius";
                }
                if( (monthNumber==12 && day>21)  || (monthNumber==1 && day<20) ){
                    return "Capricorn";
                }
                if( (monthNumber==1 && day>19)  || (monthNumber==2 && day<19) ){
                    return "Aquarius";
                }
                if( (monthNumber==2 && day>18)  || (monthNumber==3 && day<21) ){
                    return "Pisces";
                }
                return "Something not right";
            }else{
                return "Invalid date";
            }
        } catch (Exception e) {
            return "Invalid date format";
        }
    }

    @Override
    public String toString() {
        return
                "{id =" + id +
                ", username=" + username +
                ", password=" + password +
                ", horoscope=" + horoscope +
                ",};";
    }


}
