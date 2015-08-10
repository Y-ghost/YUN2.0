package com.rest.yun.exception;

import java.text.MessageFormat;
import java.util.Properties;

import com.rest.yun.util.PropertiesReader;

public enum ErrorCode {

	SERVER_ERROR("-A1"), ILLEGAL_PARAM("-A2"), INVALID_UNAME("-A3"), SERVER_CONNECTION_ERROR("-A4"),

	// Project
	SAVE_PROJECT_FAILED("A100"), UPDATE_PROJECT_FAILED("A101"), DELETE_PROJECT_FAILED("A102"),

	PROJECT_NAME_EMPTY("A103"), PROJECT_NAME_DUPLICATE("A104"), PROJECT_LIST_NULL("A105"),
	
	PROJECT_HAS_NO_HOST("A106"),

	// Host
	SAVE_HOST_FAILED("A200"), UPDATE_HOST_FAILED("A201"), DELETE_HOST_FAILED("A202"),

	HOST_CODE_EMPTY("A203"), HOST_CODE_DUPLICATE("A204"), HOST_CODE_NOLY_ONE_FOR_PROJECT("A205"),
	
	// Equipment
	SAVE_EQUIPMENT_FAILED("A300"), UPDATE_EQUIPMENT_FAILED("A301"), DELETE_EQUIPMENT_FAILED("A302"),

	EQUIPMENT_CODE_EMPTY("A303"), EQUIPMENT_CODE_DUPLICATE("A304"), EQUIPMENT_LIST_NULL("A305"),

	SELECT_EQUIPMENT_LIST_FAILED("A306"), OPEN_EQUIPMENT_FAILED("A307"), CLOSE_EQUIPMENT_FAILED("A308"),
	
	SEARCH_EQUIPMENT_FAILED("A309"),SAVE_EQUIPMENT_LIST_NULL("A310"),SET_EQUIPMENT_MODEL_FAILD("A311"),
	
	SET_EQUIPMENT_PARAM_FAILD("A312"),SET_EQUIPMENT_TIMELEN_FAILD("A313"),SET_EQUIPMENT_ABCD_FAILD("A314"),
	
	INIT_EQUIPMENT_INFO_FAILD("A315"),SELECT_EQUIPMENT_WATER_FAILED("A316"),PUT_EQUIPMENT_DATA_FAILED("A317"),
	
	INIT_EQUIPMENT_INFO_ERR("318"),

	// User
	REGISTER_USER_FAILED("A400"), UPDATE_USER_FAILED("A401"), DELETE_USER_FAILED("A402"), SELECT_USERS_LIST_FAILED("A403"),
	
	LOGIN_LOGINNAME_PASSWORD_ERROR("A404"),LOGIN_LOGINNAME_NOT_EXIST("A405"),MODIFY_PASSWORD_FAILED("A406"),
	
	USER_EMAIL_NULL("A407"),SEND_EMAIL_FAILED("A408"),NO_LOGIN("A409"),

	// System Log
	UPDATE_LOG_STATUS_FAILED("A500"), SELECT_LOG_LIST_FAILED("A501"),
	
	// Soil
	ADD_SOIL_FAILED("A600"),
	
	// Statistic
	STATISTIC_DATE_NULL("A700") , STATISTIC_EQUIPMENT_NULL("A701") , STATISTIC_WATER_FAILED("A702"), STATISTIC_HUMIDITY_FAILED("A703")
	
	, STATISTIC_EXPORT_EXCEL_FAILED("A704")
	
	;

	private String code;

	private Object[] values;

	private static final String ERROR_CONFIG = "/errorCode.properties";

	private static final Properties errorMessageConfig = PropertiesReader.read(ERROR_CONFIG);

	ErrorCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		String message = errorMessageConfig.getProperty(String.valueOf(code));
		if (message == null) {
			return "";
		}
		if (values != null) {
			message = MessageFormat.format(message, values);
		}
		return message;
	}

	public void setValues(Object[] values) {
		this.values = values;
	}

	public String getCode() {
		return code;
	}
}
