package com.mdd.payadmin.commons;
import com.mdd.payadmin.service.IMetricsService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class HourlyMetricsJob implements Job {

    @Resource
    IMetricsService iHourlyMetricsService;
    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        iHourlyMetricsService.calculateAndSaveHourlyMetrics();
    }
}
