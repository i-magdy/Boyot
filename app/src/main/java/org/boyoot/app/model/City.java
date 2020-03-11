package org.boyoot.app.model;

public class City {

    private String city;
    private String cityCode;
    private String locationCode;
    private String lat;
    private String lon;

    public City() {
    }

    public City(String city, String cityCode, String locationCode, String lat, String lon) {
        this.city = city;
        this.cityCode = cityCode;
        this.locationCode= locationCode;
        this.lat = lat;
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getLocationCode() {
        return locationCode;
    }


    public String getLat() {
        return lat;
    }

    public String getLon() {
        return lon;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public void setLocationCode(String locationLink) {
        this.locationCode = locationLink;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }


}
