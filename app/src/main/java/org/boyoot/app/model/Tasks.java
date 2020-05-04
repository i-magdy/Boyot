package org.boyoot.app.model;

public class Tasks {

    private String title;
    private String content;

    private String date;
    private boolean done;
    private String id;

    public Tasks() {
    }

    public Tasks(String title, String content, int priority, String date) {
        this.title = title;
        this.content = content;

        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public boolean isDone() {
        return done;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDone(boolean done) {
        this.done = done;
    }



    public String getDate() {
        return date;
    }

    public String getId() {
        return id;
    }


    public void setDate(String date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }
}
