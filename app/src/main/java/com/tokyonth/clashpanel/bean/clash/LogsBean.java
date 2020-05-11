package com.tokyonth.clashpanel.bean.clash;

public class LogsBean {

    private String type;
    private String payload;

    private String time;

    public LogsBean(String time, String type, String payload) {
        this.time = time;
        this.type = type;
        this.payload = payload;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

}
