package com.rest.yun.controller;

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

import com.rest.yun.beans.ControlHost;
import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.listener.Login;
import com.rest.yun.service.IControlHostService;
import com.rest.yun.util.JSONConver;

@Controller
@RequestMapping("/host")
public class HostController {

	@Autowired
	private IControlHostService controlHostService;

	/**
	 * @Title: save
	 * @author: 杨贵松
	 * @Description: 添加Host
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper save(@RequestBody ControlHost host, HttpSession session) {
		// 获取当前登录用户
		User user = (User) session.getAttribute(Constants.USER);
		controlHostService.saveControlHost(host, user.getId());
		return new ResponseWrapper(true);
	}

	/**
	 * @Title: selectHost
	 * @author: 杨贵松
	 * @Description: 查询Host列表
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectHosts(@RequestParam(required = false, defaultValue = "1") Integer pageNow,
			@RequestParam(required = false, defaultValue = "10") Integer pageSize, String criteria, HttpSession session) {

		Map<String, Object> criteriaMap = null;

		if (!StringUtils.isEmpty(criteria)) {
			criteriaMap = JSONConver.conver(criteria, Map.class);
		}

		Page<Map<String, Object>> page = controlHostService.selectHostBy(session,pageNow, pageSize, criteriaMap);

		return new ResponseWrapper(page);
	}

	/**
	 * @Title: detailHost
	 * @author: 杨贵松
	 * @Description: 查看Host详情
	 * @return ResponseWrapper
	 * @throws
	 */
	@RequestMapping(value = "{hostId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper detailHost(@PathVariable int hostId) {
		Map<String, Object> host = controlHostService.getHostById(hostId);
		return new ResponseWrapper(host);
	}

	/**
	 * @Title: update
	 * @author: 杨贵松
	 * @Description: 更新Host
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseWrapper update(@RequestBody ControlHost host, HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		controlHostService.updateControlHost(host, user.getId());
		return new ResponseWrapper(true);
	}

	/**
	 * @Title: deleteProject
	 * @author: 杨贵松
	 * @Description: 删除一个项目
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "{hostId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseWrapper deleteProject(@PathVariable int hostId) {
		controlHostService.deleteControlHost(hostId);
		return new ResponseWrapper(true);
	}

	/**
	 * 验证Host code 是否唯一
	 * 
	 * @param hostCode
	 * @param hostId
	 * @return
	 */
	@RequestMapping(value = "validation", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper validHostCode(@RequestParam String hostCode, @RequestParam(required = false, defaultValue = "0") int hostId,
			@RequestParam(required = false, defaultValue = "0") int projectId) {
		boolean result = controlHostService.validHostCode(hostCode, hostId, projectId);
		return new ResponseWrapper(result);
	}

}
