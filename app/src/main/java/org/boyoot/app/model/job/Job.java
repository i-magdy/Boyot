package org.boyoot.app.model.job;

import com.google.firebase.firestore.ServerTimestamp;

import org.boyoot.app.model.MapConfig;
import org.boyoot.app.model.Price;

import java.util.Date;

public class Job {

    private String id;
    private String jobId;
    private String contactId;
    private String phone;
    private int priority;
    private String branch;
    private String city;
    private String registerTime;
    private boolean divided;
    private boolean confirmDivide;
    private String sort;
    private CurrentWork currentWork;
    private Team team;
    private Duration duration;
    @ServerTimestamp
    private Date timeStamp;
    private Appointment appointment;
    private MapConfig mapConfig;
    private FinishTime finishTime;
    private Directions directions;
    private Price price;
    private Payment payment;

    public Job() {
    }

    public Job(String id, String contactId, String phone, int priority, String branch,String city, String registerTime, boolean divided, CurrentWork currentWork, Duration duration, MapConfig mapConfig,Price price) {
        this.id = id;
        this.contactId = contactId;
        this.phone = phone;
        this.priority = priority;
        this.branch = branch;
        this.city = city;
        this.registerTime = registerTime;
        this.divided = divided;
        this.currentWork = currentWork;
        this.duration = duration;
        this.mapConfig = mapConfig;
        this.price = price;
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

    public String getBranch() {
        return branch;
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

    public Date getTimeStamp() {
        return timeStamp;
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

    public String getCity() {
        return city;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public void setCity(String city) {
        this.city = city;
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

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
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

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public Directions getDirections() {
        return directions;
    }

    public void setDirections(Directions directions) {
        this.directions = directions;
    }

    public Price getPrice() {
        return price;
    }

    public void setPrice(Price price) {
        this.price = price;
    }

    public Payment getPayment() {
        return payment;
    }

    public void setPayment(Payment payment) {
        this.payment = payment;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public boolean isConfirmDivide() {
        return confirmDivide;
    }

    public void setConfirmDivide(boolean confirmDivide) {
        this.confirmDivide = confirmDivide;
    }

    public String getSort() {
        return sort;
    }

    public void setSort(String sort) {
        this.sort = sort;
    }
}
