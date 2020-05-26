package org.boyoot.app.model.job;

public class CurrentWork {

    private String interval;
    private int split;
    private int window;
    private int cover;
    private int stand;
    private int concealed;
    private boolean offer;
    private int discount;

    public CurrentWork() {
    }

    public CurrentWork(String interval, int split, int window, int cover, int stand, int concealed, boolean offer, int discount) {
        this.interval = interval;
        this.split = split;
        this.window = window;
        this.cover = cover;
        this.stand = stand;
        this.concealed = concealed;
        this.offer = offer;
        this.discount = discount;
    }

    public String getInterval() {
        return interval;
    }

    public int getSplit() {
        return split;
    }

    public int getWindow() {
        return window;
    }

    public int getCover() {
        return cover;
    }

    public int getStand() {
        return stand;
    }

    public int getConcealed() {
        return concealed;
    }

    public boolean isOffer() {
        return offer;
    }

    public int getDiscount() {
        return discount;
    }

    public void setInterval(String interval) {
        this.interval = interval;
    }

    public void setSplit(int split) {
        this.split = split;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public void setStand(int stand) {
        this.stand = stand;
    }

    public void setConcealed(int concealed) {
        this.concealed = concealed;
    }

    public void setOffer(boolean offer) {
        this.offer = offer;
    }

    public void setDiscount(int discount) {
        this.discount = discount;
    }
}
