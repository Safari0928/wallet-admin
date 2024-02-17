package com.mdd.mobile.service;


import com.mdd.common.core.PageResult;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.vo.PayTransactionsVo;

import java.math.BigDecimal;

public interface IPayTransactionsService {

    PageResult<PayTransactionsVo> getTransactionsList(PageValidate pageValidate, String userId);

    boolean saveTransferTransaction(String detailId, BigDecimal revenue);

    boolean saveDepositTransaction(String detailId, BigDecimal revenue);

    boolean saveWithdrawTransaction(String detailId, BigDecimal revenue);

}
