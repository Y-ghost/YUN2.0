package com.rest.yun.controller;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rest.yun.beans.EquipmentData;
import com.rest.yun.beans.EquipmentStatus;
import com.rest.yun.dto.EquipmentDataExt;
import com.rest.yun.dto.EquipmentExt;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.listener.Login;
import com.rest.yun.service.IStatisticService;

@Controller
@RequestMapping("/statistic")
public class StatisticController {

	@Autowired
	private IStatisticService statisticService;

	/**
	 * @Title:       waterList
	 * @author:      杨贵松
	 * @time         2015年1月14日 上午4:51:48
	 * @Description: 统计灌溉用水量
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="waterList",method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper waterList(@RequestParam Integer pId, @RequestParam Integer eId,@RequestParam Date startDate, @RequestParam Date endDate) {
		List<EquipmentExt<EquipmentStatus>> list = statisticService.waterList(pId,eId,startDate, endDate);
		return new ResponseWrapper(list);
	}
	
	/**
	 * @Title:       humidityList
	 * @author:      杨贵松
	 * @time         2015年1月14日 上午4:52:24
	 * @Description: 统计灌区湿度值
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="humidityList",method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper humidityList(@RequestParam Integer pId, @RequestParam Integer eId,@RequestParam Date startDate, @RequestParam Date endDate) {
		List<EquipmentDataExt<EquipmentData>> list = statisticService.humidityList(pId,eId,startDate, endDate);
		return new ResponseWrapper(list);
	}

	/**
	 * @Title:       download
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午7:02:15
	 * @Description: 生成用水量Excel
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="waterExport",method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper waterExport(@RequestParam Integer pId, @RequestParam Integer eId,@RequestParam Date startDate, @RequestParam Date endDate, HttpServletRequest request){
		String fileName = statisticService.waterExport(pId,eId,startDate, endDate,request);
		return new ResponseWrapper(fileName);
	}
	
	/**
	 * @Title:       humidityExport
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午7:31:17
	 * @Description: 生成湿度值Excel
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="humidityExport",method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper humidityExport(@RequestParam Integer pId, @RequestParam Integer eId,@RequestParam Date startDate, @RequestParam Date endDate, HttpServletRequest request){
		String fileName = statisticService.humidityExport(pId,eId,startDate, endDate,request);
		return new ResponseWrapper(fileName);
	}
	
	/**
	 * @Title:       export
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午7:59:41
	 * @Description: 导出Excel
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="exportExcel",method=RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper exportExcel( @RequestParam String fileName, HttpServletRequest request, HttpServletResponse response){
		statisticService.exportExcel(fileName,request,response);
		return null;
	}
}
