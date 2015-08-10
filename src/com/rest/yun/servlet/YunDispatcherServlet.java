package com.rest.yun.servlet;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.DispatcherServlet;

import com.rest.yun.exception.ServerException;
import com.rest.yun.util.CommonUtiles;

public class YunDispatcherServlet extends DispatcherServlet {

	private static final Logger logger = LoggerFactory.getLogger(YunDispatcherServlet.class);

	private static final long serialVersionUID = -7583067408596844161L;

	protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Enumeration<String> parames = request.getParameterNames();
		Map<String, String> map = new HashMap<String, String>();
		String method = request.getMethod();
		while (parames.hasMoreElements()) {
			String name = parames.nextElement();
			String param = request.getParameter(name);
			if ("GET".equalsIgnoreCase(method)) {
				param = CommonUtiles.fixedChinaCode(param);
			}
			map.put(name, param);
		}

		String url = request.getRequestURL().toString();

		logger.info(method + " " + url + ", paramters: " + map);
		long start = System.currentTimeMillis();
		try {
			super.doService(request, response);
		} catch (ServerException e) {
			// Catch custom exception to not print console
		}
		long end = System.currentTimeMillis();
		long takeTime = end - start;

		logger.info("Finish all logic , taken time: " + takeTime + "ms");

		// TODO delete the code
		// Mock user
//		User user = new User();
//		user.setId(1);
//		user.setLoginname("owen");
//		user.setUsername("owen");
//		request.getSession().setAttribute("user", user);
	}

}
