package com.rest.yun.service;

import java.util.Map;

import javax.servlet.http.HttpSession;

import com.rest.yun.beans.ControlHost;
import com.rest.yun.dto.Page;

public interface IControlHostService {

	/**
	 * Save Host
	 * 
	 * @param host
	 * @param userId
	 */
	void saveControlHost(ControlHost host, int userId);

	/**
	 * Delete Host
	 * 
	 * @param hostId
	 */
	void deleteControlHost(int hostId);

	/**
	 * Update host
	 * 
	 * @param host
	 * @param userId
	 */

	void updateControlHost(ControlHost host, int userId);

	/**
	 * Get host by id
	 * 
	 * @param hostId
	 * @return
	 */
	Map<String, Object> getHostById(int hostId);

	/**
	 * Search host list
	 * @param session 
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param criteria
	 * @return
	 */
	Page<Map<String, Object>> selectHostBy(HttpSession session, int pageNow, int pageSize, Map<String, Object> criteria);

	/**
	 * 检查Host code是否存(除了{hostId}这个Host)
	 * 
	 * @param hostCode
	 * @param hostId
	 *            if hostId = 0的时，表示检查所有的Host
	 * @return 如果返回true 表示存在， false 表示 不存在
	 */
	boolean validHostCode(String hostCode, int hostId, int projectId);

}
