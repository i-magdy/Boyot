package org.boyoot.app.database;


import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import org.boyoot.app.model.job.CurrentWork;

@Entity(tableName = "jobs_table")
public class Jobs {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "Key")
    private String key;

    @NonNull
    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "priority")
    private int priority;

    @ColumnInfo(name = "contactId")
    private String contactId;

    @ColumnInfo(name = "divide")
    private boolean divide;

    @ColumnInfo(name = "appointment")
    private String appointment;

    @ColumnInfo(name = "duration")
    private String duration;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "branch")
    private String branch;

    @ColumnInfo(name = "acsTotal")
    private String acsTotal;

    @ColumnInfo(name = "interval")
    private String interval;

    @ColumnInfo(name = "timeStamp")
    private long timeStamp;

    public Jobs(@NonNull String key, @NonNull String phone, int priority, String contactId, boolean divide, String appointment, String duration, String city, String branch,String acsTotal,String interval,long timeStamp) {
        this.key = key;
        this.phone = phone;
        this.priority = priority;
        this.contactId = contactId;
        this.divide = divide;
        this.appointment = appointment;
        this.duration = duration;
        this.city = city;
        this.branch = branch;
        this.acsTotal = acsTotal;
        this.interval = interval;
        this.timeStamp = timeStamp;
    }

    public void setKey(@NonNull String key) {
        this.key = key;
    }

    public void setPhone(@NonNull String phone) {
        this.phone = phone;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setDivide(boolean divide) {
        this.divide = divide;
    }

    public void setAppointment(String appointment) {
        this.appointment = appointment;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    @NonNull
    public String getKey() {
        return key;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public int getPriority() {
        return priority;
    }

    public String getContactId() {
        return contactId;
    }

    public boolean isDivide() {
        return divide;
    }

    public String getAppointment() {
        return appointment;
    }

    public String getDuration() {
        return duration;
    }

    public String getCity() {
        return city;
    }

    public String getBranch() {
        return branch;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public String getAcsTotal() {
        return acsTotal;
    }

    public String getInterval() {
        return interval;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setAcsTotal(String acsTotal) {
        this.acsTotal = acsTotal;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }
}
