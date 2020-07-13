package org.boyoot.app.model;

import java.io.Serializable;
import java.util.Date;

public class CurrentCalenderDate implements Serializable {

    private Date date;
    private long time;
    private int year;
    private int month;
    private int day;
    private String branch;
    private String jobIdKey;
    private int pathNo;

    public CurrentCalenderDate() {
    }

    public CurrentCalenderDate(Date date, long time, int year, int month, int day) {
        this.date = date;
        this.time = time;
        this.year = year;
        this.month = month;
        this.day = day;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public Date getDate() {
        return date;
    }

    public long getTime() {
        return time;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public String getJobIdKey() {
        return jobIdKey;
    }

    public int getPathNo() {
        return pathNo;
    }

    public void setJobIdKey(String jobIdKey) {
        this.jobIdKey = jobIdKey;
    }

    public void setPathNo(int pathNo) {
        this.pathNo = pathNo;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }
}
