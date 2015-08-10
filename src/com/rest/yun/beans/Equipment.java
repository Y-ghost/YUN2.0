package com.rest.yun.beans;

import java.util.Date;

public class Equipment {
    private Integer id;

    private String name;

    private Integer controlhostid;

    private String code;

    private Integer irrigationtype;

    private Float soilweight;

    private Float soilwater;

    private Double area;

    private Integer soilname;
    
    private Integer plantsname;

    private Integer rootdepth;

    private Float humidityup;

    private Float humiditydown;

    private Float temperatureup;

    private Float temperaturedown;

    private String week;

    private String timeonestart;

    private String timeoneend;

    private String timetwostart;

    private String timetwoend;

    private String timethreestart;

    private String timethreeend;

    private Integer fowparameter;

    private Integer createuser;

    private Date createtime;

    private Integer modifyuser;

    private Date modifytime;
    
    private Project project;
    
    private PlantsInfo plantsInfo;
    
    private SoilInfo SoilInfo;
    
    private Long watervalue;//用于赋值
    
	public Long getWatervalue() {
		return watervalue;
	}

	public void setWatervalue(Long watervalue) {
		this.watervalue = watervalue;
	}

	public Integer getSoilname() {
		return soilname;
	}

	public void setSoilname(Integer soilname) {
		this.soilname = soilname;
	}

	public SoilInfo getSoilInfo() {
		return SoilInfo;
	}

	public void setSoilInfo(SoilInfo soilInfo) {
		SoilInfo = soilInfo;
	}

	public PlantsInfo getPlantsInfo() {
		return plantsInfo;
	}

	public void setPlantsInfo(PlantsInfo plantsInfo) {
		this.plantsInfo = plantsInfo;
	}

	public Project getProject() {
		return project;
	}

	public void setProject(Project project) {
		this.project = project;
	}

	public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public Integer getControlhostid() {
        return controlhostid;
    }

    public void setControlhostid(Integer controlhostid) {
        this.controlhostid = controlhostid;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code == null ? null : code.trim();
    }

    public Integer getIrrigationtype() {
        return irrigationtype;
    }

    public void setIrrigationtype(Integer irrigationtype) {
        this.irrigationtype = irrigationtype;
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

    public Double getArea() {
        return area;
    }

    public void setArea(Double area) {
        this.area = area;
    }

    public Integer getPlantsname() {
        return plantsname;
    }

    public void setPlantsname(Integer plantsname) {
        this.plantsname = plantsname;
    }

    public Integer getRootdepth() {
        return rootdepth;
    }

    public void setRootdepth(Integer rootdepth) {
        this.rootdepth = rootdepth;
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

    public String getWeek() {
        return week;
    }

    public void setWeek(String week) {
        this.week = week == null ? null : week.trim();
    }

    public String getTimeonestart() {
        return timeonestart;
    }

    public void setTimeonestart(String timeonestart) {
        this.timeonestart = timeonestart;
    }

    public String getTimeoneend() {
        return timeoneend;
    }

    public void setTimeoneend(String timeoneend) {
        this.timeoneend = timeoneend;
    }

    public String getTimetwostart() {
        return timetwostart;
    }

    public void setTimetwostart(String timetwostart) {
        this.timetwostart = timetwostart;
    }

    public String getTimetwoend() {
        return timetwoend;
    }

    public void setTimetwoend(String timetwoend) {
        this.timetwoend = timetwoend;
    }

    public String getTimethreestart() {
        return timethreestart;
    }

    public void setTimethreestart(String timethreestart) {
        this.timethreestart = timethreestart;
    }

    public String getTimethreeend() {
        return timethreeend;
    }

    public void setTimethreeend(String timethreeend) {
        this.timethreeend = timethreeend;
    }

    public Integer getFowparameter() {
        return fowparameter;
    }

    public void setFowparameter(Integer fowparameter) {
        this.fowparameter = fowparameter;
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