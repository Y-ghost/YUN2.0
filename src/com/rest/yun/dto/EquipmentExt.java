package com.rest.yun.dto;

import java.io.Serializable;
import java.util.List;

import com.rest.yun.beans.Equipment;
import com.rest.yun.beans.EquipmentStatus;

public class EquipmentExt<T> implements Serializable {

	private static final long serialVersionUID = -9111114856097767106L;

	private int id;
	private String name;
	private String code;
	private int controlHostId;
	private int irrigationType;//灌溉控制类型
	private double area;//节点控制面积
	private int fowParameter;//电磁阀流量参数
	private Equipment equipment;//节点
	private EquipmentStatus equipmentStatus;
	private List<T> result;

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getControlHostId() {
		return controlHostId;
	}

	public void setControlHostId(int controlHostId) {
		this.controlHostId = controlHostId;
	}

	public int getIrrigationType() {
		return irrigationType;
	}

	public void setIrrigationType(int irrigationType) {
		this.irrigationType = irrigationType;
	}

	public double getArea() {
		return area;
	}

	public void setArea(double area) {
		this.area = area;
	}

	public int getFowParameter() {
		return fowParameter;
	}

	public void setFowParameter(int fowParameter) {
		this.fowParameter = fowParameter;
	}

	public EquipmentStatus getEquipmentStatus() {
		return equipmentStatus;
	}

	public void setEquipmentStatus(EquipmentStatus equipmentStatus) {
		this.equipmentStatus = equipmentStatus;
	}

	public List<T> getResult() {
		return result;
	}

	public void setResult(List<T> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EquipmentExt [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", code=");
		builder.append(code);
		builder.append(", controlHostId=");
		builder.append(controlHostId);
		builder.append(", irrigationType=");
		builder.append(irrigationType);
		builder.append(", area=");
		builder.append(area);
		builder.append(", fowParameter=");
		builder.append(fowParameter);
		builder.append(", equipment=");
		builder.append(equipment);
		builder.append(", equipmentStatus=");
		builder.append(equipmentStatus);
		builder.append(", result=");
		builder.append(result);
		builder.append("]");
		return builder.toString();
	}

}
