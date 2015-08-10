package com.rest.yun.util.network;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;
/**
 * @project:					yun 
 * @Title: 						ChannelSession.java 		
 * @Package 					com.rest.yun.util.network		
 * @Description: 				channel的缓存 
 * @author 						杨贵松   
 * @date 						2014年2月17日 下午8:52:08 
 * @version 					V1.0
 */
public class ChannelSession {
	//所有的channel都将保存在该map中
	private static Map<String, Channel> map = new HashMap<String, Channel>();
	//ThreadLocal处理多线程安全
	private static final ThreadLocal<Map<String, Channel>> channels = new ThreadLocal<Map<String, Channel>>(){
		@Override
		protected Map<String, Channel> initialValue() {
			return map;
		}
		
	};
	
	public static void put(String key, Channel channel) {
		channels.get().put(key, channel);
	}
	
	public static Channel get(String key) {
		return channels.get().get(key);
	}
	
}
