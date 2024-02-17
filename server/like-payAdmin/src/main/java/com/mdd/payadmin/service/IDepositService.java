package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.deposit.DepositSearchValidate;
import com.mdd.payadmin.vo.deposit.DepositDetailVo;
import com.mdd.payadmin.vo.deposit.DepositListVo;

public interface IDepositService {
    PageResult<DepositListVo> List(PageValidate pageValidate, DepositSearchValidate searchValidate);
    Long getCount(PageValidate pageValidate);

    DepositDetailVo detail(String depositId);
}
