package com.example.komplexbeadando;

public class MonthlyHoroscopeDataResponse {
    private Data data;
    private int status;
    private boolean success;

    public Data getData() { return data; }
    public int getStatus() { return status; }
    public boolean isSuccess() { return success; }

    public class Data{
        private String challengingDays;
        private String month;
        private String standoutDays;
        private String horoscope_data;

        public String getHoroscopeData() {
            return horoscope_data;
        }

        public void setHoroscopeData(String horoscopeData) {
            this.horoscope_data = horoscopeData;
        }


        public String getChallengingDays() {
            return challengingDays;
        }

        public void setChallengingDays(String challengingDays) {
            this.challengingDays = challengingDays;
        }

        public String getMonth() {
            return month;
        }

        public void setMonth(String month) {
            this.month = month;
        }

        public String getStandoutDays() {
            return standoutDays;
        }

        public void setStandoutDays(String standoutDays) {
            this.standoutDays = standoutDays;
        }
    }

}
