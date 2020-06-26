package kr.co.lunasoft.batchadmin.service;

import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import kr.co.lunasoft.batchadmin.common.LogUtil;
import kr.co.lunasoft.batchadmin.config.ApplicationContextProvider;
import kr.co.lunasoft.batchadmin.vo.batch.BatchInfoVO;
import kr.co.lunasoft.batchadmin.vo.log.batch.BatchLogMongo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

@Slf4j
public class CallURLService implements Runnable {

	BatchInfoVO batchInfo;

	BatchManageService batchManageService = (BatchManageService) ApplicationContextProvider.getBean(BatchManageService.class);

	MongoTemplate mongoTemplate = (MongoTemplate) ApplicationContextProvider.getBean("batchMongoTemplate" ,MongoTemplate.class);
	
	public CallURLService(BatchInfoVO batchInfo) {
		this.batchInfo = batchInfo;
	}
	
	@Override
	public void run() {
		
		try {
			
			Date now = new Date();
			String nowString = new SimpleDateFormat("yyyyMMddHHmmss").format(now);
			
			BatchLogMongo batchResult = new BatchLogMongo();
			batchResult.setBatch_no(Integer.parseInt(batchInfo.getBatch_no()));
			batchResult.setBatch_start_date(new Date());
			batchResult.setLog_date(new Date());
			
			log.info(batchInfo.getBatch_name() + " Call Execute");
			
			String url = batchInfo.getBatch_call_url_addr();
			if(batchInfo.getBatch_call_url_addr().contains("?")){
				url += "&batchLogKey="+ batchInfo.getBatch_no() + "_" + nowString + "&startTime=" + nowString;
			} else {
				url += "?batchLogKey="+ batchInfo.getBatch_no() + "_" + nowString + "&startTime=" + nowString;
			}
			log.info(url);
			
			get(batchResult, url);

		} catch (Exception e) {
			log.info(new LogUtil().catchLog(e));
		} finally {
			if (batchInfo.getBatch_cycle_type_code_desc().contains("only once")) {
				log.info("1회성 배치");
				batchManageService.updateBatchOff(batchInfo.getBatch_no());
			}
		}
	}
	
	public void get(BatchLogMongo batchResult, String requestURL) {

		try {
			OkHttpClient client = new OkHttpClient();
			Request request = new Request.Builder()
					.url(requestURL)
					.build();
			
			client.setReadTimeout(6, TimeUnit.HOURS);
			
			//비동기 처리 (enqueue 사용)
			client.newCall(request).enqueue(new Callback() {
				
				//비동기 처리를 위해 Callback 구현 
				@Override
				public void onFailure(Request request, IOException e) {
					log.info(batchInfo.getBatch_name() + "error + Connect Server Error is " + new LogUtil().catchLog(e));
					
					batchResult.setBatch_end_date(new Date());
					batchResult.setBatch_result_type_code(-1);
					batchResult.setBatch_fail_text(e.toString());
					
					mongoTemplate.insert(batchResult);
					
				}

				@Override
				public void onResponse(Response response) throws IOException {
					log.info(batchInfo.getBatch_name()  + " Response Body is " + response.body().string());	

					batchResult.setBatch_end_date(new Date());

					if(response.code() == 200) {
						batchResult.setBatch_result_type_code(1);
					} else { 
						batchResult.setBatch_result_type_code(-1);
						batchResult.setBatch_fail_text(response.message());
					}
					
					mongoTemplate.insert(batchResult);
				}
				
			});

		} catch (Exception e){
			log.info(batchInfo.getBatch_name() + " : " + e.toString());
		}
	}
	
}
