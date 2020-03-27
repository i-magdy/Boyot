package org.boyoot.app.model;

public class Price {

    private int cover;
    private int concealed;
    private int stand;
    private int window;
    private int split;
    private int offers;

    public Price() {
    }

    public Price(int cover, int concealed, int stand, int window, int split, int offers) {
        this.cover = cover;
        this.concealed = concealed;
        this.stand = stand;
        this.window = window;
        this.split = split;
        this.offers = offers;
    }

    public int getCover() {
        return cover;
    }

    public int getConcealed() {
        return concealed;
    }

    public int getStand() {
        return stand;
    }

    public int getWindow() {
        return window;
    }

    public int getSplit() {
        return split;
    }

    public int getOffers() {
        return offers;
    }

    public void setCover(int cover) {
        this.cover = cover;
    }

    public void setConcealed(int concealed) {
        this.concealed = concealed;
    }

    public void setStand(int stand) {
        this.stand = stand;
    }

    public void setWindow(int window) {
        this.window = window;
    }

    public void setSplit(int split) {
        this.split = split;
    }

    public void setOffers(int offers) {
        this.offers = offers;
    }
}
