package com.rest.yun.service;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.rest.yun.beans.EquipmentData;
import com.rest.yun.beans.EquipmentStatus;
import com.rest.yun.dto.EquipmentDataExt;
import com.rest.yun.dto.EquipmentExt;

public interface IStatisticService {

	List<EquipmentExt<EquipmentStatus>> waterList(Integer pId, Integer eId,
			Date startDate, Date endDate);

	List<EquipmentDataExt<EquipmentData>> humidityList(Integer pId,
			Integer eId, Date startDate, Date endDate);

	String waterExport(Integer pId, Integer eId, Date startDate, Date endDate,
			HttpServletRequest request);

	String humidityExport(Integer pId, Integer eId, Date startDate, Date endDate,
			HttpServletRequest request);

	void exportExcel(String fileName, HttpServletRequest request,
			HttpServletResponse response);

}
