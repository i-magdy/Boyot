package org.boyoot.app.model.job;

public class Payment {

    private int value;
    private String text;
    private String method;
    private String by;
    private String discount;

    public Payment() {
    }

    public Payment(int value, String text, String method, String by) {
        this.value = value;
        this.text = text;
        this.method = method;
        this.by = by;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    public String getMethod() {
        return method;
    }

    public String getBy() {
        return by;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setBy(String by) {
        this.by = by;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public String getDiscount() {
        return discount;
    }
}
