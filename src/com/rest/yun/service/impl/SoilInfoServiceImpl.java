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

import com.rest.yun.beans.SoilInfo;
import com.rest.yun.beans.User;
import com.rest.yun.constants.Constants;
import com.rest.yun.dto.Page;
import com.rest.yun.exception.ErrorCode;
import com.rest.yun.exception.ServerException;
import com.rest.yun.mapping.SoilInfoMapper;
import com.rest.yun.service.ISoilInfoService;
import com.rest.yun.util.CommonUtiles;

@Service
public class SoilInfoServiceImpl implements ISoilInfoService{
	private final static Logger LOG = LoggerFactory.getLogger(SoilInfoServiceImpl.class);

	@Autowired
	private SoilInfoMapper soilInfoMapper;

	/**
	 * @Title:       selectSoilInfo
	 * @author:      杨贵松
	 * @time         2014年12月5日 下午11:57:53
	 * @Description: 查询土壤信息列表 
	 * @throws
	 */
	@Override
	public List<SoilInfo> selectSoilInfo() {
		List<SoilInfo> list = new ArrayList<SoilInfo>();
		try {
			list = soilInfoMapper.selectSoilInfo();
		} catch (DataAccessException e) {
			LOG.error("查询土壤信息失败", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
		return list;
	}

	/**
	 * @Title:       validSoilName
	 * @author:      杨贵松
	 * @time         2014年12月6日 下午9:45:47
	 * @Description: 验证土壤名是否存在
	 * @throws
	 */
	@Override
	public boolean validSoilName(String soiltype, int soilId) {
		return soilInfoMapper.validSoilName(soiltype,soilId);
	}

	/**
	 * @Title:       save
	 * @author:      杨贵松
	 * @time         2014年12月15日 下午9:56:33
	 * @Description: 添加土壤信息
	 * @throws
	 */
	@Override
	public void save(SoilInfo soil, HttpSession session) {
		Date date;
		try {
			date = CommonUtiles.getSystemDateTime();
			User user = (User) session.getAttribute(Constants.USER);
			soil.setCreatetime(date);
			soil.setCreateuser(user.getId());
			soil.setModifytime(date);
			soil.setModifyuser(user.getId());
			//通过高斯消元法求a、b、c、d参数值
			double[][] val = {{soil.getOriginalVal1(),soil.getWaterVal1()},{soil.getOriginalVal2(),soil.getWaterVal2()},{soil.getOriginalVal3(),soil.getWaterVal3()},{soil.getOriginalVal4(),soil.getWaterVal4()}};
			float[] tmp = CommonUtiles.caculate(val);
			soil.setParametera(tmp[0]);
			soil.setParameterb(tmp[1]);
			soil.setParameterc(tmp[2]);
			soil.setParameterd(tmp[3]);
			
			soilInfoMapper.save(soil);
		} catch (ParseException e) {
			LOG.error("获取系统时间异常", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		} catch (DataAccessException e) {
			LOG.error("添加土壤信息失败", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
	}

	/**
	 * @Title:       selectSoilBy
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午6:46:33
	 * @Description: 分页查询土壤信息
	 * @throws
	 */
	@Override
	public Page<SoilInfo> selectSoilBy(Integer pageNow, Integer pageSize, Map<String, Object> criteria, HttpSession session) {
		Map<String, Object> params = new HashMap<String, Object>();
		Map<String, Object> dataTmp = new HashMap<String, Object>();
		User user = (User) session.getAttribute(Constants.USER);
		Page<SoilInfo> page = new Page<SoilInfo>(pageNow, pageSize);
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
		List<SoilInfo> list = soilInfoMapper.selectSoilForList(params);
		page.setResult(list);
		return page;
	}

	/**
	 * @Title:       selectSoilById
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午7:14:03
	 * @Description: 查询土壤详情
	 * @throws
	 */
	@Override
	public SoilInfo selectSoilById(int id) {
		return soilInfoMapper.selectSoilById(id);
	}

	/**
	 * @Title:       deleteSoil
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午7:42:05
	 * @Description: 删除土壤信息
	 * @throws
	 */
	@Override
	public void deleteSoil(int id) {
		soilInfoMapper.deleteSoil(id);
	}

	/**
	 * @Title:       updateSoil
	 * @author:      杨贵松
	 * @time         2015年2月11日 下午7:42:27
	 * @Description: 更新土壤信息
	 * @throws
	 */
	@Override
	public void updateSoil(SoilInfo soil, Integer id) {
		Date date;
		try {
			date = CommonUtiles.getSystemDateTime();
			soil.setModifytime(date);
			soil.setModifyuser(id);
			//通过高斯消元法求a、b、c、d参数值
			double[][] val = {{soil.getOriginalVal1(),soil.getWaterVal1()},{soil.getOriginalVal2(),soil.getWaterVal2()},{soil.getOriginalVal3(),soil.getWaterVal3()},{soil.getOriginalVal4(),soil.getWaterVal4()}};
			float[] tmp = CommonUtiles.caculate(val);
			soil.setParametera(tmp[0]);
			soil.setParameterb(tmp[1]);
			soil.setParameterc(tmp[2]);
			soil.setParameterd(tmp[3]);
			
			soilInfoMapper.update(soil);
		} catch (ParseException e) {
			LOG.error("获取系统时间异常", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		} catch (NumberFormatException e) {
			LOG.error("高斯消元法异常", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		} catch (DataAccessException e) {
			LOG.error("更新土壤信息失败", e);
			throw new ServerException(ErrorCode.ILLEGAL_PARAM);
		}
	}
	
}
