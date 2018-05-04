package com.bizwell.datasource.json;

/**
 * Created by charles on 2017/10/25.
 * 用于Response 的Json 类
 */
public class ResponseJson {

    private Long code;
    private String message;
    // json 字符串形式
    private Object data;

    public ResponseJson(Long code,String message,Object data){
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public long getCode() {
        return code;
    }

    public void setCode(long code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
