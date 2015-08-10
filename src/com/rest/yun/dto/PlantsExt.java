package com.rest.yun.dto;

import java.io.Serializable;
import java.util.List;

import com.rest.yun.beans.PlantsExp;
import com.rest.yun.beans.PlantsInfo;

public class PlantsExt implements Serializable {

	private static final long serialVersionUID = -9111114856097767106L;

	private PlantsInfo plants;
	private List<PlantsExp> result;

	public PlantsInfo getPlants() {
		return plants;
	}

	public void setPlants(PlantsInfo plants) {
		this.plants = plants;
	}

	public List<PlantsExp> getResult() {
		return result;
	}

	public void setResult(List<PlantsExp> result) {
		this.result = result;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlantsExt [plants=");
		builder.append(plants);
		builder.append(", result=");
		builder.append(result);
		builder.append("]");
		return builder.toString();
	}

}
