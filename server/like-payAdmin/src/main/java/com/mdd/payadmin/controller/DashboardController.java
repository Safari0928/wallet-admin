package com.mdd.payadmin.controller;

import com.mdd.common.core.AjaxResult;
import com.mdd.common.core.PageResult;
import com.mdd.common.util.RedisUtils;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.enums.AdminEnum;
import com.mdd.payadmin.service.IDashboardService;
import com.mdd.payadmin.validate.dashboard.ChartValidate;
import com.mdd.payadmin.validate.dashboard.GraphValidate;
import com.mdd.payadmin.vo.dashboard.RankVo;
import io.swagger.annotations.ApiOperation;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Resource
    IDashboardService iDashboardService;

    @GetMapping("/panel")
    @ApiOperation(value = "Panel Area")
    public AjaxResult<List<Map<String, Object>>> getPanel() {
        List<Map<String, Object>> metrics = iDashboardService.getPanel();
        if (metrics == null || metrics.isEmpty()) {
            return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);
        }
        return AjaxResult.success(metrics);
    }

    @GetMapping("/chart")
    @ApiOperation(value = "User's Country Chart")
    public AjaxResult<Map<String, Object>> getChart(@Validated ChartValidate chartValidate){
        Map<String, Object> metrics = iDashboardService.getChart(chartValidate);
        if (metrics == null || metrics.isEmpty()) {
            return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);
        }
        return AjaxResult.success(metrics);
    }

    @GetMapping("/graph")
    @ApiOperation(value = "Graph area")
    public AjaxResult<Map<String, Object>> getGraph(@Validated GraphValidate graphValidate) throws Exception {
        Map<String, Object> metrics = iDashboardService.getGraph(graphValidate);
        if (metrics == null || metrics.isEmpty()) {
            return AjaxResult.failed(AdminEnum.FAILED.getCode(), AdminEnum.FAILED.getMsg(), null);
        }
        return AjaxResult.success(metrics);
    }

    @GetMapping("/rank")
    @ApiOperation(value = "deposit top rank")
    public AjaxResult<PageResult<RankVo>> list(@Validated PageValidate pageValidate,@Validated GraphValidate graphValidate){
        PageResult<RankVo> list=iDashboardService.rank(pageValidate,graphValidate);
        return AjaxResult.success(list);
    }
}
