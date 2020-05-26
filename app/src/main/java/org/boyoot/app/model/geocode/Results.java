package org.boyoot.app.model.geocode;


public class Results {

    private Geometry geometry;
    private String place_id;
    private PlusCode plus_code;
    private String types[];


    public Geometry getGeometry() {
        return geometry;
    }

    public String getPlace_id() {
        return place_id;
    }

    public PlusCode getPlus_code() {
        return plus_code;
    }

    public String[] getTypes() {
        return types;
    }
}
