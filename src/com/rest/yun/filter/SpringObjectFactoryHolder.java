package com.rest.yun.filter;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoader;


/**
 * @project:					yun 
 * @Title: 						SpringObjectFactoryHolder.java 		
 * @Package 					com.rest.yun.filter		
 * @Description: 				手动获取Spring上下文，便于未注入的依赖，手动注入
 * @author 						杨贵松   
 * @date 						2014年3月7日 下午7:25:47 
 * @version 					V1.0
 */
@Component
public class SpringObjectFactoryHolder implements ApplicationContextAware{
	private static ApplicationContext context;
	
	public static ApplicationContext getContext() {
		if (null == SpringObjectFactoryHolder.context) {
			SpringObjectFactoryHolder.context = ContextLoader.getCurrentWebApplicationContext();
		}
		if (null == SpringObjectFactoryHolder.context) {
			SpringObjectFactoryHolder.context = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
		}
		return context;
	}
	
	@Override
	public void setApplicationContext(ApplicationContext context) throws BeansException {
		SpringObjectFactoryHolder.context = context;
	}
}
