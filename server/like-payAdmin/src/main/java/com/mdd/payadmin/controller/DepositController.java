package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.common.util.StringUtils;
import com.mdd.payadmin.commons.Constants;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IMetricsService;
import com.mdd.payadmin.service.IDepositService;
import com.mdd.payadmin.validate.deposit.DepositSearchValidate;
import com.mdd.payadmin.vo.deposit.DepositDetailVo;
import com.mdd.payadmin.vo.deposit.DepositListVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Set;

@RestController
@RequestMapping("/api/deposit")
public class DepositController {

    @Resource
    IDepositService iDepositService;

    @Resource
    IMetricsService iHourlyMetricsService;

    @GetMapping("/list")
    @ApiOperation(value = "Deposit List Api")
    public AjaxResult<PageResult<DepositListVo>> depositList(@Validated PageValidate pageValidate,
                                                             @Validated  DepositSearchValidate searchValidate) {
        Integer pageNo = pageValidate.getPageNo();
        Integer pageSize = pageValidate.getPageSize();

        if (StringUtils.isNull(searchValidate.getTransactionNumber())
                && StringUtils.isNull(searchValidate.getCardNumber())
                && StringUtils.isNull(searchValidate.getPayId())
                && StringUtils.isNull(searchValidate.getStatus())) {

            String catchKey = Constants.DEPOSIT_PREFIX + pageNo + pageSize;
            if (RedisUtils.exists(catchKey)) {
                PageResult<DepositListVo> list = (PageResult<DepositListVo>) RedisUtils.get(catchKey);
                Long count = iDepositService.getCount(pageValidate);
                if (!count.equals(list.getCount())) {
                    String commonPrefix = Constants.DEPOSIT_PREFIX;
                    Set<String> keysToDelete = RedisUtils.keys(commonPrefix + "*");
                    if (keysToDelete != null && !keysToDelete.isEmpty()) {
                        RedisUtils.delP(keysToDelete.toArray(new String[0]));
                    }
                } else if (pageNo.equals(list.getPageNo()) && pageSize.equals(list.getPageSize())) {

                    return AjaxResult.success(list);
                }
            }
        }

        PageResult<DepositListVo> list = iDepositService.List(pageValidate, searchValidate);

        RedisUtils.set("deposit:list:" + pageNo + pageSize, list, 604800);
        return AjaxResult.success(list);
    }

    @GetMapping("/detail")
    @ApiOperation(value = "Deposit Detail")
    public AjaxResult<DepositDetailVo> detail(@RequestParam String detailId)  {
        DepositDetailVo vo = iDepositService.detail(detailId);

        if (vo == null) return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);

        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), vo);
    }

    @GetMapping("/hourlymetrics")
    @ApiOperation(value = "Deposit Detail")
    public AjaxResult<Object> HourlyMetrics()  {
        BigDecimal vo = iHourlyMetricsService.calculateAndSaveHourlyMetrics();

        if (vo == null) return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);

        return AjaxResult.success(AdminEnum.SUCCESS.getCode(), AdminEnum.SUCCESS.getMsg(), vo);
    }

}
