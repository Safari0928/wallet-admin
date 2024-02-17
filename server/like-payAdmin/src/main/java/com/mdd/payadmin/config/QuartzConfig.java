package com.mdd.payadmin.config;

import com.mdd.payadmin.commons.DailyMetricsJob;
import com.mdd.payadmin.commons.HourlyMetricsJob;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class QuartzConfig {

    @Bean
    public JobDetail hourlyMetricsJobDetail() {
        return JobBuilder.newJob(HourlyMetricsJob.class) // HourlyMetricsJob, işinizi temsil eden bir sınıfı temsil eder
                .withIdentity("hourlyMetricsJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger hourlyMetricsJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(hourlyMetricsJobDetail())
                .withIdentity("hourlyMetricsTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 * ? * *")) // Bu ifade her saat başında tetiklenecek
                .build();
    }

    @Bean
    public JobDetail dailyMetricsJobDetail() {
        return JobBuilder.newJob(DailyMetricsJob.class)
                .withIdentity("dailyMetricsJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger dailyMetricsJobTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(dailyMetricsJobDetail())
                .withIdentity("dailyMetricsTrigger")
                .withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(23, 59)) // 在每天的23:59触发任务
                .build();
    }
}
