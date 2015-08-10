package com.rest.yun.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.gexin.rp.sdk.base.IIGtPush;
import com.gexin.rp.sdk.base.impl.ListMessage;
import com.gexin.rp.sdk.base.impl.Target;
import com.gexin.rp.sdk.http.IGtPush;
import com.gexin.rp.sdk.template.NotificationTemplate;
import com.rest.yun.beans.ControlHost;
import com.rest.yun.beans.DataTemp;
import com.rest.yun.beans.Equipment;
import com.rest.yun.beans.EquipmentData;
import com.rest.yun.beans.EquipmentStatus;
import com.rest.yun.beans.SensorInfo;
import com.rest.yun.beans.SystemLog;
import com.rest.yun.beans.User;
import com.rest.yun.beans.UserProjectRel;
import com.rest.yun.mapping.ControlHostMapper;
import com.rest.yun.mapping.DataTempMapper;
import com.rest.yun.mapping.EquipmentDataMapper;
import com.rest.yun.mapping.EquipmentMapper;
import com.rest.yun.mapping.EquipmentStatusMapper;
import com.rest.yun.mapping.SensorInfoMapper;
import com.rest.yun.mapping.SystemLogMapper;
import com.rest.yun.mapping.UserMapper;
import com.rest.yun.mapping.UserProjectRelMapper;
import com.rest.yun.service.NetWorkService;
import com.rest.yun.util.CheckReceiveCodingUtil;
import com.rest.yun.util.CodingFactoryUtil;
import com.rest.yun.util.CommonUtiles;

/**
 * @project:					yun 
 * @Title: 						NetWorkUtil.java 		
 * @Package 					com.rest.yun.util		
 * @Description: 				公共指令解析存储类 
 * @author 						杨贵松   
 * @date 						2014年3月6日 下午5:30:54 
 * @version 					V1.0
 */
@Service
public class NetWorkServiceImpl implements NetWorkService{
	private static final Logger log = Logger.getLogger(NetWorkServiceImpl.class.getName());
	private CodingFactoryUtil codingFactory = new CodingFactoryUtil();
	private CheckReceiveCodingUtil checkCoding = new CheckReceiveCodingUtil();
	@Autowired
	private DataTempMapper dataTempMapper;
	@Autowired
	private UserMapper userMapper;
	@Autowired
	private ControlHostMapper controlHostMapper;
	@Autowired
	private EquipmentMapper equipmentMapper;
	@Autowired
	private EquipmentStatusMapper equipmentStatusMapper;
	@Autowired
	private EquipmentDataMapper equipmentDataMapper;
	@Autowired
	private UserProjectRelMapper userProjectRelMapper;
	@Autowired
	private SensorInfoMapper sensorInfoMapper;
	@Autowired
	private SystemLogMapper systemLogMapper;
	
	/**
	 * @Title: 				saveNetData 
	 * @author 				杨贵松
	 * @date 				2014年3月6日 下午8:40:45
	 * @Description: 		服务器获取的指令，需保存到临时的存储空间中，供请求调用 
	 * @param data 
	 * void 				返回
	 */
	public void saveNetData(String code,String controlType,String Rdata){
		try {
			Date date = CommonUtiles.getSystemDateTime();
			DataTemp dataTemp = new DataTemp();
			dataTemp.setCode(code);
			dataTemp.setContraltype(controlType);
			dataTemp.setDatacontext(Rdata);
			dataTemp.setReceivetime(date);
			dataTempMapper.insert(dataTemp);
			//解析接收的节点数据存储到状态表中
			if(controlType.equals("15")){
				
				byte[] data = {};
				
				byte[] sendData = codingFactory.coding((byte) 0x01, code, (byte) 0x05, data);
				boolean flag = false;
				byte[] receiveData = null;
				if (!Rdata.equals("")) {
					receiveData = codingFactory.string2BCD(Rdata);
					flag = checkCoding.checkReceiveCoding(receiveData, sendData);
				}
				if (flag) {
					int num = receiveData[12];
					String usingData = Rdata.substring(26,Rdata.length()-6);
					List<EquipmentStatus> listStatus = new ArrayList<EquipmentStatus>();
					List<EquipmentData> listData = new ArrayList<EquipmentData>();
					int numTmp = 0;
					while(num>0){
						String eCode = usingData.substring(numTmp,numTmp+4);
						Map<String,Object> map = new HashMap<String, Object>();
						map.put("hCode", code);
						map.put("eCode", eCode);
						Equipment equipment = equipmentMapper.selectEquipmentByHcodeAndEcode(map);
						//传感器个数
						int sNum = Integer.parseInt(usingData.substring(numTmp+22,numTmp+24));
						//水流脉冲数
						long waterVal = Long.valueOf(usingData.substring(numTmp+10,numTmp+18),16);
						
						if(equipment!=null){
							//查询数据库中最新的一条采集数据
							EquipmentStatus equipmentStatus = equipmentStatusMapper.selectEquipmentStatusByEid(equipment.getId());
							
							if(equipmentStatus!=null && equipmentStatus.getWatervalue() > waterVal){//采集异常保存系统日志
								List<UserProjectRel> relList = userProjectRelMapper.selectRelByPid(equipment.getProject().getId());
								List<SystemLog> sysLogList = new ArrayList<SystemLog>();
								for(UserProjectRel Rel:relList){
									SystemLog systemLog = new SystemLog();
									systemLog.setUserid(Rel.getUserid());
									systemLog.setLogcontext("采集异常：【" + equipment.getProject().getName() + "】项目下【" + equipment.getName() + "】监控节点数据异常，当天采集的数据小于历史数据!");
									systemLog.setLogtime(date);
									systemLog.setLogtype(0);// 0 表示采集异常报警日志 , 1 表示实时土壤温度过低报警日志
									systemLog.setLogstatus("未读");// 0 表示未读, 1 表示已读
									sysLogList.add(systemLog);
								}
								systemLogMapper.insert(sysLogList);
								log.error("数据采集异常:【" + equipment.getProject().getName() + "】项目下【" + equipment.getName() + "】监控节点数据异常，当天采集的数据小于历史数据!");
								numTmp += 22 + 2 + sNum * 4;// 节点地址长度+传感器个数+传感器地址长度
							}else{//当天采集的数据合理，保存或者这是块新节点，没有采集的数据，开始保存
								//节点状态
								EquipmentStatus statusVal = new EquipmentStatus();
								statusVal.setEquipmentid(equipment.getId());
								statusVal.setCreatetime(date);
								statusVal.setWatervalue(waterVal);
								if(equipmentStatus==null){
									statusVal.setCurrentvalue(waterVal-0);
								}else{
									statusVal.setCurrentvalue(waterVal-equipmentStatus.getWatervalue());
								}
								String status = "";
								byte result = codingFactory.string2BCD(usingData.substring(numTmp+8,numTmp+10))[0];
								switch (result) {
								case 0x0F:
									status = "阀门关闭";
									break;
								case (byte) 0xF0:
									status = "阀门开启";
									break;
								case (byte) 0xF1:
									status = "等待出水";
									break;
								case 0x02:
									status = "等待关水";
									break;
								case (byte) 0xFF:
									status = "供水故障";
									break;
								case 0x00:
									status = "水阀故障";
									break;
								default:
									break;
								}
								statusVal.setStatus(status);
								if(receiveData[numTmp/2+12+3]<0){
									statusVal.setTemperature((float)Math.round(((float)(receiveData[numTmp/2+12+3]-receiveData[numTmp/2+12+4]*0.01))*10)/10);
								}else{
									statusVal.setTemperature((float)Math.round(((float)(receiveData[numTmp/2+12+3]+receiveData[numTmp/2+12+4]*0.01))*10)/10);
								}
								statusVal.setVelocity((float)Math.round(((float)(receiveData[numTmp/2+12+10]+receiveData[numTmp/2+12+11]*0.01))*10)/10);
								listStatus.add(statusVal);
								
								//传感器湿度数据
								for(int i=1;i<=sNum;i++){
									EquipmentData dataVal = new EquipmentData();
									Map<String,Object> sensorMap = new HashMap<String, Object>();
									sensorMap.put("eId", equipment.getId());
									sensorMap.put("num", i);
									SensorInfo sensorInfo = sensorInfoMapper.selectByEidAndNum(sensorMap);
									dataVal.setCreatetime(date);
									float hTmp = (float)Math.round(((float)(receiveData[numTmp/2+12+12+(i*2-1)]+receiveData[numTmp/2+12+13+(i*2-1)]*0.01))*100)/100;
									float soilWater = equipment.getSoilwater();
									if(hTmp==-1){
										hTmp = soilWater;
									}
									BigDecimal tmp = new BigDecimal(hTmp/soilWater*100);
									float humidity = tmp.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
									dataVal.setHumidity(humidity);
									dataVal.setSensorid(sensorInfo.getId());
									listData.add(dataVal);
								}
								numTmp += 22 + 2 + sNum * 4;// 节点地址长度+传感器个数+传感器地址长度
							}
						}else{
							log.error("数据采集--" + code + " 号主机下【" + eCode + "】监控节点数据采集失败，节点信息为空，可能未注册!");
							numTmp += 22 + 2 + sNum * 4;// 节点地址长度+传感器个数+传感器地址长度
						}
						num -- ;
					}
					//将采集的节点数据保存到数据库
					if(!CollectionUtils.isEmpty(listStatus)){
						equipmentStatusMapper.insert(listStatus);
					}
					if(!CollectionUtils.isEmpty(listData)){
						equipmentDataMapper.insert(listData);
					}
				} else {
					// 数据采集失败
					ControlHost host = controlHostMapper.selectByCode(code);
					List<UserProjectRel> relList = userProjectRelMapper.selectRelByPid(host.getProjectid());
					List<SystemLog> sysLogList = new ArrayList<SystemLog>();
					for(UserProjectRel Rel:relList){
						SystemLog systemLog = new SystemLog();
						systemLog.setUserid(Rel.getUserid());
						systemLog.setLogcontext("采集：" + code + "号主机的数据失败(未接收到指令或者接收的指令格式不对)!");
						systemLog.setLogtime(date);
						systemLog.setLogtype(0);// 0 表示采集异常报警日志 , 1 表示实时土壤温度过低报警日志
						systemLog.setLogstatus("未读");// 0 表示未读, 1 表示已读
						sysLogList.add(systemLog);
					}
					systemLogMapper.insert(sysLogList);
					log.error("数据采集--" + code + "号主机数据采集失败(未接收到指令或者接收的指令格式不对)!");
				}
			}
		} catch (ParseException e) {
			log.error("获取系统时间异常!"+e);
		} catch (DataAccessException e) {
			log.error("服务器保存接收的数据到DataTemp表中异常!"+e);
		}
	}
	
	/**
	 * @Title: 				getNetData 
	 * @author 				杨贵松
	 * @date 				2014年3月6日 下午8:45:25
	 * @Description: 		从临时存储空间中获取服务器返回的指令 
	 * @param data 
	 * void 				返回
	 */
	public DataTemp getNetData(String address,String controlId,Date dateTime,Date startDate){
		DataTemp dateTemp = new DataTemp();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("address", address);
		map.put("controlId", controlId);
		map.put("startDate", startDate);
		map.put("endDate", dateTime);
		try {
			dateTemp = dataTempMapper.selectDataTemp(map);
		} catch (DataAccessException e) {
			log.error("获取服务器保存的DataTemp临时数据异常!"+e);
		}
		return dateTemp;
	}
	/**
	 * @Title:       getNetDataForList
	 * @author:      杨贵松
	 * @time         2015年5月4日 下午2:44:43
	 * @Description: 从临时存储空间中获取服务器返回的指令列表
	 * @return       List<DataTemp>
	 * @throws
	 */
	public List<DataTemp> getNetDataForList(String address,String controlId,Date dateTime,Date startDate){
		List<DataTemp> dateTemp = new ArrayList<DataTemp>();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("address", address);
		map.put("controlId", controlId);
		map.put("startDate", startDate);
		map.put("endDate", dateTime);
		try {
			dateTemp = dataTempMapper.selectDataTempForList(map);
		} catch (DataAccessException e) {
			log.error("获取服务器保存的DataTemp临时数据异常!"+e);
		}
		return dateTemp;
	}
	/**
	 * @Title: 				waitData 
	 * @author 				杨贵松
	 * @date 				2014年3月7日 上午1:20:29
	 * @Description: 		等待接收主机返回数据，超过10秒结束等待 
	 * @return 
	 * String 				返回
	 * @throws ParseException 
	 * @throws InterruptedException 
	 */
	public String waitData(String address,String ContralCode,Date startDate) throws ParseException, InterruptedException{
		Date endDate = CommonUtiles.getLastDate(10);
		String dataContext = "";
		long time = 0;
		while(dataContext.equals("") && time<endDate.getTime()){
			Thread.sleep(500);
			DataTemp dataTemp = new DataTemp();
			dataTemp = getNetData(address, ContralCode,endDate,startDate);
			if(dataTemp==null){
				time = System.currentTimeMillis();
				continue;
			}else if(dataTemp.getDatacontext().equals("")){
				time = System.currentTimeMillis();
				continue;
			}else{
				dataContext = dataTemp.getDatacontext();
				break;
			}
		}
		return dataContext;
	}
	/**
	 * @Title:       waitDataForList
	 * @author:      杨贵松
	 * @time         2015年5月4日 下午2:47:13
	 * @Description: 等待接收主机返回数据列表，超过10秒结束等待
	 * @return       List<DataTemp>
	 * @throws
	 */
	public List<DataTemp> waitDataForList(String address,String ContralCode,Date startDate,int size) throws ParseException, InterruptedException{
		Date endDate = CommonUtiles.getLastDate(10);
		List<DataTemp> data = new ArrayList<DataTemp>();
		int num = 0 ;
		if(size/8==0){
			num = 1;
		}else if(size/8>=1 && size%8==0){
			num = size/8;
		}else if(size/8>=1 && size%8>0){
			num = size/8+1;
		}
		long time = 0;
		while(data.size() <= 0 && time<endDate.getTime()){
			Thread.sleep(500);
			data = getNetDataForList(address, ContralCode,endDate,startDate);
			if(data.size() < num){
				time = System.currentTimeMillis();
				continue;
			}else{
				break;
			}
		}
		return data;
	}
	/**
	 * @Title: 				waitDataForSearchEquipment 
	 * @author 				杨贵松
	 * @date 				2014年4月22日 下午3:51:33
	 * @Description: 		等待接收主机返回的搜索节点地址数据，超过46秒结束等待 
	 * @param address
	 * @param ContralCode
	 * @param startDate
	 * @return
	 * @throws ParseException 
	 * @throws InterruptedException 
	 * String 				返回
	 */
	public String waitDataForSearchEquipment(String address,String ContralCode,Date startDate) throws ParseException, InterruptedException{
		Date endDate = CommonUtiles.getLastDate(46);
		String dataContext = "";
		long time = 0;
		while(dataContext.equals("") && time<endDate.getTime()){
			Thread.sleep(500);
			DataTemp dataTemp = new DataTemp();
			dataTemp = getNetData(address, ContralCode,endDate,startDate);
			if(dataTemp==null){
				time = System.currentTimeMillis();
				continue;
			}else if(dataTemp.getDatacontext().equals("")){
				time = System.currentTimeMillis();
				continue;
			}else{
				dataContext = dataTemp.getDatacontext();
				break;
			}
		}
		return dataContext;
	}
	
	/**
	 * @Title: 				pushMsg 
	 * @author 				杨贵松
	 * @date 				2014年7月12日 下午4:22:28
	 * @Description: 		通知推送 
	 * @param hostCode 
	 * void 				返回
	 */
	public void pushMsg(String hostCode) {
		String APPID = "djUGtMrQ8A64fC664vH137";
		String APPKEY = "hKGLMQHjGr88YgzVlEzkb8";
		String MASTERSECRET = "1vCOsPMlFu7dUzZZWVm1c9";
		String CLIENTID = "";
		String API = "http://sdk.open.api.igexin.com/apiex.htm"; // OpenService接口地址
		// 推送主类
		IIGtPush push = new IGtPush(API, APPKEY, MASTERSECRET);
		try {
			List<User> list = new ArrayList<User>();
			list = userMapper.selectUserByHostCode(hostCode);
			ControlHost host = controlHostMapper.selectByCode(hostCode); 
			if (!CollectionUtils.isEmpty(list)) {
				// 接收者
				List<Target> targets = new ArrayList<Target>();
				ListMessage message = new ListMessage();
				
				// 通知模版：NotificationTemplate
				NotificationTemplate template = new NotificationTemplate();
				template.setAppId(APPID);
				template.setAppkey(APPKEY);
				template.setTitle("警告【Rainet云灌溉】"); // 通知标题
				template.setText("项目【"+host.getProject().getName()+"】下有一个节点监测的湿度过低!");//通知内容
				template.setLogo("push.png");//通知logo
				
				// 收到消息是否立即启动应用，1为立即启动，2则广播等待客户端自启动
				template.setTransmissionType(1);
				
				message.setData(template);
				message.setOffline(true); // 用户当前不在线时，是否离线存储,可选
				message.setOfflineExpireTime(72 * 3600 * 1000); // 离线有效时间，单位为毫秒，可选
				List<SystemLog> sysLogList = new ArrayList<SystemLog>();
				Date date = CommonUtiles.getSystemDateTime();
				for (User user : list) {
					//保存日志
					SystemLog systemLog = new SystemLog();
					systemLog.setUserid(user.getId());
					systemLog.setLogcontext("项目【"+host.getProject().getName()+"】下有一个节点监测的湿度过低,请及时查看!");
					systemLog.setLogtime(date);
					systemLog.setLogtype(1);// 0 表示采集异常报警日志 , 1 表示实时土壤温度过低报警日志 
					systemLog.setLogstatus("未读");// 0 表示未读, 1 表示已读
					sysLogList.add(systemLog);
					
					//声明推送用户对象
					CLIENTID = user.getClientid();

					Target target = new Target();
					target.setAppId(APPID);
					target.setClientId(CLIENTID);
					targets.add(target);
				}
				systemLogMapper.insert(sysLogList);
				
				String contentId = push.getContentId(message);
				push.pushMessageToList(contentId, targets);
				log.error("警告推送完成!");
			}
		} catch (Exception e) {
			log.error("通知推送异常!"+e);
		}
	}

	/**
	 * @Title: 				selectData 
	 * @author 				杨贵松
	 * @date 				2014年7月14日 下午4:33:28
	 * @Description: 		获取最新的一条报警指令
	 * @param hostCode 
	 * void 				返回
	 */
	public DataTemp selectData(String address, String controlId) throws Exception {
		DataTemp dateTemp = new DataTemp();
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("address", address);
		map.put("controlId", controlId);
		dateTemp = dataTempMapper.selectDataMax(map);
		return dateTemp;
	}
}
