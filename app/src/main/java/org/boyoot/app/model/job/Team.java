package org.boyoot.app.model.job;

public class Team {

    private String title;
    private int originalWorkerCount;
    private int pathNo;
    private int workers;
    private boolean divided;


    public Team() {
    }

    public Team(String title, int pathNo, int originalWorkerCount) {
        this.title = title;
        this.pathNo = pathNo;
        this.originalWorkerCount = originalWorkerCount;
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

    public void setOriginalWorkerCount(int originalWorkerCount) {
        this.originalWorkerCount = originalWorkerCount;
    }

    public int getOriginalWorkerCount() {
        return originalWorkerCount;
    }

    public void setDivided(boolean divided) {
        this.divided = divided;
    }

    public boolean isDivided() {
        return divided;
    }
}
