package com.bizwell.json;

/**
 * Created by charles on 2017/10/25.
 * 用于接收Request json数据
 */
public class RequestJson {

    private String factId;
    private String jsonData;

    public String getFactId() {
        return factId;
    }

    public void setFactId(String factId) {
        this.factId = factId;
    }

    public String getJsonData() {
        return jsonData;
    }

    public void setJsonData(String jsonData) {
        this.jsonData = jsonData;
    }
}
