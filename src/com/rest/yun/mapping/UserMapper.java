package com.rest.yun.mapping;

import java.util.List;
import java.util.Map;

import com.rest.yun.beans.User;

public interface UserMapper {
	int deleteByPrimaryKey(Integer id);

	int insert(User record);

	User selectByPrimaryKey(Integer id);

	/**
     * @Title:       update
     * @author:      杨贵松
     * @time         2014年11月11日 下午1:11:54
     * @Description: 更新用户信息
     * @return       int
     * @throws
     */
    int update(User record);

	/** 
	 * @Title:       selectUserByHostCode
	 * @author:      杨贵松
	 * @time         2014年11月4日 上午4:36:28
	 * @Description: 根据主机地址查询用户信息
	 * @return       List<User>
	 * @throws 
	 */
	List<User> selectUserByHostCode(String hostCode);

	/** 
	 * @Title:       saveUser
	 * @author:      杨贵松
	 * @time         2014年11月9日 上午11:32:52
	 * @Description: 用户注册
	 * @return       void
	 * @throws 
	 */
	void saveUser(User user);

	/** 
	 * @Title:       login
	 * @author:      杨贵松
	 * @time         2014年11月11日 上午11:28:53
	 * @Description: 根据登录名查询用户是否存在
	 * @return       User
	 * @throws 
	 */
	User validUser(String loginname);

	/** 
	 * @Title:       modifyPassword
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午12:56:30
	 * @Description: 修改用户密码
	 * @return       void
	 * @throws 
	 */
	void modifyPassword(Map<String, Object> map);

	List<User> selectUserForList(Map<String, Object> params);
}