package com.rest.yun.controller;

import java.util.List;
import java.util.Map;

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

import com.rest.yun.beans.Project;
import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.dto.ResponseWrapper;
import com.rest.yun.listener.Login;
import com.rest.yun.service.IProjectService;
import com.rest.yun.util.CommonUtiles;
import com.rest.yun.util.JSONConver;

@Controller
@RequestMapping("/project")
public class ProjectController {

	@Autowired
	private IProjectService projectService;

	/**
	 * @Title: save
	 * @author: 杨贵松
	 * @Description: 添加项目
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value="save",method = RequestMethod.POST)
	@ResponseBody
	public ResponseWrapper save(@RequestBody Project project, HttpSession session) {
		// 获取当前登录用户
		User user = (User) session.getAttribute(Constants.USER);
		projectService.saveProject(project, user.getId());
		return new ResponseWrapper(true);
	}

	/**
	 * @Title: selectProjects
	 * @author: 杨贵松
	 * @Description: 查询项目列表
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper selectProjects(@RequestParam(required = false, defaultValue = "1") Integer pageNow,
			@RequestParam(required = false, defaultValue = "10") Integer pageSize, String criteria, HttpSession session) {

		Map<String, Object> criteriaMap = null;

		if (!StringUtils.isEmpty(criteria)) {
			criteriaMap = JSONConver.conver(criteria, Map.class);
			// 处理中文乱码
			if (criteriaMap.containsKey(Constants.PROVINCE)) {
				String province = (String) criteriaMap.get(Constants.PROVINCE);
				province = CommonUtiles.decodeUrl(province);
				criteriaMap.put(Constants.PROVINCE, province);
			}

			if (criteriaMap.containsKey(Constants.CITY)) {
				String city = (String) criteriaMap.get(Constants.CITY);
				city = CommonUtiles.decodeUrl(city);
				criteriaMap.put(Constants.CITY, city);
			}
		}

		Page<Project> page = projectService.selectProjectBy(pageNow, pageSize, criteriaMap, session);

		return new ResponseWrapper(page);
	}

	/**
	 * @Title: detailProject
	 * @author: 杨贵松
	 * @Description: 查看项目详情
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "{projectId}", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper detailProject(@PathVariable int projectId) {
		Project project = projectService.getProjectById(projectId);
		return new ResponseWrapper(project);
	}

	/**
	 * @Title: update
	 * @author: 杨贵松
	 * @Description: 更新项目
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(method = RequestMethod.PUT)
	@ResponseBody
	public ResponseWrapper update(@RequestBody Project project, HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		projectService.updateProject(project, user.getId());
		return new ResponseWrapper(true);
	}

	/**
	 * @Title: deleteProject
	 * @author: 杨贵松
	 * @Description: 删除一个项目
	 * @return ResponseWrapper
	 * @throws
	 */
	@Login
	@RequestMapping(value = "{projectId}", method = RequestMethod.DELETE)
	@ResponseBody
	public ResponseWrapper deleteProject(@PathVariable int projectId) {
		projectService.deleteProject(projectId);
		return new ResponseWrapper(true);
	}

	@RequestMapping(value = "validation", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper validProjectName(@RequestParam String projectName, @RequestParam(required = false, defaultValue = "0") int projectId) {
		boolean result = projectService.validProjectName(CommonUtiles.fixedChinaCode(projectName), projectId);
		return new ResponseWrapper(result);
	}

	@Login
	@RequestMapping(value = "names", method = RequestMethod.GET)
	@ResponseBody
	public ResponseWrapper getAllProjectName(HttpSession session) {
		List<Map<String, Object>> result = projectService.getAllProjectName(session);
		return new ResponseWrapper(result);
	}

}
