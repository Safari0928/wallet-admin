package com.mdd.payadmin.commons;
import com.mdd.payadmin.service.IMetricsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class DailyMetricsJob implements Job {

    @Resource
    IMetricsService iDailyMetricsService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        iDailyMetricsService.calculateAndSaveDailyMetrics();
    }
}
