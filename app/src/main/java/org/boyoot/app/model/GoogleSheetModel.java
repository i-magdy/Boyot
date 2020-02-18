package org.boyoot.app.model;

import java.io.Serializable;
import static org.boyoot.app.utilities.PhoneUtility.getValidPhoneNumber;

public class GoogleSheetModel implements Serializable {

    private String state;
    private String time_stamp;
    private String code;
    private String area_code;
    private String number;
    private String phone;
    private String date;
    private String split;
    private String window;
    private String cover;
    private String stand;
    private String total;
    private String city;
    private String note;
    private String offers;
    private String lat;
    private String lon;

    public String getState() {
        return state;
    }

    public String getTime_stamp() {
        return time_stamp;
    }

    public String getCode() {
        return code;
    }

    public String getArea_code() {
        return area_code;
    }

    public String getNumber() {
        return number;
    }

    public String getPhone() {
        return getValidPhoneNumber(phone);
    }

    public String getDate() {
        return date;
    }

    public String getSplit() {
        return split;
    }

    public String getWindow() {
        return window;
    }

    public String getCover() {
        return cover;
    }

    public String getStand() {
        return stand;
    }

    public String getTotal() {
        return total;
    }

    public String getCity() {
        return city;
    }

    public String getNote() {
        return note;
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
}
