package org.boyoot.app.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.util.Date;

public class Tasks {

    private String title;
    private String content;
    private boolean seen;
    @ServerTimestamp
    private Date date;
    private boolean done;
    private String id;

    public Tasks() {
    }

    public Tasks(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
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



    public Date getDate() {
        return date;
    }

    public String getId() {
        return id;
    }


    public void setDate(Date date) {
        this.date = date;
    }

    public void setId(String id) {
        this.id = id;
    }
}
