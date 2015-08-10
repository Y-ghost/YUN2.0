package com.rest.yun.beans;

/**
 * @project:					yun 
 * @Title: 						DataBean.java 		
 * @Package 					com.rest.yun.beans		
 * @Description: 				存储指令的bean 
 * @author 						杨贵松   
 * @date 						2014年1月22日 下午11:59:11 
 * @version 					V1.0
 */
public class DataBean implements java.io.Serializable{

	private static final long serialVersionUID = 1L;
		/**接收到的整个数据 **/
		private byte[] receiveData;
		/**接收到的Data数据**/ 
		private byte[] useringData;
		/**发送的数据**/ 
		private byte[] sendData;

		/** 构造函数 */
		public DataBean() {
		}

		public DataBean(byte[] receiveData, byte[] useringData, byte[] sendData) {
			super();
			this.receiveData = receiveData;
			this.useringData = useringData;
			this.sendData = sendData;
		}

		public byte[] getReceiveData() {
			return receiveData;
		}

		public void setReceiveData(byte[] receiveData) {
			this.receiveData = receiveData;
		}

		public byte[] getUseringData() {
			return useringData;
		}

		public void setUseringData(byte[] useringData) {
			this.useringData = useringData;
		}

		public byte[] getSendData() {
			return sendData;
		}

		public void setSendData(byte[] sendData) {
			this.sendData = sendData;
		}
}
