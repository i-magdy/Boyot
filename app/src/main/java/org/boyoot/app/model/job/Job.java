package org.boyoot.app.model.job;

import com.google.firebase.firestore.ServerTimestamp;

import org.boyoot.app.model.MapConfig;

import java.util.Date;

public class Job {

    private String id;
    private String contactId;
    private String phone;
    private int priority;
    private String cityCode;
    private String registerTime;
    private boolean divided;
    private CurrentWork currentWork;
    private Duration duration;
    @ServerTimestamp
    private Date date;
    private Appointment appointment;
    private MapConfig mapConfig;
    private FinishTime finishTime;
    private Directions directions;

    public Job() {
    }

    public Job(String id, String contactId, String phone, int priority, String cityCode, String registerTime, boolean divided, CurrentWork currentWork, Duration duration, MapConfig mapConfig) {
        this.id = id;
        this.contactId = contactId;
        this.phone = phone;
        this.priority = priority;
        this.cityCode = cityCode;
        this.registerTime = registerTime;
        this.divided = divided;
        this.currentWork = currentWork;
        this.duration = duration;
        this.mapConfig = mapConfig;
    }

    public String getId() {
        return id;
    }

    public String getContactId() {
        return contactId;
    }

    public String getPhone() {
        return phone;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public boolean isDivided() {
        return divided;
    }

    public CurrentWork getCurrentWork() {
        return currentWork;
    }

    public Duration getDuration() {
        return duration;
    }

    public Date getDate() {
        return date;
    }

    public Appointment getAppointment() {
        return appointment;
    }

    public MapConfig getMapConfig() {
        return mapConfig;
    }

    public FinishTime getFinishTime() {
        return finishTime;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
    }

    public void setDivided(boolean divided) {
        this.divided = divided;
    }

    public void setCurrentWork(CurrentWork currentWork) {
        this.currentWork = currentWork;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setAppointment(Appointment appointment) {
        this.appointment = appointment;
    }

    public void setMapConfig(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
    }

    public void setFinishTime(FinishTime finishTime) {
        this.finishTime = finishTime;
    }
}
