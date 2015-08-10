package com.rest.yun.service;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.rest.yun.beans.User;
import com.rest.yun.dto.Page;

public interface IUserService {

	/** 
	 * @Title:       saveUser
	 * @author:      杨贵松
	 * @time         2014年11月9日 上午11:30:28
	 * @Description: 用户注册
	 * @return       boolean
	 * @throws 
	 */
	void saveUser(User user);

	/** 
	 * @Title:       login
	 * @author:      杨贵松
	 * @time         2014年11月11日 上午11:24:46
	 * @Description: 用户登录
	 * @return       boolean
	 * @throws 
	 */
	boolean login(String loginname, String password, HttpSession session);

	/** 
	 * @Title:       validLoginName
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午12:42:36
	 * @Description: 验证登录名是否存在
	 * @return       boolean
	 * @throws 
	 */
	boolean validLoginName(String loginname);

	/** 
	 * @Title:       modifyPassword
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午12:50:41
	 * @Description: 修改用户密码 
	 * @return       boolean
	 * @throws 
	 */
	void modifyPassword(int userId, String password);

	/** 
	 * @Title:       sendEmail
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午1:03:49
	 * @Description: 发送找回密码验证邮件
	 * @return       boolean
	 * @throws 
	 */
	String sendEmail(String loginname, HttpServletRequest request);

	void updateUser(User user, int modifierId);

	void deleteUser(int userId);

	User getUserById(int userId);

	Page<User> selectUsersBy(HttpSession session,int pageNow, int pageSize, Map<String, Object> criteria);

	/**
	 * @Title:       validUserName
	 * @author:      杨贵松
	 * @time         2015年1月28日 上午1:38:49
	 * @Description: 根据用户名查询用户
	 * @return       User
	 * @throws
	 */
	User validUserName(String userName);

}
