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

import com.rest.yun.beans.Equipment;
import com.rest.yun.beans.EquipmentData;
import com.rest.yun.beans.EquipmentStatus;
import com.rest.yun.beans.SensorInfo;
import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.EquipmentExt;
import com.rest.yun.dto.Page;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.listener.Login;
import com.rest.yun.service.IEquipmentExtService;
import com.rest.yun.service.IEquipmentService;
import com.rest.yun.util.JSONConver;

@Controller
@RequestMapping("/equipment")
public class EquipmentController {
	@Autowired
	private IEquipmentService equipmentService;
	@Autowired
	private IEquipmentExtService equipmentExtService;

	/**
	 * @Title:       selectEquipments
	 * @author:      杨贵松
	 * @time         2014年12月5日 下午10:07:53
	 * @Description: 查询节点详细信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "/selectEquipments", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectEquipments(@RequestParam Integer pId) {
		List<EquipmentExt<SensorInfo>> list = equipmentService.selectEquipments(pId);
		return new ResponseWrapper(list);
	}
	/**
	 * @Title: selectEquipmentExt
	 * @author: 杨贵松
	 * @time 2014年11月6日 上午12:08:14
	 * @Description: 查询实时现场数据
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "/selectEquipmentExt", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectEquipmentExt(@RequestParam(required = false, defaultValue = "0") Integer pId) {
		List<EquipmentExt<EquipmentData>> list = equipmentService.selectEquipmentExt(pId);
		return new ResponseWrapper(list);
	}

	/**
	 * @Title: openEquipments
	 * @author: 杨贵松
	 * @time 2014年11月6日 下午11:35:40
	 * @Description: 开启灌溉
	 *               optionType为判断开启还是关闭操作，optionType=0表示开启，optionType=1表示关闭
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "/openOrCloseEquipments", method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper openOrCloseEquipments(@RequestParam Integer optionType, @RequestParam String id) {
		boolean flag = equipmentService.openEquipments(optionType, id);
		return new ResponseWrapper(flag);
	}
	
	/**
	 * @Title:       searchEquipment
	 * @author:      杨贵松
	 * @time         2014年11月22日 下午3:11:27
	 * @Description: 搜索节点
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "/searchEquipment", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper searchEquipment(@RequestParam Integer pId ) {
		List<EquipmentExt<SensorInfo>> list = equipmentService.searchEquipment(pId);
		return new ResponseWrapper(list);
	}

	/**
	 * @Title:       selectEqts
	 * @author:      杨贵松
	 * @time         2014年11月26日 下午12:04:41
	 * @Description: 分页查询节点列表
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectEqts(@RequestParam(required = false, defaultValue = "1") Integer pageNow,
			@RequestParam(required = false, defaultValue = "10") Integer pageSize, String criteria, HttpSession session) {

		Map<String, Object> criteriaMap = null;

		if (!StringUtils.isEmpty(criteria)) {
			criteriaMap = JSONConver.conver(criteria, Map.class);
		}

		Page<Equipment> page = equipmentService.selectEqtForList(session,pageNow, pageSize, criteriaMap);

		return new ResponseWrapper(page);
	}

	/**
	 * @Title:       save
	 * @author:      杨贵松
	 * @time         2014年11月26日 下午12:05:09
	 * @Description: 注册节点
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper save(@RequestBody List<EquipmentExt<SensorInfo>> list, HttpSession session) {
		equipmentService.save(list,session);
		return new ResponseWrapper(true);
	}

	/**
	 * 节点详细信息
	 * 
	 * @param projectId
	 * @return
	 */
	@RequestMapping(value = "{eqtId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper detail(@PathVariable int eqtId) {
		Equipment equipment = equipmentService.getEquipmentById(eqtId);
		return new ResponseWrapper(equipment);
	}
	
	/**
	 * @Title:       updateList
	 * @author:      杨贵松
	 * @time         2014年12月28日 下午4:42:15
	 * @Description: 批量设置节点及传感器信息
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="/updateList",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper updateList(@RequestBody List<Equipment> list, HttpSession session) {
		String result = equipmentExtService.updateList(list,session);
		return new ResponseWrapper(result);
	}

	/**
	 * @Title:       setListModel
	 * @author:      杨贵松
	 * @time         2015年1月31日 下午11:40:24
	 * @Description: 设置多节点模式
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="/setListModel",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper setListModel(@RequestBody List<Equipment> list, HttpSession session) {
		boolean flag = equipmentExtService.setListModel(list,session);
		return new ResponseWrapper(flag);
	}
	/**
	 * @Title:       setAutoParam
	 * @author:      杨贵松
	 * @time         2015年2月1日 下午8:44:09
	 * @Description: 设置多借点自控参数
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="/setAutoParam",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper setAutoParam(@RequestBody List<Equipment> list, HttpSession session) {
		boolean flag = equipmentExtService.setAutoParam(list,session);
		return new ResponseWrapper(flag);
	}
	/**
	 * @Title:       setTimeLen
	 * @author:      杨贵松
	 * @time         2015年2月2日 上午12:26:18
	 * @Description: 设置多节点时段
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="/setTimeLen",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper setTimeLen(@RequestBody List<Equipment> list, HttpSession session) {
		boolean flag = equipmentExtService.setTimeLen(list,session);
		return new ResponseWrapper(flag);
	}

	/**
	 * 更新节点
	 * 
	 * @param equipment
	 * @param session
	 * @return
	 */
	@Login
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseWrapper update(@RequestBody Equipment equipment, HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		equipmentService.updateEquipment(equipment, user.getId());
		return new ResponseWrapper(true);
	}

	/**
	 * 删除节点
	 * 
	 * @param eqtId
	 * @return
	 */
	@Login
	@RequestMapping(value = "{eqtId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseWrapper delete(@PathVariable int eqtId) {
		equipmentService.deleteEquipment(eqtId);
		return new ResponseWrapper(true);
	}
	
	/**
	 * @Title:       getRelData
	 * @author:      杨贵松
	 * @time         2015年2月5日 下午5:04:05
	 * @Description: 查询现场实时累计灌溉量
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "/getRelData", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper getRelData(@RequestParam Integer pId ) {
		List<EquipmentExt<EquipmentStatus>> list = equipmentExtService.getRelData(pId);
		return new ResponseWrapper(list);
	}
	
	/**
	 * @Title:       putData
	 * @author:      杨贵松
	 * @time         2015年2月8日 下午3:51:18
	 * @Description: 赋值
	 * @return       ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="/putData",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper putData(@RequestBody List<Equipment> list) {
		String result = equipmentExtService.putData(list);
		return new ResponseWrapper(result);
	}
}
