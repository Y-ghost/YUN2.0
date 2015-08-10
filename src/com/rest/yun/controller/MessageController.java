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

import com.rest.yun.beans.SystemLog;
import com.rest.yun.dto.Page;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.service.ISystemLogService;
import com.rest.yun.util.JSONConver;

@Controller
@RequestMapping("message")
public class MessageController {

	@Autowired
	private ISystemLogService systemLogService;

	/**
	 * Save System log
	 * 
	 * @param systemLog
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper save(@RequestBody SystemLog systemLog, HttpSession session) {
		// TODO
		return new ResponseWrapper(true);
	}

	/**
	 * Select system log list
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param criteria
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectSystemLogs(@RequestParam(required = false, defaultValue = "1") Integer pageNow,
			@RequestParam(required = false, defaultValue = "10") Integer pageSize, String criteria) {

		Map<String, Object> criteriaMap = null;

		if (!StringUtils.isEmpty(criteria)) {
			criteriaMap = JSONConver.conver(criteria, Map.class);
		}

		Page<SystemLog> page = systemLogService.selectSystemLogForListBy(pageNow, pageSize, criteriaMap);

		return new ResponseWrapper(page);
	}

	/**
	 * Get system log detail
	 * 
	 * @param logId
	 * @return
	 */
	@RequestMapping(value = "{logId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper detail(@PathVariable int logId) {
		SystemLog systemLog = systemLogService.getSystemLogById(logId);
		return new ResponseWrapper(systemLog);
	}

	/**
	 * Mark system log read
	 * 
	 * @param logId
	 * @return
	 */
	@RequestMapping(value = "{logId}", method = RequestMethod.PUT)
	@ResponseBody
	public ResponseWrapper markSystemLogRead(@PathVariable int logId) {
		systemLogService.markReadStatuById(logId);
		return new ResponseWrapper(true);
	}

}
