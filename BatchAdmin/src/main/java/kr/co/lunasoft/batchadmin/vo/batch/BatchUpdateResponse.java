package kr.co.lunasoft.batchadmin.vo.batch;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class BatchUpdateResponse {
    String batchName;
    String errorText;
}
