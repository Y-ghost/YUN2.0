package com.rest.yun.util;

/**
 * @project:					yun 
 * @Title: 						CheckReceiveCodingUtil.java 		
 * @Package 					com.rest.yun.util		
 * @Description: 				校验接收到的指令格式 
 * @author 						杨贵松   
 * @date 						2014年1月22日 下午11:57:34 
 * @version 					V1.0
 */
public class CheckReceiveCodingUtil {
	/**
	 * @Title: 				checkReceiveCoding 
	 * @author 				杨贵松
	 * @date 				2014年1月23日 上午12:00:05
	 * @Description: 		校验接受数据正确与否
	 * @param receiveData	接受的数据
	 * @param sendData		发送的数据
	 * @return 
	 * boolean 				返回
	 */
	public boolean checkReceiveCoding(byte[] receiveData , byte[] sendData){
		sendData[sendData.length-1] = 0x35;//将手动发送的36改过来后进行校验
		byte[] head = new byte[5]; // 截取的前导字节及起始符
		byte[] receiveAddr = new byte[4]; // 截取接收的仪表编码
		byte[] sendAddr = new byte[4]; // 截取发送的仪表编码
		byte[] receive = new byte[receiveData.length - 5]; // 截取起始符（不包含）之后的数据
		boolean flag = false;

		for (int i = 0; i < 5; i++) {
			head[i] = receiveData[i];
		}

		if (sendData[11] == (byte) 0x01) { // '在线修改仪表编码'时需要验证发送的新编码与接收的新编码是否一致
			for (int i = 0; i < 4; i++) {
				receiveAddr[i] = receiveData[i + 7];
				sendAddr[i] = sendData[i + 12];
			}
		} else {
			for (int i = 0; i < 4; i++) {
				receiveAddr[i] = receiveData[i + 7];
				sendAddr[i] = sendData[i + 7];
			}
		}

		for (int i = 0; i < receiveData.length - 5; i++) {
			receive[i] = receiveData[i + 5];
		}

		// 验证逻辑
		flag = checkHead(head);
		if (flag) {
			flag = checkAddress(receiveAddr, sendAddr);
		}
		if (flag) {
			flag = checkSum(receive);
		}
		if (flag) {
			if (receiveData[11] != (byte) (sendData[11] + 16)) {
				flag = false;
			}
		}
		if (flag) {
			if (sendData[sendData.length - 1] != receiveData[receiveData.length - 1]) {
				flag = false;
			}
		}
		return flag;
	}
	

	/**
	 * @Title: 				checkHead 
	 * @author 				杨贵松
	 * @date 				2014年1月23日 上午12:55:34
	 * @Description: 		校验前导字节 
	 * @param head
	 * @return 
	 * boolean 				返回
	 */
	public boolean checkHead(byte[] head) {
		if (head[0] != 67)
			return false;
		if (head[1] != 72)
			return false;
		if (head[2] != 69)
			return false;
		if (head[3] != 67)
			return false;
		if (head[4] != 75)
			return false;
		else {
			return true;
		}
	}
	
	/**
	 * @Title: 				checkAddress 
	 * @author 				杨贵松
	 * @date 				2014年1月23日 上午12:55:20
	 * @Description: 		校验仪表编码receiveAddr和sendAddr
	 * @param receiveAddr
	 * @param sendAddr
	 * @return 
	 * boolean 				返回
	 */
	public boolean checkAddress(byte[] receiveAddr , byte[] sendAddr) {
		for(int i = 0 ; i < 4 ; i++){
			if(sendAddr[i] != (byte) 0xFE){
				if(receiveAddr[i] != sendAddr[i]){
					return false;
				}
			}
		}
		return true;
	}

	/**
	 * @Title: 				checkSum 
	 * @author 				杨贵松
	 * @date 				2014年1月23日 上午12:53:51
	 * @Description: 		判断校验和(key[])是否正确
	 * @param receive		为帧头之后的指令（不包含帧头）
	 * @return 
	 * boolean 				返回
	 */
	public boolean checkSum(byte[] receive) {
		int length = receive[0];
		int checksum = length;
		if(checksum<0){
			checksum = checksum + 256;
		}
		for (int i = 1; i < receive.length - 3; i++) {
			checksum += (receive[i] + 256) % 256;
		}
		int key = ((receive[receive.length - 2] + 256) % 256) * 0x100
				+ (receive[receive.length - 3] + 256) % 256;
		return (checksum == key);
	}
}
