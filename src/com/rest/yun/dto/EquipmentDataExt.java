package com.rest.yun.dto;

import java.io.Serializable;
import java.util.List;

import com.rest.yun.beans.Equipment;
import com.rest.yun.beans.SensorInfo;

public class EquipmentDataExt<T> implements Serializable {

	private static final long serialVersionUID = -4320342366410186630L;
	
	private Equipment equipment;//节点
	private SensorInfo sensor;//传感器
	private List<T> result;

	public Equipment getEquipment() {
		return equipment;
	}

	public void setEquipment(Equipment equipment) {
		this.equipment = equipment;
	}

	public SensorInfo getSensor() {
		return sensor;
	}

	public void setSensor(SensorInfo sensor) {
		this.sensor = sensor;
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
		builder.append("EquipmentExt [equipment=");
		builder.append(equipment);
		builder.append(", sensor=");
		builder.append(sensor);
		builder.append(", result=");
		builder.append(result);
		builder.append("]");
		return builder.toString();
	}

}
