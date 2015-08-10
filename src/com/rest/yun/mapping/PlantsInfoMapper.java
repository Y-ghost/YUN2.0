package com.rest.yun.mapping;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.rest.yun.beans.PlantsInfo;

public interface PlantsInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(PlantsInfo record);

    int updateByPrimaryKeySelective(PlantsInfo record);

    int updateByPrimaryKey(PlantsInfo record);
    
    /**
     * @Title:       selectPlantsInfo
     * @author:      杨贵松
     * @time         2014年12月5日 下午11:51:32
     * @Description: 查询植物列表
     * @return       List<PlantsInfo>
     * @throws
     */
    List<PlantsInfo> selectPlantsInfo();

    /**
     * @Title:       save
     * @author:      杨贵松
     * @time         2014年12月22日 下午3:10:43
     * @Description: 添加植物
     * @return       int
     * @throws
     */
	int save(PlantsInfo plants);

	/**
	 * @Title:       validPlantsName
	 * @author:      杨贵松
	 * @time         2014年12月23日 下午5:58:52
	 * @Description: 校验植物名是否存在
	 * @return       boolean
	 * @throws
	 */
	boolean validPlantsName(@Param("plantsname") String plantsname, @Param("plantsId") int plantsId);

	/**
	 * @Title:       selectPlantsById
	 * @author:      杨贵松
	 * @time         2015年3月2日 下午8:52:46
	 * @Description: 查询植物详情
	 * @return       PlantsInfo
	 * @throws
	 */
	PlantsInfo selectPlantsById(int id);
}