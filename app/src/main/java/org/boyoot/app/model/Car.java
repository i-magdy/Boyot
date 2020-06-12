package org.boyoot.app.model;

public class Car {

    private String title;
    private String carNumber;
    private int pathNo;
    private int worker;

    public Car() {
    }

    public Car(String title, String carNumber,int pathNo, int worker) {
        this.title = title;
        this.carNumber = carNumber;
        this.worker = worker;
        this.pathNo = pathNo;
    }

    public String getTitle() {
        return title;
    }

    public String getCarNumber() {
        return carNumber;
    }

    public int getWorker() {
        return worker;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCarNumber(String carNumber) {
        this.carNumber = carNumber;
    }

    public void setWorker(int worker) {
        this.worker = worker;
    }

    public void setPathNo(int pathNo) {
        this.pathNo = pathNo;
    }

    public int getPathNo() {
        return pathNo;
    }
}
