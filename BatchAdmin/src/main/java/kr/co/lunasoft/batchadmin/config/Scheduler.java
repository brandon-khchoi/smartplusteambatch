package kr.co.lunasoft.batchadmin.config;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import kr.co.lunasoft.batchadmin.common.LogUtil;
import kr.co.lunasoft.batchadmin.mapper.dbbatch.read.BatchManageMapper;
import kr.co.lunasoft.batchadmin.service.CallURLService;
import kr.co.lunasoft.batchadmin.service.CommonService;
import kr.co.lunasoft.batchadmin.vo.batch.BatchInfoVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.support.CronSequenceGenerator;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Scheduler implements DisposableBean {

    public Scheduler() {
        setExecuteServerIP();
    }

    @Autowired
    BatchManageMapper batchManageMapper;

    private static HashMap<String, ScheduledExecutorService> schedulerServiceMap = new HashMap<String, ScheduledExecutorService>();

    private static HashMap<String, BatchInfoVO> batchMap = new HashMap<String, BatchInfoVO>();

    private static String myIp;

    @Autowired
    private CommonService commonService;

    public void codeRedis() throws Exception {
        commonService.setCommonCodeList();
    }

    @Scheduled(fixedDelay = 3000)
    public void batchExecutor() {
        if ("172.31.46.41".equals(myIp)) {        //로컬환경에서 안돌도록  ip 체크
            List<BatchInfoVO> batchInfoList = batchManageMapper.selectBatchInfo();
            batchRegister(batchInfoList);
        }
    }

    private void setExecuteServerIP() {
        HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
        factory.setConnectTimeout(10000);
        factory.setReadTimeout(10000);

        RestTemplate restTemplate = new RestTemplate(factory);
        try {
            myIp = restTemplate.getForObject("http://169.254.169.254/latest/meta-data/local-ipv4", String.class);
        } catch (Exception e) {
            myIp = "127.0.0.1";
        } finally {
            log.info(myIp);
        }
    }

    private static synchronized void batchRegister(List<BatchInfoVO> batchInfoList) {

        try {
            for (BatchInfoVO vo : batchInfoList) {
                if (!batchMap.containsKey(vo.getKey())) {

                    if ("1".equals(vo.getUse_yn())) {
                        batchMap.put(vo.getKey(), vo);
                        ScheduledExecutorService service = Executors.newScheduledThreadPool(1, new ThreadFactoryBuilder().setDaemon(true).build());
                        schedulerServiceMap.put(vo.getKey(), service);

                        startWorker(vo);
                    }

                } else {
                    String newUseYn = vo.getUse_yn();

                    BatchInfoVO prevscheduledJob = batchMap.get(vo.getKey());
                    String prevUseYn = prevscheduledJob.getUse_yn();

                    if (!newUseYn.equals(prevUseYn)) {
                        batchMap.put(vo.getKey(), vo);

                        if ("1".equals(newUseYn)) {
                            startWorker(vo);

                        } else if ("0".equals(newUseYn)) {
                            stopWorker(vo.getKey());
                        }

                    }

                }

            }
        } catch (Exception e) {
            log.info(new LogUtil().catchLog(e));
        }
    }

    private static synchronized void startWorker(BatchInfoVO vo) throws Exception {
        long initialDelay = 0;

        Date now = new Date();

        if (vo.getBatch_cycle_type_code_desc().contains("delay")) {
            try {
                initialDelay = getInitialDelay(vo.getBatch_time_type_code_desc(), vo.getBatch_start_time());
            } catch (Exception e) {
                log.info(new LogUtil().catchLog(e));
            }
        } else if ("instant".equals(vo.getBatch_cycle_type_code_desc())
                && vo.getLast_start_time() != null
//                && vo.getLast_start_time().getTime() > yesterday.getTime()
                && (vo.getLast_start_time().getTime() + (vo.getBatch_cycle_sec() * 1000)) > now.getTime()
        ) {

            initialDelay = (vo.getLast_start_time().getTime() + (vo.getBatch_cycle_sec() * 1000) - now.getTime()) / 1000;

//            long startTime = vo.getLast_start_time().getTime();
//            while (now.getTime() > startTime) {
//                startTime = startTime + (vo.getBatch_cycle_sec() * 1000);
//            }
//            initialDelay = (startTime - now.getTime()) / 1000;
        }

        if (initialDelay > -1) {
            log.info(vo.getBatch_name() + " 시작시간 : " + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(now.getTime() + (initialDelay * 1000)));
            schedulerServiceMap.get(vo.getKey()).scheduleAtFixedRate(new CallURLService(vo), initialDelay, vo.getBatch_cycle_sec(), TimeUnit.SECONDS);
        }
    }

    public static synchronized void stopWorker(String key) throws Exception {
        if (schedulerServiceMap.containsKey(key) && !schedulerServiceMap.get(key).isShutdown()) {
            log.info(batchMap.get(key).getBatch_name() + " worker thread is shutdowned..");
            schedulerServiceMap.get(key).shutdownNow();
            batchMap.remove(key);
            schedulerServiceMap.remove(key);
        }
    }

    @Override
    public void destroy() throws Exception {
        if (!schedulerServiceMap.isEmpty()) {
            for (String key : schedulerServiceMap.keySet()) {
                ScheduledExecutorService service = schedulerServiceMap.get(key);
                service.shutdownNow();
                log.info(batchMap.get(key).getBatch_name() + " worker thread is shutdowned..");
            }
        }
    }

    public static String getSysdate(String param) {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sim = new SimpleDateFormat(param);

        return sim.format(cal.getTime());
    }

    public static long getInitialDelay(String timeFormat, String startTime) throws Exception {

        if ("expression".equals(timeFormat)) {
            Date now = new Date();
            CronSequenceGenerator cs = new CronSequenceGenerator(startTime);
            Date targetDate = cs.next(now);

            long initialDelay = targetDate.getTime() - now.getTime();

            return initialDelay / 1000;

        } else {
            String fromTime = getSysdate(timeFormat);
            String fromFormat = timeFormat;

            if (!timeFormat.contains("yyyyMMddHHmm")) {
                fromTime = getSysdate("HHmmss");
                fromFormat = "HHmmss";
            }

            SimpleDateFormat sdf = new SimpleDateFormat(timeFormat);
            Date targetDate = sdf.parse(startTime);

            sdf = new SimpleDateFormat(fromFormat);
            Date now = sdf.parse(fromTime);

            if (!timeFormat.contains("yyyyMMdd") && now.getTime() > targetDate.getTime()) {
                targetDate.setTime(targetDate.getTime() + 86400000);
            }

            long initialDelay = targetDate.getTime() - now.getTime();

            return initialDelay / 1000;
        }
    }

}
