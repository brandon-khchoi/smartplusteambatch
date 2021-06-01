package kr.co.lunasoft.batchadmin.vo.log.batch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Setter
@Getter
@ToString
@Document(collection = "batch_log_#{@mongoIndex.getIndexDate()}")
@CompoundIndex(name = "batch_no_1_log_date_-1", def = "{'batch_no': 1, 'log_date': -1}")
public class BatchLogMongo {

//    @Indexed(name = "log_date_-1", direction = IndexDirection.DESCENDING)
    private Date log_date;
    private int batch_no;
    private int batch_result_type_code;
    private Date batch_start_date;
    private String batch_start_date_desc;
    private Date batch_end_date;
    private String batch_end_date_desc;
    private String batch_fail_text;

    public void setBatch_fail_text(String batch_fail_text) {
        if (!"".equals(batch_fail_text)) {
            this.batch_fail_text = batch_fail_text;
        }
    }

}
