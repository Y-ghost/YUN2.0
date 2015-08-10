package com.rest.yun.beans;

import java.util.Date;

public class PlantsExp {
    private Integer id;

    private Integer plantsid;

    private String plantsseason;

    private Float humidityup;

    private Float humiditydown;

    private Float temperatureup;

    private Float temperaturedown;

    private Date startdate;

    private Date enddate;

    private Date createtime;

    private Integer createuser;

    private Date modifytime;

    private Integer modifyuser;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getPlantsid() {
        return plantsid;
    }

    public void setPlantsid(Integer plantsid) {
        this.plantsid = plantsid;
    }

    public String getPlantsseason() {
        return plantsseason;
    }

    public void setPlantsseason(String plantsseason) {
        this.plantsseason = plantsseason == null ? null : plantsseason.trim();
    }

    public Float getHumidityup() {
        return humidityup;
    }

    public void setHumidityup(Float humidityup) {
        this.humidityup = humidityup;
    }

    public Float getHumiditydown() {
        return humiditydown;
    }

    public void setHumiditydown(Float humiditydown) {
        this.humiditydown = humiditydown;
    }

    public Float getTemperatureup() {
        return temperatureup;
    }

    public void setTemperatureup(Float temperatureup) {
        this.temperatureup = temperatureup;
    }

    public Float getTemperaturedown() {
        return temperaturedown;
    }

    public void setTemperaturedown(Float temperaturedown) {
        this.temperaturedown = temperaturedown;
    }

    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getCreateuser() {
        return createuser;
    }

    public void setCreateuser(Integer createuser) {
        this.createuser = createuser;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }

    public Integer getModifyuser() {
        return modifyuser;
    }

    public void setModifyuser(Integer modifyuser) {
        this.modifyuser = modifyuser;
    }
}