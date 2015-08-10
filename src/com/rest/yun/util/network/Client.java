package com.rest.yun.util.network;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.net.InetAddress;

import org.apache.log4j.Logger;

/**
 * @project: yun
 * @Title: ChannelClient.java
 * @Package com.rest.yun.util.network
 * @Description:
 * @author 杨贵松
 * @date 2014年2月17日 下午8:59:27
 * @version V1.0
 */
public class Client {

	private final static Logger log = Logger.getLogger(Client.class.getName());
	private final String host;
	private final int port;
	private EventLoopGroup group;
	private Channel channel;

	
	public Client(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public static void sendToServer(byte[] bytes) {
		try {
//			Client client = new Client("192.168.1.110", 8091);
			String ip = InetAddress.getLocalHost().getHostAddress();
			Client client = new Client(ip, 8090);
			final Channel channel = client.connect();
			if(channel==null){
				log.info("手动连接服务器失败!");
				return;
			}
			
			bytes[bytes.length-1] = 0x36;//末位的35转换成36表示手动发送给主机的指令,服务器端有判断
			ByteBuf buf = channel.alloc().buffer();
			buf.writeBytes(bytes);
			ChannelFuture f = channel.writeAndFlush(buf);
			f.addListener(ChannelFutureListener.CLOSE);
			client.close();
			log.info("手动连接成功，已发送指令!");
		} catch (Exception e) {
			log.info("手动连接异常!"+e);
		}
	}

	public Channel connect() {
		try {
			group = new NioEventLoopGroup();
			Bootstrap bootstrap = new Bootstrap();
			bootstrap.group(group).channel(NioSocketChannel.class).handler(new ClientHandler());
			channel = bootstrap.connect(host, port).sync().channel();
			return channel;
		} catch (Exception e) {
			log.error("手动连接服务器异常!"+e);
			return null;
		}
	}

	public boolean close() {
		try {
			channel.disconnect();
			channel.close();
			group.shutdownGracefully();
			return true;
		} catch (Exception e) {
			log.info("手动关闭客户端连接异常!"+e);
			return false;
		}
	}
}
