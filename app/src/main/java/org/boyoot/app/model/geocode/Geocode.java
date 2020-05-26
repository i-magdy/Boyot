package org.boyoot.app.model.geocode;

public class Geocode {

    private String status;

    private Results results[];

    public String getStatus() {
        return status;
    }

    public Results getResults() {
        return results[0];
    }


}
