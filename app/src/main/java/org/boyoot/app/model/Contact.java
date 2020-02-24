package org.boyoot.app.model;

import com.google.firebase.firestore.FieldValue;

import java.io.Serializable;
import java.util.Date;

public class Contact implements Serializable {

    private String id;
    private String phone;
    private FieldValue timeStamp;
    private String registrationDate;
    private String priority;
    private String note;
    private Work work;
    private City city;


    public Contact() {
    }

    public Contact(String id, String phone, FieldValue timeStamp, String registrationDate, String priority, String note, Work work, City city) {
        this.id = id;
        this.phone = phone;
        this.timeStamp = timeStamp;
        this.registrationDate = registrationDate;
        this.priority = priority;
        this.note = note;
        this.work = work;
        this.city = city;
    }

    public String getId() {
        return id;
    }

    public String getPhone() {
        return phone;
    }

    public FieldValue getTimeStamp() {
        return timeStamp;
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

    public void setTimeStamp(FieldValue timeStamp) {
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
}
