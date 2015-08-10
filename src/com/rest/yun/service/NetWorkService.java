package com.rest.yun.service;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import com.rest.yun.beans.DataTemp;

public interface NetWorkService {

	public void saveNetData(String address,String contralId,String data);
	
	public DataTemp getNetData(String address,String contralId,Date dateTime,Date startDate);
	
	public String waitData(String address,String ContralCode,Date startDate) throws ParseException, InterruptedException;
	
	public List<DataTemp> waitDataForList(String address,String ContralCode,Date startDate,int size) throws ParseException, InterruptedException;
	
	public String waitDataForSearchEquipment(String address,String ContralCode,Date startDate) throws ParseException, InterruptedException;
	
	public void pushMsg(String hostCode) throws Exception;

	public DataTemp selectData(String address, String contralId) throws Exception;
}
