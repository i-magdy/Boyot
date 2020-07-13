package org.boyoot.app.model.job;

public class FinishTime {

    private long value;

    public FinishTime() {
    }

    public FinishTime(long value) {
        this.value = value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public long getValue() {
        return value;
    }
}
