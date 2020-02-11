package org.boyoot.app.model;

public class CityCodeConfigModel {

    private String city;
    private String code;

    public CityCodeConfigModel() {}

    public CityCodeConfigModel(String city, String code) {
        this.city = city;
        this.code = code;
    }

    public String getCity() {
        return city;
    }

    public String getCode() {
        return code;
    }
}
