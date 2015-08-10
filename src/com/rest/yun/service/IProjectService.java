package com.rest.yun.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import com.rest.yun.beans.Project;
import com.rest.yun.dto.Page;

public interface IProjectService {

	/**
	 * Save Project
	 * 
	 * @param project
	 */
	void saveProject(Project project, int userId);

	/**
	 * Delete Project
	 * 
	 * @param projectId
	 */
	void deleteProject(int projectId);

	/**
	 * Update project
	 * 
	 * @param project
	 */

	void updateProject(Project project, int userId);

	/**
	 * Get project by id
	 * 
	 * @param projectId
	 * @return
	 */
	Project getProjectById(int projectId);

	/**
	 * Search project list
	 * 
	 * @param pageNow
	 * @param pageSize
	 * @param criteria
	 * @return
	 */
	Page<Project> selectProjectBy(int pageNow, int pageSize, Map<String, Object> criteria, HttpSession session);

	/**
	 * 检查项目名称是否存(除了{projectId}这个项目)
	 * 
	 * @param projectName
	 * @param projectId
	 *            if projectId = 0的时，表示检查所有的项目
	 * @return 如果返回true 表示存在， false 表示 不存在
	 */
	boolean validProjectName(String projectName, int projectId);

	List<Map<String, Object>> getAllProjectName(HttpSession session);

}
