package com.rest.yun.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;

import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.rest.yun.beans.PlantsExp;
import com.rest.yun.beans.PlantsInfo;
import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.PlantsExt;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.mapping.PlantsExpMapper;
import com.rest.yun.mapping.PlantsInfoMapper;
import com.rest.yun.service.IPlantsInfoService;
import com.rest.yun.util.CommonUtiles;

@Service
public class PlantsInfoServiceImpl implements IPlantsInfoService {
	private final static Logger LOG = LoggerFactory.getLogger(PlantsInfoServiceImpl.class);

	@Autowired
	private PlantsInfoMapper plantsInfoMapper;
	@Autowired
	private PlantsExpMapper plantsExpMapper;

	/**
	 * @Title:       selectPlantsInfo
	 * @author:      杨贵松
	 * @time         2014年12月5日 下午11:47:03
	 * @Description: 查询植物列表
	 * @throws
	 */
	@Override
	public List<PlantsInfo> selectPlantsInfo() {
		List<PlantsInfo> list = new ArrayList<PlantsInfo>();
		try {
			list = plantsInfoMapper.selectPlantsInfo();
		} catch (DataAccessException e) {
			LOG.error("查询植物信息异常", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		return list;
	}

	/**
	 * @Title:       save
	 * @author:      杨贵松
	 * @time         2014年12月22日 下午2:45:49
	 * @Description: 添加新植物信息
	 * @throws
	 */
	@Override
	public void save(PlantsExt plantsExt, HttpSession session) {
		try {
			User user = (User) session.getAttribute(Constants.USER);
			Date date = CommonUtiles.getSystemDateTime();
			PlantsInfo plants = plantsExt.getPlants();
			List<PlantsExp> listTmp = new ArrayList<PlantsExp>();
			List<PlantsExp> list = new ArrayList<PlantsExp>();
			listTmp = plantsExt.getResult();
			
			plants.setCreatetime(date);
			plants.setCreateuser(user.getId());
			plants.setModifytime(date);
			plants.setModifyuser(user.getId()); 
			
			int tmp = plantsInfoMapper.save(plants);
			if(!CollectionUtils.isEmpty(listTmp) && tmp>0){
				for(PlantsExp plantsExp:listTmp){
					plantsExp.setPlantsid(plants.getId());
					
					plantsExp.setCreatetime(date);
					plantsExp.setCreateuser(user.getId());
					plantsExp.setModifytime(date);
					plantsExp.setModifyuser(user.getId()); 
					
					list.add(plantsExp);
				}
				plantsExpMapper.save(list);
			}
		} catch (ParseException e) {
			LOG.error("获取系统时间异常", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		} catch (DataAccessException e) {
			LOG.error("添加植物信息异常", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
	}

	/**
	 * @Title:       validPlantsName
	 * @author:      杨贵松
	 * @time         2014年12月23日 下午5:58:05
	 * @Description: 校验植物名是否存在
	 * @throws
	 */
	@Override
	public boolean validPlantsName(String plantsname, int plantsId) {
		return plantsInfoMapper.validPlantsName(plantsname,plantsId);
	}

	/**
	 * @Title:       selectPlantsById
	 * @author:      杨贵松
	 * @time         2015年3月2日 下午8:52:14
	 * @Description: 查询植物详情
	 * @throws
	 */
	@Override
	public PlantsExt selectPlantsById(int id) {
		PlantsInfo plants = plantsInfoMapper.selectPlantsById(id);
		List<PlantsExp> result = plantsExpMapper.selectByPID(id);
		PlantsExt pe = new PlantsExt();
		pe.setPlants(plants);
		pe.setResult(result);
		return pe;
	}
}
