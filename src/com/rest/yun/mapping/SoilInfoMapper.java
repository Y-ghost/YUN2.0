package com.rest.yun.mapping;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.rest.yun.beans.SoilInfo;

public interface SoilInfoMapper {
	/**
	 * @Title:       deleteSoil
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午7:19:53
	 * @Description: 删除土壤数据
	 * @return       int
	 * @throws
	 */
    int deleteSoil(Integer id);

    int insert(SoilInfo record);

    /**
     * @Title:       selectSoilById
     * @author:      杨贵松
     * @time         2015年2月11日 下午7:15:01
     * @Description: 根据id查询土壤详情
     * @return       SoilInfo
     * @throws
     */
    SoilInfo selectSoilById(Integer id);

    int updateByPrimaryKeySelective(SoilInfo record);

    /**
     * @Title:       update
     * @author:      杨贵松
     * @time         2015年2月11日 下午7:46:00
     * @Description: 更新土壤信息
     * @return       int
     * @throws
     */
    int update(SoilInfo record);

    /**
     * @Title:       selectSoilInfo
     * @author:      杨贵松
     * @time         2014年12月5日 下午11:59:04
     * @Description: 查询土壤列表
     * @return       List<SoilInfo>
     * @throws
     */
	List<SoilInfo> selectSoilInfo();

	/**
	 * @Title:       validSoilName
	 * @author:      杨贵松
	 * @time         2014年12月6日 下午9:47:22
	 * @Description: 验证土壤名是否存在
	 * @return       boolean
	 * @throws
	 */
	boolean validSoilName(@Param("soiltype") String soiltype, @Param("soilId") int soilId);

	/**
	 * @Title:       save
	 * @author:      杨贵松
	 * @time         2014年12月15日 下午9:56:21
	 * @Description: 添加土壤信息
	 * @return       void
	 * @throws
	 */
	void save(SoilInfo soil);

	/**
	 * @Title:       selectSoilForList
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午6:49:26
	 * @Description: 分页查询土壤信息
	 * @return       List<SoilInfo>
	 * @throws
	 */
	List<SoilInfo> selectSoilForList(Map<String, Object> params);
}