package org.boyoot.app.model;

public class Work {

    private String interval;
    private String split;
    private String window;
    private String cover;
    private String stand;
    private String offers;

    public Work() {
    }

    public Work(String interval, String split, String window, String cover, String stand, String offers) {
        this.interval = interval;
        this.split = split;
        this.window = window;
        this.cover = cover;
        this.stand = stand;
        this.offers = offers;
    }

    public String getInterval() {
        return interval;
    }

    public String getSplit() {
        return split;
    }

    public String getWindow() {
        return window;
    }

    public String getCover() {
        return cover;
    }

    public String getStand() {
        return stand;
    }

    public String getOffers() {
        return offers;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setSplit(String split) {
        this.split = split;
    }

    public void setWindow(String window) {
        this.window = window;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setStand(String stand) {
        this.stand = stand;
    }

    public void setOffers(String offers) {
        this.offers = offers;
    }
}
