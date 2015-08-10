package com.rest.yun.mapping;

import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.rest.yun.beans.Equipment;

public interface EquipmentMapper {
	int deleteByPrimaryKey(Integer id);
	
	/**
	 * @Title:       deleteAllByHid
	 * @author:      杨贵松
	 * @time         2014年11月30日 下午3:20:02
	 * @Description: 删除主机下所有节点及其相关联数据
	 * @return       int
	 * @throws
	 */
	int deleteAllByHid(Integer id);

	int insert(Equipment record);

	int save(Equipment record);

	/**
	 * @Title:       selectByPrimaryKey
	 * @author:      杨贵松
	 * @time         2014年12月30日 下午12:58:50
	 * @Description: 根据id查节点信息
	 * @return       Equipment
	 * @throws
	 */
	Equipment selectByPrimaryKey(Integer id);

	//	更新节点信息	
	int updateByPrimaryKeySelective(Equipment record);

	int updateByPrimaryKey(Equipment record);

	/**
	 * @Title: selectEquipmentByHcodeAndEcode
	 * @author: 杨贵松
	 * @Description: 根据Hcode和Ecode查询节点
	 * @return Equipment
	 * @throws
	 */
	Equipment selectEquipmentByHcodeAndEcode(Map<String, Object> map);

	/**
	 * @Title: selectByPid
	 * @author: 杨贵松
	 * @time 2014年11月6日 上午12:53:40
	 * @Description: 根据项目id查询节点信息
	 * @return List<Equipment>
	 * @throws
	 */
	List<Equipment> selectByPid(Integer pId);

	/**
	 * @Title: selectByIdStr
	 * @author: 杨贵松
	 * @time 2014年11月6日 下午11:40:22
	 * @Description: 根据多个节点id查询节点信息
	 * @return List<Equipment>
	 * @throws
	 */
	List<Equipment> selectByIdStr(Map<String, Object> map);

	/**
	 * 查询节点列表分页数据
	 * 
	 * @param map
	 *            包括分页信息和限制条件参数
	 * @return
	 * @throws DataAccessException
	 */
	List<Equipment> selectEqtForList(Map<String, Object> map) throws DataAccessException;

	
	/**
	 * @Title:       selectByPage
	 * @author:      杨贵松
	 * @time         2014年12月30日 上午6:37:34
	 * @Description: 分页统计节点数据
	 * @return       List<Equipment>
	 * @throws
	 */
	List<Equipment> selectByPage(Map<String, Object> map);

	/**
	 * @Title:       setListModel
	 * @author:      杨贵松
	 * @time         2015年1月31日 下午11:57:12
	 * @Description: 设置多节点模式
	 * @return       void
	 * @throws
	 */
	void setListModel(List<Equipment> list);

	/**
	 * @Title:       setAutoParam
	 * @author:      杨贵松
	 * @time         2015年2月1日 下午8:54:35
	 * @Description: 设置多节点自控参数
	 * @return       void
	 * @throws
	 */
	void setAutoParam(List<Equipment> list);

	/**
	 * @Title:       setTimeLen
	 * @author:      杨贵松
	 * @time         2015年2月2日 上午12:40:58
	 * @Description: 设置多节点时段
	 * @return       void
	 * @throws
	 */
	void setTimeLen(List<Equipment> list);
}