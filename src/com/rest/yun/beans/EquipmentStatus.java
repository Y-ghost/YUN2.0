package com.rest.yun.beans;

import java.util.Date;

public class EquipmentStatus {
    private Integer id;

    private Integer equipmentid;

    private Float temperature;

    private Long watervalue;
    
    private Long currentvalue;

    private Float velocity;

    private String status;

    private Date createtime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getEquipmentid() {
        return equipmentid;
    }

    public void setEquipmentid(Integer equipmentid) {
        this.equipmentid = equipmentid;
    }

    public Float getTemperature() {
        return temperature;
    }

    public void setTemperature(Float temperature) {
        this.temperature = temperature;
    }

    public Long getWatervalue() {
        return watervalue;
    }

    public void setWatervalue(Long watervalue) {
        this.watervalue = watervalue;
    }

    public Long getCurrentvalue() {
		return currentvalue;
	}

	public void setCurrentvalue(Long currentvalue) {
		this.currentvalue = currentvalue;
	}

	public Float getVelocity() {
        return velocity;
    }

    public void setVelocity(Float velocity) {
        this.velocity = velocity;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status == null ? null : status.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }
}