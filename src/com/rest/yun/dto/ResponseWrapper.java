package com.rest.yun.dto;

public class ResponseWrapper {
    
    private String code;
    
    private String message;
    
    private Object data;
    
    public ResponseWrapper() {
    }
    
    public ResponseWrapper(Object data) {
        this.code = "200";
        this.data = data;
    }
    
    public String getCode() {
        return code;
    }

    public void setCode(String code) {
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ResponseWrapper [code=");
        builder.append(code);
        builder.append(", message=");
        builder.append(message);
        builder.append(", data=");
        builder.append(data);
        builder.append("]");
        return builder.toString();
    }
}
