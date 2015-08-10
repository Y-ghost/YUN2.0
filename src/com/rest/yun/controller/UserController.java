package com.rest.yun.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.listener.Login;
import com.rest.yun.service.IUserService;
import com.rest.yun.util.CommonUtiles;
import com.rest.yun.util.JSONConver;

@Controller
@RequestMapping("/User")
public class UserController {

	@Autowired
	private IUserService userService;


	/**
	 * @Title:       save
	 * @author:      杨贵松
	 * @time         2014年11月8日 下午9:49:15
	 * @Description: 用户注册
	 * @return       ResponseWrapper
	 * @throws
	 */
	@RequestMapping(value="register",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper save(@RequestBody User user) {
		userService.saveUser(user);
		return new ResponseWrapper(true);
	}
	/**
	 * @Title:       login
	 * @author:      杨贵松
	 * @time         2014年11月11日 上午11:16:36
	 * @Description: 用户登录
	 * @return       ResponseWrapper
	 * @throws
	 */
	@RequestMapping(value="login" , method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper login(@RequestParam String loginname , @RequestParam String password, HttpSession session) {
		boolean flag = userService.login(loginname,password,session);
		return new ResponseWrapper(flag);
	}
	
	/**
	 * @Title:       exist
	 * @author:      杨贵松
	 * @time         2014年11月16日 下午7:51:40
	 * @Description: 退出系统
	 * @return       ResponseWrapper
	 * @throws
	 */
	@RequestMapping(value="exist" , method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper exist(HttpSession session) {
		session.invalidate();
		return new ResponseWrapper(true);
	}
	
	/**
	 * @Title:       validLoginName
	 * @author:      杨贵松
	 * @time         2014年11月11日 上午11:47:36
	 * @Description: 验证登录名是否正确 
	 * @return       ResponseWrapper
	 * @throws
	 */
	@RequestMapping(value="validLoginName" , method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper validLoginName(@RequestParam String loginname ) {
		boolean flag = userService.validLoginName(loginname);
		return new ResponseWrapper(flag);
	}
	/**
	 * @Title:       modifyPassword
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午12:49:27
	 * @Description: 修改用户密码 
	 * @return       ResponseWrapper
	 * @throws
	 */
	@RequestMapping(value="modifyPassword" , method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper modifyPassword(@RequestParam String userId , @RequestParam String password ,HttpSession session) {
		userService.modifyPassword(Integer.parseInt(userId),password);
		return new ResponseWrapper(true);
	}
	/**
	 * @Title:       sendEmail
	 * @author:      杨贵松
	 * @time         2014年11月11日 下午1:00:17
	 * @Description: 找回密码发送验证邮件
	 * @return       ResponseWrapper
	 * @throws
	 */
	@RequestMapping(value="sendEmail" , method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper sendEmail(@RequestParam String loginname ,HttpServletRequest request) {
		String mail = userService.sendEmail(loginname,request);
		return new ResponseWrapper(mail);
	}

	/**
	 * @Title:       reset_password
	 * @author:      杨贵松
	 * @time         2015年1月28日 上午1:29:51
	 * @Description: 重定向到修改密码页面
	 * @return       String
	 * @throws
	 */
	@RequestMapping(value="reset_password",method=RequestMethod.GET)
	@ResponseBody
	public ModelAndView reset_password(@RequestParam String userName,@RequestParam String sid,HttpSession session){
		User user = new User();
		String url = "";
		try {
			user = userService.validUserName(userName);
			if(user==null){
				url = "redirect:/indexs/modifyPasswordErr";
			}else if(user.getValidcode()==null || user.getValidcode().equals("")){
				url = "redirect:/indexs/modifyPasswordErr";
			}else if(user.getValidcode().equals(sid)){
				long date = System.currentTimeMillis();
				if(user.getOutdate().getTime()<date){
					url = "redirect:/indexs/modifyPasswordErr";
				}else{
					session.setAttribute("name", userName);
					session.setAttribute("id", user.getId());
					url = "redirect:/indexs/modifyPassword";
				}
			}else{
				url = "redirect:/indexs/modifyPasswordErr";
			}
		} catch (Exception e) {
			url = "redirect:/indexs/modifyPasswordErr";
		}
		return new ModelAndView(url);
	}
	
	/**
	 * 更新用户信息
	 * 
	 * @param user
	 * @param session
	 * @return
	 */
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseWrapper update(@RequestBody User user, HttpSession session) {
		// 获取当前登录用户
		User currentUser = (User) session.getAttribute(Constants.USER);
		userService.updateUser(user, currentUser.getId());
		return new ResponseWrapper(true);
	}

	@RequestMapping(value = "{userId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseWrapper deleteUser(@PathVariable int userId) {
		userService.deleteUser(userId);
		return new ResponseWrapper(true);
	}

	/**
	 * 获取用户详细信息
	 * 
	 * @param userId
	 * @return
	 */
	@RequestMapping(value = "{userId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper detail(@PathVariable int userId) {
		User user = userService.getUserById(userId);
		return new ResponseWrapper(user);
	}

	/**
	 * 获取用户列表
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param criteria
	 * @return
	 */

	@Login
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectUsers(@RequestParam(required = false, defaultValue = "1") Integer pageNow,
			@RequestParam(required = false, defaultValue = "10") Integer pageSize, String criteria, HttpSession session) {

		Map<String, Object> criteriaMap = null;

		if (!StringUtils.isEmpty(criteria)) {
			criteriaMap = JSONConver.conver(criteria, Map.class);
			// 处理中文乱码
			if (criteriaMap.containsKey(Constants.LOGIN_UNAME)) {
				String loginName = (String) criteriaMap.get(Constants.LOGIN_UNAME);
				loginName = CommonUtiles.decodeUrl(loginName);
				criteriaMap.put(Constants.LOGIN_UNAME, loginName);
			}
		}

		Page<User> page = userService.selectUsersBy(session,pageNow, pageSize, criteriaMap);

		return new ResponseWrapper(page);
	}
}
