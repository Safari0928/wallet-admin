package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.vo.bankCard.BankCardVo;

public interface IBankCardService {
    PageResult<BankCardVo> list(PageValidate pageValidate, String userId);
}
