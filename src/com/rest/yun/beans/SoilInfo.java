package com.rest.yun.beans;

import java.util.Date;

public class SoilInfo {
    private Integer id;

    private String soiltype;

    private Float soilweight;

    private Float soilwater;

    private Float parametera;

    private Float parameterb;

    private Float parameterc;

    private Float parameterd;

    private String province;

    private String city;

    private String county;

    private String address;
    //2014-12-15 21:33:04 添加土壤湿度标定四组数据
    private Float waterVal1;
    
    private Float originalVal1;
    
    private Float waterVal2;
    
    private Float originalVal2;
    
    private Float waterVal3;
    
    private Float originalVal3;
    
    private Float waterVal4;
    
    private Float originalVal4;

    private Date createtime;

    private Integer createuser;

    private Date modifytime;

    private Integer modifyuser;

    public Integer getId() {
        return id;
    }

	public Float getWaterVal1() {
		return waterVal1;
	}

	public void setWaterVal1(Float waterVal1) {
		this.waterVal1 = waterVal1;
	}

	public Float getOriginalVal1() {
		return originalVal1;
	}

	public void setOriginalVal1(Float originalVal1) {
		this.originalVal1 = originalVal1;
	}

	public Float getWaterVal2() {
		return waterVal2;
	}

	public void setWaterVal2(Float waterVal2) {
		this.waterVal2 = waterVal2;
	}

	public Float getOriginalVal2() {
		return originalVal2;
	}

	public void setOriginalVal2(Float originalVal2) {
		this.originalVal2 = originalVal2;
	}

	public Float getWaterVal3() {
		return waterVal3;
	}

	public void setWaterVal3(Float waterVal3) {
		this.waterVal3 = waterVal3;
	}

	public Float getOriginalVal3() {
		return originalVal3;
	}

	public void setOriginalVal3(Float originalVal3) {
		this.originalVal3 = originalVal3;
	}

	public Float getWaterVal4() {
		return waterVal4;
	}

	public void setWaterVal4(Float waterVal4) {
		this.waterVal4 = waterVal4;
	}

	public Float getOriginalVal4() {
		return originalVal4;
	}

	public void setOriginalVal4(Float originalVal4) {
		this.originalVal4 = originalVal4;
	}

	public void setId(Integer id) {
        this.id = id;
    }

    public String getSoiltype() {
        return soiltype;
    }

    public void setSoiltype(String soiltype) {
        this.soiltype = soiltype == null ? null : soiltype.trim();
    }

    public Float getSoilweight() {
        return soilweight;
    }

    public void setSoilweight(Float soilweight) {
        this.soilweight = soilweight;
    }

    public Float getSoilwater() {
        return soilwater;
    }

    public void setSoilwater(Float soilwater) {
        this.soilwater = soilwater;
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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province == null ? null : province.trim();
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city == null ? null : city.trim();
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county == null ? null : county.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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