package com.example.komplexbeadando;

import androidx.annotation.NonNull;

public class DailyHoroscopeDataResponse {

        private Data data;
        private int status;
        private boolean success;

        // Getters and Setters
        public Data getData() {
            return data;
        }

        public void setData(Data data) {
            this.data = data;
        }

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }


    public class Data {
        private String date;
        private String horoscope_data;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHoroscopeData() {
            return horoscope_data;
        }

        public void setHoroscopeData(String horoscopeData) {
            this.horoscope_data = horoscopeData;
        }

        @NonNull
        @Override
        public String toString() {
            return date+" "+horoscope_data;
        }
    }
}
