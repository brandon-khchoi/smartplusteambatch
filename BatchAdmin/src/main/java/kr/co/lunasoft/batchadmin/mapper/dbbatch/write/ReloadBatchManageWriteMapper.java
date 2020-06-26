package kr.co.lunasoft.batchadmin.mapper.dbbatch.write;

import java.util.HashMap;

import kr.co.lunasoft.batchadmin.vo.batch.ReloadBatchVO;

public interface ReloadBatchManageWriteMapper {

	public int insertReloadBatch(ReloadBatchVO vo) throws Exception;
	public int updateBatchOnOff(HashMap<String, String> paramMap) throws Exception;
	
}
