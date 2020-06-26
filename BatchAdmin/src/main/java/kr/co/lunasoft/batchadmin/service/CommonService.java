package kr.co.lunasoft.batchadmin.service;

import java.util.HashMap;
import java.util.Map;

public interface CommonService {

	String setCommonCodeList() throws Exception;
	
	String getCommonCodeList(String menu) throws Exception;
	
	Map<String, Object> getCommonCodeData(String lcode) throws Exception;

	String getCorpList(HashMap<String, Object> params) throws Exception;
	
}
