package com.rest.yun.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.rest.yun.beans.SoilInfo;
import com.rest.yun.dto.Page;


public interface ISoilInfoService {

	List<SoilInfo> selectSoilInfo();

	boolean validSoilName(String soiltype, int soilId);

	void save(SoilInfo soil, HttpSession session);

	Page<SoilInfo> selectSoilBy(Integer pageNow, Integer pageSize,
			Map<String, Object> criteriaMap, HttpSession session);

	SoilInfo selectSoilById(int id);

	void deleteSoil(int id);

	void updateSoil(SoilInfo soil, Integer id);

}
