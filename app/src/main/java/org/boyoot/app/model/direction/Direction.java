package org.boyoot.app.model.direction;

import java.util.List;

public class Direction {
    private String status;
    private List<Routes> routes;

    public Direction() {
    }

    public String getStatus() {
        return status;
    }

    public Routes getRoutes() {
        return routes.get(0);
    }
}
