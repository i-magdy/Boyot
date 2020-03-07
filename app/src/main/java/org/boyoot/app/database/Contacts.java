package org.boyoot.app.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "contacts_table")
public class Contacts {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "Id")
    private String id;

    @NonNull
    @ColumnInfo(name = "phone")
    private String phone;

    @ColumnInfo(name = "priority")
    private String priority;

    @ColumnInfo(name = "contactId")
    private String contactId;

    @ColumnInfo(name = "interval")
    private String interval;

    @ColumnInfo(name = "city")
    private String city;

    @ColumnInfo(name = "cityCode")
    private String cityCode;

    @ColumnInfo(name = "placeId")
    private String placeId;

    @ColumnInfo(name = "timeStamp")
    private String timeStamp;

    public Contacts(@NonNull String id, @NonNull String phone, String priority, String contactId, String interval, String city, String cityCode, String placeId,String timeStamp) {
        this.id = id;
        this.phone = phone;
        this.priority = priority;
        this.contactId = contactId;
        this.interval = interval;
        this.city = city;
        this.cityCode = cityCode;
        this.placeId = placeId;
        this.timeStamp = timeStamp;
    }

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    public String getPriority() {
        return priority;
    }

    public String getContactId() {
        return contactId;
    }

    public String getInterval() {
        return interval;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getTimeStamp() {
        return timeStamp;
    }
}
