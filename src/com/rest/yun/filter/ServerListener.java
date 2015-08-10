package com.rest.yun.filter;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import com.rest.yun.util.network.Server;

/**
 * @project:					yun 
 * @Title: 						ServerListener.java 		
 * @Package 					com.rest.yun.filter		
 * @Description: 				服务器端监听器 
 * @author 						杨贵松   
 * @date 						2014年3月3日 下午10:45:43 
 * @version 					V1.0
 */
@WebListener
public class ServerListener implements ServletContextListener {
	private final Server server = new Server(8091);

    /**
     * Default constructor. 
     */
    public ServerListener() {
    	
    }

	/**
     * @see ServletContextListener#contextInitialized(ServletContextEvent)
     */
    public void contextInitialized(ServletContextEvent arg0) {
    	server.start();
    }

	/**
     * @see ServletContextListener#contextDestroyed(ServletContextEvent)
     */
    public void contextDestroyed(ServletContextEvent arg0) {
    	server.stop();
    }
	
}
