package com.rest.yun.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @ClassName:    CodingFactoryUtil 
 * @Description:  命令工厂，用于转化拼接相关命令码 
 * @author        杨贵松 
 * @date          2014年1月22日 下午11:34:21 
 */
public class CodingFactoryUtil {

	/**
	 * @Title: coding 
	 *@author        		杨贵松 
	 * @date          		2014年1月22日 下午11:34:21 
	 * @Description:        指令拼接方法 
	 * @param Type          现场监测仪表类型
	 * @param Address       现场监测仪表编码
	 * @param ControlType   控制码
	 * @param dataSets      发送数据
	 * @return  byte[]      拼接好的指令
	 * @throws
	 */
	public byte[] coding(byte Type, String Address, byte ControlType,
			byte[] dataSets) {

		if (Address == null) {
			return null;
		}

		// 指令长度
		int codeLength = 0;
		// 数据位长度DATA[n]
		int dataLength = 0;
		// 数据长度(BYTE)
		byte bytelength;
		// 转换现场监测仪表编码address2Bytes(Address)
		byte[] meterAddress = this.address2Bytes(Address);
		// 转换发送数据
		if (dataSets != null) {
			dataLength += dataSets.length;
		}
		// 数据长度
		bytelength = (byte) (9 + dataLength);
		// 指令长度
		codeLength = 15 + dataLength;
		if (meterAddress == null) {
			return null;
		}
		// 计算校验和
		byte[] CRC = this.ComputeSum(bytelength, Type, meterAddress,
				ControlType, dataSets);
		byte[] msg = new byte[codeLength];
		msg[0] = 0x43;
		msg[1] = 0x48;
		msg[2] = 0x45;
		msg[3] = 0x43;
		msg[4] = 0x4B;
		msg[5] = (byte) bytelength;
		msg[6] = Type;
		msg[7] = meterAddress[0];
		msg[8] = meterAddress[1];
		msg[9] = meterAddress[2];
		msg[10] = meterAddress[3];
		msg[11] = ControlType;

		if (dataSets != null) {
			for (int i = 0; i < dataSets.length; i++) {
				msg[codeLength - (i + 4)] = dataSets[dataSets.length - (i + 1)];
			}
		}

		msg[codeLength - 3] = CRC[0];
		msg[codeLength - 2] = CRC[1];
		msg[codeLength - 1] = 0x35;
		return msg;
	}

	/**
	 * @Title: address2Bytes 
	 * @Description:  现场监测仪表编码由String转换成byte[]
	 * @param address 现场监测仪表编码
	 * @return byte[] 返回现场监测仪表编码 byte[]
	 * @throws
	 */
	public byte[] address2Bytes(String address) {
		byte[] addr = new byte[4];
		//FE为广播地址
		if(address.equals("FEFEFEFE")){
			addr[3] = (byte) 0xFE;
			addr[2] = (byte) 0xFE;
			addr[1] = (byte) 0xFE;
			addr[0] = (byte) 0xFE;
		}else{
			addr[3] = (byte) Integer.parseInt(address.substring(6, 8));
			addr[2] = (byte) Integer.parseInt(address.substring(4, 6));
			addr[1] = (byte) Integer.parseInt(address.substring(2, 4));
			addr[0] = (byte) Integer.parseInt(address.substring(0, 2));
		}
		return addr;
	}
	
	/**
	 * @Title: ComputeSum 
	 * @Description: 校验码生成工具 
	 * @param blength			数据长度
	 * @param type				现场监测仪表类型
	 * @param meterAddress		现场监测仪表编码
	 * @param controlType		控制码
	 * @param dataSets2Btye		发送数据
	 * @return  byte[] 			返回校验码
	 * @throws
	 */
	public byte[] ComputeSum(byte blength, byte type, byte[] meterAddress,
			byte controlType, byte[] dataSets2Btye) {

		int sum = 0;
		for (int i = 0; i < meterAddress.length; i++) {
			sum += (meterAddress[i] + 256) % 256;
		}
		if (dataSets2Btye != null) {
			for (int i = 0; i < dataSets2Btye.length; i++) {
				sum += (dataSets2Btye[i] + 256) % 256;
			}
		}

		sum += (blength + 256) % 256;
		sum += (type + 256) % 256;
		sum += (controlType + 256) % 256;
		byte[] key = new byte[2];
		key[0] = (byte) (sum % 256);
		key[1] = (byte) ((sum / 256) % 256);
		return key;
	}
	
	/**
	 * @Title: byte2BCD 
	 * @Description: 	byte转换成BCD码
	 * @param dataVal	传入的数据
	 * @return byte[] 	返回BCD码
	 * @throws
	 */
	public byte[] byte2BCD(byte[] dataVal) {
		int length = dataVal.length;
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			d[i] = (byte) (dataVal[i]%10 + dataVal[i]/10*16);
		}
		return d;
	}
	
	/**
	 * @Title: BCD2Int 
	 * @Description: 	BCD码转换成int
	 * @param dataVal	传入的BCD码
	 * @return int[] 	返回int[]
	 * @throws
	 */
	public int[] BCD2Int(byte[] dataVal) {
		int length = dataVal.length;
		int[] value = new int[length];
		
		for (int i = 0; i < length; i++) {
			String a = Integer.toString(((dataVal[i]+256)%256)/16) + Integer.toString(((dataVal[i]+256)%256)%16);
			value[i] = Integer.parseInt(a);
		}
		return value;
	}

	/**
	 * @Title: string2BCD 
	 * @Description: 	String[]数组转化为BCD码
	 * @param string	传入的字符串，注：传入的字符串需要保证格式
	 * @return byte[] 	返回BCD码 
	 * @throws
	 */
	public byte[] string2BCD(String[] string) {
		byte[] d = new byte[string.length];
		if (string.length == 0) {
			return null;
		}
		for(int i=0;i<string.length;i++){
			char[] hexChars = string[i].toUpperCase().toCharArray();
			d[i]= (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
		}
		return d;
	}
	/**
	 * @Title: string2BCD 
	 * @Description: 	String转化为BCD码
	 * @param string	传入的字符串，注：传入的字符串需要保证格式
	 * @return byte[] 	返回BCD码 
	 * @throws
	 */
	public byte[] string2BCD(String string) {
		if (string.length() == 0) {
			return null;
		}
		if(string.length()%2>0){
			return null;
		}
		String[] sArr = new String[string.length()/2];
		for(int i=0;i<sArr.length;i++){
			sArr[i] = string.substring(i*2,(i+1)*2);
		}
		
		byte[] d = new byte[sArr.length];
		for(int i=0;i<sArr.length;i++){
			char[] hexChars = sArr[i].toUpperCase().toCharArray();
			d[i]= (byte) (charToByte(hexChars[0]) << 4 | charToByte(hexChars[1]));
		}
		return d;
	}

	/**
	 * @Title: charToByte 
	 * @Description: 	将字符转换成int
	 * @param c
	 * @return int 		返回int 
	 * @throws
	 */
	public int charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}
	
	/**
	 * @Title: time2BCD 
	 * @Description: 	时间生成器
	 * @return byte[] 	返回时间BCD码
	 * @throws
	 */
	public byte[] time2BCD() {
		Date date = new Date();
		Calendar c = Calendar.getInstance();
		int day = c.get(Calendar.DAY_OF_WEEK)-1;
		if(day == 0){
			day =7;
		}
		SimpleDateFormat df = new SimpleDateFormat("yyyy,MM,dd,HH,mm,ss");
		String time = df.format(date);
		time = time.substring(0,2)+","+time.substring(2,5)+time.substring(5,11)+"0"+day+","+time.substring(11,time.length());
		String[] s =  time.split(",");
		byte[] b = new byte[s.length];
		for(int j=0;j<b.length;j++){
			b[j]=(byte)Integer.parseInt(s[j]);
		}
		byte[] a = byte2BCD(b);
		return a;
	}
	
	/**
	 * @Title: bytesToHexString 
	 * @Description: 	将byte[]转换成String输出
	 * @param data		传入的byte[]数据
	 * @return String 	返回
	 * @throws
	 */
	public String bytesToHexString(byte[] data) {
		StringBuilder stringBuilder = new StringBuilder("");
		if (data == null || data.length <= 0) {
			return null;
		}
		for (int i = 0; i < data.length; i++) {
			int v = data[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString().toUpperCase();
	}
	
	/**
	 * @Title:       unsigned4BytesToInt
	 * @author:      杨贵松
	 * @Description: 将byte[]数据转化成long 
	 * @return       long
	 * @throws
	 */
	public static long unsigned4BytesToInt(byte[] buf) {  
        int firstByte = 0;  
        int secondByte = 0;  
        int thirdByte = 0;  
        int fourthByte = 0;  
        int index =0;  
        firstByte = (0x000000FF & buf[index]);  
        secondByte = (0x000000FF & buf[index + 1]);  
        thirdByte = (0x000000FF & buf[index + 2]);  
        fourthByte = (0x000000FF & buf[index + 3]);  
        index = index + 4;  
        return (firstByte << 24 | secondByte << 16 | thirdByte << 8 | fourthByte) & 0xFFFFFFFFL;  
    }
	/**
	 * @Title: 				longToByte 
	 * @author 				杨贵松
	 * @date 				2014年7月3日 下午12:10:13
	 * @Description: 		long型转换成byte[4] 
	 * @param number
	 * @return 
	 * byte[] 				返回
	 */
	public byte[] longToByte(long number) {
		long temp = number;
		byte[] b = new byte[4];
		for (int i = b.length - 1; i > -1; i--) {
			b[i] = new Long(temp & 0xff).byteValue();
			temp = temp >> 8;
		}
		return b;
	}
	
	/**
	 * @Title: 				byteMerger 
	 * @author 				杨贵松
	 * @date 				2014年7月3日 下午12:19:23
	 * @Description: 		java 合并两个byte数组  
	 * @param byte_1
	 * @param byte_2
	 * @return 
	 * byte[] 				返回
	 */
    public byte[] byteMerger(byte[] byte_1, byte[] byte_2){  
        byte[] byte_3 = new byte[byte_1.length+byte_2.length];  
        System.arraycopy(byte_1, 0, byte_3, 0, byte_1.length);  
        System.arraycopy(byte_2, 0, byte_3, byte_1.length, byte_2.length);  
        return byte_3;  
    }  
}
