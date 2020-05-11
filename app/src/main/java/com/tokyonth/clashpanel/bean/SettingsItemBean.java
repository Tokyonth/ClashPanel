package com.tokyonth.clashpanel.bean;

public class SettingsItemBean {

    private String title;
    private String sub;
    private String color;
    private int icon;

    public SettingsItemBean(String title, String sub, String color, int icon) {
        this.title = title;
        this.sub = sub;
        this.color = color;
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSub() {
        return sub;
    }

    public void setSub(String sub) {
        this.sub = sub;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

}
