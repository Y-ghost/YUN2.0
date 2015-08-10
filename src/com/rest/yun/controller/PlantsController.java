package com.rest.yun.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rest.yun.beans.PlantsInfo;
import com.rest.yun.dto.PlantsExt;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.listener.Login;
import com.rest.yun.service.IPlantsInfoService;
import com.rest.yun.util.CommonUtiles;

@Controller
@RequestMapping("/plants")
public class PlantsController {
	@Autowired
	private IPlantsInfoService plantsInfoService;

	/**
	 * @Title:       selectPlantsInfo
	 * @author:      杨贵松
	 * @time         2014年12月5日 下午11:43:06
	 * @Description: 查询植物列表
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "/selectPlantsInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectPlantsInfo() {
		List<PlantsInfo> list = plantsInfoService.selectPlantsInfo();
		return new ResponseWrapper(list);
	}

	/**
	 * @Title:       save
	 * @author:      杨贵松
	 * @time         2014年12月5日 下午11:43:36
	 * @Description: 添加植物信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="/save",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper save(@RequestBody PlantsExt plantsExt, HttpSession session) {
		plantsInfoService.save(plantsExt,session);
		return new ResponseWrapper(true);
	}
	
	/**
	 * @Title:       validName
	 * @author:      杨贵松
	 * @time         2014年12月23日 下午5:54:24
	 * @Description: 校验植物名称是否存在
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="/validName",method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper validName(@RequestParam String plantsname, @RequestParam(required = false, defaultValue = "0") int plantsId) {
		boolean result = plantsInfoService.validPlantsName(CommonUtiles.fixedChinaCode(plantsname), plantsId);
		return new ResponseWrapper(result);
	}
	/**
	 * @Title:       getPlants
	 * @author:      杨贵松
	 * @time         2015年3月2日 下午8:51:10
	 * @Description: 查询植物详细信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper getPlants(@PathVariable int id) {
		PlantsExt plants = plantsInfoService.selectPlantsById(id);
		return new ResponseWrapper(plants);
	}
}
