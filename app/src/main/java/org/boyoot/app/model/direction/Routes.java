package org.boyoot.app.model.direction;

import java.util.List;

public class Routes {

    private String copyrights;
    private List<Legs> legs;

    public Routes() {
    }

    public String getCopyrights() {
        return copyrights;
    }

    public Legs getLegs() {
        return legs.get(0);
    }
}
