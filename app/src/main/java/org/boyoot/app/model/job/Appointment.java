package org.boyoot.app.model.job;

public class Appointment {


    private long value;
    private int year;
    private int month;
    private int dayOfMonth;
    private String monthOfYear;


    public Appointment() {
    }

    public Appointment(long value){
        this.value = value;
    }
    public Appointment(long value, int year, int month, int dayOfMonth, String monthOfYear) {
        this.value = value;

        this.year = year;
        this.month = month;
        this.dayOfMonth = dayOfMonth;
        this.monthOfYear = monthOfYear;
    }

    public void setValue(long value) {
        this.value = value;
    }



    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public void setMonthOfYear(String monthOfYear) {
        this.monthOfYear = monthOfYear;
    }

    public long getValue() {
        return value;
    }


    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public String getMonthOfYear() {
        return monthOfYear;
    }


}
