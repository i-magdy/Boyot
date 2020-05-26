package org.boyoot.app.model.direction;

import java.util.List;

public class Legs {
    private Duration duration;
    private Distance distance;
    private List<Steps> steps;

    public Duration getDuration() {
        return duration;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public Distance getDistance() {
        return distance;
    }
}
