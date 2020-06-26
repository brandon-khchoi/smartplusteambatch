package kr.co.lunasoft.batchadmin.mapper.dbadmin.read;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommonMapper {

	List<Map<String, Object>> getLcodeList() throws Exception;
	
	List<Map<String, Object>> getLcodeOrderList(String[] codeArray) throws Exception;

	List<Map<String, Object>> getMngUserList() throws Exception;

	List<Map<String, Object>> getAuthGroupList() throws Exception;

	List<Map<String, Object>> getMemHostingList() throws Exception;

	List<Map<String, Object>> getDeliveryCopList() throws Exception;

	List<Map<String, Object>> getServiceList() throws Exception;
	
    List<Map<String, Object>> getDeptList() throws Exception;
    
    List<Map<String, Object>> getTeamList() throws Exception;
    
    List<Map<String, Object>> getPartList() throws Exception;
    
    List<Map<String, Object>> getDutyList() throws Exception;
    
    List<Map<String, Object>> getAccountList() throws Exception;

	List<Map<String, Object>> selectCorpList(HashMap<String, Object> params) throws Exception;

	List<Map<String, Object>> selecCodeData(String lcode) throws Exception;
	
}
