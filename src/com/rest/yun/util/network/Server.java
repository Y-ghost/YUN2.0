package com.rest.yun.util.network;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.log4j.Logger;

/**
 * @project:					yun 
 * @Title: 						Server.java 		
 * @Package 					com.rest.yun.util.network		
 * @Description: 				netty服务器 
 * @author 						杨贵松   
 * @date 						2014年2月17日 下午8:48:12 
 * @version 					V1.0
 */
public class Server {

	private final Logger log = Logger.getLogger(Server.class.getName());
	private AtomicBoolean started = new AtomicBoolean(false);
	private final int port;
	
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private Channel channel;

	public Server(int port) {
		super();
		this.port = port;
	}

	public void start() {
		if (started.compareAndSet(true, true)) {
			log.error("服务器已经启动, 请勿重复操作.");
			return;
		}
		if (channel == null && !started.get()) {
			try {
				bossGroup = new NioEventLoopGroup();
				workerGroup = new NioEventLoopGroup();
				ServerBootstrap bootstrap = new ServerBootstrap();
				bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
						.childHandler(new ServerHandler());

				channel = bootstrap.bind(port).sync().channel();
				log.info("Server start success !!!");
			} catch (Exception e) {
				log.error("服务器启动异常!"+e);
			}
		}
	}
	
	public void stop() {
		try {
			channel.disconnect();
			channel.close();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			started.set(false);
		} catch (Exception ex) {
			log.error("服务器关闭异常!"+ex);
		}
	}
}
