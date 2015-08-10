package com.rest.yun.util.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;

import org.apache.log4j.Logger;

import com.rest.yun.util.CodingFactoryUtil;
/**
 * @project:					yun 
 * @Title: 						ChannelClientHandler.java 		
 * @Package 					com.rest.yun.util.network		
 * @Description: 				客户端处理程序 
 * @author 						杨贵松   
 * @date 						2014年2月17日 下午8:58:30 
 * @version 					V1.0
 */
public class ClientHandler extends ChannelInitializer<SocketChannel>{

	private final Logger log = Logger.getLogger(ClientHandler.class.getName());
	private CodingFactoryUtil codingFactory = new CodingFactoryUtil();
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
		pipeline.addLast("decoder", new LengthFieldBasedFrameDecoder(256, 5, 1, 0, 0));
		pipeline.addLast("handler", new ChannelInboundHandlerAdapter() {

			@Override
			public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
				ByteBuf buf = (ByteBuf) msg;
				byte[] message = new byte[buf.readableBytes()];
				buf.getBytes(0,message);
				log.info("Client message : " + codingFactory.bytesToHexString(message));
			}
		});
	}
}
