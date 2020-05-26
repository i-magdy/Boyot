package org.boyoot.app.model;

import java.util.Date;

public class JobAdded {

    private String date;
    private boolean added;
    private Date appointment;

    public JobAdded() {
    }

    public JobAdded(String date, boolean added, Date appointment) {
        this.date = date;
        this.added = added;
        this.appointment = appointment;
    }

    public String getDate() {
        return date;
    }

    public boolean isAdded() {
        return added;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setAdded(boolean added) {
        this.added = added;
    }


    public void setAppointment(Date appointment) {
        this.appointment = appointment;
    }

    public Date getAppointment() {
        return appointment;
    }


}
