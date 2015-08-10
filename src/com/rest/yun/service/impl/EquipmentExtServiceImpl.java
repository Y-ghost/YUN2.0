package com.rest.yun.service.impl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rest.yun.beans.ControlHost;
import com.rest.yun.beans.Equipment;
import com.rest.yun.beans.EquipmentStatus;
import com.rest.yun.beans.SoilInfo;
import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.EquipmentExt;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.mapping.ControlHostMapper;
import com.rest.yun.mapping.EquipmentDataMapper;
import com.rest.yun.mapping.EquipmentMapper;
import com.rest.yun.mapping.EquipmentStatusMapper;
import com.rest.yun.mapping.SensorInfoMapper;
import com.rest.yun.mapping.SoilInfoMapper;
import com.rest.yun.service.IEquipmentExtService;
import com.rest.yun.service.NetWorkService;
import com.rest.yun.util.CheckReceiveCodingUtil;
import com.rest.yun.util.CodingFactoryUtil;
import com.rest.yun.util.network.Client;

@Service
public class EquipmentExtServiceImpl implements IEquipmentExtService {
	private final static Logger LOG = LoggerFactory.getLogger(EquipmentExtServiceImpl.class);
	private CodingFactoryUtil codingFactory = new CodingFactoryUtil();
	private CheckReceiveCodingUtil checkCoding = new CheckReceiveCodingUtil();

	@Autowired
	private EquipmentMapper equipmentMapper;
	@Autowired
	private SensorInfoMapper sensorInfoMapper;
	@Autowired
	private EquipmentDataMapper equipmentDataMapper;
	@Autowired
	private EquipmentStatusMapper equipmentStatusMapper;
	@Autowired
	private ControlHostMapper controlHostMapper;
	@Autowired
	private SoilInfoMapper soilInfoMapper;
	@Autowired
	private NetWorkService netWorkService;

	/**
	 * @Title:       updateList
	 * @author:      杨贵松
	 * @time         2014年12月28日 下午4:43:21
	 * @Description: 初始化节点设置
	 * @throws
	 */
	@Override
	public String updateList(List<Equipment> list, HttpSession session) {
		String result = "";
		
		try {
			if(!CollectionUtils.isEmpty(list)){
				int cId = list.get(0).getControlhostid();
				ControlHost host = controlHostMapper.selectByPrimaryKey(cId);
				User user = (User) session.getAttribute(Constants.USER);
				List<Equipment> listOK = new ArrayList<Equipment>();
				List<Equipment> listError = new ArrayList<Equipment>();
				for(Equipment equipment:list){
					Equipment e = equipmentMapper.selectByPrimaryKey(equipment.getId());
					equipment.setCode(e.getCode());
					//1.模式设置
					boolean f1 = setModel(equipment,host.getCode());
					if(!f1){
						listError.add(equipment);
						continue;
					}
					//2.自控参数设置
					boolean f2 = setParam(equipment,host.getCode());
					if(!f2){
						listError.add(equipment);
						continue;
					}
					//3.设置ABCD参数值
					boolean f3 = setCoefficient(equipment,host.getCode());
					if(!f3){
						listError.add(equipment);
						continue;
					}
					//4.时段设置
					boolean f4 = true;
					if(equipment.getIrrigationtype()==2){
						f4 = setTimeLen(equipment,host.getCode());
					}
					//5.流量设置
					boolean f5 = true;
					if(equipment.getIrrigationtype()==3){
						f5 = setTimeLen(equipment,host.getCode());
					}
					if(f1&&f2&&f3&&f4&&f5){
						equipment.setModifyuser(user.getId());
						equipment.setModifytime(new Date());
						listOK.add(equipment);
						equipmentMapper.updateByPrimaryKeySelective(equipment);
					}else{
						listError.add(equipment);
					}
				}
				//将成功的节点信息保存到数据库
				if(CollectionUtils.isEmpty(listOK)){
					LOG.error("设置节点及传感器全部失败");
					throw new ServerException(ErrorCode.INIT_EQUIPMENT_INFO_FAILD);
				}else if(!CollectionUtils.isEmpty(listError)){
					String name = "";
					for(Equipment equipment:listError){
						name += equipment.getName()+"、";
					}
					LOG.error("设置【"+name.substring(0,name.length()-1)+"】等共计【"+listError.size()+"】个节点及传感器失败");
					result = "设置【"+name.substring(0,name.length()-1)+"】等共计【"+listError.size()+"】个节点及传感器失败";
				}
			}
		} catch (DataAccessException e) {
			LOG.error("设置节点及传感器异常", e);
			throw new ServerException(ErrorCode.INIT_EQUIPMENT_INFO_ERR);
		}
		return result;
	}
	
	/**
	 * @Title:       setModel
	 * @author:      杨贵松
	 * @time         2014年12月29日 下午3:07:39
	 * @Description: 单独设置一个节点的控制模式
	 * @return       boolean
	 * @throws
	 */
	public boolean setModel(Equipment equipment,String cCode){
		boolean mark = false;
		// 查询节点信息
		try {
			byte[] model = new byte[1];
			switch(equipment.getIrrigationtype()){
			case 0 :
				model[0] = (byte) 0xF2;//手动模式
				break;
			case 1 : 
				model[0] = (byte) 0xF0;//自动模式
				break;
			case 2 : 
				model[0] = (byte) 0xF1;//时段模式
				break;
			default:
				break;
			}
			
			byte[] data = codingFactory.byteMerger(model, codingFactory.string2BCD("01"+equipment.getCode()));
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, cCode, (byte) 0x22, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(cCode, "32", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				byte num = receiveData[12];
				switch (num) {
				case 0:
					mark = false;
					break;
				case (byte) 0xF0:
					mark = true;
					break;
				case (byte) 0xF1:
					mark = true;
					break;
				case (byte) 0xF2:
					mark = true;
					break;
				default:
					break;
				}
			}
		} catch (DataAccessException e) {
			LOG.error("设置节点控制模式异常", e);
			throw new ServerException(ErrorCode.SET_EQUIPMENT_MODEL_FAILD);
		}
		return mark;
	}
	
	/**
	 * @Title:       setParam
	 * @author:      杨贵松
	 * @time         2014年12月29日 下午3:08:17
	 * @Description: 单独设置一个节点的自控参数
	 * @return       boolean
	 * @throws
	 */
	public boolean setParam(Equipment equipment,String cCode){
		boolean mark = false;
		// 查询节点信息
		try {
			byte[] model = new byte[8];
			float humidityUp = equipment.getHumidityup();
			float humidityDown = equipment.getHumiditydown();
			float soilWater = equipment.getSoilwater();

			BigDecimal a = new BigDecimal(humidityUp * soilWater/100);
			BigDecimal b = new BigDecimal(humidityDown * soilWater/100);
			float hUp = a.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			float hDown = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			float tUp = equipment.getTemperatureup();
			float tDown = equipment.getTemperaturedown();
			
			model[0] = (byte) ((int) (hUp * 100) / 100);
			model[1] = (byte) ((int) (hUp * 100) % 100);
			model[2] = (byte) ((int) (hDown * 100) / 100);
			model[3] = (byte) ((int) (hDown * 100) % 100);

			// 温度值转换
			if (tUp < 0) {
				model[4] = (byte) ((int) (tUp * 100) / 100);
				model[5] = (byte) ((int) (-tUp * 100) % 100);
			} else {
				model[4] = (byte) ((int) (tUp * 100) / 100);
				model[5] = (byte) ((int) (tUp * 100) % 100);
			}
			if (tDown < 0) {
				model[6] = (byte) ((int) (tDown * 100) / 100);
				model[7] = (byte) ((int) (-tDown * 100) % 100);
			} else {
				model[6] = (byte) ((int) (tDown * 100) / 100);
				model[7] = (byte) ((int) (tDown * 100) % 100);
			}
			
			byte[] data = codingFactory.byteMerger(model, codingFactory.string2BCD("01"+equipment.getCode()));
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, cCode, (byte) 0x24, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(cCode, "34", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				byte num = receiveData[12];
				switch (num) {
				case 0x0A:
					mark = true;
					break;
				case (byte) 0xA0:
					mark = false;
					break;
				default:
					break;
				}
			}
		} catch (DataAccessException e) {
			LOG.error("设置节点自控参数异常", e);
			throw new ServerException(ErrorCode.SET_EQUIPMENT_PARAM_FAILD);
		}
		return mark;
	}
	
	/**
	 * @Title:       setCoefficient
	 * @author:      杨贵松
	 * @time         2014年12月29日 下午9:36:30
	 * @Description: 设置a、b、c、d系数到主机
	 * @return       boolean
	 * @throws
	 */
	public boolean setCoefficient(Equipment equipment,String cCode){
		boolean mark = false;
		// 查询节点信息
		try {
			byte[] param = new byte[8];
			SoilInfo soil = soilInfoMapper.selectSoilById(equipment.getSoilname());
			float paramA = soil.getParametera();
			float paramB = soil.getParameterb();
			float paramC = soil.getParameterc();
			float paramD = soil.getParameterd();
			
			// a、b、c、d参数值转换
			if (-1<paramA && paramA < 0) {
				param[0] = (byte) 120;
				param[1] = (byte) ((int) Math.round((-paramA * 100)) % 100);//由于大于-1小于0的小数转化后不整，需要Math.round函数取整
			}else if (-1>=paramA) {
				param[0] = (byte) ((int) (paramA * 100) / 100);
				param[1] = (byte) ((int) (-paramA * 100) % 100);
			} else {
				param[0] = (byte) ((int) (paramA * 100) / 100);
				param[1] = (byte) ((int) (paramA * 100) % 100);
			}
			
			if (-1<paramB && paramB < 0) {
				param[2] = (byte) 120;
				param[3] = (byte) ((int) Math.round((-paramB * 100)) % 100);
			}else if (-1>=paramB) {
				param[2] = (byte) ((int) (paramB * 100) / 100);
				param[3] = (byte) ((int) (-paramB * 100) % 100);
			} else {
				param[2] = (byte) ((int) (paramB * 100) / 100);
				param[3] = (byte) ((int) (paramB * 100) % 100);
			}
			
			if (-1<paramC && paramC < 0) {
				param[4] = (byte) 120;
				param[5] = (byte) ((int) Math.round((-paramC * 100)) % 100);
			}else if (-1>=paramC) {
				param[4] = (byte) ((int) (paramC * 100) / 100);
				param[5] = (byte) ((int) (-paramC * 100) % 100);
			} else {
				param[4] = (byte) ((int) (paramC * 100) / 100);
				param[5] = (byte) ((int) (paramC * 100) % 100);
			}
			
			if (-1<paramD && paramD < 0) {
				param[6] = (byte) 120;
				param[7] = (byte) ((int) Math.round((-paramD * 100)) % 100);
			}else if (-1>=paramD) {
				param[6] = (byte) ((int) (paramD * 100) / 100);
				param[7] = (byte) ((int) (-paramD * 100) % 100);
			} else {
				param[6] = (byte) ((int) (paramD * 100) / 100);
				param[7] = (byte) ((int) (paramD * 100) % 100);
			}
			
			byte[] data = codingFactory.byteMerger(param, codingFactory.string2BCD("01"+equipment.getCode()));
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, cCode, (byte) 0x29, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(cCode, "39", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				byte num = receiveData[12];
				switch (num) {
				case 0x0A:
					mark = true;
					break;
				case (byte) 0xA0:
					mark = false;
				break;
				default:
					break;
				}
			}
		} catch (DataAccessException e) {
			LOG.error("设置土壤传感器精度参数异常", e);
			throw new ServerException(ErrorCode.SET_EQUIPMENT_ABCD_FAILD);
		}
		return mark;
	}
	
	/**
	 * @Title:       setTimeLen
	 * @author:      杨贵松
	 * @time         2014年12月29日 下午3:08:22
	 * @Description: 单独设置一个节点的时段
	 * @return       boolean
	 * @throws
	 */
	public boolean setTimeLen(Equipment equipment,String cCode){
		boolean mark = false;
		// 查询节点信息
		try {
			
			String startone = equipment.getTimeonestart();
			String endone = equipment.getTimeoneend();
			String starttwo = equipment.getTimetwostart();
			String endtwo = equipment.getTimetwoend();
			String startthree = equipment.getTimethreestart();
			String endthree = equipment.getTimethreeend();
			
			SimpleDateFormat sdf = new SimpleDateFormat("hhmm");
			String timeone="";
			String timetwo="";
			String timethree="";
			if(!startone.equals("")){
				timeone = sdf.format(startone)+sdf.format(endone);
			}else{
				timeone = "24002400";
			}
			
			if(!starttwo.equals("")){
				timetwo = sdf.format(starttwo)+sdf.format(endtwo);
			}else{
				timetwo = "24002400";
			}
			
			if(!startthree.equals("")){
				timethree = sdf.format(startthree)+sdf.format(endthree);
			}else{
				timethree = "24002400";
			}
			
			String timeLen = "";
			String[] week = {};
			int T0 =0;
			if(!equipment.getWeek().equals("")){
				week = equipment.getWeek().split(",");
				T0 = week.length;
				for(int i=0;i<T0;i++){
					timeLen += "0"+week[i];
				}
			}
			
			byte[] data = codingFactory.byteMerger(codingFactory.string2BCD(timeone+timetwo+timethree+"01"+equipment.getCode()),codingFactory.string2BCD("0"+T0+timeLen));
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, cCode, (byte) 0x26, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(cCode, "36", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				byte num = receiveData[12];
				switch (num) {
				case 0x0A:
					mark = true;
					break;
				case (byte) 0xA0:
					mark = false;
					break;
				default:
					break;
				}
			}
		} catch (DataAccessException e) {
			LOG.error("设置节点时段控制异常", e);
			throw new ServerException(ErrorCode.SET_EQUIPMENT_PARAM_FAILD);
		}
		return mark;
	}
	
	/**
	 * @Title:       setFlowPara
	 * @author:      杨贵松
	 * @time         2015年7月23日 上午12:04:13
	 * @Description: 单独设置一个节点的流量控制参数
	 * @return       boolean
	 * @throws
	 */
	public boolean setFlowPara(Equipment equipment,String cCode){
		boolean mark = false;
		// 查询节点信息
		try {
			
			String startone = equipment.getTimeonestart();
			String endone = equipment.getTimeoneend();
			String starttwo = equipment.getTimetwostart();
			String endtwo = equipment.getTimetwoend();
			String startthree = equipment.getTimethreestart();
			String endthree = equipment.getTimethreeend();
			
			SimpleDateFormat sdf = new SimpleDateFormat("hhmm");
			String timeone="";
			String timetwo="";
			String timethree="";
			if(!startone.equals("")){
				timeone = sdf.format(startone)+sdf.format(endone);
			}else{
				timeone = "24002400";
			}
			
			if(!starttwo.equals("")){
				timetwo = sdf.format(starttwo)+sdf.format(endtwo);
			}else{
				timetwo = "24002400";
			}
			
			if(!startthree.equals("")){
				timethree = sdf.format(startthree)+sdf.format(endthree);
			}else{
				timethree = "24002400";
			}
			
			String timeLen = "";
			String[] week = {};
			int T0 =0;
			if(!equipment.getWeek().equals("")){
				week = equipment.getWeek().split(",");
				T0 = week.length;
				for(int i=0;i<T0;i++){
					timeLen += "0"+week[i];
				}
			}
			
			byte[] data = codingFactory.byteMerger(codingFactory.string2BCD(timeone+timetwo+timethree+"01"+equipment.getCode()),codingFactory.string2BCD("0"+T0+timeLen));
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, cCode, (byte) 0x26, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(cCode, "36", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				byte num = receiveData[12];
				switch (num) {
				case 0x0A:
					mark = true;
					break;
				case (byte) 0xA0:
					mark = false;
				break;
				default:
					break;
				}
			}
		} catch (DataAccessException e) {
			LOG.error("设置节点时段控制异常", e);
			throw new ServerException(ErrorCode.SET_EQUIPMENT_PARAM_FAILD);
		}
		return mark;
	}

	/**
	 * @Title:       setListModel
	 * @author:      杨贵松
	 * @time         2015年1月31日 下午11:41:05
	 * @Description: 设置多节点模式
	 * @throws
	 */
	@Override
	public boolean setListModel(List<Equipment> list, HttpSession session) {
		boolean mark = false;
		String codeStr = "";
		int model = list.get(0).getIrrigationtype();
		Date date = new Date();
		List<Equipment> listTmp = new ArrayList<Equipment>();
		User user = (User) session.getAttribute(Constants.USER);
		for(Equipment equipment:list){
			equipment.setModifytime(date);
			equipment.setModifyuser(user.getId());
			listTmp.add(equipment);
			codeStr += equipment.getCode();
		}
		// 查询节点信息
		try {
			byte[] tmp = new byte[2];
			switch(model){
			case 0 :
				tmp[0] = (byte) 0xF2;//手动模式
				break;
			case 1 : 
				tmp[0] = (byte) 0xF0;//自动模式
				break;
			case 2 : 
				tmp[0] = (byte) 0xF1;//时段模式
				break;
			default:
				break;
			}
			
			byte len = (byte)list.size();
			tmp[1] = len;            //节点个数
			
			ControlHost host = controlHostMapper.selectByPrimaryKey(list.get(0).getControlhostid());
			String hCode = host.getCode();
			
			byte[] data = codingFactory.byteMerger(tmp, codingFactory.string2BCD(codeStr));
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, hCode, (byte) 0x22, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(hCode, "32", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				byte num = receiveData[12];
				switch (num) {
				case 0:
					mark = false;
					break;
				case (byte) 0xF0:
					mark = true;
					break;
				case (byte) 0xF1:
					mark = true;
					break;
				case (byte) 0xF2:
					mark = true;
					break;
				default:
					break;
				}
			}
			if(mark){
				equipmentMapper.setListModel(listTmp);
			}
		} catch (DataAccessException e) {
			LOG.error("设置节点控制模式异常", e);
			throw new ServerException(ErrorCode.SET_EQUIPMENT_MODEL_FAILD);
		}
		return mark;
	}
	
	/**
	 * @Title:       setAutoParam
	 * @author:      杨贵松
	 * @time         2015年2月1日 下午8:45:46
	 * @Description: 设置多节点自控参数
	 * @throws
	 */

	@Override
	public boolean setAutoParam(List<Equipment> list, HttpSession session) {
		boolean mark = false;
		String codeStr = "";
		Date date = new Date();
		List<Equipment> listTmp = new ArrayList<Equipment>();
		User user = (User) session.getAttribute(Constants.USER);
		for(Equipment equipment:list){
			equipment.setModifytime(date);
			equipment.setModifyuser(user.getId());
			listTmp.add(equipment);
			codeStr += equipment.getCode();
		}
		// 查询节点信息
		try {
			byte[] tmp = new byte[9];
			float humidityUp = list.get(0).getHumidityup();
			float humidityDown = list.get(0).getHumiditydown();
			float soilWater = list.get(0).getSoilwater();

			BigDecimal a = new BigDecimal(humidityUp * soilWater/100);
			BigDecimal b = new BigDecimal(humidityDown * soilWater/100);
			float hUp = a.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			float hDown = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
			float tUp = list.get(0).getTemperatureup();
			float tDown = list.get(0).getTemperaturedown();
			
			tmp[0] = (byte) ((int) (hUp * 100) / 100);
			tmp[1] = (byte) ((int) (hUp * 100) % 100);
			tmp[2] = (byte) ((int) (hDown * 100) / 100);
			tmp[3] = (byte) ((int) (hDown * 100) % 100);

			// 温度值转换
			if (tUp < 0) {
				tmp[4] = (byte) ((int) (tUp * 100) / 100);
				tmp[5] = (byte) ((int) (-tUp * 100) % 100);
			} else {
				tmp[4] = (byte) ((int) (tUp * 100) / 100);
				tmp[5] = (byte) ((int) (tUp * 100) % 100);
			}
			if (tDown < 0) {
				tmp[6] = (byte) ((int) (tDown * 100) / 100);
				tmp[7] = (byte) ((int) (-tDown * 100) % 100);
			} else {
				tmp[6] = (byte) ((int) (tDown * 100) / 100);
				tmp[7] = (byte) ((int) (tDown * 100) % 100);
			}
			
			byte len = (byte)list.size();
			tmp[8] = len;   
			
			//节点个数
			ControlHost host = controlHostMapper.selectByPrimaryKey(list.get(0).getControlhostid());
			String hCode = host.getCode();
			
			byte[] data = codingFactory.byteMerger(tmp, codingFactory.string2BCD(codeStr));
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, hCode, (byte) 0x24, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(hCode, "34", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				byte num = receiveData[12];
				switch (num) {
				case 0x0A:
					mark = true;
					break;
				case (byte) 0xA0:
					mark = false;
					break;
				default:
					break;
				}
			}
			if(mark){
				equipmentMapper.setAutoParam(listTmp);
			}
		} catch (DataAccessException e) {
			LOG.error("设置节点自控参数异常", e);
			throw new ServerException(ErrorCode.SET_EQUIPMENT_PARAM_FAILD);
		}
		return mark;
	}

	/**
	 * @Title:       setTimeLen
	 * @author:      杨贵松
	 * @time         2015年2月2日 上午12:27:52
	 * @Description: 设置多节点时段
	 * @throws
	 */
	@Override
	public boolean setTimeLen(List<Equipment> list, HttpSession session) {
		boolean mark = false;
		String codeStr = "";
		Date date = new Date();
		Equipment obj = list.get(0);
		List<Equipment> listTmp = new ArrayList<Equipment>();
		User user = (User) session.getAttribute(Constants.USER);
		for(Equipment equipment:list){
			equipment.setModifytime(date);
			equipment.setModifyuser(user.getId());
			listTmp.add(equipment);
			codeStr += equipment.getCode();
		}
		// 查询节点信息
		try {
			
			String startone = obj.getTimeonestart();
			String endone = obj.getTimeoneend();
			String starttwo = obj.getTimetwostart();
			String endtwo = obj.getTimetwoend();
			String startthree = obj.getTimethreestart();
			String endthree = obj.getTimethreeend();
			
			String timeone="";
			String timetwo="";
			String timethree="";
			if(!startone.equals("")){
				timeone = startone+endone;
			}else{
				timeone = "24002400";
			}
			
			if(!starttwo.equals("")){
				timetwo = starttwo+endtwo;
			}else{
				timetwo = "24002400";
			}
			
			if(!startthree.equals("")){
				timethree = startthree+endthree;
			}else{
				timethree = "24002400";
			}
			
			String timeLen = "";
			String[] week = {};
			int T0 =0;
			if(!obj.getWeek().equals("")){
				week = obj.getWeek().split(",");
				T0 = week.length;
				for(int i=0;i<T0;i++){
					timeLen += "0"+week[i];
				}
			}
			
			byte[] time = codingFactory.string2BCD(timeone+timetwo+timethree);
			byte[] codeLen = new byte[]{(byte)list.size()};
			byte[] tmp = codingFactory.byteMerger(time,codeLen);
			
			byte[] codeAndWeek = codingFactory.string2BCD(codeStr+"0"+T0+timeLen);
			
			byte[] data = codingFactory.byteMerger(tmp,codeAndWeek);
			
			//节点个数
			ControlHost host = controlHostMapper.selectByPrimaryKey(list.get(0).getControlhostid());
			String hCode = host.getCode();
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, hCode, (byte) 0x26, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(hCode, "36", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				byte num = receiveData[12];
				switch (num) {
				case 0x0A:
					mark = true;
					break;
				case (byte) 0xA0:
					mark = false;
					break;
				default:
					break;
				}
			}
			if(mark){
				equipmentMapper.setTimeLen(listTmp);
			}
		} catch (DataAccessException e) {
			LOG.error("设置节点时段控制异常", e);
			throw new ServerException(ErrorCode.SET_EQUIPMENT_PARAM_FAILD);
		}
		return mark;
	}

	/**
	 * @Title:       getRelData
	 * @author:      杨贵松
	 * @time         2015年2月5日 下午5:04:23
	 * @Description: 查询现场实时累计灌溉量
	 * @throws
	 */
	@Override
	public List<EquipmentExt<EquipmentStatus>> getRelData(int pId) {
		List<EquipmentExt<EquipmentStatus>> list = new ArrayList<EquipmentExt<EquipmentStatus>>();
		try {
			ControlHost host = controlHostMapper.selectByProjectId(pId);
			String hCode = host.getCode();
			
			byte[] data = {};
			
			// 组装发送指令
			byte[] sendData = codingFactory.coding((byte) 0x01, hCode, (byte) 0x2B, data);
			// 开始的时间
			Date startDate = new Date();
			Client.sendToServer(sendData);
			// 等待获取主机返回的指令，等待10秒
			String dataContext = "";
			try {
				dataContext = netWorkService.waitData(hCode, "3B", startDate);
			} catch (ParseException e1) {
				LOG.error("获取10秒后的时间时异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			} catch (InterruptedException e1) {
				LOG.error("获取10秒后的时间时sleep异常", e1);
				throw new ServerException(ErrorCode.ILLEGAL_PARAM);
			}
			
			boolean flag = false;
			byte[] receiveData = null;
			if (!dataContext.equals("")) {
				receiveData = codingFactory.string2BCD(dataContext);
				flag = checkCoding.checkReceiveCoding(receiveData, sendData);
			}
			if (flag) {
				int num = receiveData[12];
				String usingData = dataContext.substring(26,dataContext.length()-6);
				
				for(int i=0;i<num;i++){
					//节点地址
					String eCode = usingData.substring(12*i,4+(12*i));
					//水流脉冲数
					long waterVal = Long.valueOf(usingData.substring(4+(12*i),12+(12*i)),16);
					
					Map<String,Object> map = new HashMap<String, Object>();
					map.put("hCode", hCode);
					map.put("eCode", eCode);
					Equipment equipment = equipmentMapper.selectEquipmentByHcodeAndEcode(map);
					
					EquipmentExt<EquipmentStatus> equipmentExt = new EquipmentExt<EquipmentStatus>();
					EquipmentStatus equipmentStatus = equipmentStatusMapper.selectEquipmentStatusByEid(equipment.getId());
					List<EquipmentStatus> result = new ArrayList<EquipmentStatus>();
					
					EquipmentStatus es = new EquipmentStatus();
					es.setWatervalue(waterVal);
					result.add(es);
					
					equipmentExt.setEquipmentStatus(equipmentStatus);
					equipmentExt.setEquipment(equipment);
					equipmentExt.setResult(result);
					list.add(equipmentExt);
				}
			}else{
				LOG.error("查询现场实时累计灌溉量失败");
				throw new ServerException(ErrorCode.SELECT_EQUIPMENT_WATER_FAILED);
			}
		} catch (DataAccessException e) {
			LOG.error("查询现场实时累计灌溉量异常", e);
			throw new ServerException(ErrorCode.SELECT_EQUIPMENT_WATER_FAILED);
		}
		return list;
	}
	
	/**
	 * @Title:       putData
	 * @author:      杨贵松
	 * @time         2015年2月8日 下午3:55:13
	 * @Description: 赋值
	 * @throws
	 */
	@Override
	public String putData(List<Equipment> list) {
		String result = "";
		
		try {
			if(!CollectionUtils.isEmpty(list)){
				int cId = list.get(0).getControlhostid();
				ControlHost host = controlHostMapper.selectByPrimaryKey(cId);
				String hCode = host.getCode();
				
				List<Equipment> listOK = new ArrayList<Equipment>();
				List<Equipment> listError = new ArrayList<Equipment>();
				for(Equipment equipment:list){
					byte[] eCode = codingFactory.string2BCD(equipment.getCode());
					byte[] eData = codingFactory.longToByte(equipment.getWatervalue());
					byte[] data = codingFactory.byteMerger(eCode, eData);
					
					// 组装发送指令
					byte[] sendData = codingFactory.coding((byte) 0x01, hCode, (byte) 0x0F, data);
					// 开始的时间
					Date startDate = new Date();
					Client.sendToServer(sendData);
					// 等待获取主机返回的指令，等待10秒
					String dataContext = "";
					try {
						dataContext = netWorkService.waitData(hCode, "1F", startDate);
					} catch (ParseException e1) {
						LOG.error("获取10秒后的时间时异常", e1);
						throw new ServerException(ErrorCode.ILLEGAL_PARAM);
					} catch (InterruptedException e1) {
						LOG.error("获取10秒后的时间时sleep异常", e1);
						throw new ServerException(ErrorCode.ILLEGAL_PARAM);
					}
					
					boolean flag = false;
					byte[] receiveData = null;
					if (!dataContext.equals("")) {
						receiveData = codingFactory.string2BCD(dataContext);
						flag = checkCoding.checkReceiveCoding(receiveData, sendData);
					}
					if (flag) {
						byte rData = receiveData[12];
						switch(rData){
						case 0x0A :
							listOK.add(equipment);
							break;
						case (byte) 0xA0 :
							listError.add(equipment);
							break;
						default:
							break;
						}
					}
				}
				//将成功的节点信息保存到数据库
				if(CollectionUtils.isEmpty(listOK)){
					LOG.error("赋值采集数据失败");
					throw new ServerException(ErrorCode.PUT_EQUIPMENT_DATA_FAILED);
				}else if(!CollectionUtils.isEmpty(listError)){
					String name = "";
					for(Equipment equipment:listError){
						name += equipment.getName()+"、";
					}
					LOG.error("赋值【"+name.substring(0,name.length()-1)+"】等共计【"+listError.size()+"】个节点的采集数据失败");
					result = "赋值【"+name.substring(0,name.length()-1)+"】等共计【"+listError.size()+"】个节点的采集数据失败";
				}
			}
		} catch (DataAccessException e) {
			LOG.error("赋值采集数据异常", e);
			throw new ServerException(ErrorCode.PUT_EQUIPMENT_DATA_FAILED);
		}
		return result;
	}
}
