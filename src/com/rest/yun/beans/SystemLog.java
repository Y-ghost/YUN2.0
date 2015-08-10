package com.rest.yun.beans;

import java.util.Date;

public class SystemLog {
    private Integer id;

    private Integer userid;

    private String logcontext;

    private Integer logtype;

    private Date logtime;

    private String logstatus;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public String getLogcontext() {
        return logcontext;
    }

    public void setLogcontext(String logcontext) {
        this.logcontext = logcontext == null ? null : logcontext.trim();
    }

    public Integer getLogtype() {
        return logtype;
    }

    public void setLogtype(Integer logtype) {
        this.logtype = logtype;
    }

    public Date getLogtime() {
        return logtime;
    }

    public void setLogtime(Date logtime) {
        this.logtime = logtime;
    }

	public String getLogstatus() {
		return logstatus;
	}

	public void setLogstatus(String logstatus) {
		this.logstatus = logstatus;
	}
}