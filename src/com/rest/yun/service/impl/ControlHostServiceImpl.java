package com.rest.yun.service.impl;

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
import org.springframework.util.StringUtils;

import com.rest.yun.beans.ControlHost;
import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.mapping.ControlHostMapper;
import com.rest.yun.mapping.UserMapper;
import com.rest.yun.service.IControlHostService;

@Service
public class ControlHostServiceImpl implements IControlHostService {

	private final static Logger LOG = LoggerFactory.getLogger(ControlHostServiceImpl.class);

	@Autowired
	private ControlHostMapper controlHostMapper;

	@Autowired
	private UserMapper userMapper;

	@Override
	public void saveControlHost(ControlHost host, int userId) {
		LOG.info("Adding host, code:" + host.getCode());

		validHostParams(host, userId, false);

		try {
			host.setCreatetime(new Date());
			host.setModifytime(new Date());
			host.setCreateuser(userId);
			host.setModifyuser(userId);
			controlHostMapper.insertSelective(host);

			User user = userMapper.selectByPrimaryKey(userId);
			LOG.info(user.getLoginname() + " add host, code:" + host.getCode());
		} catch (DataAccessException e) {
			LOG.error("Save host appear exception", e);
			throw new ServerException(ErrorCode.SAVE_HOST_FAILED);
		}

	}

	@Override
	public void deleteControlHost(int hostId) {
		if (hostId == 0) {
			LOG.warn("Invalid host Id");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		try {
			controlHostMapper.deleteByPrimaryKey(hostId);
		} catch (DataAccessException e) {
			LOG.error("Delete host by {#" + hostId + "} appear exception", e);
			throw new ServerException(ErrorCode.DELETE_HOST_FAILED);
		}

	}

	@Override
	public void updateControlHost(ControlHost host, int userId) {

		LOG.info("Updating host, code:" + host.getCode());

		validHostParams(host, userId, true);

		host.setModifytime(new Date());
		host.setModifyuser(userId);

		try {
			controlHostMapper.updateByPrimaryKeySelective(host);
		} catch (DataAccessException e) {
			LOG.error("Update host appear exception", e);
			throw new ServerException(ErrorCode.UPDATE_HOST_FAILED);
		}
	}

	@Override
	public Map<String, Object> getHostById(int hostId) {
		if (hostId == 0) {
			LOG.warn("Invalid host Id");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		// TODO: 添加权限控制
		return controlHostMapper.getHostAndProjectByHostId(hostId);
	}

	@Override
	public Page<Map<String, Object>> selectHostBy(HttpSession session,int pageNow, int pageSize, Map<String, Object> criteria) {
		Map<String, Object> params = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Page<Map<String, Object>> page = new Page<Map<String, Object>>(pageNow, pageSize);
		params.put(Constants.PAGE, page);
		params.put("userId", user.getId());
		
		if (criteria != null) {
			params.putAll(criteria);
		}
		List<Map<String, Object>> list = controlHostMapper.selectHostForList(params);
		page.setResult(list);
		return page;
	}

	@Override
	public boolean validHostCode(String hostCode, int hostId, int projectId) {
		// 一个项目只能有一个Host
		if (projectId != 0) {
			ControlHost host = controlHostMapper.selectByProjectId(projectId);
			if (host != null && !host.getId().equals(hostId)) {
				throw new ServerException(ErrorCode.HOST_CODE_NOLY_ONE_FOR_PROJECT);
			}
		}

		if (controlHostMapper.validHostCodeExceptById(hostCode, hostId)) {
			throw new ServerException(ErrorCode.HOST_CODE_DUPLICATE);
		}
		return true;
	}

	private void validHostParams(ControlHost host, int userId, boolean isUpdate) {
		if (host == null) {
			LOG.warn("Host is null");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		// Validation parameters
		User user = userMapper.selectByPrimaryKey(userId);
		if (user == null) {
			LOG.warn("The user {#" + userId + "} is not exists");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}

		if (StringUtils.isEmpty(host.getCode())) {
			LOG.warn("Host code is not empty");
			throw new ServerException(ErrorCode.HOST_CODE_EMPTY);
		}

		int hostId = isUpdate ? host.getId() : 0;
		int projectId = host.getProjectid() == null ? 0 : host.getProjectid();
		validHostCode(host.getCode(), hostId, projectId);

	}

}
