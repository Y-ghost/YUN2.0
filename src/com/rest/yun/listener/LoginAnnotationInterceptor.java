package com.rest.yun.listener;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.util.JSONConver;

/**
 * @project:					yun 
 * @Title: 						LoginAnnotationInterceptor.java 		
 * @Package 					com.rest.yun.listener
 * @Description: 				检查是否已经登录
 * @author 						杨贵松   
 * @date 						2014年11月19日 下午8:22:43
 * @version 					V2.0
 */
public class LoginAnnotationInterceptor extends HandlerInterceptorAdapter {

	final Logger LOG = Logger.getLogger(LoginAnnotationInterceptor.class);

	/**
	 * 验证用户是否登录
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler){

		HandlerMethod handler2 = (HandlerMethod) handler;
		Login login = handler2.getMethodAnnotation(Login.class);

		if (null == login) {
			// 没有声明权限,放行
			return true;
		}

		HttpSession session = request.getSession();
		User user = (User) session.getAttribute(Constants.USER);
		if (null == user) {
			// 需要登录
			if (login.value() == ResultTypeEnum.page) {
				//采用传统页面进行登录提示
				try {
					request.getRequestDispatcher("/login.jsp").forward(request, response);
				} catch (ServletException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (login.value() == ResultTypeEnum.json) {
				//采用ajax方式的进行登录提示
				ResponseWrapper wrapper = new ResponseWrapper();
				wrapper.setCode(ErrorCode.NO_LOGIN.getCode());
				wrapper.setMessage(ErrorCode.NO_LOGIN.getMessage());
				String result = JSONConver.converToJson(wrapper);
				responseData(response, result);
			}
			return false;
		}
		return true;

	}
	
	private void responseData(HttpServletResponse response, String result) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json");
		try {
			response.getWriter().println(result);
			response.getWriter().flush();
		} catch (IOException e) {
			LOG.error(e.getMessage());
		}
	}

}
