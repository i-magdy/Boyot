package org.boyoot.app.model;

import com.google.firebase.Timestamp;

import java.io.Serializable;

public class Contact {

    private String contactId;
    private String id;
    private String phone;
    private Timestamp timeStamp;
    private String registrationDate;
    private String priority;
    private String note;
    private String auth;
    private Work work;
    private City city;
    private MapConfig mapConfig;
    private JobAdded jobAdded;



    public Contact() {
    }

    public Contact(String id, String phone, Timestamp timeStamp, String registrationDate, String priority, String note/*,String auth*/, Work work, City city, MapConfig mapConfig,JobAdded jobAdded) {
        this.id = id;
        this.phone = phone;
        this.timeStamp = timeStamp;
        this.registrationDate = registrationDate;
        this.priority = priority;
        this.note = note;
       // this.auth = auth;
        this.work = work;
        this.city = city;
        this.mapConfig = mapConfig;
        this.jobAdded = jobAdded;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public Timestamp getTimeStamp() {
        return  timeStamp;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public String getPriority() {
        return priority;
    }

    public String getNote() {
        return note;
    }

    public Work getWork() {
        return work;
    }

    public City getCity() {
        return city;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setTimeStamp(Timestamp timeStamp) {
        this.timeStamp = timeStamp;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setWork(Work work) {
        this.work = work;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public MapConfig getMapConfig() {
       return mapConfig;

    }

    public void setMapConfig(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
    }

    public void setAuth(String auth) {
        this.auth = auth;
    }

    public String getAuth() {
        return auth;
    }

    public JobAdded getJobAdded() {
        return jobAdded;
    }

    public void setJobAdded(JobAdded jobAdded) {
        this.jobAdded = jobAdded;
    }

    public String getContactId() {
        return contactId;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }
}
