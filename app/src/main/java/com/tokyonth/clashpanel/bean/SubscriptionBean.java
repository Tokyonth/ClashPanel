package com.tokyonth.clashpanel.bean;

public class SubscriptionBean {

    private String name;
    private String url;
    private String color;
    private int id;
    private boolean selected = false;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public SubscriptionBean() {

    }

    public SubscriptionBean(String name, String url, String color, boolean selected) {
        this.name = name;
        this.url = url;
        this.color = color;
        this.selected = selected;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getName() {
        return name;
    }

    public void setName(String title) {
        this.name = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

}
