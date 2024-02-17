package com.mdd.payadmin.service;

import com.mdd.common.core.PageResult;
import com.mdd.payadmin.commons.PageValidate;
import com.mdd.payadmin.validate.transfer.TransferSearchValidate;
import com.mdd.payadmin.vo.TransactionsDefaultVo;

public interface ITransactionsService {
    PageResult<TransactionsDefaultVo> list(PageValidate pageValidate, TransferSearchValidate validate);

    Long getCount(PageValidate pageValidate);
}
