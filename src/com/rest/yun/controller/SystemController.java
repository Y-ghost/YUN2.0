package com.rest.yun.controller;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.rest.yun.dto.ResponseWrapper;

@Controller
@RequestMapping("/system")
public class SystemController {

	/**
	 * @Title:       getSystemTime
	 * @author:      杨贵松
	 * @time         2014年12月25日 下午10:13:32
	 * @Description: 获取系统时间
	 * @return       ResponseWrapper
	 * @throws
	 */

	@RequestMapping(value="getSystemTime",method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper getSystemTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String time = sdf.format(new Date());
		return new ResponseWrapper(time);
	}
}
