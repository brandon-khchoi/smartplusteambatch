package kr.co.lunasoft.batchadmin.mapper.dbbatch.read;

import java.util.HashMap;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.co.lunasoft.batchadmin.vo.batch.BatchGroupVO;
import kr.co.lunasoft.batchadmin.vo.batch.BatchInfoVO;

@Mapper
public interface BatchManageMapper {
	public List<BatchInfoVO> selectBatchInfo();
	public List<BatchInfoVO> selectBatchInfoByTeamNo(BatchInfoVO batchInfo);
	public List<BatchGroupVO> selectBatchGroupInfo(String lunaTeamNo);
	public List<BatchGroupVO> selectAllBatchGroupInfo(String lunaTeamNo);

	public int selectBatchInfoCount(BatchInfoVO batchInfo);
	
	public boolean hasBatchName(BatchInfoVO batchInfo);
	public boolean hasBatchGroupName(BatchGroupVO vo);
	
	public List<BatchInfoVO> selectSearchBatchInfoByName(HashMap<String, String> map);
	public List<BatchInfoVO> selectSearchBatchInfoByUrl(HashMap<String, String> map);
	
}
