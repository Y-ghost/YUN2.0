package com.rest.yun.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.mapping.UserMapper;
import com.rest.yun.service.IUserService;
import com.rest.yun.util.CommonUtiles;
import com.rest.yun.util.MD5;
import com.rest.yun.util.mail.MailSender;

@Service
public class UserServiceImpl implements IUserService {
	private final static Logger LOG = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserMapper userMapper;

	/**
	 * @Title: saveUser
	 * @author: 杨贵松
	 * @time 2014年11月9日 上午11:30:50
	 * @Description: 用户注册
	 */
	@Override
	public void saveUser(User user) {
		Date date = null;
		try {
			date = CommonUtiles.getSystemDateTime();
			user.setPassword(MD5.getMD5Str(user.getPassword().trim()));
			user.setCreatetime(date);
			user.setModifytime(date);
			user.setRole(1);
			user.setRightcontent("01");
			userMapper.saveUser(user);
		} catch (DataAccessException e) {
			LOG.error("register a new User exception",e);
			throw new ServerException(ErrorCode.REGISTER_USER_FAILED);
		} catch (ParseException e) {
			LOG.error("get system time exception",e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
	}

	/** 
	 * @Title:       login
	 * @author:      杨贵松
	 * @time         2014年11月11日 上午11:25:07
	 * @Description: 用户登录
	 */
	@Override
	public boolean login(String loginname, String password, HttpSession session) {
		boolean flag = false; 
		User user = new User();
		try {
			user = userMapper.validUser(loginname);
			if (user == null) {
				LOG.error("the loginname does not exist ! ");
				throw new ServerException(ErrorCode.LOGIN_LOGINNAME_NOT_EXIST);
			} else if(user.getPassword().equals(MD5.getMD5Str(password.trim()))){
				session.setAttribute(Constants.USER, user);
				flag = true;
			}else{
				LOG.error("loginname or password error ! ");
				throw new ServerException(ErrorCode.LOGIN_LOGINNAME_PASSWORD_ERROR);
			}
		} catch (DataAccessException e) {
			LOG.error("user login system exception ! ",e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		return flag;
	}

	/** 
	 * @Title:       validLoginName
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午12:43:06
	 * @Description: 验证登录名是否存在
	 */
	@Override
	public boolean validLoginName(String loginname) {
		boolean flag = false; 
		User user = new User();
		try {
			user = userMapper.validUser(loginname);
			if (user == null) {
				flag = true;
			}else{
				flag = false;
			}
		} catch (DataAccessException e) {
			LOG.error("validLoginName exception ! ",e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		return flag;
	}

	/** 
	 * @Title:       modifyPassword
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午12:51:02
	 * @Description: 修改用户密码  
	 */
	@Override
	public void modifyPassword(int userId, String password) {
		try {
			Map<String,Object> map = new HashMap<String, Object>();
			map.put("userId", userId);
			map.put("password", MD5.getMD5Str(password.trim()));
			map.put("validCode", "000000000000000000000000000000000");
			userMapper.modifyPassword(map);
		} catch (DataAccessException e) {
			LOG.error("modifyPassword exception ! ",e);
			throw new ServerException(ErrorCode.MODIFY_PASSWORD_FAILED);
		}
	}

	/** 
	 * @Title:       sendEmail
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午1:04:17
	 * @Description: 找回密码发送验证邮件
	 */
	@Override
	public String sendEmail(String loginname, HttpServletRequest request) {
		User user = new User();
		String mail = "";
		try {
			user = userMapper.validUser(loginname);
			if(user==null){
				LOG.error("The user does not exist ! ");
				throw new ServerException(ErrorCode.LOGIN_LOGINNAME_NOT_EXIST);
			}else if(user.getEmail().equals("")){
				LOG.error("The e_mail is null ! ");
				throw new ServerException(ErrorCode.USER_EMAIL_NULL);
			}else{
				String secretKey= UUID.randomUUID().toString();  //密钥
	            Date outDate = new Date(System.currentTimeMillis()+60*60*1000);//60分钟后过期
	            long date = outDate.getTime()/1000*1000;        //忽略毫秒数
	            
	            String key = user.getLoginname()+"$"+date+"$"+secretKey;
	            String validCode = MD5.getMD5Str(key);
	            user.setValidcode(validCode);
	            user.setOutdate(outDate);
	            userMapper.update(user);    //保存到数据库
	            
	            String path = request.getContextPath();
	            String basePath = request.getScheme()+"://"+request.getServerName()+path+"/";
	            String urlStr =  basePath+"User/reset_password?userName="+user.getLoginname()+"&sid="+validCode;
	            boolean flag = MailSender.iForgetPassword(user.getEmail(), urlStr, user.getLoginname(), new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(outDate));
	            if(!flag){
	            	LOG.error("send e_mail filed ! ");
	    			throw new ServerException(ErrorCode.SEND_EMAIL_FAILED);
	            }
			}
			String[] mails = user.getEmail().split("@");
			mail = mails[0].substring(0,2)+"......@"+mails[1];
		} catch (Exception e) {
			LOG.error("send e_mail exception ! ",e);
			throw new ServerException(ErrorCode.SEND_EMAIL_FAILED);
		}
		return mail;
	}

	@Override
	public void updateUser(User user, int modifierId) {
		if (user == null) {
			LOG.warn("Invalid user information when updating user");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		try {
			user.setModifyuser(modifierId);
			user.setModifytime(new Date());
			userMapper.update(user);
		} catch (DataAccessException e) {
			LOG.error("Updating a User exception", e);
			throw new ServerException(ErrorCode.UPDATE_USER_FAILED);
		}

	}

	@Override
	public void deleteUser(int userId) {
		if (userId == 0) {
			LOG.warn("Invalid user id");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		try {
			userMapper.deleteByPrimaryKey(userId);
		} catch (DataAccessException e) {
			LOG.error("Delete a User exception", e);
			throw new ServerException(ErrorCode.DELETE_USER_FAILED);
		}

	}

	@Override
	public User getUserById(int userId) {
		return userMapper.selectByPrimaryKey(userId);
	}

	@Override
	public Page<User> selectUsersBy(HttpSession session,int pageNow, int pageSize, Map<String, Object> criteria) {
		Map<String, Object> params = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Page<User> page = new Page<User>(pageNow, pageSize);
		params.put(Constants.PAGE, page);
		params.put("userId", user.getId());
		if (criteria != null) {
			params.putAll(criteria);
		}
		try {
			List<User> list = userMapper.selectUserForList(params);
			page.setResult(list);
		} catch (DataAccessException e) {
			LOG.error("Select users exception", e);
			throw new ServerException(ErrorCode.SELECT_USERS_LIST_FAILED);
		}

		return page;
	}

	/**
	 * @Title:       validUserName
	 * @author:      杨贵松
	 * @time         2015年1月28日 上午1:39:33
	 * @Description: 根据用户名查询用户信息
	 * @throws
	 */
	@Override
	public User validUserName(String userName) {
		User user = userMapper.validUser(userName);
		return user;
	}

}
