package com.rest.yun.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class IndexController {

	@RequestMapping(value = "indexs/**", method = RequestMethod.GET)
	public String handlJsp(HttpServletRequest request, HttpServletResponse resp) {
		String restOfTheUrl = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String url = restOfTheUrl.replaceAll("(/indexs)(/?)", "");
		if (StringUtils.isEmpty(url)) {
			url = "/index";
		}

		String path = request.getContextPath();
		String basePath = path + "/";

		request.setAttribute("basePath", basePath);
		return url;
	}

}
