package org.boyoot.app.model.job;

import org.boyoot.app.model.direction.Distance;
import org.boyoot.app.model.direction.Duration;
import org.boyoot.app.model.direction.Steps;

import java.util.List;

public class Directions {

    private Distance distance;
    private Duration duration;
    private List<Steps> steps;

    public Directions() {
    }

    public Distance getDistance() {
        return distance;
    }

    public Duration getDuration() {
        return duration;
    }

    public List<Steps> getSteps() {
        return steps;
    }

    public void setDistance(Distance distance) {
        this.distance = distance;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setSteps(List<Steps> steps) {
        this.steps = steps;
    }
}
