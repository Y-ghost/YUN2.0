package com.rest.yun.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import com.rest.yun.beans.DataBean;

/**
 * 
 * @project:					yun 
 * @Title: 						CommCenterUtil.java 		
 * @Package 					com.rest.yun.util
 * @Description: 				指令发送接受中心
 * @author 						杨贵松   
 * @date 						2014年2月11日 下午8:33:56
 * @version 					V2.0
 */
public class CommCenterUtil {

	private CodingFactoryUtil sendCoding = new CodingFactoryUtil();
	private DataOutputStream out;
	private DataInputStream in;
	private byte[] receive = null;
	private byte[] temp = new byte[100];
	private DataBean dataBean = new DataBean();
	private int s = 0;

	/**
	 * @Title:      sendAndReceive
	 * @author:     杨贵松
	 * @time        2014年2月11日 下午8:35:56
	 * @param       portid 		这个类主要用于对串口进行管理和设置，是对串口进行访问控制的核心类
	 * @param 		serialPort  该类主要实现串口数据通信
	 * @param 		out   		输出流
	 * @param 		in 			输入流
	 * @param 		flag 		判断空调类型：0：串口通讯、1：无线通讯
	 * @param 		receive 	接收指令的数组
	 * @param 		temp 		存储临时流数据的数组
	 * @param 		DataBean 	处理发送及接收指令的bean：ReceiveData保存接收指令、SendData保存发送的指令、UseringData保存指令中的DATA[n]数据
	 * @return 		DataBean
	 * @throws
	 */
	public DataBean sendAndReceive(byte[] data,int flag) throws Exception {
		// 获取设置的端口号
		long startTime = 0;
		int i = 0;
		while (i < 20) {
			// 读取流中的所有指令存进临时数组
			if(flag==0){
				Thread.sleep(200);
			}else if(flag==1){
				Thread.sleep(1000);
			}
			s = in.read(temp);
			i++;
			// 将不为空的指令存储到数组receive
			if (s > 0) {
				receive = new byte[s];
				for (int n = 0; n < s; n++) {
					receive[n] = temp[n];
				}
				dataBean.setReceiveData(receive);// 组装DataBean
				break;
			}else if(receive == null){
				receive = new byte[0];
				dataBean.setReceiveData(receive);// 组装DataBean
			}
		}
		if(receive.length == 0){
			/** 第一次发送无返回则重新执行一次 **/
			out.write(data);
			int j = 0;
			while (j < 20) {
				// 读取流中的所有指令存进临时数组
				if(flag==0){
					Thread.sleep(200);
				}else if(flag==1){
					Thread.sleep(1000);
				}
				s = in.read(temp);
				j++;
				// 将不为空的指令存储到数组receive
				if (s > 0) {
					receive = new byte[s];
					for (int n = 0; n < s; n++) {
						receive[n] = temp[n];
					}
					dataBean.setReceiveData(receive);// 组装DataBean
					break;
				}else if(receive == null){
					receive = new byte[0];
					dataBean.setReceiveData(receive);// 组装DataBean
				}
			}
		}
		// 截取DATA[n]数据，给DataBean的UseringData属性赋值
		byte[] useredData = null ;
		if(receive.length != 0){
			if(flag==0 && receive.length > 17){
				useredData = new byte[receive.length - 17];
				for (int m = 0; m < receive.length - 17; m++) {
					useredData[m] = receive[m + 14];
				}
			}
			if(flag==1 && receive.length > 20){
				useredData = new byte[receive.length - 20-4];
				for (int m = 0; m < receive.length - 20-4; m++) {
					useredData[m] = receive[m + 18+4];
				}
			}
		}
		dataBean.setUseringData(useredData);
		long endTime=System.currentTimeMillis(); //获取结束时间
		System.out.println("接收指令："+sendCoding.bytesToHexString(receive).toUpperCase());
		System.out.println("程序运行时间： "+(endTime-startTime)+"ms");
		return dataBean;
	}
}
