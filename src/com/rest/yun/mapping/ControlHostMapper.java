package com.rest.yun.mapping;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.rest.yun.beans.ControlHost;

public interface ControlHostMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(ControlHost record);

	int insertSelective(ControlHost record);


	int updateByPrimaryKeySelective(ControlHost record);

	int updateByPrimaryKey(ControlHost record);

	List<Map<String, Object>> selectHostForList(Map<String, Object> params);

	ControlHost selectByProjectId(int projectId);

	/**
	 * @Title:       selectByPrimaryKey
	 * @author:      杨贵松
	 * @time         2014年12月29日 下午9:15:56
	 * @Description: 根据id查询主机信息
	 * @return       ControlHost
	 * @throws
	 */
	ControlHost selectByPrimaryKey(Integer id);
	
	/**
	 * 验证host code 是否存在
	 * 
	 * @param code
	 * @param hostId
	 *            if hostId = 0,验证所有的host，if hostId != 0,表示验证除了这个host之外其他所用的host
	 * @return
	 */
	boolean validHostCodeExceptById(@Param("code") String code, @Param("hostId") int hostId);

	/**
	 * 获取host的基本信息和项目的部分信息
	 * 
	 * @param hostId
	 * @return
	 */
	Map<String, Object> getHostAndProjectByHostId(int hostId);

	/**
	 * @Title: selectByCode
	 * @author: 杨贵松
	 * @time 2014年11月4日 上午4:30:28
	 * @Description: 根据通讯地址查询主机信息
	 * @return ControlHost
	 * @throws
	 */
	ControlHost selectByCode(String code);

	int selectAllHostCounts(Map<String, Object> map);

	List<ControlHost> selectAllHostPages(Map<String, Object> map);
}