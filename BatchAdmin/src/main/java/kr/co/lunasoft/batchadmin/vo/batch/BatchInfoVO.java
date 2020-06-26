package kr.co.lunasoft.batchadmin.vo.batch;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BatchInfoVO {
	
	private String batch_no;
	
	private String batch_group_no;

	private String batch_name;
	
	private int batch_cycle_type_code;

	@ApiModelProperty(required=false, hidden=true)
	private String batch_cycle_type_code_desc;

	private long batch_cycle_sec;
	
	private int batch_time_type_code;

	@ApiModelProperty(required=false, hidden=true)
	private String batch_time_type_code_desc;
	
	private String batch_start_time;
	
	private String batch_call_url_addr;
	
	@ApiModelProperty(required=false)
	private String work_yn;
	
	private String use_yn;
	
	@ApiModelProperty(required=false, hidden=true)
	private String create_date;
	
	private String create_user_code;
	
	private String create_user_no;
	
	@ApiModelProperty(required=false, hidden=true)
	private String update_date;
	
	private String update_user_code;
	private String update_user_no;

	@ApiModelProperty(required=false, hidden=true)
	private String luna_team_no;
	
	@ApiModelProperty(required=false, hidden=true)
	private int page;
	
	@ApiModelProperty(required=false, hidden=true)
	private int perPage;
	
	@ApiModelProperty(required=false, hidden=true)
	public String getKey() {
		return this.batch_group_no + "_" + this.batch_no;
	}
	
}
