package com.rest.yun.mapping;

import java.util.List;
import java.util.Map;

import com.rest.yun.beans.SensorInfo;

public interface SensorInfoMapper {
    int deleteByPrimaryKey(Integer id);

    /**
     * @Title:       save
     * @author:      杨贵松
     * @time         2014年11月26日 下午5:21:45
     * @Description: 批量保存传感器数据
     * @return       int
     * @throws
     */
    int save(List<SensorInfo> list);

    int insertSelective(SensorInfo record);

    SensorInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SensorInfo record);

    int updateByPrimaryKey(SensorInfo record);

	/** 
	 * @Title:       selectByEidAndNum
	 * @author:      杨贵松
	 * @Description: 根据节点id和传感器number查询传感器信息 
	 * @return       SensorInfo
	 * @throws 
	 */
	SensorInfo selectByEidAndNum(Map<String,Object> map);

	/** 
	 * @Title:       selectSensorInfoByEid
	 * @author:      杨贵松
	 * @time         2014年11月6日 上午3:55:08
	 * @Description: 查询节点下的传感器
	 * @return       List<SensorInfo>
	 * @throws 
	 */
	List<SensorInfo> selectSensorInfoByEid(Integer eId);

	/**
	 * @Title:       deleteAllByHid
	 * @author:      杨贵松
	 * @time         2014年11月30日 下午3:59:41
	 * @Description: 根据主机id删除其关联的所有传感器信息
	 * @return       void
	 * @throws
	 */
	void deleteAllByHid(int controlHostId);
}