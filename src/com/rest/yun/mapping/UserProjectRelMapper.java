package com.rest.yun.mapping;

import java.util.List;

import com.rest.yun.beans.UserProjectRel;

public interface UserProjectRelMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(UserProjectRel record);

    int insertSelective(UserProjectRel record);

    UserProjectRel selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(UserProjectRel record);

    int updateByPrimaryKey(UserProjectRel record);

	/**
	 * @Title:       selectRelByPid
	 * @author:      杨贵松
	 * @Description: 根据项目id查询关联关系 
	 * @return       List<UserProjectRel>
	 * @throws 
	 */
	List<UserProjectRel> selectRelByPid(Integer pId);
}