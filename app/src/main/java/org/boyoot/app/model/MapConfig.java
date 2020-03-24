package org.boyoot.app.model;

public class MapConfig {

    private String compound_code;
    private String globalCode;
    private String placeId;
    private String lat;
    private String lng;
    private boolean saved;

    public MapConfig() {
    }

    public MapConfig(String compound_code, String globalCode, String placeId, String lat, String lng, boolean saved) {
        this.compound_code = compound_code;
        this.globalCode = globalCode;
        this.placeId = placeId;
        this.lat = lat;
        this.lng = lng;
        this.saved = saved;
    }


    public String getCompound_code() {
        return compound_code;
    }

    public String getGlobalCode() {
        return globalCode;
    }

    public String getPlaceId() {
        return placeId;
    }

    public String getLat() {
        return lat;
    }

    public String getLng() {
        return lng;
    }

    public boolean isSaved() {
        return saved;
    }


    public void setCompound_code(String compound_code) {
        this.compound_code = compound_code;
    }

    public void setGlobalCode(String globalCode) {
        this.globalCode = globalCode;
    }

    public void setPlaceId(String placeId) {
        this.placeId = placeId;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }


}
