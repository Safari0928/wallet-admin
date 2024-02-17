package com.mdd.payadmin.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.mdd.common.entity.PayTransactions;
import com.mdd.common.entity.payAdmin.PayDailyMetrics;
import com.mdd.common.entity.payAdmin.PayHourlyMetrics;
import com.mdd.common.entity.payDeposit.PayDeposit;
import com.mdd.common.entity.payTransfer.PayTransferDetails;
import com.mdd.common.entity.payUser.PayUser;
import com.mdd.common.entity.payWithdraw.PayWithdraw;
import com.mdd.common.exception.OperateException;
import com.mdd.common.mapper.PayTransactionsMapper;
import com.mdd.common.mapper.payAdmin.PayDailyMetricsMapper;
import com.mdd.common.mapper.payAdmin.PayHourlyMetricsMapper;
import com.mdd.common.mapper.payDeposit.PayDepositMapper;
import com.mdd.common.mapper.payTransfer.PayTransferDetailsMapper;
import com.mdd.common.mapper.payUser.PayUserMapper;
import com.mdd.common.mapper.payWithdraw.PayWithdrawMapper;
import com.mdd.common.util.ToolUtils;
import com.mdd.payadmin.service.IMetricsService;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class MetricsServiceImpl implements IMetricsService {

    @Resource
    PayHourlyMetricsMapper hourlyMetricsMapper;
    @Resource
    PayDailyMetricsMapper dailyMetricsMapper;
    @Resource
    PayWithdrawMapper payWithdrawMapper;
    @Resource
    PayDepositMapper payDepositMapper;
    @Resource
    PayTransferDetailsMapper payTransferDetailsMapper;
    @Resource
    PayTransactionsMapper payTransactionsMapper;
    @Resource
    PayUserMapper payUserMapper;

    @Override
    public BigDecimal calculateAndSaveHourlyMetrics() {

        BigDecimal total = BigDecimal.ZERO;
        DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd HH");
        DateTime now = new DateTime();

        DateTime previousHour = now.minusHours(1);
        String startOfHour = dateFormat.print(previousHour) + ":00:00";
        String endOfHour = dateFormat.print(previousHour) + ":59:59";

        PayHourlyMetrics payHourlyMetrics = new PayHourlyMetrics();
        payHourlyMetrics.setUuid(ToolUtils.makeUUID());

        Map<String, Object> stat = new LinkedHashMap<>();
        stat.put("withdrawAmount", payWithdrawMapper.sum("amount", new QueryWrapper<PayWithdraw>()
                .eq("status", 1)
                .ge("complete_time", startOfHour)
                .le("complete_time", endOfHour)
                .orderByDesc("complete_time")
        ));

        stat.put("depositAmount", payDepositMapper.sum("amount", new QueryWrapper<PayDeposit>()
                .eq("status", 1)
                .ge("complete_time", startOfHour)
                .le("complete_time", endOfHour)
                .orderByDesc("complete_time")
        ));

        stat.put("transferAmount", payTransferDetailsMapper.sum("amount", new QueryWrapper<PayTransferDetails>()
                .eq("status", 1)
                .ge("complete_time", startOfHour)
                .le("complete_time", endOfHour)
                .orderByDesc("complete_time")
        ));

        stat.put("transactionAmount", payTransactionsMapper.sum("amount", new QueryWrapper<PayTransactions>()
                .eq("status", 1)
                .ge("complete_time", startOfHour)
                .le("complete_time", endOfHour)
                .orderByDesc("complete_time")
        ));

        stat.put("transactionRevenue", payTransactionsMapper.sum("revenue", new QueryWrapper<PayTransactions>()
                .eq("status", 1)
                .ge("complete_time", startOfHour)
                .le("complete_time", endOfHour)
                .orderByDesc("complete_time")
        ));
        stat.put("userCount", payUserMapper.selectCount(new QueryWrapper<PayUser>()
                .ge("create_time",startOfHour)
                .le("create_time",endOfHour)));


        stat.put("orders", payTransactionsMapper.selectCount(new QueryWrapper<PayTransactions>()
                .eq("status",1)
                .ge("complete_time",startOfHour)
                .le("complete_time",endOfHour)));

        payHourlyMetrics.setWithdrawal((BigDecimal) stat.get("withdrawAmount"));
        payHourlyMetrics.setDeposit((BigDecimal) stat.get("depositAmount"));
        payHourlyMetrics.setTransfer((BigDecimal) stat.get("transferAmount"));
        payHourlyMetrics.setTransaction((BigDecimal) stat.get("transactionAmount"));
        payHourlyMetrics.setRevenue((BigDecimal) stat.get("transactionRevenue"));
        payHourlyMetrics.setUsers(BigInteger.valueOf((Long) stat.get("userCount")));
        payHourlyMetrics.setOrders(BigInteger.valueOf((Long) stat.get("orders")));
        payHourlyMetrics.setCreateTime(startOfHour);

        int err = hourlyMetricsMapper.insert(payHourlyMetrics);
        if (err != 1) {
            throw new OperateException("Error", err);
        }
        return total;
    }


    @Override
    public BigDecimal calculateAndSaveDailyMetrics() {
        BigDecimal total = BigDecimal.ZERO;
        DateTimeFormatter dateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime now = new DateTime();

        // 获取前一天的日期
        DateTime previousDay = now.minusDays(1);
        String previousDate = dateFormat.print(previousDay);

        PayDailyMetrics payDailyMetrics = new PayDailyMetrics();
        payDailyMetrics.setUuid(ToolUtils.makeUUID());

        Map<String, Object> stat = new LinkedHashMap<>();

        // 查询前一天每小时的数据并累加
        BigDecimal hourlyWithdrawAmount = hourlyMetricsMapper.sum("withdrawal", new QueryWrapper<PayHourlyMetrics>()
                .ge("create_time", previousDate + " 00:00:00")
                .le("create_time", previousDate + " 23:59:59")
        );

        BigDecimal hourlyDepositAmount = hourlyMetricsMapper.sum("deposit", new QueryWrapper<PayHourlyMetrics>()
                .ge("create_time", previousDate + " 00:00:00")
                .le("create_time", previousDate + " 23:59:59")
        );

        BigDecimal hourlyTransferAmount = hourlyMetricsMapper.sum("transfer", new QueryWrapper<PayHourlyMetrics>()
                .ge("create_time", previousDate + " 00:00:00")
                .le("create_time", previousDate + " 23:59:59")
        );

        BigDecimal hourlyTransactionAmount = hourlyMetricsMapper.sum("transaction", new QueryWrapper<PayHourlyMetrics>()
                .ge("create_time", previousDate + " 00:00:00")
                .le("create_time", previousDate + " 23:59:59")
        );

        BigDecimal hourlyTransactionRevenue = hourlyMetricsMapper.sum("revenue", new QueryWrapper<PayHourlyMetrics>()
                .ge("create_time", previousDate + " 00:00:00")
                .le("create_time", previousDate + " 23:59:59")
        );

        BigDecimal hourlyUsers = hourlyMetricsMapper.sum("users", new QueryWrapper<PayHourlyMetrics>()
                .ge("create_time", previousDate + " 00:00:00")
                .le("create_time", previousDate + " 23:59:59")
        );

        BigDecimal hourlyOrders = hourlyMetricsMapper.sum("orders",new QueryWrapper<PayHourlyMetrics>()
                .ge("create_time", previousDate + " 00:00:00")
                .le("create_time", previousDate + " 23:59:59")
        );

        // 设置每天的数据
        stat.put("withdrawAmount", hourlyWithdrawAmount);
        stat.put("depositAmount", hourlyDepositAmount);
        stat.put("transferAmount", hourlyTransferAmount);
        stat.put("transactionAmount", hourlyTransactionAmount);
        stat.put("transactionRevenue", hourlyTransactionRevenue);
        stat.put("users", hourlyUsers);
        stat.put("orders", hourlyOrders);

        payDailyMetrics.setWithdrawal((BigDecimal) stat.get("withdrawAmount"));
        payDailyMetrics.setDeposit((BigDecimal) stat.get("depositAmount"));
        payDailyMetrics.setTransfer((BigDecimal) stat.get("transferAmount"));
        payDailyMetrics.setTransaction((BigDecimal) stat.get("transactionAmount"));
        payDailyMetrics.setRevenue((BigDecimal) stat.get("transactionRevenue"));
        payDailyMetrics.setUsers((BigDecimal) stat.get("users"));
        payDailyMetrics.setOrders((BigDecimal) stat.get("orders"));
        payDailyMetrics.setDate(previousDate);

        // 插入每天的数据到 dailyMetricsMapper
        int err = dailyMetricsMapper.insert(payDailyMetrics);
        if (err != 1) {
            throw new OperateException("Error kodu", err);
        }

        return total;
    }
}
