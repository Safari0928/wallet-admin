package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.vo.BankAccountVo;

public interface IBankAccountService {
    PageResult<BankAccountVo> list(PageValidate pageValidate, String userId);
}
