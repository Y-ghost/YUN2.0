package com.rest.yun.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.jxls.exception.ParsePropertyException;
import net.sf.jxls.transformer.XLSTransformer;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rest.yun.beans.Equipment;
import com.rest.yun.beans.EquipmentData;
import com.rest.yun.beans.EquipmentStatus;
import com.rest.yun.beans.SensorInfo;
import com.rest.yun.dto.EquipmentDataExt;
import com.rest.yun.dto.EquipmentExt;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.mapping.EquipmentDataMapper;
import com.rest.yun.mapping.EquipmentMapper;
import com.rest.yun.mapping.EquipmentStatusMapper;
import com.rest.yun.mapping.SensorInfoMapper;
import com.rest.yun.service.IStatisticService;
import com.rest.yun.util.CommonUtiles;

@Service
public class StatisticServiceImpl implements IStatisticService {

	private final static Logger LOG = LoggerFactory.getLogger(StatisticServiceImpl.class);

	@Autowired
	private EquipmentMapper equipmentMapper;
	@Autowired
	private EquipmentStatusMapper equipmentStatusMapper;
	@Autowired
	private SensorInfoMapper sensorInfoMapper;
	@Autowired
	private EquipmentDataMapper equipmentDataMapper;

	/**
	 * @Title:       selectEquipmentExtsBy
	 * @author:      杨贵松
	 * @time         2014年12月30日 上午6:37:48
	 * @Description: 分页统计节点数据
	 * @throws
	 */
//	@Override
//	public Page<EquipmentExt<EquipmentData>> selectEquipmentExtsBy( Integer pageNow, Integer pageSize, Map<String, Object> criteria, Integer pId) {
//		Map<String, Object> params = new HashMap<String, Object>();
//		Page<EquipmentExt<EquipmentData>> page = new Page<EquipmentExt<EquipmentData>>(pageNow, pageSize);
//		params.put(Constants.PAGE, page);
//		params.put("pId", pId);
//		if (criteria != null) {
//			params.putAll(criteria);
//		}
//		
//		List<EquipmentExt<EquipmentData>> list = new ArrayList<EquipmentExt<EquipmentData>>();
//		try {
//			List<Equipment> eList = equipmentMapper.selectByPage(params);
//			if (!CollectionUtils.isEmpty(eList)) {
//				for (Equipment equipment : eList) {
//					EquipmentExt<EquipmentData> equipmentExt = new EquipmentExt<EquipmentData>();
//					EquipmentStatus equipmentStatus = equipmentStatusMapper.selectEquipmentStatusByeEid(equipment.getId());
//					List<SensorInfo> sList = sensorInfoMapper.selectSensorInfoByEid(equipment.getId());
//					List<EquipmentData> edList = new ArrayList<EquipmentData>();
//					for (SensorInfo sensor : sList) {
//						EquipmentData equipmentData = equipmentDataMapper.selectByPrimaryKey(sensor.getId());
//						if (equipmentData == null) {
//							equipmentData = new EquipmentData();
//							equipmentData.setHumidity((float) 0);
//							equipmentData.setSensorid(sensor.getId());
//						}
//						edList.add(equipmentData);
//					}
//
//					equipmentExt.setId(equipment.getId());
//					equipmentExt.setName(equipment.getName());
//					equipmentExt.setCode(equipment.getCode());
//					equipmentExt.setControlHostId(equipment.getControlhostid());
//					equipmentExt.setEquipmentStatus(equipmentStatus);
//					equipmentExt.setEquipment(equipment);
//					equipmentExt.setResult(edList);
//
//					list.add(equipmentExt);
//				}
//			} else {
//				LOG.warn("Equipment is null");
//				throw new ServerException(ErrorCode.EQUIPMENT_LIST_NULL);
//			}
//		} catch (DataAccessException e) {
//			LOG.error("查询实时现场数据异常", e);
//			throw new ServerException(ErrorCode.SELECT_EQUIPMENT_LIST_FAILED);
//		}
//		page.setResult(list);
//		return page;
//	}

	/**
	 * @Title:       waterList
	 * @author:      杨贵松
	 * @time         2015年1月14日 上午5:45:32
	 * @Description: 统计灌溉用水量
	 * @throws
	 */
	@Override
	public List<EquipmentExt<EquipmentStatus>> waterList(Integer pId, Integer eId, Date startDate, Date endDate) {
		List<EquipmentExt<EquipmentStatus>> list = new ArrayList<EquipmentExt<EquipmentStatus>>();
				
		List<Date> dateList = new ArrayList<Date>();
		dateList = CommonUtiles.betweenDayList(startDate,endDate);
		
		//统计日期为空的话终止执行
		if(CollectionUtils.isEmpty(dateList)){
			LOG.error("统计日期不能为空");
			throw new ServerException(ErrorCode.STATISTIC_DATE_NULL);
		}
		
		List<Equipment> eList = new ArrayList<Equipment>();
		try {
			//查询节点
			if(eId==-1){
				eList = equipmentMapper.selectByPid(pId);
			}else{
				Equipment equipment = equipmentMapper.selectByPrimaryKey(eId);
				eList.add(equipment);
			}
		} catch (DataAccessException e) {
			LOG.error("统计灌水量异常",e);
			throw new ServerException(ErrorCode.STATISTIC_WATER_FAILED);
		}
		if(CollectionUtils.isEmpty(eList)){
			LOG.error("统计分析未查询到节点");
			throw new ServerException(ErrorCode.STATISTIC_EQUIPMENT_NULL);
		}
		
		//根据节点查询状态水量
		try {
			for(Equipment e:eList){
				EquipmentExt<EquipmentStatus> equipmentExt = new EquipmentExt<EquipmentStatus>();
				equipmentExt.setEquipment(e);
				
				//查询每个日期的总灌水量
				List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
				for(Date date : dateList){
					EquipmentStatus equipmentStatus = new EquipmentStatus();
					equipmentStatus.setCreatetime(date);
					equipmentStatus.setEquipmentid(e.getId());
					long currentvalue = equipmentStatusMapper.selectEquipmentStatusByEidDate(equipmentStatus);
					equipmentStatus.setCurrentvalue(currentvalue);
					
					statusList.add(equipmentStatus);
				}
				equipmentExt.setResult(statusList);
				
				list.add(equipmentExt);
			}
		} catch (DataAccessException e) {
			LOG.error("统计灌水量异常",e);
			throw new ServerException(ErrorCode.STATISTIC_WATER_FAILED);
		}

		return list;
	}

	/**
	 * @Title:       humidityList
	 * @author:      杨贵松
	 * @time         2015年1月14日 上午5:45:36
	 * @Description: 统计灌区湿度值
	 * @throws
	 */
	@Override
	public List<EquipmentDataExt<EquipmentData>> humidityList(Integer pId, Integer eId, Date startDate, Date endDate) {
		List<EquipmentDataExt<EquipmentData>> list = new ArrayList<EquipmentDataExt<EquipmentData>>();
		
		List<Date> dateList = new ArrayList<Date>();
		dateList = CommonUtiles.betweenDayList(startDate,endDate);
		//统计日期为空的话终止执行
		if(CollectionUtils.isEmpty(dateList)){
			LOG.error("统计日期不能为空");
			throw new ServerException(ErrorCode.STATISTIC_DATE_NULL);
		}
		
		List<Equipment> eList = new ArrayList<Equipment>();
		try {
			//查询节点
			if(eId==-1){
				eList = equipmentMapper.selectByPid(pId);
			}else{
				Equipment equipment = equipmentMapper.selectByPrimaryKey(eId);
				eList.add(equipment);
			}
		} catch (DataAccessException e) {
			LOG.error("统计灌区湿度异常",e);
			throw new ServerException(ErrorCode.STATISTIC_HUMIDITY_FAILED);
		}
		
		if(CollectionUtils.isEmpty(eList)){
			LOG.error("统计分析未查询到节点");
			throw new ServerException(ErrorCode.STATISTIC_EQUIPMENT_NULL);
		}
		
		//根据节点查询湿度值
		try {
			for(Equipment e:eList){
				List<SensorInfo> sList = sensorInfoMapper.selectSensorInfoByEid(e.getId());
				
				if(CollectionUtils.isEmpty(sList)){
					//如果该节点下无传感器，跳过本次循环
					continue;
				}
				
				for(SensorInfo sensor:sList){
					EquipmentDataExt<EquipmentData> equipmentDataExt = new EquipmentDataExt<EquipmentData>();
					equipmentDataExt.setEquipment(e);
					equipmentDataExt.setSensor(sensor);
					
					//查询每个日期的湿度值
					List<EquipmentData> dList = new ArrayList<EquipmentData>();
					for(Date date : dateList){
						EquipmentData equipmentData = new EquipmentData();
						equipmentData.setCreatetime(date);
						equipmentData.setSensorid(sensor.getId());
						
						float humidity = equipmentDataMapper.selectHumidityBySid(equipmentData);
						equipmentData.setHumidity(humidity);
						
						dList.add(equipmentData);
					}
					equipmentDataExt.setResult(dList);
					list.add(equipmentDataExt);
				}
			}
		} catch (DataAccessException e) {
			LOG.error("统计灌区湿度异常",e);
			throw new ServerException(ErrorCode.STATISTIC_HUMIDITY_FAILED);
		}
		
		return list;
	}

	/**
	 * @Title:       waterExport
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午6:19:22
	 * @Description: 导出用水量Excel
	 * @throws
	 */
	@Override
	public String waterExport(Integer pId, Integer eId, Date startDate, Date endDate, HttpServletRequest request) {
		String fileName = UUID.randomUUID() + ".xls";
		String ctxPath = request.getSession().getServletContext() .getRealPath("/") + "download/";
		String srcFilePath = ctxPath+ File.separatorChar + "waterTemp.xls";
		String exportFilePath = ctxPath + File.separatorChar +"xls/" + File.separatorChar + fileName;
		
		List<EquipmentExt<EquipmentStatus>> list = waterInfoList(pId,eId,startDate,endDate);
		
		Map<String,Object> beanParams = new HashMap<String, Object>();
		beanParams.put("waterList", list);
		importExcel(srcFilePath,beanParams,exportFilePath);
		return fileName;
	}
	
	/**
	 * @Title:       waterInfoList
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午10:15:02
	 * @Description: 统计用水量详情
	 * @throws
	 */
	public List<EquipmentExt<EquipmentStatus>> waterInfoList(Integer pId, Integer eId, Date startDate, Date endDate) {
		List<EquipmentExt<EquipmentStatus>> list = new ArrayList<EquipmentExt<EquipmentStatus>>();
				
		List<Date> dateList = new ArrayList<Date>();
		dateList = CommonUtiles.betweenDayList(startDate,endDate);
		
		//统计日期为空的话终止执行
		if(CollectionUtils.isEmpty(dateList)){
			LOG.error("统计日期不能为空");
			throw new ServerException(ErrorCode.STATISTIC_DATE_NULL);
		}
		
		List<Equipment> eList = new ArrayList<Equipment>();
		try {
			//查询节点
			if(eId==-1){
				eList = equipmentMapper.selectByPid(pId);
			}else{
				Equipment equipment = equipmentMapper.selectByPrimaryKey(eId);
				eList.add(equipment);
			}
		} catch (DataAccessException e) {
			LOG.error("统计灌水量异常",e);
			throw new ServerException(ErrorCode.STATISTIC_WATER_FAILED);
		}
		if(CollectionUtils.isEmpty(eList)){
			LOG.error("统计分析未查询到节点");
			throw new ServerException(ErrorCode.STATISTIC_EQUIPMENT_NULL);
		}
		
		//根据节点查询状态水量
		try {
			for(Equipment e:eList){
				EquipmentExt<EquipmentStatus> equipmentExt = new EquipmentExt<EquipmentStatus>();
				equipmentExt.setEquipment(e);
				
				//查询每个日期的总灌水量
				List<EquipmentStatus> statusList = new ArrayList<EquipmentStatus>();
				for(Date date : dateList){
					EquipmentStatus equipmentStatus = new EquipmentStatus();
					equipmentStatus.setCreatetime(date);
					equipmentStatus.setEquipmentid(e.getId());
					statusList = equipmentStatusMapper.selectEquipmentStatusList(equipmentStatus);
				}
				equipmentExt.setResult(statusList);
				
				list.add(equipmentExt);
			}
		} catch (DataAccessException e) {
			LOG.error("统计灌水量异常",e);
			throw new ServerException(ErrorCode.STATISTIC_WATER_FAILED);
		}

		return list;
	}
	/**
	 * @Title:       humidityExport
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午7:30:36
	 * @Description: 导出湿度值Excel
	 * @throws
	 */
	@Override
	public String humidityExport(Integer pId, Integer eId, Date startDate, Date endDate, HttpServletRequest request ) {
		String fileName = UUID.randomUUID() + ".xls";
		String ctxPath = request.getSession().getServletContext() .getRealPath("/") + "download/";
		String srcFilePath = ctxPath+ File.separatorChar + "humidityTemp.xls";
		String exportFilePath = ctxPath + File.separatorChar +"xls/" + File.separatorChar + fileName;

		List<EquipmentDataExt<EquipmentData>> list =  humidityInfoList(pId,eId,startDate,endDate);
		
		Map<String,Object> beanParams = new HashMap<String, Object>();
		beanParams.put("humidityList", list);
		importExcel(srcFilePath,beanParams,exportFilePath);
		return fileName;
	}
	
	/**
	 * @Title:       humidityInfoList
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午10:19:30
	 * @Description: 湿度详情
	 * @return       List<EquipmentDataExt<EquipmentData>>
	 * @throws
	 */
	public List<EquipmentDataExt<EquipmentData>> humidityInfoList(Integer pId, Integer eId, Date startDate, Date endDate) {
		List<EquipmentDataExt<EquipmentData>> list = new ArrayList<EquipmentDataExt<EquipmentData>>();
		
		List<Date> dateList = new ArrayList<Date>();
		dateList = CommonUtiles.betweenDayList(startDate,endDate);
		//统计日期为空的话终止执行
		if(CollectionUtils.isEmpty(dateList)){
			LOG.error("统计日期不能为空");
			throw new ServerException(ErrorCode.STATISTIC_DATE_NULL);
		}
		
		List<Equipment> eList = new ArrayList<Equipment>();
		try {
			//查询节点
			if(eId==-1){
				eList = equipmentMapper.selectByPid(pId);
			}else{
				Equipment equipment = equipmentMapper.selectByPrimaryKey(eId);
				eList.add(equipment);
			}
		} catch (DataAccessException e) {
			LOG.error("统计灌区湿度异常",e);
			throw new ServerException(ErrorCode.STATISTIC_HUMIDITY_FAILED);
		}
		
		if(CollectionUtils.isEmpty(eList)){
			LOG.error("统计分析未查询到节点");
			throw new ServerException(ErrorCode.STATISTIC_EQUIPMENT_NULL);
		}
		
		//根据节点查询湿度值
		try {
			for(Equipment e:eList){
				List<SensorInfo> sList = sensorInfoMapper.selectSensorInfoByEid(e.getId());
				
				if(CollectionUtils.isEmpty(sList)){
					//如果该节点下无传感器，跳过本次循环
					continue;
				}
				
				for(SensorInfo sensor:sList){
					EquipmentDataExt<EquipmentData> equipmentDataExt = new EquipmentDataExt<EquipmentData>();
					equipmentDataExt.setEquipment(e);
					equipmentDataExt.setSensor(sensor);
					
					//查询每个日期的湿度值
					List<EquipmentData> dList = new ArrayList<EquipmentData>();
					for(Date date : dateList){
						EquipmentData equipmentData = new EquipmentData();
						equipmentData.setCreatetime(date);
						equipmentData.setSensorid(sensor.getId());
						
						dList = equipmentDataMapper.selectHumidityList(equipmentData);
					}
					equipmentDataExt.setResult(dList);
					list.add(equipmentDataExt);
				}
			}
		} catch (DataAccessException e) {
			LOG.error("统计灌区湿度异常",e);
			throw new ServerException(ErrorCode.STATISTIC_HUMIDITY_FAILED);
		}
		
		return list;
	}
	/**
	 * @Title:       importExcel
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午9:26:03
	 * @Description: 在服务器生成Excel
	 * @return       void
	 * @throws
	 */
	public void importExcel(String srcFilePath, Map<String ,Object> beanParams , String exportFilePath){
		try {
			XLSTransformer former = new XLSTransformer();
			former.transformXLS(srcFilePath, beanParams, exportFilePath);
		} catch (IOException e) {
			LOG.error("导出Excel异常1",e);
			throw new ServerException(ErrorCode.STATISTIC_EXPORT_EXCEL_FAILED);
		} catch (ParsePropertyException e) {
			LOG.error("导出Excel异常2",e);
			throw new ServerException(ErrorCode.STATISTIC_EXPORT_EXCEL_FAILED);
		} catch (InvalidFormatException e) {
			LOG.error("导出Excel异常3",e);
			throw new ServerException(ErrorCode.STATISTIC_EXPORT_EXCEL_FAILED);
		}
	}
	/**
	 * @throws IOException 
	 * @Title:       exportExcel
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午6:13:01
	 * @Description: 导出Excel
	 * @return       void
	 * @throws
	 */
	public void exportExcel (String fileName , HttpServletRequest request, HttpServletResponse response){
		try {
			java.io.BufferedInputStream bis = null;
			java.io.BufferedOutputStream bos = null;
			response.setContentType("text/html;charset=utf-8");
			request.setCharacterEncoding("UTF-8");

			String ctxPath = request.getSession().getServletContext() .getRealPath("/") + "download/" + File.separatorChar +"xls/";
			String downLoadPath = ctxPath + File.separatorChar + fileName;

			long fileLength = new File(downLoadPath).length();
			response.setContentType("application/x-msdownload;");
			response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("utf-8"), "ISO8859-1"));
			response.setHeader("Content-Length", String.valueOf(fileLength));
			bis = new BufferedInputStream(new FileInputStream(downLoadPath));
			bos = new BufferedOutputStream(response.getOutputStream());
			byte[] buff = new byte[2048];
			int bytesRead;
			while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
				bos.write(buff, 0, bytesRead);
			}
			if (bis != null)
				bis.close();
			if (bos != null)
				bos.close();
			
			//删除文件夹下所有的文件，减少磁盘占用
			CommonUtiles.delAllFile(ctxPath);
			
		} catch (UnsupportedEncodingException e) {
			LOG.error("导出Excel异常1",e);
			throw new ServerException(ErrorCode.STATISTIC_EXPORT_EXCEL_FAILED);
		} catch (FileNotFoundException e) {
			LOG.error("导出Excel异常1",e);
			throw new ServerException(ErrorCode.STATISTIC_EXPORT_EXCEL_FAILED);
		} catch (IOException e) {
			LOG.error("导出Excel异常1",e);
			throw new ServerException(ErrorCode.STATISTIC_EXPORT_EXCEL_FAILED);
		}
	}
}
