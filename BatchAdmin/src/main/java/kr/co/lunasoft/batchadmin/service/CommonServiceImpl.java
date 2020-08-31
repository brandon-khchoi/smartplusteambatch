package kr.co.lunasoft.batchadmin.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.google.gson.Gson;

import kr.co.lunasoft.batchadmin.common.LogUtil;
import kr.co.lunasoft.batchadmin.mapper.dbadmin.read.CommonMapper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CommonServiceImpl implements CommonService{

	@Resource
	private CommonMapper commonMapper;

	@Resource(name="redisTemplate") 
	private ValueOperations<String, String> valueOperations; 
	
	@Resource(name = "redisTemplate") 
	private ListOperations<String, String> listOperations;
	
	@Resource(name = "redisTemplate") 
	private HashOperations<String, String, String> hashOperations;
	
	@Resource(name = "redisTemplate") 
	private SetOperations<String, String> setOperations; 
	
	@Resource(name="redisTemplate") 
	private ZSetOperations<String, String> zSetOperations;

	
	private Gson gson = new Gson();

	
	@Override
	public String setCommonCodeList() throws Exception {
		String rtnResult = ""; 
		try {
			LogUtil logUtil = new LogUtil();
			
			logUtil.startJob();
	        cacheLcodeList();
	        cacheLcodeOrderList();
	        cacheMngUserList();
	        cacheAuthGroupList();
	        cacheMemHostingList();
	        cacheDeliveryCopList();
	        cacheServiceList();
	        cacheDeptList();
	        cacheTeamList();
	        cachePartList();
	        cacheDutyList();
	        logUtil.finishJob();
	        log.info("setCommonCodeList {} ", logUtil.executeTimeLog());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return rtnResult;
	}
	

	/** 
	 * 1. db_comm.tb_comm_code 테이블 전체를 읽고
	 * 2. group_code 기준으로 redis에 넣고 사용
	 * */
	private void cacheLcodeList() {
		try {
			List<Map<String, Object>> code = new ArrayList<Map<String,Object>>();
			code = commonMapper.getLcodeList();
			
			Map<String, Object> code_data = new HashMap<String,Object>();
			String lcode = "";
			int data_cnt = code.size();
			int loop_cnt = 1;
			for (Map<String, Object> map : code) {
				if("".equals(lcode)) {
					lcode = map.get("group_code").toString();
				}else if(!lcode.equals(map.get("group_code").toString())) {
					valueOperations.set(lcode, gson.toJson(code_data));
					code_data.clear();
					lcode = map.get("group_code").toString();
				}
				code_data.put(map.get("comm_code").toString(), map.get("comm_code_kor_name").toString());
				if(data_cnt == loop_cnt) {
					valueOperations.set(lcode, gson.toJson(code_data));
					code_data.clear();
				}
				loop_cnt ++;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/** 
	 * cacheLcodeList 공통코드 리스트의 display_no 로 정렬 필요한 코드
	 * */
	private void cacheLcodeOrderList() {
		try {
			List<Map<String, Object>> code = new ArrayList<Map<String,Object>>();
			String[] codeArray = {"LC004"};
			code = commonMapper.getLcodeOrderList(codeArray);
			
			List<Map<String, Object>> codeList = new ArrayList<Map<String, Object>>();

			String preGcode = "";
			int listSize = code.size();
			for (int i=0; i< listSize ; i++) {
				Map<String, Object> displayNoData = new HashMap<String,Object>();
				Map<String, Object> code_data = new HashMap<String,Object>();
				
				Map<String, Object> map = code.get(i);
				String currentGcode = map.get("group_code").toString();
				
				if(!preGcode.equals(currentGcode)) { // 이전과 다른 경우
					if(i != 0) {
						valueOperations.set("ORDER_" + preGcode, gson.toJson(codeList)); // 이전 루프에 대해서 넣기
						codeList.clear();
					}
				}
				
				code_data.put("comm_code", map.get("comm_code").toString());
				code_data.put("comm_code_kor_name", map.get("comm_code_kor_name").toString());
				displayNoData.put(map.get("display_no").toString(), code_data);
				codeList.add(displayNoData);				
				preGcode = currentGcode;
				
				if(i+ 1 == listSize) { // 마지막 index
					valueOperations.set("ORDER_" + preGcode, gson.toJson(codeList));
				}
				
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void cacheMngUserList() {
		try {
			List<Map<String, Object>> mng = new ArrayList<Map<String,Object>>();
			mng = commonMapper.getMngUserList();
			
			Map<String, Object> sales_mng_data = new HashMap<String,Object>();
			Map<String, Object> maintain_mng_data = new HashMap<String,Object>();
			String luna_team_no = "";
			
			for (Map<String, Object> map : mng) {
				luna_team_no = map.get("luna_team_no").toString();
	            if("7".equals(luna_team_no)){
	            	sales_mng_data.put(map.get("luna_no").toString(), map.get("luna_eng_kor_nick_name").toString());
	            }else if ("8".equals(luna_team_no)){
	            	maintain_mng_data.put(map.get("luna_no").toString(), map.get("luna_eng_kor_nick_name").toString());
	            }
			}
			valueOperations.set("manager_sales", gson.toJson(sales_mng_data));
			valueOperations.set("manager_maintain", gson.toJson(maintain_mng_data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cacheAuthGroupList() {
		try {
			List<Map<String, Object>> group = new ArrayList<Map<String,Object>>();
			group = commonMapper.getAuthGroupList();
			
			Map<String, Object> group_data = new HashMap<String,Object>();
			for (Map<String, Object> map : group) {
				group_data.put(map.get("luna_auth_no").toString(), map.get("luna_auth_name").toString());
			}
			valueOperations.set("luna_auth_group_no", gson.toJson(group_data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cacheMemHostingList() {
		try {
			List<Map<String, Object>> hosting = new ArrayList<Map<String,Object>>();
			hosting = commonMapper.getMemHostingList();
			
			Map<String, Object> hosting_data = new HashMap<String,Object>();
			for (Map<String, Object> map : hosting) {
				hosting_data.put(map.get("hosting_no").toString(), map.get("hosting_name").toString());
			}
			valueOperations.set("hosting_no", gson.toJson(hosting_data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cacheDeliveryCopList() {
		try {
			List<Map<String, Object>> deliv = new ArrayList<Map<String,Object>>();
			deliv = commonMapper.getDeliveryCopList();
			
			Map<String, Object> deliv_data = new HashMap<String,Object>();
			for (Map<String, Object> map : deliv) {
				deliv_data.put(map.get("delivery_company_no").toString(), map.get("delivery_company_name").toString());
			}
			valueOperations.set("delivery_company_no", gson.toJson(deliv_data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void cacheServiceList() {
		try {
			List<Map<String, Object>> service = new ArrayList<Map<String,Object>>();
			service = commonMapper.getServiceList();
			
			Map<String, Object> service_data = new HashMap<String,Object>();
			for (Map<String, Object> map : service) {
				service_data.put(map.get("service_no").toString(), map.get("service_name").toString());
			}
			valueOperations.set("service_no", gson.toJson(service_data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void cacheDeptList() {
		try {
			List<Map<String, Object>> dept = new ArrayList<Map<String,Object>>();
			dept = commonMapper.getDeptList();
			
			Map<String, Object> dept_data = new HashMap<String, Object>();
			for (Map<String, Object> map : dept) {
				dept_data.put(map.get("luna_dept_no").toString(), map.get("luna_dept_name").toString());
			}
			valueOperations.set("dept_no", gson.toJson(dept_data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void cacheTeamList() {
		try {
			List<Map<String, Object>> team = new ArrayList<Map<String,Object>>();
			team = commonMapper.getTeamList();
			
			List<Map<String, Object>> result_data = new ArrayList<Map<String,Object>>();
			Map<String, Object> team_data = null;
			for (Map<String, Object> map : team) {
				team_data = new HashMap<String, Object>();
				team_data.put("luna_team_no" , map.get("luna_team_no").toString());
				team_data.put("luna_team_name" , map.get("luna_team_name").toString());
				team_data.put("luna_dept_no" , map.get("luna_dept_no").toString());
				
				result_data.add(team_data);
			}
			valueOperations.set("team_no", gson.toJson(result_data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void cachePartList() {
		try {
			List<Map<String, Object>> part = new ArrayList<Map<String,Object>>();
			part = commonMapper.getPartList();
			
			List<Map<String, Object>> result_data = new ArrayList<Map<String,Object>>();
			Map<String, Object> part_data = null;
			for (Map<String, Object> map : part) {
				part_data = new HashMap<String, Object>();
				part_data.put("luna_part_no", map.get("luna_part_no").toString());
				part_data.put("luna_part_name", map.get("luna_part_name").toString());
				part_data.put("luna_team_no", map.get("luna_team_no").toString());
				
				result_data.add(part_data);
			}
			valueOperations.set("part_no", gson.toJson(result_data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void cacheDutyList() {
		try {
			List<Map<String, Object>> duty = new ArrayList<Map<String,Object>>();
			duty = commonMapper.getDutyList();
			
			Map<String, Object> duty_data = new HashMap<String, Object>();
			for (Map<String, Object> map : duty) {
				duty_data.put(map.get("luna_duty_no").toString(), map.get("luna_duty_name").toString());
			}
			valueOperations.set("duty_no", gson.toJson(duty_data));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private Map<String, Object> getAccAllList() {
		try {
			List<Map<String, Object>> account = new ArrayList<Map<String,Object>>();
			account = commonMapper.getAccountList();
			
			Map<String, Object> account_data = new HashMap<String, Object>();
			for (Map<String, Object> map : account) {
				account_data.put(map.get("luna_no").toString(), map.get("luna_eng_kor_nick_name").toString());
			}
			return account_data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}	


	@Override
	public String getCommonCodeList(String menu) throws Exception {
		String rtnResult = "";
		Map<String, Object> result = new HashMap<String, Object>();
		try {
			
			if("account".equals(menu)) {
				result.put("dept_no", valueOperations.get("dept_no"));
				result.put("team_no", valueOperations.get("team_no"));
				result.put("part_no", valueOperations.get("part_no"));
				result.put("duty_no", valueOperations.get("duty_no"));
				result.put("luna_auth_group_no", valueOperations.get("luna_auth_group_no"));
			}else if("user".equals(menu)) {
				result.put("member_type_code", valueOperations.get("LC001"));
				result.put("member_stat_code", valueOperations.get("LC002"));
				result.put("inventory_company_code", valueOperations.get("LC018"));
				result.put("manager_sales", valueOperations.get("manager_sales"));
				result.put("manager_maintain", valueOperations.get("manager_maintain"));
				result.put("hosting_no", valueOperations.get("hosting_no"));
				result.put("service_no", valueOperations.get("service_no"));
				
			}else if("setting".equals(menu)) {
				result.put("service_stat_code", valueOperations.get("LC004"));
				result.put("service_stat_code_order", valueOperations.get("ORDER_LC004"));
			}
			
			else if("sales".equals(menu)) {
				result.put("member_stat_code", valueOperations.get("LC001"));
				result.put("sales_code", valueOperations.get("LC010"));//영업항목
				result.put("sales_stat_code", valueOperations.get("LC011"));//영업상태
				result.put("sales_type_code", valueOperations.get("LC012"));//영업형태
				result.put("member_inflow_path_code", valueOperations.get("LC005"));//유입경로
				result.put("mall_service_grade", valueOperations.get("LC003"));//등급
				result.put("manager_sales_luna_no", valueOperations.get("manager_sales"));
				result.put("luna_account", getAccAllList());
			}
			
			else if("faq".equals(menu)) {
				result.put("faq_display", valueOperations.get("LC032"));
				result.put("faq_type", valueOperations.get("LC033"));
				result.put("luna_account", getAccAllList());
				  
			} else if("notice".equals(menu)) {
				result.put("notice_type", valueOperations.get("LC030"));
				result.put("notice_display", valueOperations.get("LC031"));
				
			} else if("news".equals(menu)) {
				result.put("news_type", valueOperations.get("LC034"));
				
			} else if("qna".equals(menu)) {
				result.put("luna_account", getAccAllList());

			} else if ("batch".equals(menu)) {
				result.put("batch_cycle_type_code", valueOperations.get("LC120"));
				result.put("batch_time_type_code", valueOperations.get("LC121"));
				result.put("team_no", valueOperations.get("team_no"));
			}
			
			rtnResult = gson.toJson(result);
			return rtnResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnResult;
	}


	@Override
	public String getCorpList(HashMap<String, Object> params) throws Exception {
		String rtnResult = "";
		try {
			List<Map<String, Object>> corp_list = new ArrayList<Map<String,Object>>();
			corp_list = commonMapper.selectCorpList(params);
			
			rtnResult = gson.toJson(corp_list);
			return rtnResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnResult;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getCommonCodeData(String lcode) throws Exception {
		Map<String, Object> rtnResult = new HashMap<String, Object>();
		List<Map<String, Object>> result = new ArrayList<Map<String,Object>>();
		Map<String, Object> code_data = new HashMap<String,Object>();
		String lCode = lcode.toUpperCase();
		try {
			rtnResult = gson.fromJson(valueOperations.get(lCode), Map.class);
			if(ObjectUtils.isEmpty(rtnResult)) {
				result = commonMapper.selecCodeData(lCode);
				
				if(!ObjectUtils.isEmpty(result)) {
					for(Map<String, Object> map : result ) {
						code_data.put(String.valueOf(map.get("comm_code")), String.valueOf(map.get("comm_code_kor_name")));
					}
				}else {
					code_data.put("msg", "등록되지 않은 코드정보입니다.");
				}
				rtnResult = code_data;
				
			}
			return rtnResult;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rtnResult;
	}
	
}
