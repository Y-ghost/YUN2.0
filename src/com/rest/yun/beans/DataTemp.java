package com.rest.yun.beans;

import java.util.Date;

public class DataTemp {
    private Integer id;

    private String code;

    private String contraltype;

    private String datacontext;

    private Date receivetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public String getContraltype() {
        return contraltype;
    }

    public void setContraltype(String contraltype) {
        this.contraltype = contraltype == null ? null : contraltype.trim();
    }

    public String getDatacontext() {
        return datacontext;
    }

    public void setDatacontext(String datacontext) {
        this.datacontext = datacontext == null ? null : datacontext.trim();
    }

    public Date getReceivetime() {
        return receivetime;
    }

    public void setReceivetime(Date receivetime) {
        this.receivetime = receivetime;
    }
}