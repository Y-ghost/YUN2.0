package com.rest.yun.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.rest.yun.beans.SystemLog;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.mapping.SystemLogMapper;
import com.rest.yun.service.ISystemLogService;

@Service
public class SystemLogServiceImpl implements ISystemLogService {

	private final static Logger LOG = LoggerFactory.getLogger(SystemLogServiceImpl.class);

	@Autowired
	private SystemLogMapper systemLogMapper;

	@Override
	public void markReadStatuById(int logId) {
		try {
			systemLogMapper.updateStatusByPrimaryKey(Constants.LOG_STATUS_READ, logId);
		} catch (DataAccessException e) {
			LOG.error("Mark system log read to failed", e);
			throw new ServerException(ErrorCode.UPDATE_LOG_STATUS_FAILED);
		}

	}

	@Override
	public SystemLog getSystemLogById(int logId) {
		if (logId == 0) {
			LOG.warn("Get system log by invalid id");
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		return systemLogMapper.selectByPrimaryKey(logId);
	}

	@Override
	public Page<SystemLog> selectSystemLogForListBy(int pageNow, int pageSize, Map<String, Object> criteria) {
		Map<String, Object> params = new HashMap<String, Object>();

		Page<SystemLog> page = new Page<SystemLog>(pageNow, pageSize);
		params.put(Constants.PAGE, page);

		if (criteria != null) {
			params.putAll(criteria);
		}
		try {
			List<SystemLog> list = systemLogMapper.selectSystemLogForListBy(params);
			page.setResult(list);
		} catch (DataAccessException e) {
			LOG.error("Get system log list exception", e);
			throw new ServerException(ErrorCode.SELECT_LOG_LIST_FAILED);
		}
		return page;
	}

}
