package org.boyoot.app.model;

import com.google.android.libraries.places.api.model.PlusCode;

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
