package kr.co.lunasoft.batchadmin.vo.common;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ResponseVo {
	
	private int responseCode = 0;
	private String responseMsg = "Success";

	/** front 에서 변수명 그대로 사용 변수명 수정X */
	private Object current_page;
	private Object per_page;
	private Object total_cnt; 
	private Object last_page;
	/** front 에서 변수명 그대로 사용 변수명 수정X */
	
	private Object data;
}
