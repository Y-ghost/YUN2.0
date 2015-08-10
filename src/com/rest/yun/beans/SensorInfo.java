package com.rest.yun.beans;

import java.util.Date;

public class SensorInfo {
    private Integer id;

    private Integer equipmentid;

    private Integer number;

    private Float parametera;

    private Float parameterb;

    private Float parameterc;

    private Float parameterd;

    private Integer createuser;

    private Date createtime;

    private Integer modifyuser;

    private Date modifytime;

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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Float getParametera() {
        return parametera;
    }

    public void setParametera(Float parametera) {
        this.parametera = parametera;
    }

    public Float getParameterb() {
        return parameterb;
    }

    public void setParameterb(Float parameterb) {
        this.parameterb = parameterb;
    }

    public Float getParameterc() {
        return parameterc;
    }

    public void setParameterc(Float parameterc) {
        this.parameterc = parameterc;
    }

    public Float getParameterd() {
        return parameterd;
    }

    public void setParameterd(Float parameterd) {
        this.parameterd = parameterd;
    }

    public Integer getCreateuser() {
        return createuser;
    }

    public void setCreateuser(Integer createuser) {
        this.createuser = createuser;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getModifyuser() {
        return modifyuser;
    }

    public void setModifyuser(Integer modifyuser) {
        this.modifyuser = modifyuser;
    }

    public Date getModifytime() {
        return modifytime;
    }

    public void setModifytime(Date modifytime) {
        this.modifytime = modifytime;
    }
}