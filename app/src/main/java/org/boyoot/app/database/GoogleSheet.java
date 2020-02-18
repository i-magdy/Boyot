package org.boyoot.app.database;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "google_sheet")
public class GoogleSheet implements Serializable {

    @NonNull
    @PrimaryKey
    @ColumnInfo(name = "phone")
    private String phone;

    @NonNull
    @ColumnInfo(name = "state")
    private String state;

    @NonNull
    @ColumnInfo(name = "city")
    private String city;

    @NonNull
    @ColumnInfo(name = "date")
    private String date;

    @NonNull
    @ColumnInfo(name = "contactId")
    private String contactId;

    public GoogleSheet(@NonNull String phone, @NonNull String state, @NonNull String city, @NonNull String date, @NonNull String contactId) {
        this.phone = phone;
        this.state = state;
        this.city = city;
        this.date = date;
        this.contactId = contactId;
    }

    @NonNull
    public String getPhone() {
        return phone;
    }

    @NonNull
    public String getState() {
        return state;
    }

    @NonNull
    public String getCity() {
        return city;
    }

    @NonNull
    public String getDate() {
        return date;
    }

    @NonNull
    public String getContactId() {
        return contactId;
    }
}
