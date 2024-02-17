package com.mdd.mobile.service;

import com.mdd.common.core.PageResult;
import com.mdd.mobile.validate.common.PageValidate;
import com.mdd.mobile.validate.deposit.CreateDepositValidate;
import com.mdd.mobile.validate.deposit.SendDepositValidate;
import com.mdd.mobile.vo.deposit.*;

import java.math.BigDecimal;


public interface IDepositService {

    PageResult<DepositVo> list(PageValidate pageValidate);

    AddDepositVo createDeposit(CreateDepositValidate createDepositValidate);

    DepositDetailVo detail(String id);

    CompleteDepositVo completeDeposit(String depositId, boolean verify);

    CompleteDepositVo sendDeposit(SendDepositValidate sendDepositValidate);
}
