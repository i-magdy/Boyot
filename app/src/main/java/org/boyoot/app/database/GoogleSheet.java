package org.boyoot.app.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;


@Entity(tableName = "sheet_table")
public class GoogleSheet implements Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "phone")
    private String phone;

    @NonNull
    @ColumnInfo(name = "timeStamp")
    private String timeStamp;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "split")
    private String split;

    @NonNull
    @ColumnInfo(name = "window")
    private String window;

    @NonNull
    @ColumnInfo(name = "cover")
    private String cover;

    @NonNull
    @ColumnInfo(name = "stand")
    private String stand;

    @ColumnInfo(name = "concealed")
    private String concealed;

    @NonNull
    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "note")
    private String note;

    @ColumnInfo(name = "locationLink")
    private String locationLink;

    @ColumnInfo(name = "offers")
    private String offers;

    @ColumnInfo(name = "plusCode")
    private String plusCode;

    @ColumnInfo(name = "lat")
    private String lat;

    @ColumnInfo(name = "lon")
    private String lon;

    @ColumnInfo(name = "state")
    private String state;

    @ColumnInfo(name = "contactId")
    private String contactId;

    @ColumnInfo(name = "cloudId")
    private String cloudId;

    public GoogleSheet(@NonNull String phone, @NonNull String timeStamp, @NonNull String date, @NonNull String split, @NonNull String window, @NonNull String cover, @NonNull String stand,String concealed, @NonNull String city, String note, String offers,String state,String plusCode) {
        this.phone = phone;
        this.timeStamp = timeStamp;
        this.date = date;
        this.split = split;
        this.window = window;
        this.cover = cover;
        this.stand = stand;
        this.concealed = concealed;
        this.city = city;
        this.note = note;
        this.offers = offers;
        this.state = state;
        this.plusCode = plusCode;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @NonNull
    public String getTimeStamp() {
        return timeStamp;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getSplit() {
        return split;
    }

    @NonNull
    public String getWindow() {
        return window;
    }

    @NonNull
    public String getCover() {
        return cover;
    }

    @NonNull
    public String getStand() {
        return stand;
    }

    public String getConcealed(){
        return concealed;
    }
    @NonNull
    public String getCity() {
        return city;
    }

    public String getNote() {
        return note;
    }

    public String getLocationLink() {
        return locationLink;
    }

    public String getOffers() {
        return offers;
    }

    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public String getState() {
        return state;
    }

    public String getContactId() {
        return contactId;
    }

    public String getCloudId() {
        return cloudId;
    }

    public void setLocationLink(String locationLink) {
        this.locationLink = locationLink;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setContactId(String contactId) {
        this.contactId = contactId;
    }

    public void setCloudId(String cloudId) {
        this.cloudId = cloudId;
    }

    public String getPlusCode() {
        return plusCode;
    }
}
