package kr.co.lunasoft.batchadmin.common;

import java.util.HashMap;
import java.util.Map;

public class Pagination {

	public static Map<String, Object> getPageParam(Map<String, Object>  params) {
		Map<String, Object> page_param = new HashMap<String, Object>();
		int current_page = 1;
		int per_page = 20;
		
		if(params.get("current_page") != null) {
			current_page = Integer.parseInt(params.get("current_page").toString());
		}
		if(params.get("per_page") != null) {
			per_page = Integer.parseInt(params.get("per_page").toString());
		}
		
		int cur_page = current_page>0 ? current_page : 1;
		int slice = cur_page==1 ? 0 : (cur_page*per_page)-per_page;
		
		page_param.put("per_page", per_page);
		page_param.put("cur_page", cur_page);
		page_param.put("slice", slice);
		
		return page_param;
	}
	
	public static Map<String, Object> getPageResult(double total_cnt, double per_page, double current_page) {
		Map<String, Object> page_param = new HashMap<String, Object>();
        int last_page = (int) Math.ceil(total_cnt / per_page);
        
        page_param.put("total_cnt", total_cnt);
        page_param.put("per_page", per_page);
        page_param.put("current_page", current_page);
        page_param.put("last_page", last_page);
		
		return page_param;
	}
	
	public static Map<String, Object> getPageResultByCamel(double total_cnt, double per_page, double current_page) {
		Map<String, Object> page_param = new HashMap<String, Object>();
		int last_page = (int) Math.ceil(total_cnt / per_page);
		
		page_param.put("totalCnt", total_cnt);
		page_param.put("perPage", per_page);
		page_param.put("currentPage", current_page);
		page_param.put("lastPage", last_page);
		
		return page_param;
	}
}
