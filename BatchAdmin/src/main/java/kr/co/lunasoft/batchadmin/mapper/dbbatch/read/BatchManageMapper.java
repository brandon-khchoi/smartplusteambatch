package kr.co.lunasoft.batchadmin.mapper.dbbatch.read;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.lunasoft.batchadmin.vo.batch.BatchGroupVO;
import kr.co.lunasoft.batchadmin.vo.batch.BatchInfoVO;

@Mapper
public interface BatchManageMapper {
	List<BatchInfoVO> selectBatchInfo();
	List<BatchInfoVO> selectBatchInfoByTeamNo(BatchInfoVO batchInfo);
	List<BatchGroupVO> selectBatchGroupInfo(String lunaTeamNo);
	List<BatchGroupVO> selectAllBatchGroupInfo(String lunaTeamNo);

	int selectBatchInfoCount(BatchInfoVO batchInfo);
	
	boolean hasBatchName(BatchInfoVO batchInfo);
	boolean hasBatchGroupName(BatchGroupVO vo);
	
	List<BatchInfoVO> selectSearchBatchInfoByName(HashMap<String, String> map);
	List<BatchInfoVO> selectSearchBatchInfoByUrl(HashMap<String, String> map);
	
	BatchInfoVO selectBathcInfoByBatchNo(String batchNo);
	
}
