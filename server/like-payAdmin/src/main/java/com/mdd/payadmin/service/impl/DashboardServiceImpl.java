package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.github.yulichang.query.MPJQueryWrapper;
import com.mdd.common.config.GlobalConfig;
import com.mdd.common.core.PageResult;
import com.mdd.common.entity.PayTransactions;
import com.mdd.common.entity.payAdmin.PayDailyMetrics;
import com.mdd.common.entity.payAdmin.PayHourlyMetrics;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.mapper.PayTransactionsMapper;
import com.mdd.common.mapper.payAdmin.PayDailyMetricsMapper;
import com.mdd.common.mapper.payAdmin.PayHourlyMetricsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.util.RedisUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.ChartEnum;
import com.mdd.payadmin.enums.GraphEnums;
import com.mdd.payadmin.enums.StatusEnums;
import com.mdd.payadmin.enums.TypeDetailEnums;
import com.mdd.payadmin.service.IDashboardService;
import com.mdd.payadmin.validate.dashboard.ChartValidate;
import com.mdd.payadmin.validate.dashboard.GraphValidate;
import com.mdd.payadmin.vo.dashboard.DayChartVo;
import com.mdd.payadmin.vo.dashboard.RankVo;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class DashboardServiceImpl implements IDashboardService {

    @Resource
    PayHourlyMetricsMapper payHourlyMetricsMapper;

    @Resource
    PayDailyMetricsMapper payDailyMetricsMapper;

    @Resource
    PayUserMapper payUserMapper;

    @Resource
    PayTransactionsMapper transactionsMapper;

    final String key ="PanelData: ";

    @Override
    public List<Map<String, Object>> getPanel(){
        List<Map<String, Object>> panelData = new ArrayList<>();

        String depositKey =key+ TypeDetailEnums.TYPE_DEPOSIT.getMsg() +": "+ TypeDetailEnums.TYPE_DEPOSIT.getCode()+TypeDetailEnums.TYPE_DEPOSIT.getMsg();
        String withdrawKey =key+TypeDetailEnums.TYPE_WITHDRAWAL.getMsg()+": "+TypeDetailEnums.TYPE_WITHDRAWAL.getCode()+TypeDetailEnums.TYPE_WITHDRAWAL.getMsg();
        String orderKey =key+TypeDetailEnums.TYPE_ORDER_COUNT.getMsg()+": "+TypeDetailEnums.TYPE_ORDER_COUNT.getCode()+TypeDetailEnums.TYPE_ORDER_COUNT.getMsg();
        String revenueKey =key+TypeDetailEnums.TYPE_REVENUE.getMsg()+": "+TypeDetailEnums.TYPE_REVENUE.getCode()+TypeDetailEnums.TYPE_REVENUE.getMsg();
        String transactionKey =key+TypeDetailEnums.TYPE_TRANSACTION.getMsg()+": "+TypeDetailEnums.TYPE_TRANSACTION.getCode()+TypeDetailEnums.TYPE_TRANSACTION.getMsg();
        String userKey =key+TypeDetailEnums.TYPE_USER.getMsg()+": "+TypeDetailEnums.TYPE_USER.getCode()+TypeDetailEnums.TYPE_USER.getMsg();
        String transferKey =key+TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getMsg()+": "+TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getCode()+TypeDetailEnums.TYPE_TRANSFER_DEPOSIT.getMsg();

        if(RedisUtils.exists(depositKey)&&RedisUtils.exists(withdrawKey)&&RedisUtils.exists(orderKey) ){
            BigDecimal totalRevenue = (BigDecimal) RedisUtils.get(revenueKey);
            BigDecimal totalTradingVolume = (BigDecimal) RedisUtils.get(transactionKey);
            BigDecimal totalDepositVolume = (BigDecimal) RedisUtils.get(depositKey);
            BigDecimal totalWithdrawalVolume = (BigDecimal) RedisUtils.get(withdrawKey);
            Integer totalOrder = (Integer) RedisUtils.get(orderKey);
            Integer totalRegisteredUser = (Integer) RedisUtils.get(userKey);
            BigDecimal totalTransferVolume = (BigDecimal) RedisUtils.get(transferKey);
            BigDecimal totalExchangesVolume = BigDecimal.valueOf(0);

            panelData.add(createMetric("total revenue",totalRevenue));
            panelData.add(createMetric("total trading volume",totalTradingVolume));
            panelData.add(createMetric("total registered user",totalRegisteredUser));
            panelData.add(createMetric("total order", totalOrder));
            panelData.add(createMetric("total withdrawal volume", totalWithdrawalVolume));
            panelData.add(createMetric("total deposit volume",totalDepositVolume));
            panelData.add(createMetric("total transfer volume",totalTransferVolume));
            panelData.add(createMetric("total exchange volume",totalExchangesVolume));
        }
        return panelData;
    }

    private Map<String, Object> createMetric(String content, Object value) {
        Map<String, Object> metric = new HashMap<>();
        metric.put("content", content);
        metric.put("value", value);
        return metric;
    }

    @Override
    public Map<String, Object> getGraph(GraphValidate graphValidate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = sdf.parse(graphValidate.getStartDate());
            Date endDate = sdf.parse(graphValidate.getEndDate());
            long daysDifference = TimeUnit.MILLISECONDS.toDays(endDate.getTime() - startDate.getTime());
            if(daysDifference<=3){
            return hourlyChart(graphValidate);

        }else{
            return dailyChart(graphValidate);
        }
    }

    @Override
    public PageResult<RankVo> rank(PageValidate pageValidate, GraphValidate graphValidate) {

        MPJQueryWrapper<PayTransactions> mpjQueryWrapper = new MPJQueryWrapper<>();
        mpjQueryWrapper
                .select("U.country,t.status,t.currency_type,t.type_detail, t.user_id, t.pay_id, IFNULL(SUM(t.amount), 0) AS amount")
                .ge("t.complete_time", graphValidate.getStartDate() + " 00:00:00")
                .le("t.complete_time", graphValidate.getEndDate() + " 23:59:59")
                .eq("t.currency_type", 1)
                .eq("t.status", 1)
                .eq("t.type_detail", graphValidate.getType())
                .leftJoin("?_pay_user U ON U.uuid = t.user_id".replace("?_", GlobalConfig.tablePrefix))
                .groupBy("U.country, t.user_id, t.pay_id")
                .orderByDesc("amount");

        IPage<RankVo> iPage = transactionsMapper.selectJoinPage(
                new Page<>(pageValidate.getPageNo(), pageValidate.getPageSize()),
                RankVo.class,
                mpjQueryWrapper);

        return  PageResult.iPageHandle(iPage.getTotal(), iPage.getCurrent(), iPage.getSize(), iPage.getRecords());
    }

    private Map<String, Object> hourlyChart(GraphValidate graphValidate) throws Exception {
        String startDate = graphValidate.getStartDate();
        String endDate = graphValidate.getEndDate();
        Integer type = graphValidate.getType();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date startTime = sdf.parse(startDate + " 00:00:00");
            Date endTime = sdf.parse(endDate + " 23:59:59");
            String key=null;
            String metricMethod = null;
            switch (type){
                case 1:
                    key = GraphEnums.REVENUE.getName();
                    metricMethod = GraphEnums.REVENUE.getMsg();
                    break;
                case 2:
                    key = GraphEnums.DEPOSIT.getName();
                    metricMethod = GraphEnums.DEPOSIT.getMsg();
                    break;
                case 3:
                    key=GraphEnums.WITHDRAWAL.getName();
                    metricMethod= GraphEnums.WITHDRAWAL.getMsg();
                    break;
                case 4:
                    key=GraphEnums.TRANSFER.getName();
                    metricMethod = GraphEnums.TRANSFER.getMsg();
                    break;
                case 5:
                    key=GraphEnums.EXCHANGES.getName();
                    metricMethod=  GraphEnums.EXCHANGES.getMsg();
                    break;
                case 6:
                    key= GraphEnums.USERS.getName();
                    metricMethod=  GraphEnums.USERS.getMsg();
            }
            QueryWrapper<PayHourlyMetrics> queryWrapper = new QueryWrapper<>();
            queryWrapper.ge("create_time", startTime);
            queryWrapper.le("create_time", endTime);
            queryWrapper.select(key, "create_time");
            List<PayHourlyMetrics> hourlyMetricsList = payHourlyMetricsMapper.selectList(queryWrapper);

            List<DayChartVo> chartData = new ArrayList<>();
            for (PayHourlyMetrics metrics : hourlyMetricsList) {
                DayChartVo chartVo = new DayChartVo();
                Method metricGetMethod = PayHourlyMetrics.class.getMethod(metricMethod);
                Object metricValue = metricGetMethod.invoke(metrics);
                BigDecimal value = (metricValue instanceof BigDecimal)
                        ? (BigDecimal) metricValue
                        : new BigDecimal((BigInteger) metricValue);
                Class<?> chartVoClass = chartVo.getClass();
                Field field = chartVoClass.getDeclaredField(key);
                field.setAccessible(true);
                field.set(chartVo, value);
                chartVo.setDay(metrics.getCreateTime().replace(".0",""));
                chartData.add(chartVo);
            }
        List<String> hours = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();

        for (DayChartVo chartVo : chartData) {
            hours.add(chartVo.getDay());
            Class<?> chartVoClass = chartVo.getClass();
            Field field = chartVoClass.getDeclaredField(key);
            field.setAccessible(true);
            BigDecimal value = (BigDecimal) field.get(chartVo);
            values.add(value);
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("date", hours);
        responseData.put("data", values);

        return responseData;
    }

    private Map<String, Object> dailyChart(GraphValidate graphValidate) throws Exception {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = sdf.parse(graphValidate.getStartDate());
        Date endDate = sdf.parse(graphValidate.getEndDate());
        String key=null;
        String metricMethod = null;
        switch (graphValidate.getType()){
            case 1:
                key = GraphEnums.REVENUE.getName();
                metricMethod = GraphEnums.REVENUE.getMsg();
                break;
            case 2:
                key = GraphEnums.DEPOSIT.getName();
                metricMethod = GraphEnums.DEPOSIT.getMsg();
                break;
            case 3:
                key=GraphEnums.WITHDRAWAL.getName();
                metricMethod= GraphEnums.WITHDRAWAL.getMsg();
                break;
            case 4:
                key=GraphEnums.TRANSFER.getName();
                metricMethod = GraphEnums.TRANSFER.getMsg();
                break;
            case 5:
                key=GraphEnums.EXCHANGES.getName();
                metricMethod=  GraphEnums.EXCHANGES.getMsg();
                break;
            case 6:
                key= GraphEnums.USERS.getName();
                metricMethod=  GraphEnums.USERS.getMsg();
        }
        QueryWrapper<PayDailyMetrics> queryWrapper = new QueryWrapper<>();
        queryWrapper.ge("date", startDate);
        queryWrapper.le("date", endDate);
        queryWrapper.select(key, "date");
        List<PayDailyMetrics> dailyMetrics = payDailyMetricsMapper.selectList(queryWrapper);

        List<DayChartVo> chartData = new ArrayList<>();
        for (PayDailyMetrics metrics : dailyMetrics) {
            DayChartVo chartVo = new DayChartVo();
            Method metricGetMethod = PayDailyMetrics.class.getMethod(metricMethod);
            BigDecimal metricValue = (BigDecimal) metricGetMethod.invoke(metrics);
            Class<?> chartVoClass = chartVo.getClass();
            Field field = chartVoClass.getDeclaredField(key);
            field.setAccessible(true);
            field.set(chartVo, metricValue);
            chartVo.setDay(metrics.getDate().replace(".0",""));
            chartData.add(chartVo);
        }
        List<String> days = new ArrayList<>();
        List<BigDecimal> values = new ArrayList<>();

        for (DayChartVo chartVo : chartData) {
            days.add(chartVo.getDay());
            Class<?> chartVoClass = chartVo.getClass();
            Field field = chartVoClass.getDeclaredField(key);
            field.setAccessible(true);
            BigDecimal value = (BigDecimal) field.get(chartVo);
            values.add(value);
        }

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("date", days);
        responseData.put("data", values);

        return responseData;
    }

    @Override
    public Map<String, Object> getChart(ChartValidate chartValidate){
        if(chartValidate.getType()== ChartEnum.ALL_USERS.getCode()){
            return createChartData(payUserMapper.selectList(null));
        }else if(chartValidate.getType()==ChartEnum.UNVERIFIED.getCode()){
            return createChartData(payUserMapper.selectList(new QueryWrapper<PayUser>().eq("verify_account", StatusEnums.FAIL.getCode())));
        }else if(chartValidate.getType()==ChartEnum.VERIFIED.getCode()){
            return createChartData(payUserMapper.selectList(new QueryWrapper<PayUser>().eq("verify_account", StatusEnums.SUCCESS.getCode())));
        }
        return new HashMap<>();
    }

    private Map<String, Object> createChartData(List<PayUser> users) {
        Map<String, Object> chartData = new HashMap<>();
        Map<String, Integer> countryCounts = new HashMap<>();

        for (PayUser user : users) {
            String country = user.getCountry();
            countryCounts.put(country, countryCounts.getOrDefault(country, 0) + 1);
        }

        List<Map<String, Object>> dataPoints = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : countryCounts.entrySet()) {
            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("value", entry.getValue());
            dataPoint.put("name", entry.getKey());
            dataPoints.add(dataPoint);
        }

        chartData.put("params", countryCounts.keySet());
        chartData.put("data", dataPoints);

        return chartData;
    }

}
