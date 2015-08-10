package com.rest.yun.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rest.yun.beans.SoilInfo;
import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.listener.Login;
import com.rest.yun.service.ISoilInfoService;
import com.rest.yun.util.CommonUtiles;
import com.rest.yun.util.JSONConver;

@Controller
@RequestMapping("/soil")
public class SoilInfoController {
	@Autowired
	private ISoilInfoService soilInfoService;

	/**
	 * @Title:       selectEquipments
	 * @author:      杨贵松
	 * @time         2014年12月5日 下午11:53:18
	 * @Description: 查询土壤列表
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "/selectSoilInfo", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectEquipments() {
		List<SoilInfo> list = soilInfoService.selectSoilInfo();
		return new ResponseWrapper(list);
	}

	/**
	 * @Title:       selectSoils
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午6:42:41
	 * @Description: 分页查询土壤信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectSoils(@RequestParam(required = false, defaultValue = "1") Integer pageNow,
			@RequestParam(required = false, defaultValue = "10") Integer pageSize, String criteria, HttpSession session) {

		Map<String, Object> criteriaMap = null;

		if (!StringUtils.isEmpty(criteria)) {
			criteriaMap = JSONConver.conver(criteria, Map.class);
			// 处理中文乱码
			if (criteriaMap.containsKey(Constants.PROVINCE)) {
				String province = (String) criteriaMap.get(Constants.PROVINCE);
				province = CommonUtiles.decodeUrl(province);
				criteriaMap.put(Constants.PROVINCE, province);
			}

			if (criteriaMap.containsKey(Constants.CITY)) {
				String city = (String) criteriaMap.get(Constants.CITY);
				city = CommonUtiles.decodeUrl(city);
				criteriaMap.put(Constants.CITY, city);
			}
		}

		Page<SoilInfo> page = soilInfoService.selectSoilBy(pageNow, pageSize, criteriaMap, session);

		return new ResponseWrapper(page);
	}
	
	/**
	 * @Title:       detailProject
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午7:09:02
	 * @Description: 查询土壤详细信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "{id}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper detailSoil(@PathVariable int id) {
		SoilInfo soil = soilInfoService.selectSoilById(id);
		return new ResponseWrapper(soil);
	}
	/**
	 * @Title:       delSoil
	 * @author:      杨贵松
	 * @time         2015年3月2日 下午8:50:16
	 * @Description: 删除土壤信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseWrapper delSoil(@PathVariable int id) {
		soilInfoService.deleteSoil(id);
		return new ResponseWrapper(true);
	}
	/**
	 * @Title:       validProjectName
	 * @author:      杨贵松
	 * @time         2014年12月6日 下午9:43:54
	 * @Description: 验证土壤名是否存在
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "validName", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper validName(@RequestParam String soilType, @RequestParam(required = false, defaultValue = "0") int id) {
		boolean result = soilInfoService.validSoilName(CommonUtiles.fixedChinaCode(soilType), id);
		return new ResponseWrapper(result);
	}
	
	/**
	 * @Title:       save
	 * @author:      杨贵松
	 * @time         2014年12月5日 下午11:54:59
	 * @Description: 添加土壤信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="/save",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper save(@RequestBody SoilInfo soil, HttpSession session) {
		//....
		soilInfoService.save(soil,session);
		return new ResponseWrapper(true);
	}
	
	/**
	 * @Title:       update
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午7:41:49
	 * @Description: 更新土壤信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseWrapper update(@RequestBody SoilInfo soil, HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		soilInfoService.updateSoil(soil, user.getId());
		return new ResponseWrapper(true);
	}
}
