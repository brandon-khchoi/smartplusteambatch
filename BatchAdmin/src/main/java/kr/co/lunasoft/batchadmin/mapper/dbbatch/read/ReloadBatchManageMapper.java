package kr.co.lunasoft.batchadmin.mapper.dbbatch.read;

import java.util.List;

import kr.co.lunasoft.batchadmin.vo.batch.ReloadBatchVO;

public interface ReloadBatchManageMapper {

	public boolean reloadBatchExistByName(String batchName);
	public List<ReloadBatchVO> selectReloadBatchList();
	
}
