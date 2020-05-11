package com.tokyonth.clashpanel.bean.yaml;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ClashConfig implements Serializable {

    private int port;

    @SerializedName("socks-port")
    private int socksPort;

    @SerializedName("redir-port")
    private int redirPort;

    @SerializedName("allow-lan")
    private boolean allowLan;

    private String mode;

    @SerializedName("log-level")
    private String logLevel;

    @SerializedName("external-controller")
    private String externalController;

    private String secret;

    @SerializedName("Proxy")
    private Proxy proxy;

    @SerializedName("Proxy Group")
    private ProxyGroup proxyGroup;

    @SerializedName("Rule")
    private Rule rule;

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public int getSocksPort() {
        return socksPort;
    }

    public void setSocksPort(int socksPort) {
        this.socksPort = socksPort;
    }

    public int getRedirPort() {
        return redirPort;
    }

    public void setRedirPort(int redirPort) {
        this.redirPort = redirPort;
    }

    public boolean isAllowLan() {
        return allowLan;
    }

    public void setAllowLan(boolean allowLan) {
        this.allowLan = allowLan;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public String getLogLevel() {
        return logLevel;
    }

    public void setLogLevel(String logLevel) {
        this.logLevel = logLevel;
    }

    public String getExternalController() {
        return externalController;
    }

    public void setExternalController(String externalController) {
        this.externalController = externalController;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public Proxy getProxy() {
        return proxy;
    }

    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

    public ProxyGroup getProxyGroup() {
        return proxyGroup;
    }

    public void setProxyGroup(ProxyGroup proxyGroup) {
        this.proxyGroup = proxyGroup;
    }

    public Rule getRule() {
        return rule;
    }

    public void setRule(Rule rule) {
        this.rule = rule;
    }

}
