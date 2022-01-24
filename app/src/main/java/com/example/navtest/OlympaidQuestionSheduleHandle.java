package com.example.navtest;

public class OlympaidQuestionSheduleHandle {
    private int date, month, year, hour;

    public OlympaidQuestionSheduleHandle(int date, int month, int year, int hour) {
        this.date = date;
        this.month = month;
        this.year = year;
        this.hour = hour;
    }

    public OlympaidQuestionSheduleHandle() {
    }



    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }
}
