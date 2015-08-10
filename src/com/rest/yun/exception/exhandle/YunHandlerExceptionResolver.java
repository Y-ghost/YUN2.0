package com.rest.yun.exception.exhandle;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.AbstractHandlerExceptionResolver;

import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.util.JSONConver;

public class YunHandlerExceptionResolver extends AbstractHandlerExceptionResolver {

	private static Logger LOG = LoggerFactory.getLogger(YunHandlerExceptionResolver.class);

	private ModelAndView handException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		String requestMethd = request.getMethod();
		String requestURI = request.getRequestURI();
		LOG.error("Failed request: " + requestMethd + " " + requestURI);

		// Handle ServerException first
		if (ex instanceof ServerException) {
			ResponseWrapper wrapper = new ResponseWrapper();
			ErrorCode errorCode = ((ServerException) ex).getErrorCode();
			wrapper.setCode(errorCode.getCode());
			wrapper.setMessage(errorCode.getMessage());
			String result = JSONConver.converToJson(wrapper);
			responseData(response, result);
			return null;
		}

		// unknow exception
		LOG.error("Exception information: ", ex);
		ResponseWrapper wrapper = new ResponseWrapper();
		wrapper.setCode(ErrorCode.SERVER_ERROR.getCode());
		wrapper.setMessage(ErrorCode.SERVER_ERROR.getMessage());
		String result = JSONConver.converToJson(wrapper);
		responseData(response, result);
		return null;
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

	@Override
	protected ModelAndView doResolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		return handException(request, response, handler, ex);
	}
}
