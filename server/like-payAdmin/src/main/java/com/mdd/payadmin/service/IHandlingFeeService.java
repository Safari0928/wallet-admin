package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.handlingfee.HandlingFeeSearchValidate;
import com.mdd.payadmin.vo.HandlingFeeVo;

import java.util.List;

public interface IHandlingFeeService {
    PageResult<HandlingFeeVo> getAll(PageValidate pageValidate, HandlingFeeSearchValidate searchValidate);

    List<HandlingFeeVo> getList();

    long databaseDataSize();
}
