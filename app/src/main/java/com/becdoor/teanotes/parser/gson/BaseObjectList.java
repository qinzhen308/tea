package com.becdoor.teanotes.parser.gson;

import java.io.Serializable;
import java.util.List;

public class BaseObjectList<T> implements Serializable {

	public final static int STATUS_OK=200;
	public final static int STATUS_REGIST_SUCCESS=100;
	public final static int STATUS_EXPIRE=211;//用户登录过期，需要清除登录状态或者重新登录

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public int status;
	public String msg;
	public String token;
	public List<T> data;
}
