package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.dashboard.ChartValidate;
import com.mdd.payadmin.validate.dashboard.GraphValidate;
import com.mdd.payadmin.vo.dashboard.RankVo;

import java.util.List;
import java.util.Map;

public interface IDashboardService {

    Map<String, Object> getGraph(GraphValidate graphValidate) throws Exception;

    PageResult<RankVo> rank(PageValidate pageValidate, GraphValidate graphValidate);
    Map<String, Object> getChart(ChartValidate chartValidate);

    List<Map<String, Object>> getPanel();
}
