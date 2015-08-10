package com.rest.yun.mapping;

import java.util.List;
import java.util.Map;

import com.rest.yun.beans.DataTemp;

public interface DataTempMapper {
	
	DataTemp selectDataTemp(Map<String, Object> map);
	
	List<DataTemp> selectDataTempForList(Map<String, Object> map);

	void insert(DataTemp dataTemp);

	List<DataTemp> selectAllOldData();

	void deleteAllOldData(List<DataTemp> list);

	DataTemp selectDataMax(Map<String, Object> map);

	/** 
	 * @Title:       selectDataCount
	 * @author:      杨贵松
	 * @time         2014年11月5日 上午2:35:50
	 * @Description: 通过检查心跳包，监测判断主机是否在线 
	 * @return       int
	 * @throws 
	 */
	int selectDataCount(Map<String, Object> dataTmp);
}