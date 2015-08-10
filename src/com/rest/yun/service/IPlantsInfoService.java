package com.rest.yun.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rest.yun.beans.PlantsInfo;
import com.rest.yun.dto.PlantsExt;


public interface IPlantsInfoService {

	List<PlantsInfo> selectPlantsInfo();

	void save(PlantsExt plantsExt, HttpSession session);

	boolean validPlantsName(String plantsname, int plantsId);

	PlantsExt selectPlantsById(int id);

}
