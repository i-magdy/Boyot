package org.boyoot.app.model;

public class City {

    private String city;
    private String cityCode;
    private String locationLink;
    private String placeId;
    private String lat;
    private String lon;

    public City() {
    }

    public City(String city, String cityCode, String locationLink, String placeId, String lat, String lon) {
        this.city = city;
        this.cityCode = cityCode;
        this.locationLink = locationLink;
        this.placeId = placeId;
        this.lat = lat;
        this.lon = lon;
    }

    public String getCity() {
        return city;
    }

    public String getCityCode() {
        return cityCode;
    }

    public String getLocationLink() {
        return locationLink;
    }

    public String getPlaceId() {
        return placeId;
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

    public void setLocationLink(String locationLink) {
        this.locationLink = locationLink;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }


}
