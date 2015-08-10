package com.rest.yun.service;

import java.util.Map;

import com.rest.yun.beans.SystemLog;
import com.rest.yun.dto.Page;

public interface ISystemLogService {

	/**
	 * Mark system read
	 * 
	 * @param logId
	 */

	void markReadStatuById(int logId);

	/**
	 * Get system detail info by id
	 * 
	 * @param logId
	 * @return
	 */
	SystemLog getSystemLogById(int logId);

	/**
	 * Select system log list
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param criteria
	 * @return
	 */
	Page<SystemLog> selectSystemLogForListBy(int pageNow, int pageSize, Map<String, Object> criteria);

}
