package com.example.roboticcar;

public class notification_data {

    private String Data;
    private String DateTime;

    public notification_data() {
    }

    @Override
    public String toString() {
        return "notification_data{" +
                "Data='" + Data + '\'' +
                ", DateTime='" + DateTime + '\'' +
                '}';
    }

    public  String getData() {
        return Data;
    }

    public void setData(String data) {
        Data = data;
    }

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }



}
