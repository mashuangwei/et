package com.msw.et.job;


import lombok.extern.slf4j.Slf4j;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;

import java.util.Date;

@Slf4j
@DisallowConcurrentExecution
public class ChickenJob extends QuartzJobBean {

    @Override
    protected void executeInternal(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getJobDetail().getJobDataMap();

        log.info("分布式节点quartz-cluster-node-second，商品添加完成后执行任务，任务时间：{}, goodId: {}", new Date(),  jobDataMap.get("goodId"));
    }
}
