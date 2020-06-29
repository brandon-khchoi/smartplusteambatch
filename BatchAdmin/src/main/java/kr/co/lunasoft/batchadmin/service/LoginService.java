package kr.co.lunasoft.batchadmin.service;

import java.util.Map;

public interface LoginService {

	String loginUserInfoCheck(Map<String, String> params) throws Exception;

	String modifyPassWord(Map<String, Object> params) throws Exception;
	
}
