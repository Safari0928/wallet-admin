package com.mdd.common.mapper.payAdmin;

import com.mdd.common.core.basics.IBaseMapper;
import com.mdd.common.entity.payAdmin.PayDailyMetrics;
import com.mdd.common.entity.payAdmin.PayHourlyMetrics;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PayDailyMetricsMapper extends IBaseMapper<PayDailyMetrics> {
}
