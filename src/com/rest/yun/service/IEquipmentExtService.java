package com.rest.yun.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.rest.yun.beans.Equipment;
import com.rest.yun.beans.EquipmentStatus;
import com.rest.yun.dto.EquipmentExt;

public interface IEquipmentExtService {
	/**
	 * @return 
	 * @Title:       updateList
	 * @author:      杨贵松
	 * @time         2014年12月28日 下午4:42:50
	 * @Description: 批量设置节点及传感器信息
	 * @return       String
	 * @throws
	 */
	String updateList(List<Equipment> list, HttpSession session);

	/**
	 * @return 
	 * @Title:       setListModel
	 * @author:      杨贵松
	 * @time         2015年1月31日 下午11:40:00
	 * @Description: 设置多节点模式
	 * @return       boolean
	 * @throws
	 */
	boolean setListModel(List<Equipment> list, HttpSession session);

	/**
	 * @Title:       setAutoParam
	 * @author:      杨贵松
	 * @time         2015年2月1日 下午8:44:45
	 * @Description: 设置多借点自控参数
	 * @return       void
	 * @throws
	 */
	boolean setAutoParam(List<Equipment> list, HttpSession session);

	/**
	 * @Title:       setTimeLen
	 * @author:      杨贵松
	 * @time         2015年2月2日 上午12:27:08
	 * @Description: 设置多节点时段
	 * @return       void
	 * @throws
	 */
	boolean setTimeLen(List<Equipment> list, HttpSession session);

	/**
	 * @Title:       getRelData
	 * @author:      杨贵松
	 * @time         2015年2月5日 下午5:03:48
	 * @Description: 查询现场实时累计灌溉量
	 * @return       List<EquipmentExt<EquipmentStatus>>
	 * @throws
	 */
	List<EquipmentExt<EquipmentStatus>> getRelData(int pId);

	/**
	 * @Title:       putData
	 * @author:      杨贵松
	 * @time         2015年2月8日 下午3:54:39
	 * @Description: 赋值
	 * @return       String
	 * @throws
	 */
	String putData(List<Equipment> list);

}
