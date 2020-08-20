package kr.co.lunasoft.batchadmin.service;

import com.google.gson.Gson;
import kr.co.lunasoft.batchadmin.common.LogUtil;
import kr.co.lunasoft.batchadmin.config.Scheduler;
import kr.co.lunasoft.batchadmin.mapper.dbbatch.read.BatchManageMapper;
import kr.co.lunasoft.batchadmin.mapper.dbbatch.write.BatchManageWriteMapper;
import kr.co.lunasoft.batchadmin.vo.batch.BatchGroupVO;
import kr.co.lunasoft.batchadmin.vo.batch.BatchInfoVO;
import kr.co.lunasoft.batchadmin.vo.common.ResponseVo;
import kr.co.lunasoft.batchadmin.vo.log.batch.BatchLogMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Slf4j
@Service
public class BatchManageService {

    @Autowired
    BatchManageMapper batchManageMapper;

    @Autowired
    BatchManageWriteMapper batchManageWriteMapper;

    @Autowired
    Gson gson;

    @Qualifier("batchMongoTemplate")
    @Autowired
    MongoTemplate mongoTemplate;

    public ResponseVo getBatchList(BatchInfoVO batchInfo) {
        ResponseVo responseVo = new ResponseVo();

        int perPage = batchInfo.getPerPage();
        int currentPage = batchInfo.getPage();
        int lastPage = 0;
        int startIndex = 0;

        int totCnt = batchManageMapper.selectBatchInfoCount(batchInfo);

        startIndex = perPage * (currentPage - 1);
        batchInfo.setPage(startIndex);
        responseVo.setTotal_cnt(totCnt);

        List<BatchInfoVO> batchInfoList = batchManageMapper.selectBatchInfoByTeamNo(batchInfo);

        responseVo.setData(batchInfoList);

        responseVo.setCurrent_page(currentPage);
        responseVo.setPer_page(perPage);
        if (totCnt % perPage == 0)
            lastPage = (totCnt / perPage);
        else
            lastPage = (totCnt / perPage) + 1;

        responseVo.setLast_page(lastPage);

        return responseVo;
    }

    public String getSearchBatchList(String searchType, String searchUseYn, String searchText, String groupNo, String lunaTeamNo) {

        HashMap<String, String> params = new HashMap<>();

        params.put("searchText", "%" + searchText + "%");
        params.put("groupNo", groupNo);
        params.put("lunaTeamNo", lunaTeamNo);
        params.put("searchUseYn", searchUseYn);

        List<BatchInfoVO> batchInfoList = null;

        if ("1".equals(searchType)) {
            batchInfoList = batchManageMapper.selectSearchBatchInfoByName(params);
        } else if ("2".equals(searchType)) {
            batchInfoList = batchManageMapper.selectSearchBatchInfoByUrl(params);
        }

        return gson.toJson(batchInfoList);
    }

    public String getBatchGroupList(String lunaTeamNo) {
        List<BatchGroupVO> batchGroupList = batchManageMapper.selectBatchGroupInfo(lunaTeamNo);
        return gson.toJson(batchGroupList);
    }

    public String getAllBatchGroupList(String lunaTeamNo) {
        List<BatchGroupVO> batchGroupList = batchManageMapper.selectAllBatchGroupInfo(lunaTeamNo);
        return gson.toJson(batchGroupList);
    }

    public String updateBatchList(List<BatchInfoVO> batchInfoList) {

        String resultStr = "";
        boolean isSuccess = true;

        for (int i = 0; i < batchInfoList.size(); i++) {
            BatchInfoVO batchInfo = batchInfoList.get(i);

            int result = 0;

            log.info("update batch_no: " + batchInfo.getBatch_no() + "/ batch_name : " + batchInfo.getBatch_name());

            try {
                if (batchManageMapper.hasBatchName(batchInfo)) {
                    result = -1;
                } else {
                    if (batchInfo.getBatch_no() == null || "".equals(batchInfo.getBatch_no())) {
                        //새 배치 생성
                        result = batchManageWriteMapper.insertBatchInfo(batchInfo);
                    } else {
                        result = batchManageWriteMapper.updateBatchInfo(batchInfo);
                    }
                }
            } catch (Exception e) {
                log.info(new LogUtil().catchLog(e));
            }

            if (result == -1) {
                resultStr += (i + 1) + ". " + batchInfo.getBatch_name() + " 동일 배치 명 존재 <br>";
                isSuccess = false;
            } else if (result == 1) {
                resultStr += (i + 1) + ". " + batchInfo.getBatch_name() + " 업데이트 성공 <br>";
            } else {
                resultStr += (i + 1) + ". " + batchInfo.getBatch_name() + " 업데이트 실패 <br>";
                isSuccess = false;
            }
        }
        if (isSuccess) {
            resultStr = "success";
        }

        return resultStr;
    }

    public String deleteBatchInfo(List<BatchInfoVO> batchInfoList) {

        String resultStr = "";
        int result = 0;
        for (BatchInfoVO batchInfo : batchInfoList) {
            if (!"".equals(batchInfo.getBatch_no())) {
                log.info("Delete batch_no: " + batchInfo.getBatch_no() + "/ batch_name : " + batchInfo.getBatch_name());
                try {
                    result = batchManageWriteMapper.deleteBatchInfo(batchInfo.getBatch_no());
                    Scheduler.stopWorker(batchInfo.getKey());
                } catch (Exception e) {
                    log.info(new LogUtil().catchLog(e));
                }

                if (result != 1) {
                    resultStr += batchInfo.getBatch_name() + " 삭제 실패 <br>";
                }
            }
        }
        if ("".equals(resultStr)) {
            resultStr = "success";
        }

        return resultStr;
    }

    public String deleteBatchGroup(String batchGroupNo, String lunaNo) {
        
        try {
            HashMap<String, String> map = new HashMap<>();
            map.put("batchGroupNo", batchGroupNo);
            map.put("lunaNo", lunaNo);
            batchManageWriteMapper.updateBatchUseYnByGroup(map);
            int result = batchManageWriteMapper.deleteBatchGroup(batchGroupNo);
            if (result == 1) {
                return "success";
            } else {
                return "fail";
            }
        } catch (Exception e) {
            log.info(new LogUtil().catchLog(e));
            return "fail";
        }
    }

    public String saveBatchGroup(List<BatchGroupVO> groupList) {

        String resultStr = "";

        boolean isSuccess = true;

        List<BatchGroupVO> modGroupList = new ArrayList<>();

        List<BatchGroupVO> originGroupList = batchManageMapper.selectAllBatchGroupInfo(groupList.get(0).getLuna_team_no());

        for (BatchGroupVO vo : groupList) {
            if ("".equals(vo.getBatch_group_no())) {
                modGroupList.add(vo);
            } else {
                for (BatchGroupVO originVo : originGroupList) {
                    if (vo.getBatch_group_no().equals(originVo.getBatch_group_no())) {
                        if (!vo.getBatch_group_name().equals(originVo.getBatch_group_name())) {
                            modGroupList.add(vo);
                            break;
                        } else if (!vo.getDisplay_no().equals(originVo.getDisplay_no())) {
                            modGroupList.add(vo);
                            break;
                        } else if (!vo.getUse_yn().equals(originVo.getUse_yn())) {
                            modGroupList.add(vo);
                            break;
                        }
                    }
                }
            }
        }

        log.info(modGroupList.toString());

        for (int i = 0; i < modGroupList.size(); i++) {
            BatchGroupVO vo = modGroupList.get(i);
            log.info(vo.toString());
            try {

                int result = 0;
                if ("".equals(vo.getBatch_group_no())) {

                    if (batchManageMapper.hasBatchGroupName(vo)) {
                        result = -1;
                    } else {
                        result = batchManageWriteMapper.insertBatchGroup(vo);
                    }

                } else {
                    result = batchManageWriteMapper.updateBatchGroup(vo);
                }

                if (result == -1) {
                    resultStr += (i + 1) + ". " + vo.getBatch_group_name() + " 동일 배치 그룹 명 존재 <br>";
                    isSuccess = false;
                } else if (result == 1) {
                    resultStr += (i + 1) + ". " + vo.getBatch_group_name() + " 업데이트 성공 <br>";
                } else if (result != 1) {
                    resultStr += (i + 1) + ". " + vo.getBatch_group_name() + " 업데이트 실패 <br>";
                    isSuccess = false;
                }

            } catch (Exception e) {
                log.info(new LogUtil().catchLog(e));
                return "배치그룹 생성 실패";
            }
        }
        if (isSuccess) {
            resultStr = "success";
        }

        return resultStr;
    }

    public void updateBatchOff(String batchNo) {
        try {
            batchManageWriteMapper.updateBatchOff(batchNo);

        } catch (Exception e) {
            log.info(new LogUtil().catchLog(e));
        }
    }

    public String selectBatchLog(int batchNo) {

        Query query = new Query(Criteria.where("batch_no").is(batchNo));
        query.with(new Sort(Sort.Direction.DESC, "log_date"));
        query.limit(300);

        List<BatchLogMongo> bathcList = mongoTemplate.find(query, BatchLogMongo.class);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (BatchLogMongo vo : bathcList) {
            vo.setBatch_start_date_desc(sdf.format(vo.getBatch_start_date()));
            vo.setBatch_end_date_desc(sdf.format(vo.getBatch_end_date()));
        }

        return gson.toJson(bathcList);
    }

}
