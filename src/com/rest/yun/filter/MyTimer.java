package com.rest.yun.filter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.rest.yun.beans.ControlHost;
import com.rest.yun.beans.DataTemp;
import com.rest.yun.mapping.ControlHostMapper;
import com.rest.yun.mapping.DataTempMapper;
import com.rest.yun.service.NetWorkService;
import com.rest.yun.util.CheckReceiveCodingUtil;
import com.rest.yun.util.CodingFactoryUtil;
import com.rest.yun.util.network.Client;

/**
 * 
 * @project:					yun 
 * @Title: 						MyTimer.java 		
 * @Package 					com.rest.yun.filter
 * @Description: 				时钟任务
 * @author 						杨贵松   
 * @date 						2015年2月2日 上午1:49:36
 * @version 					V2.0
 */
@Component
public class MyTimer {
	private Logger log = Logger.getLogger(MyTimer.class.getName());
	private CodingFactoryUtil codingFactory = new CodingFactoryUtil();
	private CheckReceiveCodingUtil checkCoding = new CheckReceiveCodingUtil();
	@Autowired
	private NetWorkService netWorkService;
	@Autowired
	private DataTempMapper dataTempMapper;
	@Autowired
	private ControlHostMapper controlHostMapper;
	
	/**
	 * @Title:       autoDeleteOldTempData
	 * @author:      杨贵松
	 * @time         2015年2月2日 上午2:19:23
	 * @Description: 每天定时01：00整，系统自动删除存储接收指令的临时表旧数据（大于一天的数据）
	 * @return       void
	 * @throws
	 */
	public void autoDeleteOldTempData() {
		// 先查询数据list
		try {
			long startTime = System.currentTimeMillis();
			List<DataTemp> list = new ArrayList<DataTemp>();
			list = dataTempMapper.selectAllOldData();
			if (list.size()>0) {
				// 删除历史数据
				dataTempMapper.deleteAllOldData(list);
				log.info("每天定时01：00整，系统自动删除存储接收指令的临时表旧数据成功");
			}
			long endTime = System.currentTimeMillis();
			log.info("每天定时01：00整，系统自动删除存储接收指令的临时表旧数据,总用时：" + (endTime - startTime) + " ms.");
		} catch (Exception e) {
			log.error("每天定时01：00整，系统自动删除存储接收指令的临时表旧数据异常!" + e);
		}
	}
	
	/**
	 * @Title:       autoValidContrlHostTime
	 * @author:      杨贵松
	 * @time         2015年2月2日 上午2:19:33
	 * @Description: 每天定时03：00整，系统自动对主机进行校时
	 * @return       void
	 * @throws
	 */
	public void autoValidContrlHostTime() {
		try {
			// 查询主机列表list
			List<ControlHost> list = new ArrayList<ControlHost>();
			Map<String,Object> mapTmp = new HashMap<String, Object>();
			mapTmp.put("projectId", "");//查询公用方法
			int hostCount = controlHostMapper.selectAllHostCounts(mapTmp);
			
			if (hostCount > 0) {
				int num = 0;
				if(hostCount / 500 > 0 && hostCount % 500 == 0){
					num = hostCount / 500;
				}else{
					num = hostCount / 500 + 1;
				}
				for (int i = 0; i < num; i++) {
					Map<String, Object> map = new HashMap<String, Object>();
					map.put("index", i);
					map.put("pageSize", 500);
					map.put("projectId", "");//查询公用方法
					list = controlHostMapper.selectAllHostPages(map);
					if (list.size() > 0) {
						// 多线程校验时间
						for (final ControlHost host : list) {
							validControlHostTime validControlHostTime = new validControlHostTime(host.getCode());
							validControlHostTime.start();
						}
						log.info("每天定时03：00整，系统自动对控制器主机校时，第 ‘" + (i + 1) + "’组 完成!");
					} else {
						log.info("每天定时03：00整，系统自动对控制器主机校时，第 ‘" + (i + 1) + "’组 失败，未查询到主机信息!");
					}
					Thread.sleep(1000 * 60);// 校时分组进行，500一组，每组停顿60秒
				}
			} else {
				log.info("每天定时03：00整，系统自动对控制器主机校时,未查询到主机信息!");
			}
		} catch (Exception e) {
			log.error("每天定时03：00整，系统自动对控制器主机校时异常!" + e);
		}
	}
	
	/**
	 * 
	 * @project:					yun 
	 * @Title: 						MyTimer.java 		
	 * @Package 					com.rest.yun.filter
	 * @Description: 				多线程--现场控制器主机校时
	 * @author 						杨贵松   
	 * @date 						2015年2月2日 上午2:19:45
	 * @version 					V2.0
	 */
	class validControlHostTime extends Thread {
		private String code;

		public validControlHostTime(String code) {
			this.code = code;
		}

		@Override
		public void run() {
			try {
				String time = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
				byte[] data = codingFactory.string2BCD(time);

				byte[] sendData = codingFactory.coding((byte) 0x01, code, (byte) 0x06, data);
				// 开始的时间
				Date startDate = new Date();
				Client.sendToServer(sendData);
				// 等待获取主机返回的指令，等待10秒
				String dataContext = netWorkService.waitData(code, "16", startDate);

				boolean flag = false;
				byte[] receiveData = null;
				if (!dataContext.equals("")) {
					receiveData = codingFactory.string2BCD(dataContext);
					flag = checkCoding.checkReceiveCoding(receiveData, sendData);
				}
				if (flag) {
					byte num = receiveData[12];
					if (num == (byte)0xA0) {
						// 校时失败
						log.info("编号为： " + code + " 的监控主机校时失败!");
					}
				} else {
					// 校时失败
					log.info("编号为： " + code + " 的监控主机校时失败(未接收到指令或者接收的指令格式不对)!");
				}
			} catch (Exception e) {
				// 校时异常
				log.info("编号为： " + code + " 的监控主机校时异常!" + e);
			}
		}
	}
}
