package com.rest.yun.beans;

import java.util.Date;

public class EquipmentData {
    private Integer id;

    private Integer sensorid;

    private Float humidity;

    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSensorid() {
        return sensorid;
    }

    public void setSensorid(Integer sensorid) {
        this.sensorid = sensorid;
    }

    public Float getHumidity() {
        return humidity;
    }

    public void setHumidity(Float humidity) {
        this.humidity = humidity;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}