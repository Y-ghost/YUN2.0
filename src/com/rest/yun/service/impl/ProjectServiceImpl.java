package com.rest.yun.service.impl;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import com.rest.yun.beans.Project;
import com.rest.yun.beans.User;
import com.rest.yun.beans.UserProjectRel;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.mapping.DataTempMapper;
import com.rest.yun.mapping.ProjectMapper;
import com.rest.yun.mapping.UserMapper;
import com.rest.yun.mapping.UserProjectRelMapper;
import com.rest.yun.service.IProjectService;
import com.rest.yun.util.CommonUtiles;

@Service
public class ProjectServiceImpl implements IProjectService {

	private final static Logger LOG = LoggerFactory.getLogger(ProjectServiceImpl.class);

	@Autowired
	private ProjectMapper projectMapper;
	@Autowired
	private DataTempMapper dataTempMapper;

	@Autowired
	private UserMapper userMapper;

	@Autowired
	private UserProjectRelMapper userProjectRelMapper;

	@Override
	public void saveProject(Project project, int userId) {
		if (project == null) {
			LOG.warn("Project is null");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		// Validation parameters
		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			LOG.warn("The user {#" + userId + "} is not exists when saving project");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		if (StringUtils.isEmpty(project.getName())) {
			LOG.warn("Project Name is not empty");
			throw new ServerException(ErrorCode.PROJECT_NAME_EMPTY);
		}

		boolean isExistName = projectMapper.validProjectName(project.getName(), 0);

		if (isExistName) {
			LOG.warn("The project name {" + project.getName() + "} is  exists when updating project");
			throw new ServerException(ErrorCode.PROJECT_NAME_DUPLICATE);
		}

		try {
			project.setCreateuser(userId);
			project.setModifyuser(userId);
			project.setCreatetime(new Date());
			project.setModifytime(new Date());

			int projectId = projectMapper.insertSelective(project);
			if (projectId > 0) {
				UserProjectRel upr = new UserProjectRel();
				upr.setUserid(userId);
				upr.setProjectid(project.getId());
				upr.setCreatetime(new Date());
				upr.setModifytime(new Date());
				upr.setCreateuser(userId);
				upr.setModifyuser(userId);
				userProjectRelMapper.insertSelective(upr);
			}
			LOG.info("用户[" + user.getUsername() + "] 添加了新项目，名为：" + project.getName());
		} catch (DataAccessException e) {
			LOG.error("Save project appear exception", e);
			throw new ServerException(ErrorCode.SAVE_PROJECT_FAILED);
		}
	}

	@Override
	public void deleteProject(int projectId) {
		if (projectId == 0) {
			LOG.warn("Invalid project Id");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		try {
			projectMapper.deleteByPrimaryKey(projectId);
		} catch (DataAccessException e) {
			LOG.error("Delete project by {#" + projectId + "} appear exception", e);
			throw new ServerException(ErrorCode.DELETE_PROJECT_FAILED);
		}

	}

	@Override
	public void updateProject(Project project, int userId) {
		if (project == null) {
			LOG.warn("Project is null");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		// 验证参数
		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			LOG.warn("The user {#" + userId + "} is not exists when saving project");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		if (StringUtils.isEmpty(project.getName())) {
			LOG.warn("Project Name is not empty");
			throw new ServerException(ErrorCode.PROJECT_NAME_EMPTY);
		}

		// 验证项目名称是否存在
		boolean isExistName = projectMapper.validProjectName(project.getName(), project.getId());

		if (isExistName) {
			LOG.warn("The project name {" + project.getName() + "} is  exists when updating project");
			throw new ServerException(ErrorCode.PROJECT_NAME_DUPLICATE);
		}

		try {
			project.setModifyuser(userId);
			projectMapper.updateByPrimaryKeySelective(project);
		} catch (DataAccessException e) {
			LOG.error("Update project appear exception", e);
			throw new ServerException(ErrorCode.UPDATE_PROJECT_FAILED);
		}
	}

	@Override
	public Page<Project> selectProjectBy(int pageNow, int pageSize, Map<String, Object> criteria, HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> dataTmp = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Page<Project> page = new Page<Project>(pageNow, pageSize);
		Date date;
		try {
			date = CommonUtiles.getLastDate(-300);
		} catch (ParseException e) {
			LOG.error("get 5 minute times exception", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		// 获取5分钟之前的一个时间点
		dataTmp.put("lastTime", date);
		params.put(Constants.PAGE, page);
		params.put("userId", user.getId());
		if (criteria != null) {
			params.putAll(criteria);
		}
		List<Project> listTmp = projectMapper.selectProjectForList(params);
		List<Project> list  = new ArrayList<Project>();
		try {
			if (!CollectionUtils.isEmpty(listTmp)) {
				for (Project project : listTmp) {
					dataTmp.put("pId", project.getId());
					int dataCount = dataTempMapper.selectDataCount(dataTmp);
					if (dataCount > 0) {
						project.setWifiStatus("在线");
					} else {
						project.setWifiStatus("离线");
					}
					list.add(project);
				}
			}
		} catch (DataAccessException e) {
			LOG.error("get project exception",e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		page.setResult(list);
		return page;
	}

	@Override
	public Project getProjectById(int projectId) {
		if (projectId == 0) {
			LOG.warn("Project Id is empty");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		return projectMapper.selectByPrimaryKey(projectId);
	}

	@Override
	public boolean validProjectName(String projectName, int projectId) {
		return projectMapper.validProjectName(projectName, projectId);
	}

	@Override
	public List<Map<String, Object>> getAllProjectName(HttpSession session) {
		User user = (User) session.getAttribute(Constants.USER);
		int userId = user.getId();
		return projectMapper.getAllProjectName(userId);
	}

}
