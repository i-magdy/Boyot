package org.boyoot.app.model.job;

import java.io.Serializable;

public class TimePickerModel implements Serializable {

    private int hourOfDay;
    private int min;

    public TimePickerModel(int hourOfDay, int min) {
        this.hourOfDay = hourOfDay;
        this.min = min;
    }

    public int getHourOfDay() {
        return hourOfDay;
    }

    public int getMin() {
        return min;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
