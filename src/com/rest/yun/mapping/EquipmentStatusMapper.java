package com.rest.yun.mapping;

import java.util.List;

import com.rest.yun.beans.EquipmentStatus;

public interface EquipmentStatusMapper {
    int deleteByPrimaryKey(Integer id);

//    批量添加节点状态数据
    int insert(List<EquipmentStatus> listStatus);

    int insertSelective(EquipmentStatus record);

    EquipmentStatus selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(EquipmentStatus record);

    int updateByPrimaryKey(EquipmentStatus record);

	/** 
	 * @Title:       selectEquipmentStatusByeEid
	 * @author:      杨贵松
	 * @time         2014年11月6日 上午1:16:25
	 * @Description: 根据节点id查询其状态数据
	 * @return       EquipmentStatus
	 * @throws
	 */
	EquipmentStatus selectEquipmentStatusByEid(Integer eId);

	/**
	 * @Title:       deleteAllByHid
	 * @author:      杨贵松
	 * @time         2014年11月30日 下午4:14:12
	 * @Description: 删除主机下所有节点及其相关联数据
	 * @return       void
	 * @throws
	 */
	void deleteAllByHid(int controlHostId);
	
	/**
	 * @Title:       selectEquipmentStatusByEidDate
	 * @author:      杨贵松
	 * @time         2015年1月14日 上午6:17:32
	 * @Description: 查询单独一个节点一天总的灌水量
	 * @return       EquipmentStatus
	 * @throws
	 */
	long selectEquipmentStatusByEidDate(EquipmentStatus equipmentStatus);

	/**
	 * @Title:       selectEquipmentStatusList
	 * @author:      杨贵松
	 * @time         2015年1月25日 下午10:17:21
	 * @Description: 统计用水量详情
	 * @return       List<EquipmentStatus>
	 * @throws
	 */
	List<EquipmentStatus> selectEquipmentStatusList(
			EquipmentStatus equipmentStatus);

}