package kr.co.lunasoft.batchadmin.vo.batch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ReloadBatchVO {
	
	private String batchName;
	private String callUrl;
	private String useYn;
	private int cycleSec;
	private int cycleTypeCode;
	private int timeTypeCode;
	private String startTime;
}
