package com.mdd.payadmin.service;

import java.math.BigDecimal;

public interface IMetricsService {
    BigDecimal calculateAndSaveHourlyMetrics();
    BigDecimal calculateAndSaveDailyMetrics();
}
