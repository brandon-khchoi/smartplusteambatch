package kr.co.lunasoft.batchadmin.controller;

import io.swagger.annotations.ApiOperation;
import kr.co.lunasoft.batchadmin.mapper.dbbatch.read.BatchManageMapper;
import kr.co.lunasoft.batchadmin.mapper.dbbatch.write.BatchManageWriteMapper;
import kr.co.lunasoft.batchadmin.service.BatchManageService;
import kr.co.lunasoft.batchadmin.vo.batch.BatchGroupVO;
import kr.co.lunasoft.batchadmin.vo.batch.BatchInfoVO;
import kr.co.lunasoft.batchadmin.vo.common.ResponseVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@CrossOrigin(origins="*")
@RestController
@RequestMapping(value = "/batchManage")
public class BatchManageController {

	@Autowired
	BatchManageMapper batchManageMapper;

	@Autowired
	BatchManageWriteMapper batchManageWriteMapper;

	@Autowired
	BatchManageService batchManageService;
	
	
	@RequestMapping(value="batchList", method= {RequestMethod.POST})
	@ResponseBody
	public ResponseVo batchList(
			@RequestBody BatchInfoVO batchInfo
			){
		
		log.info(batchInfo.toString());
		
		return batchManageService.getBatchList(batchInfo);
	}

	@RequestMapping(value="batchGroupList", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String batchGroupList(
			@RequestParam(name="lunaTeamNo") String lunaTeamNo
			){
		return batchManageService.getBatchGroupList(lunaTeamNo);
	}

	@RequestMapping(value="allBatchGroupList", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String allBatchGroupList(
			@RequestParam(name="lunaTeamNo") String lunaTeamNo
			){
		return batchManageService.getAllBatchGroupList(lunaTeamNo);
	}
	
	//	그룹 추가
	@RequestMapping(value = "/saveBatchGroup", method= {RequestMethod.POST})
	@ResponseBody
	public String insertBatchGroup(
			@RequestBody List<BatchGroupVO> groupList
			) {

		return batchManageService.saveBatchGroup(groupList);
	}

	//배치 저장
	@ApiOperation(value = "배치 생성/수정 API",
			httpMethod = "POST",
			produces = "text/plain",
			consumes = "application/json",
			protocols = "http",
			response = String.class
			)
	@RequestMapping(value = "/updateBatch", method= {RequestMethod.POST})
	@ResponseBody
	public String updateBatch(
			@RequestBody List<BatchInfoVO> batchInfoList
			){
		
		return batchManageService.updateBatchList(batchInfoList);
	}
	
	//	배치 삭제
	@RequestMapping(value = "/deleteBatch", method= {RequestMethod.POST})
	@ResponseBody
	public String deleteBatch(
			@RequestBody List<BatchInfoVO> batchInfoList
			){
		
		return batchManageService.deleteBatchInfo(batchInfoList);
	}

	@RequestMapping(value = "/deleteBatchGroup", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String deleteBatchGroup(
			@RequestParam(name="groupNo") String groupNo,
			@RequestParam(name="lunaNo") String lunaNo
			){
		
		return batchManageService.deleteBatchGroup(groupNo, lunaNo);
	}
	
	@RequestMapping(value="searchBatch", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String searchBatch(
			@RequestParam(name="searchType") String searchType,
			@RequestParam(name="searchUseYn") String searchUseYn,
			@RequestParam(name="searchText") String searchText,
			@RequestParam(name="groupNo") String groupNo,
			@RequestParam(name="lunaTeamNo") String lunaTeamNo
			) {
		return batchManageService.getSearchBatchList(searchType, searchUseYn, searchText, groupNo, lunaTeamNo);
	}
	
	@RequestMapping(value="batchLogList", method= {RequestMethod.GET, RequestMethod.POST})
	@ResponseBody
	public String batchLogList(
			@RequestParam(name="batchNo") int batchNo
			) {
		return batchManageService.selectBatchLog(batchNo);
	}
	
}
