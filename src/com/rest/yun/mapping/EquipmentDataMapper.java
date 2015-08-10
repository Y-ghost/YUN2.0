package com.rest.yun.mapping;

import java.util.List;

import com.rest.yun.beans.EquipmentData;

public interface EquipmentDataMapper {
    int deleteByPrimaryKey(Integer id);
    //批量添加传感器数据
    int insert(List<EquipmentData> listData);

    int insertSelective(EquipmentData record);
    //根据传感器id查询最新的一条数据 
    EquipmentData selectBySid(Integer sId);

    int updateByPrimaryKeySelective(EquipmentData record);

    int updateByPrimaryKey(EquipmentData record);
    //根据主机id删除其关联的所有节点数据信息
	void deleteAllByHid(int controlHostId);
	
	//获取单个传感器当日的湿度值
	float selectHumidityBySid(EquipmentData equipmentData);
	
	//湿度详情
	List<EquipmentData> selectHumidityList(EquipmentData equipmentData);
}