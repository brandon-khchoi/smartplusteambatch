package kr.co.lunasoft.batchadmin.mapper.dbbatch.write;

import org.apache.ibatis.annotations.Mapper;

import kr.co.lunasoft.batchadmin.vo.batch.BatchGroupVO;
import kr.co.lunasoft.batchadmin.vo.batch.BatchInfoVO;

@Mapper
public interface BatchManageWriteMapper {
	int insertBatchInfo(BatchInfoVO batchInfo) throws Exception;

	int insertBatchGroup(BatchGroupVO groupInfo) throws Exception;

	int updateBatchInfo(BatchInfoVO batchInfo) throws Exception;

	void updateBatchOff(String batchNo) throws Exception;

	int deleteBatchInfo(String batchNo) throws Exception;

	int deleteBatchGroup(String batchGroupNo) throws Exception;
	
	int updateBatchGroup(BatchGroupVO groupInfo) throws Exception;
}
