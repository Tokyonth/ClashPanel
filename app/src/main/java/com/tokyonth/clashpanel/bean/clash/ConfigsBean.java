package com.tokyonth.clashpanel.bean.clash;

public class ConfigsBean {

    private int port;
    private int socket_port;
    private int redir_port;
    private boolean allow_lan;
    private String mode;
    private String log_level;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSocket_port() {
        return socket_port;
    }

    public void setSocket_port(int socket_port) {
        this.socket_port = socket_port;
    }

    public int getRedir_port() {
        return redir_port;
    }

    public void setRedir_port(int redir_port) {
        this.redir_port = redir_port;
    }

    public boolean isAllow_lan() {
        return allow_lan;
    }

    public void setAllow_lan(boolean allow_lan) {
        this.allow_lan = allow_lan;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLog_level() {
        return log_level;
    }

    public void setLog_level(String log_level) {
        this.log_level = log_level;
    }

}
