package com.tokyonth.clashpanel.bean.clash.rules;

import java.util.ArrayList;

public class RulesBean {

    private ArrayList<Rules> rules;

    public ArrayList<Rules> getRules() {
        return rules;
    }

    public void setRules(ArrayList<Rules> rules) {
        this.rules = rules;
    }

    public static class Rules {

        private String type;
        private String payload;
        private String proxy;

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

        public String getProxy() {
            return proxy;
        }

        public void setProxy(String proxy) {
            this.proxy = proxy;
        }

    }


}
