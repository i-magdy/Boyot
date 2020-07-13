package org.boyoot.app.model;

import java.util.List;

public class Branch {


    private String title;
    private String branchId;
    private String dayStart;
    private int start;
    private String dayEnd;
    private int end;
    private List<Car> cars;
    private String location;
    private MapConfig mapConfig;

    public Branch() {
    }

    public Branch(String title, String branchId, String dayStart, String dayEnd, String location) {
        this.title = title;
        this.branchId = branchId;
        this.dayStart = dayStart;
        this.dayEnd = dayEnd;
        this.location = location;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setBranchId(String branchId) {
        this.branchId = branchId;
    }

    public void setDayStart(String dayStart) {
        this.dayStart = dayStart;
    }

    public void setDayEnd(String dayEnd) {
        this.dayEnd = dayEnd;
    }

    public void setCars(List<Car> cars) {
        this.cars = cars;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setMapConfig(MapConfig mapConfig) {
        this.mapConfig = mapConfig;
    }

    public String getTitle() {
        return title;
    }

    public String getBranchId() {
        return branchId;
    }

    public String getDayStart() {
        return dayStart;
    }

    public String getDayEnd() {
        return dayEnd;
    }

    public List<Car> getCars() {
        return cars;
    }

    public String getLocation() {
        return location;
    }

    public MapConfig getMapConfig() {
        return mapConfig;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public void setEnd(int end) {
        this.end = end;
    }
}
