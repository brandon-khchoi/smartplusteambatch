package kr.co.lunasoft.batchadmin.vo.log.batch;

import java.util.Date;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
@Document(collection = "batch_log")
public class BatchLogMongo {

	private Date log_date;
	private int batch_no;
	private int batch_result_type_code;
	private Date batch_start_date;
	private String batch_start_date_desc;
	private Date batch_end_date;
	private String batch_end_date_desc;
	private String batch_fail_text;

	public void setBatch_fail_text(String batch_fail_text) {
		if(!"".equals(batch_fail_text)) {
			this.batch_fail_text = batch_fail_text;
		}
	}

}
