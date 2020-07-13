package org.boyoot.app.model.job;

public class Team {

    private String title;
    private int pathNo;
    private int workers;

    public Team() {
    }

    public Team(String title, int pathNo, int workers) {
        this.title = title;
        this.pathNo = pathNo;
        this.workers = workers;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setPathNo(int pathNo) {
        this.pathNo = pathNo;
    }

    public void setWorkers(int workers) {
        this.workers = workers;
    }

    public String getTitle() {
        return title;
    }

    public int getPathNo() {
        return pathNo;
    }

    public int getWorkers() {
        return workers;
    }
}
